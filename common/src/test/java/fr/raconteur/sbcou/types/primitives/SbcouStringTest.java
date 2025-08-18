package fr.raconteur.sbcou.types.primitives;

import fr.raconteur.sbcou.BaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SbcouString Tests")
class SbcouStringTest extends BaseTest {

    @Test
    @DisplayName("Should create SbcouString from string value")
    void shouldCreateFromStringValue() {
        // Given
        String value = "Hello World";
        
        // When
        SbcouString sbcouString = new SbcouString(value);
        
        // Then
        assertNotNull(sbcouString);
        assertEquals(value, sbcouString.getValue());
        assertEquals("Hello World", sbcouString.getDisplayValue());
    }

    @Test
    @DisplayName("Should create SbcouString from object value")
    void shouldCreateFromObjectValue() {
        // Given
        Integer objectValue = 42;
        
        // When
        SbcouString sbcouString = new SbcouString(objectValue);
        
        // Then
        assertNotNull(sbcouString);
        assertEquals("42", sbcouString.getValue());
        assertEquals("42", sbcouString.getDisplayValue());
    }

    @Test
    @DisplayName("Should create SbcouString from empty string")
    void shouldCreateFromEmptyString() {
        // Given
        String value = "";
        
        // When
        SbcouString sbcouString = new SbcouString(value);
        
        // Then
        assertNotNull(sbcouString);
        assertEquals("", sbcouString.getValue());
        assertEquals("", sbcouString.getDisplayValue());
    }

    @Test
    @DisplayName("Should create SbcouString with special characters")
    void shouldCreateWithSpecialCharacters() {
        // Given
        String value = "Hello \"World\" with \n newlines and \t tabs";
        
        // When
        SbcouString sbcouString = new SbcouString(value);
        
        // Then
        assertNotNull(sbcouString);
        assertEquals(value, sbcouString.getValue());
        assertEquals(value, sbcouString.getDisplayValue());
    }

    @Test
    @DisplayName("Should be equal when created with same string value")
    void shouldBeEqualWhenCreatedWithSameValue() {
        // Given
        String value = "test";
        
        // When
        SbcouString sbcouString1 = new SbcouString(value);
        SbcouString sbcouString2 = new SbcouString(value);
        
        // Then
        assertTrue(sbcouString1.equals(sbcouString2));
        assertEquals(sbcouString1.hashCode(), sbcouString2.hashCode());
    }

    @Test
    @DisplayName("Should be equal when created with equivalent values from different constructors")
    void shouldBeEqualWhenCreatedWithEquivalentValues() {
        // Given
        String stringValue = "42";
        Integer objectValue = 42;
        
        // When
        SbcouString fromString = new SbcouString(stringValue);
        SbcouString fromObject = new SbcouString(objectValue);
        
        // Then
        assertTrue(fromString.equals(fromObject));
        assertEquals(fromString.hashCode(), fromObject.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when created with different values")
    void shouldNotBeEqualWhenCreatedWithDifferentValues() {
        // Given
        SbcouString sbcouString1 = new SbcouString("hello");
        SbcouString sbcouString2 = new SbcouString("world");
        
        // Then
        assertFalse(sbcouString1.equals(sbcouString2));
    }

    @Test
    @DisplayName("Should not be equal to null")
    void shouldNotBeEqualToNull() {
        // Given
        SbcouString sbcouString = new SbcouString("test");
        
        // Then
        assertFalse(sbcouString.equals(null));
    }

    @Test
    @DisplayName("Should be equal to itself")
    void shouldBeEqualToItself() {
        // Given
        SbcouString sbcouString = new SbcouString("test");
        
        // Then
        assertTrue(sbcouString.equals(sbcouString));
        assertEquals(sbcouString.hashCode(), sbcouString.hashCode());
    }

    @Test
    @DisplayName("Should handle null object value")
    void shouldHandleNullObjectValue() {
        // Given & When & Then
        assertThrows(NullPointerException.class, () -> {
            new SbcouString((Object) null);
        });
    }
}