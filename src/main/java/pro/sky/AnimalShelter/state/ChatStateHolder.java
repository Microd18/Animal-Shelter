package pro.sky.AnimalShelter.state;

import org.springframework.stereotype.Component;
import pro.sky.AnimalShelter.enums.BotState;

import java.util.HashMap;
import java.util.Map;

@Component
public class ChatStateHolder {
    private final Map<Long, BotState> chatStates = new HashMap<>();

    public void setState(Long chatId, BotState state) {
        chatStates.put(chatId, state);
    }

    public BotState getState(Long chatId) {
        return chatStates.getOrDefault(chatId, BotState.STOP);
    }
}
