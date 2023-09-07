package pro.sky.AnimalShelter.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.AnimalShelter.entity.ChatState;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.exception.ChatStateNotFoundException;
import pro.sky.AnimalShelter.repository.ChatStateRepository;

/**
 * Сервис для управления очередью состояний чата.
 */
@Service
public class ChatStateService {
    private final ChatStateRepository chatStateRepository;
    Logger logger = LoggerFactory.getLogger(ChatStateService.class);

    public ChatStateService(ChatStateRepository chatStateRepository) {
        this.chatStateRepository = chatStateRepository;
    }

    public Boolean isBotStarted(Long chatId) {
        logger.info("isBotStarted method was invoked");
        Boolean result;
        return chatStateRepository.findByChatId(chatId).isPresent();
    }

    public BotCommand getCurrentStateByChatId(Long chatId) {
        logger.info("getCurrentStateByChatId method was invoked");
        ChatState foundChatState = chatStateRepository.findByChatId(chatId)
                .orElseThrow(() -> {
                    logger.error("There is no chatState with chatId = " + chatId);
                    return new ChatStateNotFoundException(chatId);
                });
        return foundChatState.getCurrentState();
    }

    public BotCommand getPreviousStateByChatId(Long chatId) {
        logger.info("getCurrentStateByChatId method was invoked");
        ChatState foundChatState = chatStateRepository.findByChatId(chatId)
                .orElseThrow(() -> {
                    logger.error("There is no chatState with chatId = " + chatId);
                    return new ChatStateNotFoundException(chatId);
                });
        return foundChatState.getStepBackState();

    }

    /**
     * Добавляет новое состояние в историю чата.
     *
     * @param chatId Идентификатор чата.
     * @param state  Состояние для добавления в историю.
     */
    public ChatState updateChatState(Long chatId, BotCommand state) {
        logger.info("updateChatState method was invoked");
        ChatState updatedChatState = new ChatState();
        if (chatStateRepository.findByChatId(chatId).isEmpty()) {
            updatedChatState.setChatId(chatId);
            updatedChatState.setCurrentState(state);
        } else {
            ChatState foundChatState = chatStateRepository.findByChatId(chatId).get();
            BotCommand stepBack = foundChatState.getCurrentState();
            BotCommand twoStepBack = foundChatState.getStepBackState();
            foundChatState.setTwoStepBackState(twoStepBack);
            foundChatState.setStepBackState(stepBack);
            foundChatState.setCurrentState(state);
            updatedChatState = foundChatState;
            }
        chatStateRepository.save(updatedChatState);
        return updatedChatState;
    }

    public ChatState clearChatState(Long chatId) {
        logger.info("clearChatState method was invoked");
        ChatState foundChatState = chatStateRepository.findByChatId(chatId)
                .orElseThrow(() -> {
                    logger.error("There is no chatState with chatId = " + chatId);
                    return new ChatStateNotFoundException(chatId);
                });
        chatStateRepository.delete(foundChatState);
        return foundChatState;
    }
}
