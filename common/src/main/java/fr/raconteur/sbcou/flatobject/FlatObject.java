package fr.raconteur.sbcou.flatobject;

import fr.raconteur.sbcou.types.SbcouData;

import java.util.*;

public class FlatObject {
    TreeMap<FlatKey, SbcouData<?>> map = new TreeMap<>();

    public FlatObject() {
        super();
    }

    public SbcouData<?> put(FlatKey key, SbcouData<?> value) {
        return map.put(key, value);
    }

    private void putAll(FlatObject m) {
        map.putAll(m.map);
    }

    public SbcouData<?> get(FlatKey key) {
        return map.get(key);
    }

    public void putSubFlatObject(FlatKey key, FlatObject other) {
        other.map.forEach((key1, value) -> map.put(key.getChild(key1), value));
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
}
