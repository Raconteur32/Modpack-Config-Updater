package fr.raconteur.sbcou.flatobject;

import fr.raconteur.sbcou.BaseTest;
import fr.raconteur.sbcou.types.SbcouData;
import fr.raconteur.sbcou.types.SbcouDoNotExist;
import fr.raconteur.sbcou.types.nested.SbcouObject;
import fr.raconteur.sbcou.types.primitives.SbcouInteger;
import fr.raconteur.sbcou.types.primitives.SbcouString;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("FlatObject Tests")
class FlatObjectTest extends BaseTest {

    @Test
    @DisplayName("isSingleValue and toSbcouValue should return the ROOT value directly")
    void shouldReturnSingleRootValue() {
        SbcouInteger value = new SbcouInteger(42);
        FlatObject flat = value.flatten();

        assertTrue(flat.isSingleValue());
        SbcouData<?> result = flat.toSbcouValue();
        assertSame(value, result);
    }

    @Test
    @DisplayName("Builder and FlatObject should support contains and get")
    void shouldSupportContainsAndGet() {
        SbcouObject rootObj = new SbcouObject();
        FlatKey k = FlatKey.getSinglePartFlatKeyFromString("k");
        SbcouInteger v = new SbcouInteger(5);

        FlatObject.Builder builder = FlatObject.builder(rootObj)
                .put(k, v);

        assertTrue(builder.isComposedValue());
        assertTrue(builder.contains(FlatKey.ROOT));
        assertSame(rootObj, builder.get(FlatKey.ROOT));
        assertTrue(builder.contains(k));
        assertSame(v, builder.get(k));

        FlatObject flat = builder.build();
        assertTrue(flat.isComposedValue());
        assertTrue(flat.contains(FlatKey.ROOT));
        assertTrue(flat.contains(k));
        assertSame(v, flat.get(k));
    }

    @Test
    @DisplayName("toSbcouValue should rebuild SbcouObject when ROOT is present with additional entries")
    void shouldRebuildObjectFromFlatMap() {
        FlatKey nameKey = FlatKey.getSinglePartFlatKeyFromString("name");
        FlatKey ageKey = FlatKey.getSinglePartFlatKeyFromString("age");
        FlatKey addressKey = FlatKey.getSinglePartFlatKeyFromString("address");

        SbcouString nameVal = new SbcouString("John");
        SbcouInteger ageVal = new SbcouInteger(30);
        SbcouInteger zipBaseVal = new SbcouInteger(11111);
        SbcouString streetVal = new SbcouString("Main");
        SbcouInteger zipChildVal = new SbcouInteger(99999);
        FlatObject.Builder builder = FlatObject.builder(new SbcouObject());
        builder
            .put(nameKey, nameVal)
            .put(ageKey, ageVal)
            // base object under address with a default zip that will be overridden by child key
            .put(addressKey, new SbcouObject(Map.of("zip", zipBaseVal)))
            // children under address
            .put(addressKey.getChild(FlatKey.getSinglePartFlatKeyFromString("street")), streetVal)
            .put(addressKey.getChild(FlatKey.getSinglePartFlatKeyFromString("zip")), zipChildVal);

        FlatObject flat = builder.build();
        assertFalse(flat.isSingleValue());
        assertTrue(flat.isComposedValue());

        SbcouData<?> rebuilt = flat.toSbcouValue();
        SbcouObject obj = assertInstanceOf(SbcouObject.class, rebuilt);

        assertSame(nameVal, obj.getValue().get("name"));
        assertSame(ageVal, obj.getValue().get("age"));

        SbcouObject address = assertInstanceOf(SbcouObject.class, obj.getValue().get("address"));
        assertSame(streetVal, address.getValue().get("street"));
        assertSame(zipChildVal, address.getValue().get("zip"));
    }

    @Test
    @DisplayName("toSbcouValue should throw when ROOT is missing")
    void shouldThrowWhenRootMissing() {
        FlatObject flat = FlatObject.builder(new SbcouObject())
                .put(FlatKey.getSinglePartFlatKeyFromString("a"), new SbcouInteger(2))
                .build();

        // Simulate missing ROOT by removing it directly (package-private access allowed in tests)
        flat.map.remove(FlatKey.ROOT);
        assertThrows(RuntimeException.class, flat::toSbcouValue);
    }

    @Test
    @DisplayName("Builder putSubFlatObject should nest values under parent key")
    void shouldSupportBuilderPutSubFlatObject() {
        SbcouInteger val = new SbcouInteger(7);
        FlatObject child = val.flatten();
        FlatObject flat = FlatObject.builder(new SbcouObject())
                .putSubFlatObject(FlatKey.getSinglePartFlatKeyFromString("nested"), child)
                .build();

        SbcouData<?> rebuilt = flat.toSbcouValue();
        SbcouObject obj = assertInstanceOf(SbcouObject.class, rebuilt);
        assertSame(val, obj.getValue().get("nested"));
    }

    @Test
    @DisplayName("When base value is single, children define object via merge semantics")
    void shouldPreferChildrenWhenBaseIsSingleValue() {
        FlatKey foo = FlatKey.getSinglePartFlatKeyFromString("foo");
        SbcouInteger barVal = new SbcouInteger(7);
        FlatObject flat = FlatObject.builder(new SbcouObject())
                .put(foo, new SbcouString("ignored"))
                .put(foo.getChild(FlatKey.getSinglePartFlatKeyFromString("bar")), barVal)
                .build();

        SbcouData<?> rebuilt = flat.toSbcouValue();
        SbcouObject obj = assertInstanceOf(SbcouObject.class, rebuilt);
        SbcouObject fooObj = assertInstanceOf(SbcouObject.class, obj.getValue().get("foo"));
        assertSame(barVal, fooObj.getValue().get("bar"));
    }

    @Test
    @DisplayName("getDiffTo with other single value produces NEW diffs for other's root children and DELETED for this-only keys")
    void shouldDiffAgainstSingleValue() {
        FlatObject thisFlat = FlatObject.builder(new SbcouObject())
                .put(FlatKey.getSinglePartFlatKeyFromString("a"), new SbcouInteger(1))
                .put(FlatKey.getSinglePartFlatKeyFromString("b"), new SbcouString("x"))
                .build();

        SbcouInteger otherRoot = new SbcouInteger(42);
        FlatObject otherFlat = otherRoot.flatten();

        FlatObjectDiff diff = thisFlat.getDiffTo(otherFlat);
        // expect NEW only for keys in other not in this (but here other has only ROOT), and DELETED for keys present only in this
        assertTrue(diff.getDiffs().containsKey(FlatKey.getSinglePartFlatKeyFromString("a")));
        assertEquals(FlatObjectDiff.FlatObjectEntryDiffType.DELETED, diff.getDiffs().get(FlatKey.getSinglePartFlatKeyFromString("a")).entryDiffType);
        assertTrue(diff.getDiffs().containsKey(FlatKey.getSinglePartFlatKeyFromString("b")));
        assertEquals(FlatObjectDiff.FlatObjectEntryDiffType.DELETED, diff.getDiffs().get(FlatKey.getSinglePartFlatKeyFromString("b")).entryDiffType);
    }

    @Test
    @DisplayName("getDiffTo with other composed value surfaces changes/new and deletions")
    void shouldDiffAgainstComposedValue() {
        FlatKey a = FlatKey.getSinglePartFlatKeyFromString("a");
        FlatKey b = FlatKey.getSinglePartFlatKeyFromString("b");
        FlatKey d = FlatKey.getSinglePartFlatKeyFromString("d");

        FlatObject thisFlat = FlatObject.builder(new SbcouObject())
                .put(a, new SbcouInteger(1))
                .put(b, new SbcouInteger(2))
                .build();

        SbcouInteger newB = new SbcouInteger(3);
        SbcouString newD = new SbcouString("new");
        FlatObject otherFlat = FlatObject.builder(new SbcouObject())
                .put(b, newB) // changed
                .put(d, newD) // new
                .build();

        FlatObjectDiff diff = thisFlat.getDiffTo(otherFlat);

        // Expect b updated (DIFFERENT), d added (NEW), a deleted (DELETED)
        assertEquals(FlatObjectDiff.FlatObjectEntryDiffType.DIFFERENT, diff.getDiffs().get(b).entryDiffType);
        assertEquals(FlatObjectDiff.FlatObjectEntryDiffType.NEW, diff.getDiffs().get(d).entryDiffType);
        assertEquals(FlatObjectDiff.FlatObjectEntryDiffType.DELETED, diff.getDiffs().get(a).entryDiffType);
    }
} 