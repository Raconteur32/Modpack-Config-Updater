package fr.raconteur.sbcou.flatobject;

import com.google.common.collect.ImmutableMap;
import fr.raconteur.sbcou.types.SbcouData;
import fr.raconteur.sbcou.types.SbcouDeleted;


import java.util.Map;
import java.util.TreeMap;

public class FlatObjectDiff {

    private final TreeMap<FlatKey, FlatObjectDiffEntry> diffs;

    public FlatObjectDiff(FlatObject first, FlatObject second) {
        diffs = new TreeMap<>();

        for (Map.Entry<FlatKey, SbcouData<?>> firstEntry : first.map.entrySet()) {
            if  (!second.map.containsKey(firstEntry.getKey())) {
                diffs.put(
                        firstEntry.getKey(),
                        new FlatObjectDiffEntry(
                                firstEntry.getKey(),
                                FlatObjectEntryDiffType.DELETED,
                                firstEntry.getValue(),
                                second.map.get(firstEntry.getKey())
                        )
                );
            } else {
                if (firstEntry.getValue() == second.map.get(firstEntry.getKey())) {
                    diffs.put(
                            firstEntry.getKey(),
                            new FlatObjectDiffEntry(
                                    firstEntry.getKey(),
                                    FlatObjectEntryDiffType.EQUAL,
                                    firstEntry.getValue(),
                                    second.map.get(firstEntry.getKey())
                            )
                    );
                } else if (!firstEntry.getValue().getStringType().equals(second.map.get(firstEntry.getKey()).getStringType())) {
                    diffs.put(
                            firstEntry.getKey(),
                            new FlatObjectDiffEntry(
                                    firstEntry.getKey(),
                                    FlatObjectEntryDiffType.TYPE_DIFFERENT,
                                    firstEntry.getValue(),
                                    second.map.get(firstEntry.getKey())
                            )
                    );
                }  else {
                    diffs.put(
                            firstEntry.getKey(),
                            new FlatObjectDiffEntry(
                                    firstEntry.getKey(),
                                    FlatObjectEntryDiffType.DIFFERENT,
                                    firstEntry.getValue(),
                                    second.map.get(firstEntry.getKey())
                            )
                    );
                }
            }
        }
        for (Map.Entry<FlatKey, SbcouData<?>> secondEntry : second.map.entrySet()) {
            if (!first.map.containsKey(secondEntry.getKey())) {
                diffs.put(
                        secondEntry.getKey(),
                        new FlatObjectDiffEntry(
                                secondEntry.getKey(),
                                FlatObjectEntryDiffType.NEW,
                                new SbcouDeleted(),
                                secondEntry.getValue()
                        )
                );
            }
        }
    }

    public Map<FlatKey, FlatObjectDiffEntry> getDiffs() {
        return ImmutableMap.copyOf(diffs);
    }

    public enum FlatObjectEntryDiffType {
        EQUAL,
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
