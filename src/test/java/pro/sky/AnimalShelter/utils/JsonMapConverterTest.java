package pro.sky.AnimalShelter.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.enums.UserReportStates;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JsonMapConverterTest {

    @InjectMocks
    private JsonMapConverter jsonMapConverter;

    @Test
    @DisplayName("Тест на успешное преобразование объекта в JSON-строку:")
    public void testToCommandStatesJson() {
        Map<Long, Deque<BotCommand>> commandStates = new HashMap<>();
        Deque<BotCommand> commandDeque = new LinkedList<>();
        commandDeque.add(BotCommand.DOG);
        commandStates.put(1L, commandDeque);
        String expectedJson = "{\"1\":[\"DOG\"]}";
        String json = jsonMapConverter.toCommandStatesJson(commandStates);
        assertNotNull(json);
        assertEquals(expectedJson, json);
    }

    @Test
    @DisplayName("Тест на успешное преобразование JSON-строки в объект:")
    public void testToCommandStatesMap() {
        // Arrange
        String json = "{\"1\":[\"DOG\"]}";
        Map<Long, Deque<BotCommand>> expectedCommandStates = new HashMap<>();
        Deque<BotCommand> commandDeque = new LinkedList<>();
        commandDeque.add(BotCommand.DOG);
        expectedCommandStates.put(1L, commandDeque);
        Map<Long, Deque<BotCommand>> commandStates = jsonMapConverter.toCommandStatesMap(json);
        assertNotNull(commandStates);
        assertEquals(expectedCommandStates, commandStates);
    }

    @Test
    @DisplayName("Тест на успешное преобразование объекта в JSON-строку для другого типа данных:")
    public void testToUserReportStatesJson() {
        Map<Long, Deque<UserReportStates>> userReportStates = new HashMap<>();
        Deque<UserReportStates> stateDeque = new LinkedList<>();
        stateDeque.add(UserReportStates.PHOTO);
        userReportStates.put(1L, stateDeque);
        String expectedJson = "{\"1\":[\"PHOTO\"]}";
        String json = jsonMapConverter.toUserReportStatesJson(userReportStates);
        assertNotNull(json);
        assertEquals(expectedJson, json);
    }

    @Test
    @DisplayName("Тест на успешное преобразование JSON-строки в объект для другого типа данных:")
    public void testToUserReportStatesMap() {
        // Arrange
        String json = "{\"1\":[\"RATION\"]}";
        Map<Long, Deque<UserReportStates>> expectedUserReportStates = new HashMap<>();
        Deque<UserReportStates> stateDeque = new LinkedList<>();
        stateDeque.add(UserReportStates.RATION);
        expectedUserReportStates.put(1L, stateDeque);
        Map<Long, Deque<UserReportStates>> userReportStates = jsonMapConverter.toUserReportStatesMap(json);
        assertNotNull(userReportStates);
        assertEquals(expectedUserReportStates, userReportStates);
    }

    @Test
    @DisplayName("Тестирование преобразования пустого значения в JSON-строку и обратно")
    void testToCommandStatesEmptyJson() {
        Map<Long, Deque<BotCommand>> commandStates = Collections.emptyMap();
        String expectedJson = "{}";
        String json = jsonMapConverter.toCommandStatesJson(commandStates);
        Map<Long, Deque<BotCommand>> parsedCommandStates = jsonMapConverter.toCommandStatesMap(json);
        assertNotNull(json);
        assertEquals(expectedJson, json);
        assertNotNull(parsedCommandStates);
        assertTrue(parsedCommandStates.isEmpty());
    }

    @Test
    @DisplayName("Тестирование преобразования JSON-строки в null")
    void testToCommandStatesMapNull() {
        String json = null;
        Map<Long, Deque<BotCommand>> commandStates = jsonMapConverter.toCommandStatesMap(json);
        assertNotNull(commandStates);
        assertTrue(commandStates.isEmpty());
    }

    @Test
    @DisplayName("Тестирование ошибочного формата JSON-строки")
    void testToCommandStatesMapMalformedJson() {
        String malformedJson = "{invalid_json}";
        Map<Long, Deque<BotCommand>> parsedCommandStates = jsonMapConverter.toCommandStatesMap(malformedJson);
        assertNotNull(parsedCommandStates);
        assertTrue(parsedCommandStates.isEmpty());
    }

    @Test
    @DisplayName("Тестирование преобразования JSON-строки, содержащей пустую коллекцию:")
    void testToUserReportStatesMapEmpty() {
        String json = "{\"1\": []}";
        Map<Long, Deque<UserReportStates>> expectedMap = new HashMap<>();
        Deque<UserReportStates> emptyDeque = new LinkedList<>();
        expectedMap.put(1L, emptyDeque);
        Map<Long, Deque<UserReportStates>> actualMap = jsonMapConverter.toUserReportStatesMap(json);
        assertNotNull(actualMap);
        assertEquals(expectedMap, actualMap);
    }

    @Test
    @DisplayName("Тестирование случая, когда исходный объект для преобразования пустой")
    void testToCommandStatesJsonEmptyObject() {
        Map<Long, Deque<BotCommand>> commandStates = Collections.emptyMap();
        String expectedJson = "{}";
        String json = jsonMapConverter.toCommandStatesJson(commandStates);
        assertNotNull(json);
        assertEquals(expectedJson, json);
    }

    @Test
    @DisplayName("Тестирование случая, когда JSON-строка содержит пустой массив")
    void testToUserReportStatesMapEmptyArray() {
        // Arrange
        String json = "{\"1\": []}";
        Map<Long, Deque<UserReportStates>> expectedMap = new HashMap<>();
        Deque<UserReportStates> emptyDeque = new LinkedList<>();
        expectedMap.put(1L, emptyDeque);
        Map<Long, Deque<UserReportStates>> actualMap = jsonMapConverter.toUserReportStatesMap(json);
        assertNotNull(actualMap);
        assertEquals(expectedMap, actualMap);
    }
}

