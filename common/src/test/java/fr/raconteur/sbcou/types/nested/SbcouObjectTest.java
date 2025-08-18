package fr.raconteur.sbcou.types.nested;

import fr.raconteur.sbcou.BaseTest;
import fr.raconteur.sbcou.types.SbcouData;
import fr.raconteur.sbcou.types.primitives.SbcouInteger;
import fr.raconteur.sbcou.types.primitives.SbcouString;
import fr.raconteur.sbcou.types.primitives.SbcouBoolean;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;
import java.util.Map;
import java.util.HashMap;

@DisplayName("SbcouObject Tests")
class SbcouObjectTest extends BaseTest {

    @Test
    @DisplayName("Should create empty SbcouObject with default constructor")
    void shouldCreateEmptyObjectWithDefaultConstructor() {
        // When
        SbcouObject sbcouObject = new SbcouObject();
        
        // Then
        assertNotNull(sbcouObject);
        assertTrue(sbcouObject.getValue().isEmpty());
        assertEquals("{\r\n    \r\n}", sbcouObject.getDisplayValue());
    }

    @Test
    @DisplayName("Should create empty SbcouObject with empty map")
    void shouldCreateEmptyObjectWithEmptyMap() {
        // Given
        Map<String, SbcouData<?>> emptyMap = new HashMap<>();
        
        // When
        SbcouObject sbcouObject = new SbcouObject(emptyMap);
        
        // Then
        assertNotNull(sbcouObject);
        assertTrue(sbcouObject.getValue().isEmpty());
        assertEquals("{\r\n    \r\n}", sbcouObject.getDisplayValue());
    }

    @Test
    @DisplayName("Should create SbcouObject with single key-value pair")
    void shouldCreateWithSingleKeyValue() {
        // Given
        Map<String, SbcouData<?>> map = Map.of("key1", new SbcouInteger(42));
        
        // When
        SbcouObject sbcouObject = new SbcouObject(map);
        
        // Then
        assertNotNull(sbcouObject);
        assertEquals(1, sbcouObject.getValue().size());
        assertEquals(BigInteger.valueOf(42), sbcouObject.getValue().get("key1").getValue());
    }

    @Test
    @DisplayName("Should create SbcouObject with multiple key-value pairs")
    void shouldCreateWithMultipleKeyValues() {
        // Given
        Map<String, SbcouData<?>> map = Map.of(
            "intKey", new SbcouInteger(10),
            "stringKey", new SbcouString("hello"),
            "boolKey", new SbcouBoolean(true)
        );
        
        // When
        SbcouObject sbcouObject = new SbcouObject(map);
        
        // Then
        assertNotNull(sbcouObject);
        assertEquals(3, sbcouObject.getValue().size());
        assertEquals(BigInteger.valueOf(10), sbcouObject.getValue().get("intKey").getValue());
        assertEquals("hello", sbcouObject.getValue().get("stringKey").getValue());
        assertEquals(true, sbcouObject.getValue().get("boolKey").getValue());
    }

    @Test
    @DisplayName("Should return immutable map from getValue")
    void shouldReturnImmutableMap() {
        // Given
        Map<String, SbcouData<?>> map = Map.of("key", new SbcouInteger(42));
        SbcouObject sbcouObject = new SbcouObject(map);
        
        // When
        Map<String, SbcouData<?>> value = sbcouObject.getValue();
        
        // Then
        assertThrows(UnsupportedOperationException.class, () -> {
            value.put("newKey", new SbcouInteger(100));
        });
    }

    @Test
    @DisplayName("Should be equal when created with same key-value pairs")
    void shouldBeEqualWhenCreatedWithSameKeyValues() {
        // Given
        Map<String, SbcouData<?>> map1 = Map.of(
            "key1", new SbcouInteger(42),
            "key2", new SbcouString("test")
        );
        Map<String, SbcouData<?>> map2 = Map.of(
            "key1", new SbcouInteger(42),
            "key2", new SbcouString("test")
        );
        
        // When
        SbcouObject sbcouObject1 = new SbcouObject(map1);
        SbcouObject sbcouObject2 = new SbcouObject(map2);
        
        // Then
        assertTrue(sbcouObject1.equals(sbcouObject2));
        assertEquals(sbcouObject1.hashCode(), sbcouObject2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when created with different key-value pairs")
    void shouldNotBeEqualWhenCreatedWithDifferentKeyValues() {
        // Given
        SbcouObject sbcouObject1 = new SbcouObject(Map.of("key", new SbcouInteger(1)));
        SbcouObject sbcouObject2 = new SbcouObject(Map.of("key", new SbcouInteger(2)));
        
        // Then
        assertFalse(sbcouObject1.equals(sbcouObject2));
    }

    @Test
    @DisplayName("Should not be equal when created with different keys")
    void shouldNotBeEqualWhenCreatedWithDifferentKeys() {
        // Given
        SbcouObject sbcouObject1 = new SbcouObject(Map.of("key1", new SbcouInteger(42)));
        SbcouObject sbcouObject2 = new SbcouObject(Map.of("key2", new SbcouInteger(42)));
        
        // Then
        assertFalse(sbcouObject1.equals(sbcouObject2));
    }

    @Test
    @DisplayName("Should not be equal to null")
    void shouldNotBeEqualToNull() {
        // Given
        SbcouObject sbcouObject = new SbcouObject(Map.of("key", new SbcouInteger(42)));
        
        // Then
        assertFalse(sbcouObject.equals(null));
    }

    @Test
    @DisplayName("Should be equal to itself")
    void shouldBeEqualToItself() {
        // Given
        SbcouObject sbcouObject = new SbcouObject(Map.of("key", new SbcouInteger(42)));
        
        // Then
        assertTrue(sbcouObject.equals(sbcouObject));
        assertEquals(sbcouObject.hashCode(), sbcouObject.hashCode());
    }

    @Test
    @DisplayName("Should format display value correctly")
    void shouldFormatDisplayValueCorrectly() {
        // Given
        SbcouObject sbcouObject = new SbcouObject(Map.of(
            "name", new SbcouString("John"),
            "age", new SbcouInteger(30)
        ));
        
        // When
        String displayValue = sbcouObject.getDisplayValue();
        
        // Then
        assertTrue(displayValue.startsWith("{\r\n"));
        assertTrue(displayValue.endsWith("\r\n}"));
        assertTrue(displayValue.contains("name : John") || displayValue.contains("age : 30"));
    }

    @Test
    @DisplayName("Should handle nested objects")
    void shouldHandleNestedObjects() {
        // Given
        SbcouObject innerObject = new SbcouObject(Map.of("inner", new SbcouInteger(1)));
        SbcouObject outerObject = new SbcouObject(Map.of(
            "nested", innerObject,
            "value", new SbcouString("outer")
        ));
        
        // When & Then
        assertNotNull(outerObject);
        assertEquals(2, outerObject.getValue().size());
        assertTrue(outerObject.getValue().get("nested") instanceof SbcouObject);
        assertTrue(outerObject.getValue().get("value") instanceof SbcouString);
    }

    @Test
    @DisplayName("Should handle keys with special characters")
    void shouldHandleKeysWithSpecialCharacters() {
        // Given
        Map<String, SbcouData<?>> map = Map.of(
            "key:with:colons", new SbcouInteger(1),
            "key,with,commas", new SbcouString("test"),
            "key\\with\\backslashes", new SbcouBoolean(true)
        );
        
        // When
        SbcouObject sbcouObject = new SbcouObject(map);
        
        // Then
        assertNotNull(sbcouObject);
        assertEquals(3, sbcouObject.getValue().size());
        assertEquals(BigInteger.valueOf(1), sbcouObject.getValue().get("key:with:colons").getValue());
        assertEquals("test", sbcouObject.getValue().get("key,with,commas").getValue());
        assertEquals(true, sbcouObject.getValue().get("key\\with\\backslashes").getValue());
    }

    @Test
    @DisplayName("Should handle mixed value types")
    void shouldHandleMixedValueTypes() {
        // Given
        SbcouList list = new SbcouList(new SbcouInteger(1), new SbcouInteger(2));
        SbcouObject nestedObject = new SbcouObject(Map.of("nested", new SbcouString("value")));
        
        Map<String, SbcouData<?>> map = Map.of(
            "integer", new SbcouInteger(42),
            "string", new SbcouString("hello"),
            "boolean", new SbcouBoolean(false),
            "list", list,
            "object", nestedObject
        );
        
        // When
        SbcouObject sbcouObject = new SbcouObject(map);
        
        // Then
        assertNotNull(sbcouObject);
        assertEquals(5, sbcouObject.getValue().size());
        assertEquals(BigInteger.valueOf(42), sbcouObject.getValue().get("integer").getValue());
        assertEquals("hello", sbcouObject.getValue().get("string").getValue());
        assertEquals(false, sbcouObject.getValue().get("boolean").getValue());
        assertTrue(sbcouObject.getValue().get("list") instanceof SbcouList);
        assertTrue(sbcouObject.getValue().get("object") instanceof SbcouObject);
    }

    @Test
    @DisplayName("Should be equal when key order differs")
    void shouldBeEqualWhenKeyOrderDiffers() {
        // Given
        Map<String, SbcouData<?>> map1 = new HashMap<>();
        map1.put("a", new SbcouInteger(1));
        map1.put("b", new SbcouInteger(2));
        
        Map<String, SbcouData<?>> map2 = new HashMap<>();
        map2.put("b", new SbcouInteger(2));
        map2.put("a", new SbcouInteger(1));
        
        // When
        SbcouObject sbcouObject1 = new SbcouObject(map1);
        SbcouObject sbcouObject2 = new SbcouObject(map2);
        
        // Then
        assertTrue(sbcouObject1.equals(sbcouObject2));
        assertEquals(sbcouObject1.hashCode(), sbcouObject2.hashCode());
    }
}