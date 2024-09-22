
# JSON Parser

This project provides a simple JSON parser implemented in Java. It allows for converting JSON strings into Java objects and vice versa, without using any external libraries.

## Features

### Parse JSON String to Java Object:
- Convert a JSON string into a `Map<String, Object>`.
- Convert a JSON string into an instance of a specified class.

### Convert Java Object to JSON String:
- Serialize Java objects (maps and collections) back to JSON string format.

### Supported Java Types:
- Primitive types (e.g., `int`, `double`, `boolean`)
- Boxed types (e.g., `Integer`, `Double`, `Boolean`)
- `null` values
- Arrays
- Collections (e.g., `List`, `Set`, `Map`)
- Nested objects and arrays within JSON

## Limitations
- **Cyclic Dependencies**: Cyclic dependencies between objects are not handled.
- **Non-representable JSON Types**: Special numeric values like `NaN` and `Infinity` are not supported by the parser.

## Classes

### `JsonParser`
The main class that provides the public API for parsing JSON strings to objects and converting objects back to JSON strings. It relies on the `JsonObjectMapper` and `JsonTokenizer` for internal processing.

### `JsonObjectMapper`
Responsible for the conversion between JSON tokens and Java objects, including:
- Mapping JSON strings to Java `Map<String, Object>`.
- Converting `Map<String, Object>` back to JSON strings.
- Handling complex/nested objects and arrays.

### `JsonTokenizer`
Splits a JSON string into individual tokens to facilitate parsing.

## Test Cases

The `JsonParserTest` class includes a comprehensive set of unit tests using JUnit 5, covering various cases:

- Parsing simple and complex JSON objects.
- Handling arrays of primitives and objects.
- Working with `null` values.
- Validating the correctness of JSON string conversions.
