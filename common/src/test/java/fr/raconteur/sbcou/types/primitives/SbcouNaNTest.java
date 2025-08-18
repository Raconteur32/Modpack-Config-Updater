package fr.raconteur.sbcou.types.primitives;

import fr.raconteur.sbcou.BaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SbcouNaN Tests")
class SbcouNaNTest extends BaseTest {

    @Test
    @DisplayName("Should create SbcouNaN with NaN value")
    void shouldCreateWithNaNValue() {
        // Given
        String value = "NaN";
        
        // When
        SbcouNaN sbcouNaN = new SbcouNaN(value);
        
        // Then
        assertNotNull(sbcouNaN);
        assertEquals(value, sbcouNaN.getValue());
        assertEquals("NaN", sbcouNaN.getDisplayValue());
    }

    @Test
    @DisplayName("Should create SbcouNaN with +NaN value")
    void shouldCreateWithPositiveNaNValue() {
        // Given
        String value = "+NaN";
        
        // When
        SbcouNaN sbcouNaN = new SbcouNaN(value);
        
        // Then
        assertNotNull(sbcouNaN);
        assertEquals(value, sbcouNaN.getValue());
        assertEquals("+NaN", sbcouNaN.getDisplayValue());
    }

    @Test
    @DisplayName("Should create SbcouNaN with -NaN value")
    void shouldCreateWithNegativeNaNValue() {
        // Given
        String value = "-NaN";
        
        // When
        SbcouNaN sbcouNaN = new SbcouNaN(value);
        
        // Then
        assertNotNull(sbcouNaN);
        assertEquals(value, sbcouNaN.getValue());
        assertEquals("-NaN", sbcouNaN.getDisplayValue());
    }

    @Test
    @DisplayName("Should throw exception for invalid NaN value")
    void shouldThrowExceptionForInvalidNaNValue() {
        // Given
        String[] invalidValues = {"nan", "NAN", "not-a-number", "invalid", ""};
        
        for (String invalidValue : invalidValues) {
            // When & Then
            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                new SbcouNaN(invalidValue);
            });
            assertTrue(exception.getMessage().contains("is not supported for NaN type"));
        }
    }

    @Test
    @DisplayName("Should be equal when created with same NaN value")
    void shouldBeEqualWhenCreatedWithSameValue() {
        // Given
        String value = "NaN";
        
        // When
        SbcouNaN sbcouNaN1 = new SbcouNaN(value);
        SbcouNaN sbcouNaN2 = new SbcouNaN(value);
        
        // Then
        assertTrue(sbcouNaN1.equals(sbcouNaN2));
        assertEquals(sbcouNaN1.hashCode(), sbcouNaN2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when created with different NaN values")
    void shouldNotBeEqualWhenCreatedWithDifferentValues() {
        // Given
        SbcouNaN sbcouNaN1 = new SbcouNaN("NaN");
        SbcouNaN sbcouNaN2 = new SbcouNaN("+NaN");
        SbcouNaN sbcouNaN3 = new SbcouNaN("-NaN");
        
        // Then
        assertFalse(sbcouNaN1.equals(sbcouNaN2));
        assertFalse(sbcouNaN1.equals(sbcouNaN3));
        assertFalse(sbcouNaN2.equals(sbcouNaN3));
    }

    @Test
    @DisplayName("Should not be equal to null")
    void shouldNotBeEqualToNull() {
        // Given
        SbcouNaN sbcouNaN = new SbcouNaN("NaN");
        
        // Then
        assertFalse(sbcouNaN.equals(null));
    }

    @Test
    @DisplayName("Should be equal to itself")
    void shouldBeEqualToItself() {
        // Given
        SbcouNaN sbcouNaN = new SbcouNaN("NaN");
        
        // Then
        assertTrue(sbcouNaN.equals(sbcouNaN));
        assertEquals(sbcouNaN.hashCode(), sbcouNaN.hashCode());
    }

    @Test
    @DisplayName("Should handle all accepted NaN values")
    void shouldHandleAllAcceptedNaNValues() {
        // Given
        String[] acceptedValues = {"+NaN", "NaN", "-NaN"};
        
        for (String value : acceptedValues) {
            // When
            SbcouNaN sbcouNaN = new SbcouNaN(value);
            
            // Then
            assertNotNull(sbcouNaN);
            assertEquals(value, sbcouNaN.getValue());
            assertEquals(value, sbcouNaN.getDisplayValue());
        }
    }
}