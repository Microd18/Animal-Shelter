package pro.sky.AnimalShelter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.AnimalShelter.entity.Chat;
import pro.sky.AnimalShelter.entity.ChatState;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.repository.ChatRepository;
import pro.sky.AnimalShelter.repository.ChatStateRepository;

import java.util.Optional;

import static pro.sky.AnimalShelter.enums.BotCommand.START;
import static pro.sky.AnimalShelter.enums.BotCommand.STOP;

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
        log.info("getCurrentStateByChatId method was invoked");
        var foundChat = chatRepository.findByChatId(chatId);
        if (foundChat.isEmpty()) {
            return STOP;
        } else if (!foundChat.get().isBotStarted()) {
            return STOP;
        }
        return chatStateRepository.findByChatId(foundChat.get().getId()).get().getCurrentState();
    }

    /**
     * Получает предыдущее состояние чата по его идентификатору.
     *
     * @param chatId Идентификатор чата.
     * @return Предыдущее состояние чата или {@code STOP}, если чат не существует или бот не запущен.
     */
    public BotCommand getPreviousStateByChatId(Long chatId) {
        log.info("getCurrentStateByChatId method was invoked");
        Optional<Chat> foundChat = chatRepository.findByChatId(chatId);
        if (foundChat.isPresent()) {
            ChatState foundChatState = chatStateRepository.findByChatId(foundChat.get().getId()).orElse(null);
            if (foundChatState == null) {
                return STOP;
            } else {
                return foundChatState.getStepBackState();
            }
        } else {
            return STOP;
        }
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
        } else {
            ChatState foundChatState = chatStateRepository.findByChatId(chatRepository.findByChatId(chatId).get().getId()).get();
            Chat foundChat = chatRepository.findByChatId(chatId).get();
            BotCommand stepBack = foundChatState.getCurrentState();
            BotCommand twoStepBack = foundChatState.getStepBackState();
            foundChatState.setTwoStepBackState(twoStepBack);
            foundChatState.setStepBackState(stepBack);
            foundChatState.setCurrentState(state);
            if (state == START) {
                foundChat.setBotStarted(true);
                chatRepository.save(foundChat);
            }
            updatedChatState = foundChatState;
        }
        chatStateRepository.save(updatedChatState);
    }


    /**
     * Останавливает бота в указанном чате.
     *
     * @param chatId Идентификатор чата.
     */
    public void stopBot(Long chatId) {
        log.info("stopBot method was invoked");
        Chat foundChat = chatRepository.findByChatId(chatId).orElse(null);
        if (foundChat != null) {
            ChatState foundChatState = chatStateRepository.findByChatId(foundChat.getId()).get();
            foundChat.setBotStarted(false);
            foundChatState.setCurrentState(null);
            foundChatState.setStepBackState(null);
            foundChatState.setTwoStepBackState(null);
            chatRepository.save(foundChat);
            chatStateRepository.save(foundChatState);
        }
    }
}
