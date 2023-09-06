package pro.sky.AnimalShelter.state;

import org.springframework.stereotype.Component;
import pro.sky.AnimalShelter.enums.BotCommand;

import java.util.*;

import static pro.sky.AnimalShelter.enums.BotCommand.STOP;

/**
 * Класс, отвечающий за управление состояниями чатов и бота.
 */
@Component
public class ChatStateHolder {

    /**
     * Мапа для отслеживания состояния запущенности бота в каждом чате.
     */
    private final Map<Long, Boolean> isBotStartedMap = new HashMap<>();

    /**
     * Мапа для хранения истории состояний чатов.
     */
    private final Map<Long, Deque<BotCommand>> chatStateHistory = new HashMap<>();

    /**
     * Максимальный размер истории состояний чата.
     */
    private static final int MAX_HISTORY_SIZE = 3;

    /**
     * Получает текущее состояние чата по его идентификатору.
     *
     * @param chatId Идентификатор чата.
     * @return Текущее состояние чата.
     */
    public BotCommand getCurrentStateById(long chatId) {
        Deque<BotCommand> stateStack = chatStateHistory.get(chatId);
        if (stateStack == null || stateStack.isEmpty()) {
            return STOP;
        }
        return stateStack.peek();
    }

    /**
     * Добавляет новое состояние в историю чата.
     *
     * @param chatId Идентификатор чата.
     * @param state  Состояние для добавления в историю.
     */
    public void addState(long chatId, BotCommand state) {
        Deque<BotCommand> stateStack = chatStateHistory.computeIfAbsent(chatId, k -> new LinkedList<>());
        if (stateStack.size() >= MAX_HISTORY_SIZE) {
            stateStack.pollLast();
        }
        stateStack.push(state);
    }

    /**
     * Получает предыдущее состояние чата по его идентификатору.
     *
     * @param chatId Идентификатор чата.
     * @return Предыдущее состояние чата.
     */
    public BotCommand getPreviousState(long chatId) {
        Deque<BotCommand> stateStack = chatStateHistory.get(chatId);
        if (stateStack == null || stateStack.isEmpty()) {
            return STOP;
        }
        Iterator<BotCommand> iterator = stateStack.descendingIterator();
        iterator.next();
        return iterator.next();
    }

    /**
     * Устанавливает состояние запущенности бота в чате.
     *
     * @param chatId    Идентификатор чата.
     * @param isStarted Значение, указывающее, запущен ли бот в чате.
     */
    public void setBotStarted(Long chatId, boolean isStarted) {
        isBotStartedMap.put(chatId, isStarted);
    }

    /**
     * Проверяет, запущен ли бот в чате.
     *
     * @param chatId Идентификатор чата.
     * @return true, если бот запущен в чате, иначе false.
     */
    public boolean isBotStarted(Long chatId) {
        return isBotStartedMap.getOrDefault(chatId, false);
    }
}
