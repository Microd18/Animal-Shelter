package pro.sky.AnimalShelter.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.enums.UserReportStates;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
@ExtendWith(MockitoExtension.class)
public class JsonMapConverterTest {

    @Test
    @DisplayName("Проверяет, что объект типа Map<Long, Deque<BotCommand>> успешно преобразуется в JSON-строку")
    public void testToCommandStatesJson() {
        JsonMapConverter converter = new JsonMapConverter();
        Map<Long, Deque<BotCommand>> map = new HashMap<>();
        Deque<BotCommand> deque = new ArrayDeque<>();
        deque.add(BotCommand.START);
        deque.add(BotCommand.HELP);
        map.put(123456789L, deque);
        String expectedJson = "{\"123456789\":[\"START\",\"HELP\"]}";
        String actualJson = converter.toCommandStatesJson(map);
        assertEquals(expectedJson, actualJson);
    }


    @Test
    @DisplayName("Проверяет, что объект типа Map<Long, Deque<UserReportStates>> успешно преобразуется в JSON-строку")
    public void testToUserReportStatesJson() {
        JsonMapConverter converter = new JsonMapConverter();
        Map<Long, Deque<UserReportStates>> map = new HashMap<>();
        Deque<UserReportStates> deque = new ArrayDeque<>();
        deque.add(UserReportStates.PHOTO);
        deque.add(UserReportStates.RATION);
        map.put(123456789L, deque);
        String expectedJson = "{\"123456789\":[\"PHOTO\",\"RATION\"]}";
        String actualJson = converter.toUserReportStatesJson(map);
        assertEquals(expectedJson, actualJson);
    }

}