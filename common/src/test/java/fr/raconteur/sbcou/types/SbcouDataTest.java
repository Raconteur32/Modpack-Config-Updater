package fr.raconteur.sbcou.types;

import fr.raconteur.sbcou.BaseTest;
import fr.raconteur.sbcou.types.nested.SbcouList;
import fr.raconteur.sbcou.types.nested.SbcouObject;
import fr.raconteur.sbcou.types.primitives.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@DisplayName("SbcouData Database Retrieval Tests")
class SbcouDataTest extends BaseTest {

    @Test
    @DisplayName("Should retrieve SbcouInteger from database by ID")
    void shouldRetrieveSbcouIntegerFromDatabaseById() {
        // Given
        SbcouInteger originalInteger = new SbcouInteger(42);
        int dataId = originalInteger.getDataId();
        
        // When
        SbcouData<?> retrievedData = SbcouData.sbcouDataFromId(dataId);
        
        // Then
        assertNotNull(retrievedData);
        assertInstanceOf(SbcouInteger.class, retrievedData);
        assertEquals(BigInteger.valueOf(42), retrievedData.getValue());
        assertEquals("INTEGER", retrievedData.getStringType());
        assertEquals(dataId, retrievedData.getDataId());
    }

    @Test
    @DisplayName("Should retrieve SbcouString from database by ID")
    void shouldRetrieveSbcouStringFromDatabaseById() {
        // Given
        SbcouString originalString = new SbcouString("hello world");
        int dataId = originalString.getDataId();
        
        // When
        SbcouData<?> retrievedData = SbcouData.sbcouDataFromId(dataId);
        
        // Then
        assertNotNull(retrievedData);
        assertInstanceOf(SbcouString.class, retrievedData);
        assertEquals("hello world", retrievedData.getValue());
        assertEquals("STRING", retrievedData.getStringType());
        assertEquals(dataId, retrievedData.getDataId());
    }

    @Test
    @DisplayName("Should retrieve SbcouReal from database by ID")
    void shouldRetrieveSbcouRealFromDatabaseById() {
        // Given
        SbcouReal originalReal = new SbcouReal(new BigDecimal("3.14159"));
        int dataId = originalReal.getDataId();
        
        // When
        SbcouData<?> retrievedData = SbcouData.sbcouDataFromId(dataId);
        
        // Then
        assertNotNull(retrievedData);
        assertInstanceOf(SbcouReal.class, retrievedData);
        assertEquals(new BigDecimal("3.14159"), retrievedData.getValue());
        assertEquals("REAL", retrievedData.getStringType());
        assertEquals(dataId, retrievedData.getDataId());
    }

    @Test
    @DisplayName("Should retrieve SbcouBoolean from database by ID")
    void shouldRetrieveSbcouBooleanFromDatabaseById() {
        // Given
        SbcouBoolean originalBoolean = new SbcouBoolean(true);
        int dataId = originalBoolean.getDataId();
        
        // When
        SbcouData<?> retrievedData = SbcouData.sbcouDataFromId(dataId);
        
        // Then
        assertNotNull(retrievedData);
        assertInstanceOf(SbcouBoolean.class, retrievedData);
        assertEquals(true, retrievedData.getValue());
        assertEquals("BOOLEAN", retrievedData.getStringType());
        assertEquals(dataId, retrievedData.getDataId());
    }

    @Test
    @DisplayName("Should retrieve SbcouNull from database by ID")
    void shouldRetrieveSbcouNullFromDatabaseById() {
        // Given
        SbcouNull originalNull = new SbcouNull();
        int dataId = originalNull.getDataId();
        
        // When
        SbcouData<?> retrievedData = SbcouData.sbcouDataFromId(dataId);
        
        // Then
        assertNotNull(retrievedData);
        assertInstanceOf(SbcouNull.class, retrievedData);
        assertNull(retrievedData.getValue());
        assertEquals("NULL", retrievedData.getStringType());
        assertEquals(dataId, retrievedData.getDataId());
    }

    @Test
    @DisplayName("Should retrieve SbcouNaN from database by ID")
    void shouldRetrieveSbcouNaNFromDatabaseById() {
        // Given
        SbcouNaN originalNaN = new SbcouNaN("NaN");
        int dataId = originalNaN.getDataId();
        
        // When
        SbcouData<?> retrievedData = SbcouData.sbcouDataFromId(dataId);
        
        // Then
        assertNotNull(retrievedData);
        assertInstanceOf(SbcouNaN.class, retrievedData);
        assertEquals("NaN", retrievedData.getValue());
        assertEquals("NAN", retrievedData.getStringType());
        assertEquals(dataId, retrievedData.getDataId());
    }

    @Test
    @DisplayName("Should retrieve SbcouInfinity from database by ID")
    void shouldRetrieveSbcouInfinityFromDatabaseById() {
        // Given
        SbcouInfinity originalInfinity = new SbcouInfinity("Infinity");
        int dataId = originalInfinity.getDataId();
        
        // When
        SbcouData<?> retrievedData = SbcouData.sbcouDataFromId(dataId);
        
        // Then
        assertNotNull(retrievedData);
        assertInstanceOf(SbcouInfinity.class, retrievedData);
        assertEquals("Infinity", retrievedData.getValue());
        assertEquals("INFINITY", retrievedData.getStringType());
        assertEquals(dataId, retrievedData.getDataId());
    }

    @Test
    @DisplayName("Should retrieve SbcouList from database by ID")
    void shouldRetrieveSbcouListFromDatabaseById() {
        // Given
        SbcouList originalList = new SbcouList(
            new SbcouInteger(1),
            new SbcouString("test"),
            new SbcouBoolean(false)
        );
        int dataId = originalList.getDataId();
        
        // When
        SbcouData<?> retrievedData = SbcouData.sbcouDataFromId(dataId);
        
        // Then
        assertNotNull(retrievedData);
        assertInstanceOf(SbcouList.class, retrievedData);
        assertEquals("LIST", retrievedData.getStringType());
        assertEquals(dataId, retrievedData.getDataId());
        
        @SuppressWarnings("unchecked")
        List<SbcouData<?>> retrievedList = (List<SbcouData<?>>) retrievedData.getValue();
        assertEquals(3, retrievedList.size());
        assertEquals(BigInteger.valueOf(1), retrievedList.get(0).getValue());
        assertEquals("test", retrievedList.get(1).getValue());
        assertEquals(false, retrievedList.get(2).getValue());
    }

    @Test
    @DisplayName("Should retrieve SbcouObject from database by ID")
    void shouldRetrieveSbcouObjectFromDatabaseById() {
        // Given
        SbcouObject originalObject = new SbcouObject(Map.of(
            "name", new SbcouString("John"),
            "age", new SbcouInteger(30),
            "active", new SbcouBoolean(true)
        ));
        int dataId = originalObject.getDataId();
        
        // When
        SbcouData<?> retrievedData = SbcouData.sbcouDataFromId(dataId);
        
        // Then
        assertNotNull(retrievedData);
        assertInstanceOf(SbcouObject.class, retrievedData);
        assertEquals("OBJECT", retrievedData.getStringType());
        assertEquals(dataId, retrievedData.getDataId());
        
        @SuppressWarnings("unchecked")
        Map<String, SbcouData<?>> retrievedMap = (Map<String, SbcouData<?>>) retrievedData.getValue();
        assertEquals(3, retrievedMap.size());
        assertEquals("John", retrievedMap.get("name").getValue());
        assertEquals(BigInteger.valueOf(30), retrievedMap.get("age").getValue());
        assertEquals(true, retrievedMap.get("active").getValue());
    }

    @Test
    @DisplayName("Should throw exception for non-existent ID")
    void shouldThrowExceptionForNonExistentId() {
        // Given
        int nonExistentId = 999999;
        
        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            SbcouData.sbcouDataFromId(nonExistentId);
        });
        assertTrue(exception.getMessage().contains("Value with id " + nonExistentId + " not found"));
    }

    @Test
    @DisplayName("Should retrieve multiple different types in sequence")
    void shouldRetrieveMultipleDifferentTypesInSequence() {
        // Given
        SbcouInteger integer = new SbcouInteger(100);
        SbcouString string = new SbcouString("sequence test");
        SbcouReal real = new SbcouReal(2.718);
        SbcouBoolean bool = new SbcouBoolean(false);
        
        int intId = integer.getDataId();
        int stringId = string.getDataId();
        int realId = real.getDataId();
        int boolId = bool.getDataId();
        
        // When
        SbcouData<?> retrievedInt = SbcouData.sbcouDataFromId(intId);
        SbcouData<?> retrievedString = SbcouData.sbcouDataFromId(stringId);
        SbcouData<?> retrievedReal = SbcouData.sbcouDataFromId(realId);
        SbcouData<?> retrievedBool = SbcouData.sbcouDataFromId(boolId);
        
        // Then
        assertInstanceOf(SbcouInteger.class, retrievedInt);
        assertInstanceOf(SbcouString.class, retrievedString);
        assertInstanceOf(SbcouReal.class, retrievedReal);
        assertInstanceOf(SbcouBoolean.class, retrievedBool);
        
        assertEquals(BigInteger.valueOf(100), retrievedInt.getValue());
        assertEquals("sequence test", retrievedString.getValue());
        assertEquals(new BigDecimal("2.718"), retrievedReal.getValue());
        assertEquals(false, retrievedBool.getValue());
    }

    @Test
    @DisplayName("Should maintain equality between original and retrieved objects")
    void shouldMaintainEqualityBetweenOriginalAndRetrieved() {
        // Given
        SbcouInteger original = new SbcouInteger(42);
        int dataId = original.getDataId();
        
        // When
        SbcouData<?> retrieved = SbcouData.sbcouDataFromId(dataId);
        
        // Then
        assertTrue(original.equals(retrieved));
        assertEquals(original.hashCode(), retrieved.hashCode());
        assertEquals(original.getDataId(), retrieved.getDataId());
    }

    @Test
    @DisplayName("Should handle nested structures correctly")
    void shouldHandleNestedStructuresCorrectly() {
        // Given
        SbcouObject innerObject = new SbcouObject(Map.of("inner", new SbcouString("value")));
        SbcouList outerList = new SbcouList(
            innerObject,
            new SbcouInteger(123)
        );
        int listId = outerList.getDataId();
        
        // When
        SbcouData<?> retrievedList = SbcouData.sbcouDataFromId(listId);
        
        // Then
        assertInstanceOf(SbcouList.class, retrievedList);
        @SuppressWarnings("unchecked")
        List<SbcouData<?>> listValue = (List<SbcouData<?>>) retrievedList.getValue();
        assertEquals(2, listValue.size());
        
        assertInstanceOf(SbcouObject.class, listValue.get(0));
        assertInstanceOf(SbcouInteger.class, listValue.get(1));
        
        @SuppressWarnings("unchecked")
        Map<String, SbcouData<?>> objectValue = (Map<String, SbcouData<?>>) listValue.get(0).getValue();
        assertEquals("value", objectValue.get("inner").getValue());
        assertEquals(BigInteger.valueOf(123), listValue.get(1).getValue());
    }

    @Test
    @DisplayName("Should handle special string values correctly")
    void shouldHandleSpecialStringValuesCorrectly() {
        // Given
        SbcouString emptyString = new SbcouString("");
        SbcouString specialChars = new SbcouString("Special: chars, with \\ backslashes");
        SbcouString unicodeString = new SbcouString("Unicode: 你好世界 🌍");
        
        // When
        SbcouData<?> retrievedEmpty = SbcouData.sbcouDataFromId(emptyString.getDataId());
        SbcouData<?> retrievedSpecial = SbcouData.sbcouDataFromId(specialChars.getDataId());
        SbcouData<?> retrievedUnicode = SbcouData.sbcouDataFromId(unicodeString.getDataId());
        
        // Then
        assertEquals("", retrievedEmpty.getValue());
        assertEquals("Special: chars, with \\ backslashes", retrievedSpecial.getValue());
        assertEquals("Unicode: 你好世界 🌍", retrievedUnicode.getValue());
    }
}