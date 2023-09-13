package pro.sky.AnimalShelter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.AnimalShelter.entity.ChatState;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.repository.ChatStateRepository;

import static pro.sky.AnimalShelter.enums.BotCommand.START;
import static pro.sky.AnimalShelter.enums.BotCommand.STOP;

/**
 * Сервис для управления очередью состояний чата.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatStateService {
    private final ChatStateRepository chatStateRepository;

    public Boolean isBotStarted(Long chatId) {
        log.info("isBotStarted method was invoked");
        if (chatStateRepository.findByChatId(chatId).isEmpty()) {
            return false;
        } else {
            ChatState foundChatState = chatStateRepository.findByChatId(chatId).get();
            return foundChatState.isBotStarted();
        }
    }

    public BotCommand getCurrentStateByChatId(Long chatId) {
        log.info("getCurrentStateByChatId method was invoked");
        ChatState foundChatState = chatStateRepository.findByChatId(chatId).orElse(null);
        if (foundChatState == null ) {
            return STOP;
        } else if (!foundChatState.isBotStarted()) {
            return STOP;
        }
        return foundChatState.getCurrentState();
    }

    public BotCommand getPreviousStateByChatId(Long chatId) {
        log.info("getCurrentStateByChatId method was invoked");
        ChatState foundChatState = chatStateRepository.findByChatId(chatId).orElse(null);
        if (foundChatState == null) {
            return STOP;
        }
        return foundChatState.getStepBackState();

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
        if (chatStateRepository.findByChatId(chatId).isEmpty()) {
            updatedChatState.setChatId(chatId);
            updatedChatState.setCurrentState(state);
            updatedChatState.setBotStarted(true);
        } else {
            ChatState foundChatState = chatStateRepository.findByChatId(chatId).get();
            BotCommand stepBack = foundChatState.getCurrentState();
            BotCommand twoStepBack = foundChatState.getStepBackState();
            foundChatState.setTwoStepBackState(twoStepBack);
            foundChatState.setStepBackState(stepBack);
            foundChatState.setCurrentState(state);
            if (state == START) {
                foundChatState.setBotStarted(true);
            }
            updatedChatState = foundChatState;
        }
        chatStateRepository.save(updatedChatState);
    }

    public void stopBot(Long chatId) {
        log.info("stopBot method was invoked");
        ChatState foundChatState = chatStateRepository.findByChatId(chatId).orElse(null);
        if (foundChatState != null) {
            foundChatState.setBotStarted(false);
            foundChatState.setCurrentState(null);
            foundChatState.setStepBackState(null);
            foundChatState.setTwoStepBackState(null);
            chatStateRepository.save(foundChatState);
        }
    }
}
