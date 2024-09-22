import org.junit.jupiter.api.Test;
import org.orlov.JsonParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class JsonParserTest {

    JsonParser parser = new JsonParser();

    @Test
    void testParseSimpleObject() throws Exception {
        String jsonString = "{\"name\": \"John\", \"age\": 30, \"isActive\": true}";
        Map<String, Object> result = (Map<String, Object>) parser.parse(jsonString);

        assertEquals("John", result.get("name"));
        assertEquals(30, result.get("age"));
        assertEquals(true, result.get("isActive"));
    }

    @Test
    void testParseNestedObject() throws Exception {
        String jsonString = "{\"user\": {\"name\": \"John\", \"age\": 30}, \"isActive\": true}";
        Map<String, Object> result = (Map<String, Object>) parser.parse(jsonString);

        Map<String, Object> user = (Map<String, Object>) result.get("user");
        assertEquals("John", user.get("name"));
        assertEquals(30, user.get("age"));
        assertEquals(true, result.get("isActive"));
    }

    @Test
    void testParseArrayOfPrimitives() throws Exception {
        String jsonString = "{\"friends\": [\"Jane\", \"Doe\", \"Smith\"]}";
        Map<String, Object> result = (Map<String, Object>) parser.parse(jsonString);

        List<Object> friends = (List<Object>) result.get("friends");
        assertEquals(3, friends.size());
        assertEquals("Jane", friends.get(0));
        assertEquals("Doe", friends.get(1));
        assertEquals("Smith", friends.get(2));
    }

    @Test
    void testParseArrayOfObjects() throws Exception {
        String jsonString = "{\"users\": [{\"name\": \"John\", \"age\": 30}, {\"name\": \"Jane\", \"age\": 25}]}";
        Map<String, Object> result = (Map<String, Object>) parser.parse(jsonString);

        List<Object> users = (List<Object>) result.get("users");
        assertEquals(2, users.size());

        Map<String, Object> user1 = (Map<String, Object>) users.get(0);
        Map<String, Object> user2 = (Map<String, Object>) users.get(1);

        assertEquals("John", user1.get("name"));
        assertEquals(30, user1.get("age"));
        assertEquals("Jane", user2.get("name"));
        assertEquals(25, user2.get("age"));
    }

    @Test
    void testParseWithNullValues() throws Exception {
        String jsonString = "{\"name\": \"John\", \"age\": null, \"isActive\": true}";
        Map<String, Object> result = (Map<String, Object>) parser.parse(jsonString);

        assertEquals("John", result.get("name"));
        assertNull(result.get("age"));
        assertEquals(true, result.get("isActive"));
    }

    @Test
    void testInvalidJson() {
        String invalidJsonString = "{\"name\": \"John\", \"age\": 30, \"isActive\": true";

        Exception exception = assertThrows(Exception.class, () -> {
            parser.parse(invalidJsonString);
        });

        String expectedMessage = "Некорректный JSON";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testEmptyJson() throws Exception {
        String jsonString = "{}";
        Map<String, Object> result = (Map<String, Object>) parser.parse(jsonString);

        assertTrue(result.isEmpty());
    }

    @Test
    void testComplexJson() throws Exception {
        String jsonString = "{\"name\": \"John\", \"details\": {\"age\": 30, \"hobbies\": [\"reading\", \"coding\"]}, \"isActive\": true}";
        Map<String, Object> result = (Map<String, Object>) parser.parse(jsonString);

        assertEquals("John", result.get("name"));

        Map<String, Object> details = (Map<String, Object>) result.get("details");
        assertEquals(30, details.get("age"));

        List<Object> hobbies = (List<Object>) details.get("hobbies");
        assertEquals(2, hobbies.size());
        assertEquals("reading", hobbies.get(0));
        assertEquals("coding", hobbies.get(1));

        assertEquals(true, result.get("isActive"));
    }

    @Test
    void testToJsonStringConversion() throws Exception {
        Map<String, Object> object = Map.of(
                "name", "John",
                "age", 30,
                "isActive", true,
                "friends", List.of("Jane", "Doe")
        );

        String jsonString = parser.toJson(object);

        String expectedJsonString = "{\"name\": \"John\", \"age\": 30, \"isActive\": true, \"friends\": [\"Jane\", \"Doe\"]}";

        Map<String, Object> expectedMap = (Map<String, Object>) parser.parse(expectedJsonString);
        Map<String, Object> actualMap = (Map<String, Object>) parser.parse(jsonString);

        assertEquals(expectedMap, actualMap);
    }

    @Test
    void testToJsonStringWithNestedObjects() throws Exception {
        Map<String, Object> nestedObject = Map.of(
                "user", Map.of("name", "Jane", "age", 25),
                "isActive", false
        );
        String jsonString = parser.toJson(nestedObject);

        String expectedJsonString = "{\"user\": {\"name\": \"Jane\", \"age\": 25}, \"isActive\": false}";

        Map<String, Object> expectedMap = (Map<String, Object>) parser.parse(expectedJsonString);
        Map<String, Object> actualMap = (Map<String, Object>) parser.parse(jsonString);

        assertEquals(expectedMap, actualMap);
    }

    @Test
    void testToJsonStringWithArray() throws Exception {
        Map<String, Object> objectWithArray = Map.of(
                "name", "John",
                "friends", List.of("Jane", "Doe", "Smith")
        );
        String jsonString = parser.toJson(objectWithArray);

        String expectedJsonString = "{\"name\": \"John\", \"friends\": [\"Jane\", \"Doe\", \"Smith\"]}";

        Map<String, Object> expectedMap = (Map<String, Object>) parser.parse(expectedJsonString);
        Map<String, Object> actualMap = (Map<String, Object>) parser.parse(jsonString);

        assertEquals(expectedMap, actualMap);
    }

    @Test
    void testToJsonStringWithNullValues() throws Exception {
        Map<String, Object> objectWithNull = new HashMap<>();
        objectWithNull.put("name", "John");
        objectWithNull.put("age", null);
        objectWithNull.put("isActive", true);

        String jsonString = parser.toJson(objectWithNull);

        String expectedJsonString = "{\"name\": \"John\", \"age\": null, \"isActive\": true}";

        Map<String, Object> expectedMap = (Map<String, Object>) parser.parse(expectedJsonString);
        Map<String, Object> actualMap = (Map<String, Object>) parser.parse(jsonString);

        assertEquals(expectedMap, actualMap);
    }


    @Test
    void testToJsonStringWithEmptyObject() throws Exception {
        Map<String, Object> emptyObject = Map.of();
        String jsonString = parser.toJson(emptyObject);

        String expectedJsonString = "{}";

        Map<String, Object> expectedMap = (Map<String, Object>) parser.parse(expectedJsonString);
        Map<String, Object> actualMap = (Map<String, Object>) parser.parse(jsonString);

        assertEquals(expectedMap, actualMap);
    }

    @Test
    void testParseToObject() throws Exception {
        String jsonString = "{\"name\": \"John\", \"age\": 30}";
        Person result = parser.parseToObject(jsonString, Person.class);

        assertEquals("John", result.getName());
        assertEquals(30, result.getAge());
    }
}

class Person {
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
