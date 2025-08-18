package fr.raconteur.sbcou.types.primitives;

import fr.raconteur.sbcou.BaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

@DisplayName("SbcouReal Tests")
class SbcouRealTest extends BaseTest {

    @Test
    @DisplayName("Should create SbcouReal from BigDecimal value")
    void shouldCreateFromBigDecimalValue() {
        // Given
        BigDecimal value = new BigDecimal("123.456");
        
        // When
        SbcouReal sbcouReal = new SbcouReal(value);
        
        // Then
        assertNotNull(sbcouReal);
        assertEquals(value, sbcouReal.getValue());
        assertEquals("123.456", sbcouReal.getDisplayValue());
    }

    @Test
    @DisplayName("Should create SbcouReal from int value")
    void shouldCreateFromIntValue() {
        // Given
        int value = 42;
        
        // When
        SbcouReal sbcouReal = new SbcouReal(value);
        
        // Then
        assertNotNull(sbcouReal);
        assertEquals(BigDecimal.valueOf(value), sbcouReal.getValue());
        assertEquals("42", sbcouReal.getDisplayValue());
    }

    @Test
    @DisplayName("Should create SbcouReal from long value")
    void shouldCreateFromLongValue() {
        // Given
        long value = 9876543210L;
        
        // When
        SbcouReal sbcouReal = new SbcouReal(value);
        
        // Then
        assertNotNull(sbcouReal);
        assertEquals(BigDecimal.valueOf(value), sbcouReal.getValue());
        assertEquals("9876543210", sbcouReal.getDisplayValue());
    }

    @Test
    @DisplayName("Should create SbcouReal from double value")
    void shouldCreateFromDoubleValue() {
        // Given
        double value = 3.14159;
        
        // When
        SbcouReal sbcouReal = new SbcouReal(value);
        
        // Then
        assertNotNull(sbcouReal);
        assertEquals(BigDecimal.valueOf(value), sbcouReal.getValue());
        assertEquals("3.14159", sbcouReal.getDisplayValue());
    }

    @Test
    @DisplayName("Should create SbcouReal from float value")
    void shouldCreateFromFloatValue() {
        // Given
        float value = 2.718f;
        
        // When
        SbcouReal sbcouReal = new SbcouReal(value);
        
        // Then
        assertNotNull(sbcouReal);
        assertEquals(BigDecimal.valueOf(value), sbcouReal.getValue());
    }

    @Test
    @DisplayName("Should create SbcouReal from string value")
    void shouldCreateFromStringValue() {
        // Given
        String value = "999.999";
        
        // When
        SbcouReal sbcouReal = new SbcouReal(value);
        
        // Then
        assertNotNull(sbcouReal);
        assertEquals(new BigDecimal(value), sbcouReal.getValue());
        assertEquals("999.999", sbcouReal.getDisplayValue());
    }

    @Test
    @DisplayName("Should create SbcouReal from negative values")
    void shouldCreateFromNegativeValues() {
        // Given
        double negativeValue = -42.5;
        
        // When
        SbcouReal sbcouReal = new SbcouReal(negativeValue);
        
        // Then
        assertNotNull(sbcouReal);
        assertEquals(BigDecimal.valueOf(negativeValue), sbcouReal.getValue());
        assertEquals("-42.5", sbcouReal.getDisplayValue());
    }

    @Test
    @DisplayName("Should create SbcouReal from zero")
    void shouldCreateFromZero() {
        // Given
        int value = 0;
        
        // When
        SbcouReal sbcouReal = new SbcouReal(value);
        
        // Then
        assertNotNull(sbcouReal);
        assertEquals(BigDecimal.ZERO, sbcouReal.getValue());
        assertEquals("0", sbcouReal.getDisplayValue());
    }

    @Test
    @DisplayName("Should throw exception for invalid string value")
    void shouldThrowExceptionForInvalidStringValue() {
        // Given
        String invalidValue = "not-a-number";
        
        // When & Then
        assertThrows(NumberFormatException.class, () -> {
            new SbcouReal(invalidValue);
        });
    }

    @Test
    @DisplayName("Should handle very large decimal numbers")
    void shouldHandleVeryLargeDecimalNumbers() {
        // Given
        String largeNumber = "99999999999999999999999999999999999999999999999999.123456789";
        
        // When
        SbcouReal sbcouReal = new SbcouReal(largeNumber);
        
        // Then
        assertNotNull(sbcouReal);
        assertEquals(new BigDecimal(largeNumber), sbcouReal.getValue());
        assertEquals(largeNumber, sbcouReal.getDisplayValue());
    }

    @Test
    @DisplayName("Should be equal when created with same real value")
    void shouldBeEqualWhenCreatedWithSameValue() {
        // Given
        double value = 3.14159;
        
        // When
        SbcouReal sbcouReal1 = new SbcouReal(value);
        SbcouReal sbcouReal2 = new SbcouReal(value);
        
        // Then
        assertTrue(sbcouReal1.equals(sbcouReal2));
        assertEquals(sbcouReal1.hashCode(), sbcouReal2.hashCode());
    }

    @Test
    @DisplayName("Should be equal when created with equivalent values from different constructors")
    void shouldBeEqualWhenCreatedWithEquivalentValues() {
        // Given
        int intValue = 42;
        long longValue = 42L;
        double doubleValue = 42.0;
        float floatValue = 42.0f;
        String stringValue = "42";
        BigDecimal bigDecimalValue = new BigDecimal("42");
        
        // When
        SbcouReal fromInt = new SbcouReal(intValue);
        SbcouReal fromLong = new SbcouReal(longValue);
        SbcouReal fromDouble = new SbcouReal(doubleValue);
        SbcouReal fromFloat = new SbcouReal(floatValue);
        SbcouReal fromString = new SbcouReal(stringValue);
        SbcouReal fromBigDecimal = new SbcouReal(bigDecimalValue);
        
        // Then
        assertTrue(fromInt.equals(fromLong));
        assertTrue(fromInt.equals(fromString));
        assertTrue(fromInt.equals(fromBigDecimal));

        // assertTrue(fromInt.equals(fromDouble)); False because scale is important for BigDecimal

        assertTrue(fromDouble.equals(fromFloat));
    }

    @Test
    @DisplayName("Should not be equal when created with different values")
    void shouldNotBeEqualWhenCreatedWithDifferentValues() {
        // Given
        SbcouReal sbcouReal1 = new SbcouReal(3.14);
        SbcouReal sbcouReal2 = new SbcouReal(2.71);
        
        // Then
        assertFalse(sbcouReal1.equals(sbcouReal2));
    }

    @Test
    @DisplayName("Should not be equal to null")
    void shouldNotBeEqualToNull() {
        // Given
        SbcouReal sbcouReal = new SbcouReal(3.14);
        
        // Then
        assertFalse(sbcouReal.equals(null));
    }

    @Test
    @DisplayName("Should be equal to itself")
    void shouldBeEqualToItself() {
        // Given
        SbcouReal sbcouReal = new SbcouReal(3.14);
        
        // Then
        assertTrue(sbcouReal.equals(sbcouReal));
        assertEquals(sbcouReal.hashCode(), sbcouReal.hashCode());
    }
}