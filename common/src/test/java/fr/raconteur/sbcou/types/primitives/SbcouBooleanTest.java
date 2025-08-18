package fr.raconteur.sbcou.types.primitives;

import fr.raconteur.sbcou.BaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SbcouBoolean Tests")
class SbcouBooleanTest extends BaseTest {

    @Test
    @DisplayName("Should create SbcouBoolean from Boolean value")
    void shouldCreateFromBooleanValue() {
        // Given
        Boolean value = true;
        
        // When
        SbcouBoolean sbcouBoolean = new SbcouBoolean(value);
        
        // Then
        assertNotNull(sbcouBoolean);
        assertEquals(value, sbcouBoolean.getValue());
        assertEquals("true", sbcouBoolean.getDisplayValue());
    }

    @Test
    @DisplayName("Should create SbcouBoolean from boolean primitive")
    void shouldCreateFromBooleanPrimitive() {
        // Given
        boolean value = false;
        
        // When
        SbcouBoolean sbcouBoolean = new SbcouBoolean(value);
        
        // Then
        assertNotNull(sbcouBoolean);
        assertEquals(false, sbcouBoolean.getValue());
        assertEquals("false", sbcouBoolean.getDisplayValue());
    }

    @Test
    @DisplayName("Should create SbcouBoolean from string value")
    void shouldCreateFromStringValue() {
        // Given
        String value = "true";
        
        // When
        SbcouBoolean sbcouBoolean = new SbcouBoolean(value);
        
        // Then
        assertNotNull(sbcouBoolean);
        assertEquals(true, sbcouBoolean.getValue());
        assertEquals("true", sbcouBoolean.getDisplayValue());
    }

    @Test
    @DisplayName("Should create SbcouBoolean from false string")
    void shouldCreateFromFalseString() {
        // Given
        String value = "false";
        
        // When
        SbcouBoolean sbcouBoolean = new SbcouBoolean(value);
        
        // Then
        assertNotNull(sbcouBoolean);
        assertEquals(false, sbcouBoolean.getValue());
        assertEquals("false", sbcouBoolean.getDisplayValue());
    }

    @Test
    @DisplayName("Should parse non-true strings as false")
    void shouldParseNonTrueStringsAsFalse() {
        // Given
        String[] falseValues = {"false", "0", "no", "off", "random", ""};
        
        for (String value : falseValues) {
            // When
            SbcouBoolean sbcouBoolean = new SbcouBoolean(value);
            
            // Then
            assertEquals(false, sbcouBoolean.getValue(), "Value '" + value + "' should parse as false");
            assertEquals("false", sbcouBoolean.getDisplayValue());
        }
    }

    @Test
    @DisplayName("Should be equal when created with same boolean value")
    void shouldBeEqualWhenCreatedWithSameValue() {
        // Given
        boolean value = true;
        
        // When
        SbcouBoolean sbcouBoolean1 = new SbcouBoolean(value);
        SbcouBoolean sbcouBoolean2 = new SbcouBoolean(value);
        
        // Then
        assertTrue(sbcouBoolean1.equals(sbcouBoolean2));
        assertEquals(sbcouBoolean1.hashCode(), sbcouBoolean2.hashCode());
    }

    @Test
    @DisplayName("Should be equal when created with equivalent values from different constructors")
    void shouldBeEqualWhenCreatedWithEquivalentValues() {
        // Given
        Boolean booleanValue = true;
        boolean primitiveValue = true;
        String stringValue = "true";
        
        // When
        SbcouBoolean fromBoolean = new SbcouBoolean(booleanValue);
        SbcouBoolean fromPrimitive = new SbcouBoolean(primitiveValue);
        SbcouBoolean fromString = new SbcouBoolean(stringValue);
        
        // Then
        assertTrue(fromBoolean.equals(fromPrimitive));
        assertTrue(fromBoolean.equals(fromString));
        assertTrue(fromPrimitive.equals(fromString));
        
        // Verify hash codes are equal too
        assertEquals(fromBoolean.hashCode(), fromPrimitive.hashCode());
        assertEquals(fromBoolean.hashCode(), fromString.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when created with different values")
    void shouldNotBeEqualWhenCreatedWithDifferentValues() {
        // Given
        SbcouBoolean sbcouBooleanTrue = new SbcouBoolean(true);
        SbcouBoolean sbcouBooleanFalse = new SbcouBoolean(false);
        
        // Then
        assertFalse(sbcouBooleanTrue.equals(sbcouBooleanFalse));
    }

    @Test
    @DisplayName("Should not be equal to null")
    void shouldNotBeEqualToNull() {
        // Given
        SbcouBoolean sbcouBoolean = new SbcouBoolean(true);
        
        // Then
        assertFalse(sbcouBoolean.equals(null));
    }

    @Test
    @DisplayName("Should be equal to itself")
    void shouldBeEqualToItself() {
        // Given
        SbcouBoolean sbcouBoolean = new SbcouBoolean(true);
        
        // Then
        assertTrue(sbcouBoolean.equals(sbcouBoolean));
        assertEquals(sbcouBoolean.hashCode(), sbcouBoolean.hashCode());
    }
}