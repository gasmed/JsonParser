package org.orlov;

import java.util.Map;

public class JsonParser {

    private JsonTokenizer tokenizer = new JsonTokenizer();
    private JsonObjectMapper objectMapper = new JsonObjectMapper();

    public Object parse(String json) throws Exception {
        return objectMapper.toMap(json);
    }

    public <T> T parseToObject(String json, Class<T> clazz) throws Exception {
        Map<String, Object> map = objectMapper.toMap(json);
        return objectMapper.toClassObject(map, clazz);
    }

    public String toJson(Object object) throws Exception {
        return objectMapper.toJsonString(object);
    }
}
