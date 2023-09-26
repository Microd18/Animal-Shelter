package pro.sky.AnimalShelter.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Deque;
import java.util.Map;

/**
 * Класс-конвертер для преобразования между JSON и объектами типа Map<Long, Deque<>>.
 */
@Component
public class JsonMapConverter<T extends Enum<?>> {


    /**
     * Объект ObjectMapper используется для сериализации и десериализации JSON.
     */
    private final ObjectMapper objectMapper;

    public JsonMapConverter() {
        this.objectMapper = new ObjectMapper();
    }

    public JsonMapConverter(Class<T> enumClass) {
        this.objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Deque.class, new EnumDequeDeserializer<>(enumClass));
        objectMapper.registerModule(module);
    }

    /**
     * Преобразует объект типа Map<Long, Deque<>> в JSON-строку.
     *
     * @param attribute объект для преобразования
     * @return JSON-строка
     * @throws RuntimeException если произошла ошибка при преобразовании
     */
    public String toJson(Map<Long, Deque<T>> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting map to JSON", e);
        }
    }

    /**
     * Преобразует JSON-строку в объект типа Map<Long, Deque<>>.
     *
     * @param dbData JSON-строка для преобразования
     * @return объект типа Map<Long, Deque<>>
     * @throws RuntimeException если произошла ошибка при преобразовании
     */
    public Map<Long, Deque<T>> toMap(String dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            TypeReference<Map<Long, Deque<T>>> typeRef = new TypeReference<>() {};
            return objectMapper.readValue(dbData, typeRef);
        } catch (IOException e) {
            throw new RuntimeException("Error converting JSON to map", e);
        }
    }
}

