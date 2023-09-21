package pro.sky.AnimalShelter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.AnimalShelter.entity.Chat;
import pro.sky.AnimalShelter.entity.ChatState;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.repository.ChatRepository;
import pro.sky.AnimalShelter.repository.ChatStateRepository;

import static pro.sky.AnimalShelter.enums.BotCommand.START;

/**
 * Сервис для управления очередью состояний чата.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatStateService {

    /**
     * Репозиторий для доступа к данным о состояниях чатов.
     */
    private final ChatStateRepository chatStateRepository;

    /**
     * Репозиторий для доступа к данным о чатах.
     */
    private final ChatRepository chatRepository;

    /**
     * Получает текущее состояние чата по его идентификатору.
     *
     * @param chatId Идентификатор чата.
     * @return Текущее состояние чата или {@code STOP}, если чат не существует или бот не запущен.
     */
    public BotCommand getCurrentStateByChatId(Long chatId) {
        return chatRepository.findByChatId(chatId)
                .filter(Chat::isBotStarted)
                .flatMap(chat -> chatStateRepository.findByChatId(chat.getId()))
                .map(ChatState::getCurrentState)
                .orElse(BotCommand.STOP);
    }

    /**
     * Получает предыдущее состояние чата по его идентификатору.
     *
     * @param chatId Идентификатор чата.
     * @return Предыдущее состояние чата или {@code STOP}, если чат не существует или бот не запущен.
     */
    public BotCommand getPreviousStateByChatId(Long chatId) {
        log.info("getPreviousStateByChatId method was invoked");
        return chatRepository.findByChatId(chatId)
                .map(foundChat -> chatStateRepository.findByChatId(foundChat.getId())
                        .map(ChatState::getStepBackState)
                        .orElse(BotCommand.STOP))
                .orElse(BotCommand.STOP);
    }

    /**
     * Получает последнее состояние чата по его идентификатору.
     *
     * @param chatId Идентификатор чата.
     * @return Предыдущее состояние чата или {@code STOP}, если чат не существует или бот не запущен.
     */
    public BotCommand getLastStateByChatId(Long chatId) {
        log.info("getLastStateByChatId method was invoked");
        return chatRepository.findByChatId(chatId)
                .map(foundChat -> chatStateRepository.findByChatId(foundChat.getId())
                        .map(ChatState::getTwoStepBackState)
                        .orElse(BotCommand.STOP))
                .orElse(BotCommand.STOP);
    }

    /**
     * Добавляет новое состояние в историю чата.
     *
     * @param chatId Идентификатор чата.
     * @param state  Состояние для добавления в историю.
     */
    public void updateChatState(Long chatId, BotCommand state) {
        log.info("updateChatState method was invoked");
        ChatState updatedChatState = new ChatState();
        Chat updatedChat = new Chat();
        if (chatRepository.findByChatId(chatId).isEmpty()) {
            updatedChat.setChatId(chatId);
            updatedChat.setBotStarted(true);
            Chat savedChat = chatRepository.save(updatedChat);
            updatedChatState.setCurrentState(state);
            updatedChatState.setChat(savedChat);
            chatStateRepository.save(updatedChatState);
        } else {
            chatRepository.findByChatId(chatId).ifPresent(foundChat ->
                    chatStateRepository.findByChatId(foundChat.getId()).ifPresent(foundChatState -> {
                        BotCommand stepBack = foundChatState.getCurrentState();
                        BotCommand twoStepBack = foundChatState.getStepBackState();
                        foundChatState.setTwoStepBackState(twoStepBack);
                        foundChatState.setStepBackState(stepBack);
                        foundChatState.setCurrentState(state);
                        if (state == START) {
                            foundChat.setBotStarted(true);
                            chatRepository.save(foundChat);
                        }
                        chatStateRepository.save(foundChatState);
                    }));
        }
    }

    /**
     * Останавливает бота в указанном чате.
     *
     * @param chatId Идентификатор чата.
     */
    public void stopBot(Long chatId) {
        log.info("stopBot method was invoked");
        chatRepository.findByChatId(chatId).ifPresent(foundChat ->
                chatStateRepository.findByChatId(foundChat.getId()).ifPresent(foundChatState -> {
                    foundChat.setBotStarted(false);
                    foundChatState.setCurrentState(null);
                    foundChatState.setStepBackState(null);
                    foundChatState.setTwoStepBackState(null);
                    chatRepository.save(foundChat);
                    chatStateRepository.save(foundChatState);
                }));
    }
}
