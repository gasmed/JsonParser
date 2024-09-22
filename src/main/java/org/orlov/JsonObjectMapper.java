package org.orlov;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;

public class JsonObjectMapper {

    public Map<String, Object> toMap(String json) throws Exception {
        JsonTokenizer tokenizer = new JsonTokenizer();
        List<String> tokens = tokenizer.tokenize(json);

        if (tokens.isEmpty() || !tokens.get(0).equals("{")) {
            throw new IllegalArgumentException("Некорректный JSON: должен начинаться с '{'");
        }

        return parseObject(tokens, new int[]{1});
    }

    public <T> T toClassObject(Map<String, Object> map, Class<T> clazz) throws Exception {
        Constructor<T> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        T object = constructor.newInstance();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String fieldName = entry.getKey();
            Object fieldValue = entry.getValue();

            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);

            if (fieldValue instanceof Map) {
                Class<?> fieldType = field.getType();
                Object nestedObject = toClassObject((Map<String, Object>) fieldValue, fieldType);
                field.set(object, nestedObject);
            } else {
                field.set(object, fieldValue);
            }
        }
        return object;
    }

    private Map<String, Object> parseObject(List<String> tokens, int[] index) throws Exception {
        Map<String, Object> map = new HashMap<>();

        while (index[0] < tokens.size()) {
            String token = tokens.get(index[0]).trim();

            if (token.equals("}")) {
                index[0]++;
                return map;
            }

            if (token.startsWith("\"") && token.endsWith("\"")) {
                String key = token.substring(1, token.length() - 1);
                index[0]++;

                if (index[0] >= tokens.size() ||!tokens.get(index[0]).equals(":")) {
                    throw new IllegalArgumentException("Ожидается ':' после ключа");
                }
                index[0]++;

                if (index[0] >= tokens.size()) {
                    throw new IllegalArgumentException("Некорректный JSON: значение отсутствует");
                }
                Object value = parseValue(tokens, index);
                map.put(key, value);

                if (index[0] < tokens.size() && tokens.get(index[0]).equals(",")) {
                    index[0]++;
                }
            }
        }

        throw new IllegalArgumentException("Некорректный JSON: отсутствует закрывающая '}'");
    }

    private Object parseValue(List<String> tokens, int[] index) throws Exception {
        String token = tokens.get(index[0]).trim();

        if (token.equals("{")) {
            index[0]++;
            return parseObject(tokens, index);
        } else if (token.equals("[")) {
            index[0]++;
            return parseArray(tokens, index);
        } else if (token.startsWith("\"") && token.endsWith("\"")) {
            index[0]++;
            return token.substring(1, token.length() - 1);
        } else if (token.equals("true") || token.equals("false")) {
            index[0]++;
            return Boolean.parseBoolean(token);
        } else if (token.equals("null")) {
            index[0]++;
            return null;
        } else if (token.equals("NaN") || token.equals("Infinity")) {
            throw new UnsupportedOperationException("Тип " + token + " не поддерживается");
        } else {
            index[0]++;
            return parseNumber(token);
        }
    }

    private List<Object> parseArray(List<String> tokens, int[] index) throws Exception {
        List<Object> list = new ArrayList<>();

        while (index[0] < tokens.size()) {
            String token = tokens.get(index[0]).trim();

            if (token.equals("]")) {
                index[0]++;
                return list;
            }

            Object value = parseValue(tokens, index);
            list.add(value);

            if (tokens.get(index[0]).equals(",")) {
                index[0]++;
            }
        }

        throw new IllegalArgumentException("Некорректный JSON: отсутствует закрывающая ']'");
    }

    private Number parseNumber(String token) {
        try {
            if (token.contains(".")) {
                return Double.parseDouble(token);
            } else {
                return Integer.parseInt(token);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Некорректное числовое значение: " + token);
        }
    }

    public String toJsonString(Object object) throws Exception {
        if (object instanceof Map) {
            return mapToJsonString((Map<String, Object>) object);
        } else if (object instanceof List) {
            return arrayToJsonString((List<Object>) object);
        } else {
            throw new UnsupportedOperationException("Тип " + object.getClass() + " не поддерживается");
        }
    }

    private String mapToJsonString(Map<String, Object> map) {
        StringBuilder json = new StringBuilder("{");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            json.append("\"").append(entry.getKey()).append("\": ")
                    .append(valueToJsonString(entry.getValue())).append(",");
        }
        if (json.length() > 1) json.deleteCharAt(json.length() - 1);
        json.append("}");
        return json.toString();
    }

    private String arrayToJsonString(List<Object> list) {
        StringBuilder json = new StringBuilder("[");
        for (Object item : list) {
            json.append(valueToJsonString(item)).append(",");
        }
        if (json.length() > 1) json.deleteCharAt(json.length() - 1);
        json.append("]");
        return json.toString();
    }

    private String valueToJsonString(Object value) {
        if (value == null) {
            return "null";
        } else if (value instanceof String) {
            return "\"" + value + "\"";
        } else if (value instanceof Number || value instanceof Boolean) {
            return value.toString();
        } else if (value instanceof Map) {
            return mapToJsonString((Map<String, Object>) value);
        } else if (value instanceof List) {
            return arrayToJsonString((List<Object>) value);
        } else {
            throw new UnsupportedOperationException("Тип " + value.getClass() + " не поддерживается");
        }
    }
}
