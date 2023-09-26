package pro.sky.AnimalShelter.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;

/**
 * Десериализатор для преобразования JSON-массива строк в Deque перечисления (Enum).
 * Этот десериализатор принимает JSON-массив строк, представляющих значения перечисления, и преобразует их в Deque,
 * сохраняя порядок элементов массива.
 *
 * @param <T> Тип перечисления (Enum), который будет десериализован.
 */
public class EnumDequeDeserializer<T extends Enum> extends JsonDeserializer<Deque<T>> {

    /**
     * Класс перечисления (Enum), который будет десериализован.
     */
    private final Class<T> enumClass;

    /**
     * Конструктор класса десериализатора.
     *
     * @param enumClass Класс перечисления (Enum), который будет десериализован.
     */
    public EnumDequeDeserializer(Class<T> enumClass) {
        this.enumClass = enumClass;
    }

    /**
     * Метод десериализации JSON-массива строк в Deque перечисления (Enum).
     *
     * @param jsonParser             Парсер JSON.
     * @param deserializationContext Контекст десериализации.
     * @return Deque перечисления (Enum) с десериализованными значениями.
     * @throws IOException Возникает, если возникают ошибки при чтении JSON или десериализации.
     */
    @Override
    public Deque<T> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        Deque<T> deque = new LinkedList<>();
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        if (node.isArray()) {
            ArrayNode arrayNode = (ArrayNode) node;
            for (JsonNode element : arrayNode) {
                if (element.isTextual()) {
                    String enumValue = element.asText();
                    try {
                        T enumInstance = (T) Enum.valueOf(enumClass, enumValue);
                        deque.add(enumInstance);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return deque;
    }
}
