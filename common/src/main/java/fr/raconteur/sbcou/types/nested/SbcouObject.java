package fr.raconteur.sbcou.types.nested;

import com.google.common.collect.ImmutableMap;
import fr.raconteur.sbcou.db.DbDataValues;
import fr.raconteur.sbcou.flatobject.FlatKey;
import fr.raconteur.sbcou.flatobject.FlatObject;
import fr.raconteur.sbcou.types.SbcouData;
import fr.raconteur.sbcou.types.SbcouNested;

import java.util.*;
import java.util.stream.Collectors;

public class SbcouObject extends SbcouNested<Map<String, SbcouData<?>>> {
    
    public SbcouObject(Map<String, SbcouData<?>> value) {
        super("OBJECT", value);
    }
    
    public SbcouObject() {
        super("OBJECT", Map.of());
    }

    public static SbcouObject fromDbStringValue(String dbSerializedValue) {
        Map<String, SbcouData<?>> values = new HashMap<>();
        Arrays.stream(dbSerializedValue.split("(?<!\\\\),")).map(s -> s.split("(?<!\\\\):")).forEach(strings -> values.put(unescapeKey(strings[0]), SbcouData.sbcouDataFromId(Integer.parseInt(strings[1]))));
        return new SbcouObject(values);
    }

    @Override
    public Map<String, SbcouData<?>> getValue() {
        return ImmutableMap.copyOf(value);
    }

    @Override
    public String getDisplayValue() {
        List<String> displayList = new ArrayList<>();
        StringBuilder display = new StringBuilder("{\r\n    ");
        for (Map.Entry<String, SbcouData<?>> entry : getValue().entrySet()) {
            String displayLine = entry.getKey() + " : " +
                    entry.getValue().getDisplayValue().replace("\r\n", "\r\n    ");
            displayList.add(displayLine);
        }
        display.append(String.join(",\r\n    ",  displayList)).append("\r\n}");
        return display.toString();
    }

    @Override
    protected byte[] getDbSerializedValue() {
        if (getValue() == null || getValue().isEmpty()) {
            return DbDataValues.getStringValueAsBytes("");
        }
        
        String serialized = getValue().entrySet().stream().sorted(Map.Entry.comparingByKey())
                .map(entry -> escapeKey(entry.getKey()) + ":" + entry.getValue().getDataId())
                .collect(Collectors.joining(","));
        
        return DbDataValues.getStringValueAsBytes(serialized);
    }
    
    /**
     * Escapes special characters in keys
     * Replaces : with \: and , with \,
     *
     * @param key The key to escape
     * @return The escaped key
     */
    private static String escapeKey(String key) {
        if (key == null) {
            return "";
        }
        return key.replace("\\", "\\\\")
                  .replace(":", "\\:")
                  .replace(",", "\\,");
    }

    /**
     * Unescape special character in keys
     * Replaces \: wit : and \, with ,
     *
     * @param key The key to unescape
     * @return The unescaped key
     */
    private static String unescapeKey(String key) {
        if (key == null) {
            return "";
        }
        return key.replace("\\\\", "\\")
                .replace("\\:", ":")
                .replace("\\,", ",");
    }

    @Override
    public FlatObject flatten() {
        FlatObject flatObject = new FlatObject();

        for (Map.Entry<String, SbcouData<?>> entry : getValue().entrySet()) {
            FlatKey newFlatKey = FlatKey.getFlatKeyFromSingleString(escapeKey(entry.getKey()));
            flatObject.putSubFlatObject(newFlatKey, entry.getValue().flatten());
        }

        return flatObject;
    }
}