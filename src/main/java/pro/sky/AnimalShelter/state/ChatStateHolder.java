package pro.sky.AnimalShelter.state;

import org.springframework.stereotype.Component;
import pro.sky.AnimalShelter.enums.BotCommand;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

@Component
public class ChatStateHolder {
    private final Map<Long, BotCommand> chatStates = new HashMap<>();
    private final Map<Long, Deque<BotCommand>> chatStateHistory = new HashMap<>();

    public BotCommand getState(long chatId) {
        return chatStates.getOrDefault(chatId, BotCommand.STOP);
    }

    public void setState(long chatId, BotCommand state) {
        chatStates.put(chatId, state);
    }

    public void pushState(long chatId, BotCommand state) {
        chatStateHistory.computeIfAbsent(chatId, k -> new LinkedList<>()).push(state);
        setState(chatId, state);
    }

    public BotCommand popState(long chatId) {
        Deque<BotCommand> stateStack = chatStateHistory.get(chatId);
        if (stateStack == null || stateStack.isEmpty()) {
            return BotCommand.START;
        }
        BotCommand previousState = stateStack.pop();
        setState(chatId, previousState);
        return previousState;
    }
}
