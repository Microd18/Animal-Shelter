package pro.sky.AnimalShelter.state;

import org.springframework.stereotype.Component;
import pro.sky.AnimalShelter.enums.BotCommand;

import java.util.*;

@Component
public class ChatStateHolder {

    private final Map<Long, Boolean> isBotStartedMap = new HashMap<>();
    private final Map<Long, Deque<BotCommand>> chatStateHistory = new HashMap<>();
    private static final int MAX_HISTORY_SIZE = 3;

    public BotCommand getCurrentStateById(long chatId) {
        Deque<BotCommand> stateStack = chatStateHistory.get(chatId);
        if (stateStack == null || stateStack.isEmpty()) {
            return BotCommand.STOP;
        }
        return stateStack.peek();
    }

    public void addState(long chatId, BotCommand state) {
        Deque<BotCommand> stateStack = chatStateHistory.computeIfAbsent(chatId, k -> new LinkedList<>());
        if (stateStack.size() >= MAX_HISTORY_SIZE) {
            stateStack.pollLast();
        }
        stateStack.push(state);
    }

    public BotCommand getPreviousState(long chatId) {
        Deque<BotCommand> stateStack = chatStateHistory.get(chatId);
        if (stateStack == null || stateStack.isEmpty()) {
            return BotCommand.STOP;
        }
        Iterator<BotCommand> iterator = stateStack.descendingIterator();
        iterator.next();
        return iterator.next();
    }

    public void setBotStarted(Long chatId, boolean isStarted) {
        isBotStartedMap.put(chatId, isStarted);
    }

    public boolean isBotStarted(Long chatId) {
        return isBotStartedMap.getOrDefault(chatId, false);
    }
}
