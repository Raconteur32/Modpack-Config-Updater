package fr.raconteur.sbcou.flatobject;

import fr.raconteur.sbcou.types.SbcouData;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class FlatPatchPreparation {
    Map<FlatKey, FlatPatchPreparationEntry> preparations;

    public FlatPatchPreparation(TreeMap<FlatKey, FlatObjectDiff.FlatObjectDiffEntry> diffs) {
        preparations = new HashMap<>();
        update(diffs);
    }

    public void update(TreeMap<FlatKey, FlatObjectDiff.FlatObjectDiffEntry> diffs) {
        for (Map.Entry<FlatKey, FlatObjectDiff.FlatObjectDiffEntry> entry : diffs.entrySet()) {
            FlatPatchPreparationEntry actual = preparations.get(entry.getKey());
            if (entry.getValue().entryDiffType != FlatObjectDiff.FlatObjectEntryDiffType.EQUAL) {
                if (actual == null || (!actual.oldValue.equals(entry.getValue().oldValue) || !actual.newValue.equals(entry.getValue().newValue))) {
                    preparations.put(entry.getKey(), FlatPatchPreparationEntry.fromDiff(entry.getValue()));
                }
            }
        }
        for (Map.Entry<FlatKey, FlatPatchPreparationEntry> entry : preparations.entrySet()) {
            if (!diffs.containsKey(entry.getKey())) {
                preparations.remove(entry.getKey());
            }
        }
    }

    public enum PatchActionChoice {
        UNDETERMINED,
        INCLUDE_AS_DEFAULT,
        OVERRIDE
    }

    public static class FlatPatchPreparationEntry {
        FlatKey flatKey;
        SbcouData<?> oldValue;
        SbcouData<?> newValue;

        PatchActionChoice action;

        public FlatPatchPreparationEntry(FlatKey flatKey, PatchActionChoice action, SbcouData<?> oldValue, SbcouData<?> newValue) {
            this.flatKey = flatKey;
            this.action = action;
            this.oldValue = oldValue;
            this.newValue = newValue;
        }

        public static FlatPatchPreparationEntry fromDiff(FlatObjectDiff.FlatObjectDiffEntry diff) {
            if (diff.entryDiffType == FlatObjectDiff.FlatObjectEntryDiffType.EQUAL) {
                throw new RuntimeException("Can't create a patch preparation entry when there is no difference");
            }
            return new FlatPatchPreparationEntry(diff.flatKey, PatchActionChoice.UNDETERMINED, diff.oldValue, diff.newValue);
        }
    }
}
