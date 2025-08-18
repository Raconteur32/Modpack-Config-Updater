package fr.raconteur.sbcou.types.primitives;

import fr.raconteur.sbcou.BaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SbcouInfinity Tests")
class SbcouInfinityTest extends BaseTest {

    @Test
    @DisplayName("Should create SbcouInfinity with Infinity value")
    void shouldCreateWithInfinityValue() {
        // Given
        String value = "Infinity";
        
        // When
        SbcouInfinity sbcouInfinity = new SbcouInfinity(value);
        
        // Then
        assertNotNull(sbcouInfinity);
        assertEquals(value, sbcouInfinity.getValue());
        assertEquals("Infinity", sbcouInfinity.getDisplayValue());
    }

    @Test
    @DisplayName("Should create SbcouInfinity with +Infinity value")
    void shouldCreateWithPositiveInfinityValue() {
        // Given
        String value = "+Infinity";
        
        // When
        SbcouInfinity sbcouInfinity = new SbcouInfinity(value);
        
        // Then
        assertNotNull(sbcouInfinity);
        assertEquals(value, sbcouInfinity.getValue());
        assertEquals("+Infinity", sbcouInfinity.getDisplayValue());
    }

    @Test
    @DisplayName("Should create SbcouInfinity with -Infinity value")
    void shouldCreateWithNegativeInfinityValue() {
        // Given
        String value = "-Infinity";
        
        // When
        SbcouInfinity sbcouInfinity = new SbcouInfinity(value);
        
        // Then
        assertNotNull(sbcouInfinity);
        assertEquals(value, sbcouInfinity.getValue());
        assertEquals("-Infinity", sbcouInfinity.getDisplayValue());
    }

    @Test
    @DisplayName("Should throw exception for invalid Infinity value")
    void shouldThrowExceptionForInvalidInfinityValue() {
        // Given
        String[] invalidValues = {"infinity", "INFINITY", "inf", "INF", "∞", "invalid", ""};
        
        for (String invalidValue : invalidValues) {
            // When & Then
            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                new SbcouInfinity(invalidValue);
            });
            assertTrue(exception.getMessage().contains("is not supported for Infinity type"));
        }
    }

    @Test
    @DisplayName("Should be equal when created with same Infinity value")
    void shouldBeEqualWhenCreatedWithSameValue() {
        // Given
        String value = "Infinity";
        
        // When
        SbcouInfinity sbcouInfinity1 = new SbcouInfinity(value);
        SbcouInfinity sbcouInfinity2 = new SbcouInfinity(value);
        
        // Then
        assertTrue(sbcouInfinity1.equals(sbcouInfinity2));
        assertEquals(sbcouInfinity1.hashCode(), sbcouInfinity2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when created with different Infinity values")
    void shouldNotBeEqualWhenCreatedWithDifferentValues() {
        // Given
        SbcouInfinity sbcouInfinity1 = new SbcouInfinity("Infinity");
        SbcouInfinity sbcouInfinity2 = new SbcouInfinity("+Infinity");
        SbcouInfinity sbcouInfinity3 = new SbcouInfinity("-Infinity");
        
        // Then
        assertFalse(sbcouInfinity1.equals(sbcouInfinity2));
        assertFalse(sbcouInfinity1.equals(sbcouInfinity3));
        assertFalse(sbcouInfinity2.equals(sbcouInfinity3));
    }

    @Test
    @DisplayName("Should not be equal to null")
    void shouldNotBeEqualToNull() {
        // Given
        SbcouInfinity sbcouInfinity = new SbcouInfinity("Infinity");
        
        // Then
        assertFalse(sbcouInfinity.equals(null));
    }

    @Test
    @DisplayName("Should be equal to itself")
    void shouldBeEqualToItself() {
        // Given
        SbcouInfinity sbcouInfinity = new SbcouInfinity("Infinity");
        
        // Then
        assertTrue(sbcouInfinity.equals(sbcouInfinity));
        assertEquals(sbcouInfinity.hashCode(), sbcouInfinity.hashCode());
    }

    @Test
    @DisplayName("Should handle all accepted Infinity values")
    void shouldHandleAllAcceptedInfinityValues() {
        // Given
        String[] acceptedValues = {"+Infinity", "Infinity", "-Infinity"};
        
        for (String value : acceptedValues) {
            // When
            SbcouInfinity sbcouInfinity = new SbcouInfinity(value);
            
            // Then
            assertNotNull(sbcouInfinity);
            assertEquals(value, sbcouInfinity.getValue());
            assertEquals(value, sbcouInfinity.getDisplayValue());
        }
    }
}