package fr.raconteur.sbcou.types.nested;

import fr.raconteur.sbcou.BaseTest;
import fr.raconteur.sbcou.types.SbcouData;
import fr.raconteur.sbcou.types.primitives.SbcouInteger;
import fr.raconteur.sbcou.types.primitives.SbcouString;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;
import java.util.List;
import java.util.ArrayList;

@DisplayName("SbcouList Tests")
class SbcouListTest extends BaseTest {

    @Test
    @DisplayName("Should create empty SbcouList with empty list")
    void shouldCreateEmptyList() {
        // Given
        List<SbcouData<?>> emptyList = new ArrayList<>();
        
        // When
        SbcouList sbcouList = new SbcouList(emptyList);
        
        // Then
        assertNotNull(sbcouList);
        assertTrue(sbcouList.getValue().isEmpty());
        assertEquals("[\r\n    \r\n]", sbcouList.getDisplayValue());
    }

    @Test
    @DisplayName("Should create SbcouList with single element")
    void shouldCreateWithSingleElement() {
        // Given
        SbcouInteger element = new SbcouInteger(42);
        
        // When
        SbcouList sbcouList = new SbcouList(element);
        
        // Then
        assertNotNull(sbcouList);
        assertEquals(1, sbcouList.getValue().size());
        assertEquals(element.getValue(), sbcouList.getValue().get(0).getValue());
    }

    @Test
    @DisplayName("Should create SbcouList with multiple elements using varargs")
    void shouldCreateWithMultipleElements() {
        // Given
        SbcouInteger int1 = new SbcouInteger(1);
        SbcouString str1 = new SbcouString("hello");
        SbcouInteger int2 = new SbcouInteger(2);
        
        // When
        SbcouList sbcouList = new SbcouList(int1, str1, int2);
        
        // Then
        assertNotNull(sbcouList);
        assertEquals(3, sbcouList.getValue().size());
        assertEquals(int1.getValue(), sbcouList.getValue().get(0).getValue());
        assertEquals(str1.getValue(), sbcouList.getValue().get(1).getValue());
        assertEquals(int2.getValue(), sbcouList.getValue().get(2).getValue());
    }

    @Test
    @DisplayName("Should create SbcouList from List collection")
    void shouldCreateFromListCollection() {
        // Given
        List<SbcouData<?>> elements = List.of(
            new SbcouInteger(10),
            new SbcouString("test"),
            new SbcouInteger(20)
        );
        
        // When
        SbcouList sbcouList = new SbcouList(elements);
        
        // Then
        assertNotNull(sbcouList);
        assertEquals(3, sbcouList.getValue().size());
        assertEquals(BigInteger.valueOf(10), sbcouList.getValue().get(0).getValue());
        assertEquals("test", sbcouList.getValue().get(1).getValue());
        assertEquals(BigInteger.valueOf(20), sbcouList.getValue().get(2).getValue());
    }

    @Test
    @DisplayName("Should return immutable list from getValue")
    void shouldReturnImmutableList() {
        // Given
        SbcouList sbcouList = new SbcouList(new SbcouInteger(42));
        
        // When
        List<SbcouData<?>> value = sbcouList.getValue();
        
        // Then
        assertThrows(UnsupportedOperationException.class, () -> {
            value.add(new SbcouInteger(100));
        });
    }

    @Test
    @DisplayName("Should be equal when created with same elements")
    void shouldBeEqualWhenCreatedWithSameElements() {
        // Given
        SbcouInteger element1 = new SbcouInteger(42);
        SbcouString element2 = new SbcouString("test");
        
        // When
        SbcouList sbcouList1 = new SbcouList(element1, element2);
        SbcouList sbcouList2 = new SbcouList(element1, element2);
        
        // Then
        assertTrue(sbcouList1.equals(sbcouList2));
        assertEquals(sbcouList1.hashCode(), sbcouList2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when created with different elements")
    void shouldNotBeEqualWhenCreatedWithDifferentElements() {
        // Given
        SbcouList sbcouList1 = new SbcouList(new SbcouInteger(1));
        SbcouList sbcouList2 = new SbcouList(new SbcouInteger(2));
        
        // Then
        assertFalse(sbcouList1.equals(sbcouList2));
    }

    @Test
    @DisplayName("Should not be equal to null")
    void shouldNotBeEqualToNull() {
        // Given
        SbcouList sbcouList = new SbcouList(new SbcouInteger(42));
        
        // Then
        assertFalse(sbcouList.equals(null));
    }

    @Test
    @DisplayName("Should be equal to itself")
    void shouldBeEqualToItself() {
        // Given
        SbcouList sbcouList = new SbcouList(new SbcouInteger(42));
        
        // Then
        assertTrue(sbcouList.equals(sbcouList));
        assertEquals(sbcouList.hashCode(), sbcouList.hashCode());
    }

    @Test
    @DisplayName("Should format display value correctly for mixed types")
    void shouldFormatDisplayValueCorrectly() {
        // Given
        SbcouList sbcouList = new SbcouList(
            new SbcouInteger(1),
            new SbcouString("hello")
        );
        
        // When
        String displayValue = sbcouList.getDisplayValue();
        
        // Then
        assertTrue(displayValue.startsWith("[\r\n"));
        assertTrue(displayValue.endsWith("\r\n]"));
        assertTrue(displayValue.contains("1"));
        assertTrue(displayValue.contains("hello"));
    }

    @Test
    @DisplayName("Should handle nested lists")
    void shouldHandleNestedLists() {
        // Given
        SbcouList innerList = new SbcouList(new SbcouInteger(1), new SbcouInteger(2));
        SbcouList outerList = new SbcouList(innerList, new SbcouString("outer"));
        
        // When & Then
        assertNotNull(outerList);
        assertEquals(2, outerList.getValue().size());
        assertTrue(outerList.getValue().get(0) instanceof SbcouList);
        assertTrue(outerList.getValue().get(1) instanceof SbcouString);
    }

    @Test
    @DisplayName("Should maintain element order")
    void shouldMaintainElementOrder() {
        // Given
        SbcouInteger first = new SbcouInteger(1);
        SbcouString second = new SbcouString("middle");
        SbcouInteger third = new SbcouInteger(3);
        
        // When
        SbcouList sbcouList = new SbcouList(first, second, third);
        
        // Then
        List<SbcouData<?>> values = sbcouList.getValue();
        assertEquals(first.getValue(), values.get(0).getValue());
        assertEquals(second.getValue(), values.get(1).getValue());
        assertEquals(third.getValue(), values.get(2).getValue());
    }
}