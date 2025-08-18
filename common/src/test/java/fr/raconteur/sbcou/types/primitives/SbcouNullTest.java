package fr.raconteur.sbcou.types.primitives;

import fr.raconteur.sbcou.BaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SbcouNull Tests")
class SbcouNullTest extends BaseTest {

    @Test
    @DisplayName("Should create SbcouNull with default constructor")
    void shouldCreateWithDefaultConstructor() {
        // When
        SbcouNull sbcouNull = new SbcouNull();
        
        // Then
        assertNotNull(sbcouNull);
        assertNull(sbcouNull.getValue());
        assertEquals("null", sbcouNull.getDisplayValue());
    }

    @Test
    @DisplayName("Should be equal when both are created with default constructor")
    void shouldBeEqualWhenBothCreatedWithDefaultConstructor() {
        // When
        SbcouNull sbcouNull1 = new SbcouNull();
        SbcouNull sbcouNull2 = new SbcouNull();
        
        // Then
        assertTrue(sbcouNull1.equals(sbcouNull2));
        assertEquals(sbcouNull1.hashCode(), sbcouNull2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal to null")
    void shouldNotBeEqualToNull() {
        // Given
        SbcouNull sbcouNull = new SbcouNull();
        
        // Then
        assertFalse(sbcouNull.equals(null));
    }

    @Test
    @DisplayName("Should be equal to itself")
    void shouldBeEqualToItself() {
        // Given
        SbcouNull sbcouNull = new SbcouNull();
        
        // Then
        assertTrue(sbcouNull.equals(sbcouNull));
        assertEquals(sbcouNull.hashCode(), sbcouNull.hashCode());
    }

    @Test
    @DisplayName("Should have consistent string representation")
    void shouldHaveConsistentStringRepresentation() {
        // Given
        SbcouNull sbcouNull = new SbcouNull();
        
        // Then
        assertEquals("null", sbcouNull.getDisplayValue());
    }
}