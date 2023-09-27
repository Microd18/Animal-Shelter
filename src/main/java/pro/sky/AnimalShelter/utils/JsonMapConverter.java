package pro.sky.AnimalShelter.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import pro.sky.AnimalShelter.enums.BotCommand;
import pro.sky.AnimalShelter.enums.UserReportStates;

import java.io.IOException;
import java.util.Deque;
import java.util.Map;

/**
 * Класс-конвертер для преобразования между JSON и объектами типа Map<Long, Deque<BotCommand>>.
 */
@Component
public class JsonMapConverter {

    /**
     * Объект ObjectMapper используется для сериализации и десериализации JSON.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Преобразует объект типа Map<Long, Deque<BotCommand>> в JSON-строку.
     *
     * @param attribute объект для преобразования
     * @return JSON-строка
     * @throws RuntimeException если произошла ошибка при преобразовании
     */
    public String toCommandStatesJson(Map<Long, Deque<BotCommand>> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting map to JSON", e);
        }
    }

    /**
     * Преобразует JSON-строку в объект типа Map<Long, Deque<BotCommand>>.
     *
     * @param dbData JSON-строка для преобразования
     * @return объект типа Map<Long, Deque<BotCommand>>
     * @throws RuntimeException если произошла ошибка при преобразовании
     */
    public Map<Long, Deque<BotCommand>> toCommandStatesMap(String dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            TypeReference<Map<Long, Deque<BotCommand>>> typeRef = new TypeReference<>() {
            };
            return objectMapper.readValue(dbData, typeRef);
        } catch (IOException e) {
            throw new RuntimeException("Error converting JSON to map", e);
        }
    }

    /**
     * Преобразует объект типа Map<Long, Deque<UserReportStates>> в JSON-строку.
     *
     * @param attribute объект для преобразования
     * @return JSON-строка
     * @throws RuntimeException если произошла ошибка при преобразовании
     */
    public String toUserReportStatesJson(Map<Long, Deque<UserReportStates>> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting map to JSON", e);
        }
    }

    /**
     * Преобразует JSON-строку в объект типа Map<Long, Deque<UserReportStates>>.
     *
     * @param dbData JSON-строка для преобразования
     * @return объект типа Map<Long, Deque<BotCommand>>
     * @throws RuntimeException если произошла ошибка при преобразовании
     */
    public Map<Long, Deque<UserReportStates>> toUserReportStatesMap(String dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            TypeReference<Map<Long, Deque<UserReportStates>>> typeRef = new TypeReference<>() {};
            return objectMapper.readValue(dbData, typeRef);
        } catch (IOException e) {
            throw new RuntimeException("Error converting JSON to map", e);
        }
    }
}

