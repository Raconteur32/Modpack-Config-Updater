package fr.raconteur.sbcou.flatobject;

import com.google.common.collect.ImmutableMap;
import fr.raconteur.sbcou.types.SbcouData;
import fr.raconteur.sbcou.types.SbcouDoNotExist;

import java.util.Map;
import java.util.TreeMap;

public class FlatObjectDiff {

    private final TreeMap<FlatKey, FlatObjectDiffEntry> diffs;

    public FlatObjectDiff(FlatObject first, FlatObject second) {
        diffs = new TreeMap<>();

        // Iterate entries from first: detect DELETED, DIFFERENT, TYPE_DIFFERENT
        for (Map.Entry<FlatKey, SbcouData<?>> firstEntry : first.map.entrySet()) {
            FlatKey key = firstEntry.getKey();
            SbcouData<?> firstValue = firstEntry.getValue();
            SbcouData<?> secondValue = second.map.get(key);

            if (secondValue == null) {
                // present in first, missing in second => DELETED
                diffs.put(key, new FlatObjectDiffEntry(key, FlatObjectEntryDiffType.DELETED, firstValue, null));
            } else if (firstValue != secondValue) {
                // Different instances; check type
                if (!firstValue.getStringType().equals(secondValue.getStringType())) {
                    diffs.put(key, new FlatObjectDiffEntry(key, FlatObjectEntryDiffType.TYPE_DIFFERENT, firstValue, secondValue));
                } else if (!firstValue.equals(secondValue)) {
                    // Same type but not equal in DB identity
                    diffs.put(key, new FlatObjectDiffEntry(key, FlatObjectEntryDiffType.DIFFERENT, firstValue, secondValue));
                }
                // else equal in DB identity -> no diff
            }
            // else exact same instance -> no diff
        }

        // Iterate entries from second only: detect NEW
        for (Map.Entry<FlatKey, SbcouData<?>> secondEntry : second.map.entrySet()) {
            FlatKey key = secondEntry.getKey();
            if (!first.map.containsKey(key)) {
                diffs.put(key, new FlatObjectDiffEntry(key, FlatObjectEntryDiffType.NEW, new SbcouDoNotExist(), secondEntry.getValue()));
            }
        }
    }

    public Map<FlatKey, FlatObjectDiffEntry> getDiffs() {
        return ImmutableMap.copyOf(diffs);
    }

    public enum FlatObjectEntryDiffType {
        DIFFERENT,
        TYPE_DIFFERENT,
        NEW,
        DELETED
    }

    public static class FlatObjectDiffEntry {
        FlatKey flatKey;
        FlatObjectEntryDiffType entryDiffType;
        SbcouData<?> oldValue;
        SbcouData<?> newValue;

        FlatObjectDiffEntry(FlatKey flatKey, FlatObjectEntryDiffType entryDiffType,  SbcouData<?> oldValue, SbcouData<?> newValue) {
            this.flatKey = flatKey;
            this.entryDiffType = entryDiffType;
            this.oldValue = oldValue;
            this.newValue = newValue;
        }
    }

}
