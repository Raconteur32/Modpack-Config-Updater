package fr.raconteur.sbcou.flatobject;

import fr.raconteur.sbcou.types.SbcouData;
import fr.raconteur.sbcou.types.SbcouDoNotExist;
import fr.raconteur.sbcou.types.nested.SbcouObject;
import fr.raconteur.sbcou.types.primitives.SbcouNull;

import java.util.*;

public class FlatObject {
    // package-private for use by FlatObjectDiff and others in the same package
    final TreeMap<FlatKey, SbcouData<?>> map;

    private FlatObject(TreeMap<FlatKey, SbcouData<?>> map) {
        this.map = map;
    }

    public static Builder builder(SbcouData<?> rootValue) {
        return new Builder(rootValue);
    }

    public String getDisplayValue() {
        List<String> displayList = new ArrayList<>();
        StringBuilder display = new StringBuilder("{\r\n    ");
        for (Map.Entry<FlatKey, SbcouData<?>> entry : map.entrySet()) {
            FlatKey key = entry.getKey();
            SbcouData<?> value = entry.getValue();
            String displayLine = key.getFormattedKey() + " : " + value.getDisplayValue().replace("\r\n", "\r\n    ");
            displayList.add(displayLine);
        }
        display.append(String.join(",\r\n    ",  displayList)).append("\r\n}");
        return display.toString();
    }

    public boolean isSingleValue() {
        return map.size() == 1 && map.containsKey(FlatKey.ROOT);
    }

    public boolean isComposedValue() {
        return map.size() != 1 && map.containsKey(FlatKey.ROOT);
    }

    public boolean contains(FlatKey key) {
        return map.containsKey(key);
    }

    public SbcouData<?> get(FlatKey key) {
        return map.get(key);
    }

    public SbcouData<?> toSbcouValue() {
        // Case 1: exactly one entry at ROOT → return that value directly
        if (isSingleValue()) {
            return map.get(FlatKey.ROOT);
        }

        // Case 2: root present with additional entries → composed value, rebuild SbcouObject
        if (isComposedValue()) {
            Map<String, SbcouData<?>> objectEntries = new HashMap<>();
            for (Map.Entry<FlatKey, SbcouData<?>> entry : map.entrySet()) {
                FlatKey key = entry.getKey();
                if (key == FlatKey.ROOT) {
                    continue;
                }
                if (key.getSize() == 1) {
                    String topLevelName = key.getParts().getFirst();
                    FlatObject merged = entry.getValue()
                            .flatten()
                            .getUpdatedFromOtherFlatObject(getSubFlatObject(key, entry.getValue()));
                    objectEntries.put(topLevelName, merged.toSbcouValue());
                }
            }
            return new SbcouObject(objectEntries);
        }

        // Case 3: no ROOT present → cannot convert
        throw new RuntimeException("Cannot convert FlatObject to SbcouData: ROOT key is missing");
    }

    public FlatObjectDiff getDiffTo(FlatObject other) {
        return new FlatObjectDiff(this, other);
    }

    private FlatObject getSubFlatObject(FlatKey baseKey, SbcouData<?> baseValue) {
        Builder builder = builder(baseValue);
        for (Map.Entry<FlatKey, SbcouData<?>> entry : map.entrySet()) {
            FlatKey key = entry.getKey();
            if (key.getAllParents().contains(baseKey)) {
                builder.put(key.getWithoutFirstPart(), entry.getValue());
            }
        }
        return builder.build();
    }

    private FlatObject getUpdatedFromOtherFlatObject(FlatObject other) {
        if (!this.isSingleValue() && !other.isSingleValue()) {
            return builder(this.map.get(FlatKey.ROOT))
                    .putAll(this)
                    .putAll(other)
                    .build();
        }
        return other;
    }

    public static class Builder {
        private final TreeMap<FlatKey, SbcouData<?>> working = new TreeMap<>();

        public Builder(SbcouData<?> rootValue) {
            working.put(FlatKey.ROOT, rootValue);
        }

        public boolean isSingleValue() {
            return working.size() == 1 && working.containsKey(FlatKey.ROOT);
        }

        public boolean isComposedValue() {
            return working.size() != 1 && working.containsKey(FlatKey.ROOT);
        }

        public boolean contains(FlatKey key) {
            return working.containsKey(key);
        }

        public SbcouData<?> get(FlatKey key) {
            return working.get(key);
        }

        public Builder put(FlatKey key, SbcouData<?> value) {
            working.put(key, value);
            return this;
        }

        public Builder putAll(FlatObject other) {
            working.putAll(other.map);
            return this;
        }

        public Builder putSubFlatObject(FlatKey key, FlatObject other) {
            other.map.forEach((k, v) -> working.put(key.getChild(k), v));
            return this;
        }

        public Builder resetComposedValueRoot() {
            if (!isComposedValue()) {
                return this;
            }
            Map<String, SbcouData<?>> objectEntries = new HashMap<>();
            for (Map.Entry<FlatKey, SbcouData<?>> entry : working.entrySet()) {
                FlatKey key = entry.getKey();
                if (key == FlatKey.ROOT) continue;
                if (key.getSize() == 1) {
                    String topLevelName = key.getParts().getFirst();
                    // Build sub-flat for this top-level key from current working map
                    Builder subBuilder = new Builder(entry.getValue());
                    for (Map.Entry<FlatKey, SbcouData<?>> subEntry : working.entrySet()) {
                        FlatKey k2 = subEntry.getKey();
                        if (k2.getAllParents().contains(key)) {
                            subBuilder.put(k2.getWithoutFirstPart(), subEntry.getValue());
                        }
                    }
                    FlatObject subFlat = subBuilder.build();
                    objectEntries.put(topLevelName, subFlat.toSbcouValue());
                }
            }
            working.put(FlatKey.ROOT, new SbcouObject(objectEntries));
            return this;
        }

        public FlatObject build() {
            return new FlatObject(new TreeMap<>(working));
        }
    }
}
