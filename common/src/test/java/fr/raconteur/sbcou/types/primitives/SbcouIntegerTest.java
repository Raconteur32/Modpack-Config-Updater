package fr.raconteur.sbcou.types.primitives;

import fr.raconteur.sbcou.BaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;

@DisplayName("SbcouInteger Tests")
class SbcouIntegerTest extends BaseTest {

    @Test
    @DisplayName("Should create SbcouInteger from int value and get basic properties")
    void shouldCreateFromIntValue() {
        // Given
        int value = 42;
        
        // When
        SbcouInteger sbcouInteger = new SbcouInteger(value);
        
        // Then
        assertNotNull(sbcouInteger);
        assertEquals(BigInteger.valueOf(value), sbcouInteger.getValue());
        assertEquals("42", sbcouInteger.getDisplayValue());
    }

    @Test
    @DisplayName("Should create SbcouInteger from long value")
    void shouldCreateFromLongValue() {
        // Given
        long value = 9876543210L;
        
        // When
        SbcouInteger sbcouInteger = new SbcouInteger(value);
        
        // Then
        assertNotNull(sbcouInteger);
        assertEquals(BigInteger.valueOf(value), sbcouInteger.getValue());
        assertEquals("9876543210", sbcouInteger.getDisplayValue());
    }

    @Test
    @DisplayName("Should create SbcouInteger from BigInteger value")
    void shouldCreateFromBigIntegerValue() {
        // Given
        BigInteger value = new BigInteger("123456789012345678901234567890");
        
        // When
        SbcouInteger sbcouInteger = new SbcouInteger(value);
        
        // Then
        assertNotNull(sbcouInteger);
        assertEquals(value, sbcouInteger.getValue());
        assertEquals("123456789012345678901234567890", sbcouInteger.getDisplayValue());
    }

    @Test
    @DisplayName("Should create SbcouInteger from string value")
    void shouldCreateFromStringValue() {
        // Given
        String value = "987654321";
        
        // When
        SbcouInteger sbcouInteger = new SbcouInteger(value);
        
        // Then
        assertNotNull(sbcouInteger);
        assertEquals(new BigInteger(value), sbcouInteger.getValue());
        assertEquals("987654321", sbcouInteger.getDisplayValue());
    }

    @Test
    @DisplayName("Should create SbcouInteger from negative int value")
    void shouldCreateFromNegativeIntValue() {
        // Given
        int value = -42;
        
        // When
        SbcouInteger sbcouInteger = new SbcouInteger(value);
        
        // Then
        assertNotNull(sbcouInteger);
        assertEquals(BigInteger.valueOf(value), sbcouInteger.getValue());
        assertEquals("-42", sbcouInteger.getDisplayValue());
    }

    @Test
    @DisplayName("Should create SbcouInteger from zero value")
    void shouldCreateFromZeroValue() {
        // Given
        int value = 0;
        
        // When
        SbcouInteger sbcouInteger = new SbcouInteger(value);
        
        // Then
        assertNotNull(sbcouInteger);
        assertEquals(BigInteger.ZERO, sbcouInteger.getValue());
        assertEquals("0", sbcouInteger.getDisplayValue());
    }

    @Test
    @DisplayName("Should throw exception for invalid string value")
    void shouldThrowExceptionForInvalidStringValue() {
        // Given
        String invalidValue = "not-a-number";
        
        // When & Then
        assertThrows(NumberFormatException.class, () -> {
            new SbcouInteger(invalidValue);
        });
    }

    @Test
    @DisplayName("Should handle very large positive numbers")
    void shouldHandleVeryLargePositiveNumbers() {
        // Given
        String largeNumber = "99999999999999999999999999999999999999999999999999";
        
        // When
        SbcouInteger sbcouInteger = new SbcouInteger(largeNumber);
        
        // Then
        assertNotNull(sbcouInteger);
        assertEquals(new BigInteger(largeNumber), sbcouInteger.getValue());
        assertEquals(largeNumber, sbcouInteger.getDisplayValue());
    }

    @Test
    @DisplayName("Should handle very large negative numbers")
    void shouldHandleVeryLargeNegativeNumbers() {
        // Given
        String largeNegativeNumber = "-99999999999999999999999999999999999999999999999999";
        
        // When
        SbcouInteger sbcouInteger = new SbcouInteger(largeNegativeNumber);
        
        // Then
        assertNotNull(sbcouInteger);
        assertEquals(new BigInteger(largeNegativeNumber), sbcouInteger.getValue());
        assertEquals(largeNegativeNumber, sbcouInteger.getDisplayValue());
    }

    @Test
    @DisplayName("Should be equal when created with same integer value")
    void shouldBeEqualWhenCreatedWithSameValue() {
        // Given
        int value = 42;
        
        // When
        SbcouInteger sbcouInteger1 = new SbcouInteger(value);
        SbcouInteger sbcouInteger2 = new SbcouInteger(value);
        
        // Then
        assertTrue(sbcouInteger1.equals(sbcouInteger2));
        assertEquals(sbcouInteger1.hashCode(), sbcouInteger2.hashCode());
    }

    @Test
    @DisplayName("Should be equal when created with equivalent values from different constructors")
    void shouldBeEqualWhenCreatedWithEquivalentValues() {
        // Given
        int intValue = 123;
        long longValue = 123L;
        String stringValue = "123";
        BigInteger bigIntValue = BigInteger.valueOf(123);
        
        // When
        SbcouInteger fromInt = new SbcouInteger(intValue);
        SbcouInteger fromLong = new SbcouInteger(longValue);
        SbcouInteger fromString = new SbcouInteger(stringValue);
        SbcouInteger fromBigInt = new SbcouInteger(bigIntValue);
        
        // Then
        assertTrue(fromInt.equals(fromLong));
        assertTrue(fromInt.equals(fromString));
        assertTrue(fromInt.equals(fromBigInt));
        assertTrue(fromLong.equals(fromString));
        assertTrue(fromLong.equals(fromBigInt));
        assertTrue(fromString.equals(fromBigInt));
        
        // Verify hash codes are equal too
        assertEquals(fromInt.hashCode(), fromLong.hashCode());
        assertEquals(fromInt.hashCode(), fromString.hashCode());
        assertEquals(fromInt.hashCode(), fromBigInt.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when created with different values")
    void shouldNotBeEqualWhenCreatedWithDifferentValues() {
        // Given
        SbcouInteger sbcouInteger1 = new SbcouInteger(42);
        SbcouInteger sbcouInteger2 = new SbcouInteger(43);
        
        // Then
        assertFalse(sbcouInteger1.equals(sbcouInteger2));
    }

    @Test
    @DisplayName("Should not be equal to null or different types")
    void shouldNotBeEqualToNullOrDifferentTypes() {
        // Given
        SbcouInteger sbcouInteger = new SbcouInteger(42);
        
        // Then
        assertFalse(sbcouInteger.equals(null));
        assertFalse(sbcouInteger.equals("42"));
        assertFalse(sbcouInteger.equals(42));
        assertFalse(sbcouInteger.equals(BigInteger.valueOf(42)));
    }

    @Test
    @DisplayName("Should be equal to itself")
    void shouldBeEqualToItself() {
        // Given
        SbcouInteger sbcouInteger = new SbcouInteger(42);
        
        // Then
        assertTrue(sbcouInteger.equals(sbcouInteger));
        assertEquals(sbcouInteger.hashCode(), sbcouInteger.hashCode());
    }
}