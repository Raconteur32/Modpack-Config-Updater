package fr.raconteur.sbcou.flatobject;

import fr.raconteur.sbcou.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("FlatKey Tests")
class FlatKeyTest extends BaseTest {

    @Test
    @DisplayName("ROOT should represent empty key with no parts or parents")
    void shouldRepresentRoot() {
        FlatKey root = FlatKey.ROOT;

        assertTrue(root.isEmpty());
        assertEquals("", root.getFormattedKey());
        assertEquals(0, root.getSize());
        assertTrue(root.getRawParts().isEmpty());
        assertTrue(root.getParts().isEmpty());
        assertNull(root.getDirectParent());
        assertTrue(root.getAllParents().isEmpty());
    }

    @Test
    @DisplayName("Single-part key created from single string should escape and unescape correctly")
    void shouldCreateSinglePartFromSingleString() {
        String part = "alpha";
        FlatKey key = FlatKey.getSinglePartFlatKeyFromString(part);

        assertFalse(key.isEmpty());
        assertEquals(1, key.getSize());
        assertEquals(List.of(part), key.getParts());
        assertEquals("\"" + part + "\"", key.getFormattedKey());
        assertEquals(List.of("\"" + part + "\""), key.getRawParts());
        assertNull(key.getDirectParent());
        assertEquals(part, key.getName());
    }

    @Test
    @DisplayName("Escaping: dot, quote, and backslash in a single part should round-trip")
    void shouldEscapeAndUnescapeSpecialCharacters() {
        // human raw: a.b"c\d
        String raw = "a.b\"c\\d"; // includes dot, quote, backslash
        FlatKey key = FlatKey.getSinglePartFlatKeyFromString(raw);

        // expectedFormatted (human): "a\.b\"c\\d"
        String expectedFormatted = "\"a\\.b\\\"c\\\\d\"";

        assertEquals(1, key.getSize());
        assertEquals(List.of(raw), key.getParts());
        assertEquals(expectedFormatted, key.getFormattedKey());
        assertEquals(List.of(expectedFormatted), key.getRawParts());
        assertEquals(raw, key.getName());
    }

    @Test
    @DisplayName("Composite key from formatted string should parse parts and parents")
    void shouldCreateCompositeFromFormattedString() {
        // human formatted: "a"."b"."c"
        FlatKey key = FlatKey.getFlatKeyFromFormattedKey("\"a\".\"b\".\"c\"");

        assertEquals(3, key.getSize());
        assertEquals(List.of("a", "b", "c"), key.getParts());
        assertEquals(List.of("\"a\"", "\"b\"", "\"c\""), key.getRawParts());
        assertEquals("c", key.getName());

        // human formatted: "a"."b"
        FlatKey expectedParent = FlatKey.getFlatKeyFromFormattedKey("\"a\".\"b\"");
        assertSame(expectedParent, key.getDirectParent());

        List<FlatKey> parents = key.getAllParents();
        assertEquals(2, parents.size());
        assertSame(expectedParent, parents.get(0));
        // human formatted: "a"
        assertSame(FlatKey.getFlatKeyFromFormattedKey("\"a\""), parents.get(1));
    }

    @Test
    @DisplayName("Child composition should append raw parts and handle ROOT appropriately")
    void shouldComposeChildKeys() {
        // human formatted: "a"."b"
        FlatKey parent = FlatKey.getFlatKeyFromFormattedKey("\"a\".\"b\"");
        FlatKey childSinglePartWithDot = FlatKey.getSinglePartFlatKeyFromString("c.d"); // remains one logical part

        FlatKey composed = parent.getChild(childSinglePartWithDot);
        assertEquals(3, composed.getSize());
        assertEquals(List.of("a", "b", "c.d"), composed.getParts());
        assertSame(parent, composed.getDirectParent());

        // parent + ROOT == parent
        FlatKey parentPlusRoot = parent.getChild(FlatKey.ROOT);
        assertSame(parent, parentPlusRoot);

        // ROOT + child == child
        FlatKey fromRoot = FlatKey.ROOT.getChild(childSinglePartWithDot);
        assertSame(childSinglePartWithDot, fromRoot);

        // ROOT + ROOT is invalid
        assertThrows(RuntimeException.class, () -> FlatKey.ROOT.getChild(FlatKey.ROOT));
    }

    @Test
    @DisplayName("getWithoutFirstPart should drop first part of composite keys")
    void shouldDropFirstPart() {
        FlatKey composite = FlatKey.getFlatKeyFromFormattedKey("\"a\".\"b\".\"c\"");
        FlatKey tail = composite.getWithoutFirstPart();
        assertEquals(List.of("b", "c"), tail.getParts());
        assertEquals("\"b\".\"c\"", tail.getFormattedKey());
    }

    @Test
    @DisplayName("getWithoutFirstPart should throw on ROOT and single-part keys")
    void shouldThrowOnRootOrSinglePart() {
        assertThrows(RuntimeException.class, () -> FlatKey.ROOT.getWithoutFirstPart());
        FlatKey single = FlatKey.getFlatKeyFromFormattedKey("\"only\"");
        assertThrows(RuntimeException.class, single::getWithoutFirstPart);
    }

    @Test
    @DisplayName("Factory methods should cache instances for identical formatted keys")
    void shouldCacheInstances() {
        // human formatted: "a"
        FlatKey k1 = FlatKey.getFlatKeyFromFormattedKey("\"a\"");
        // human formatted: "a"
        FlatKey k2 = FlatKey.getFlatKeyFromFormattedKey("\"a\"");
        FlatKey k3 = FlatKey.getSinglePartFlatKeyFromString("a");

        assertSame(k1, k2);
        assertSame(k1, k3);
    }

    @Test
    @DisplayName("Invalid formatted keys should throw IllegalArgumentException")
    void shouldRejectInvalidFormattedKeys() {
        // human formatted: a (not quoted)
        assertThrows(IllegalArgumentException.class, () -> FlatKey.getFlatKeyFromFormattedKey("a")); // not quoted
        // human formatted: "a".b (second part not quoted)
        assertThrows(IllegalArgumentException.class, () -> FlatKey.getFlatKeyFromFormattedKey("\"a\".b")); // second part not quoted
        // human formatted: "a". (trailing dot)
        assertThrows(IllegalArgumentException.class, () -> FlatKey.getFlatKeyFromFormattedKey("\"a\".")); // trailing dot
        // human formatted: "a".."b" (empty segment)
        assertThrows(IllegalArgumentException.class, () -> FlatKey.getFlatKeyFromFormattedKey("\"a\"..\"b\"")); // empty segment
    }

    @Test
    @DisplayName("compareTo should order by length then alphabetical for ties")
    void shouldCompareByLengthThenAlphabetical() {
        FlatKey a = FlatKey.getFlatKeyFromFormattedKey("\"a\"");       // length 3
        FlatKey bb = FlatKey.getFlatKeyFromFormattedKey("\"bb\"");     // length 4
        FlatKey ac = FlatKey.getFlatKeyFromFormattedKey("\"ac\"");     // length 4
        FlatKey ab = FlatKey.getFlatKeyFromFormattedKey("\"ab\"");     // length 4
        FlatKey a_b = FlatKey.getFlatKeyFromFormattedKey("\"a\".\"b\""); // length 7

        List<FlatKey> list = new ArrayList<>(List.of(a_b, bb, a, ac, ab));
        Collections.sort(list);

        assertSame(a, list.get(0));
        assertSame(ab, list.get(1));
        assertSame(ac, list.get(2));
        assertSame(bb, list.get(3));
        assertSame(a_b, list.get(4));
    }
} 