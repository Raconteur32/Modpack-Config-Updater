package fr.raconteur.sbcou.types.nested;

import com.google.common.collect.ImmutableList;
import fr.raconteur.sbcou.db.versions.DbDataValues;
import fr.raconteur.sbcou.flatobject.FlatObject;
import fr.raconteur.sbcou.types.SbcouData;
import fr.raconteur.sbcou.types.SbcouDoNotExist;
import fr.raconteur.sbcou.types.SbcouNested;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SbcouList extends SbcouNested<List<SbcouData<?>>> {
    
    public SbcouList(List<SbcouData<?>> value) {
        super("LIST", value.stream().filter(v -> !(v instanceof SbcouDoNotExist)).collect(Collectors.toCollection(ArrayList::new)));
    }
    
    public SbcouList(SbcouData<?>... values) {
        super("LIST", Arrays.stream(values).filter(v -> !(v instanceof SbcouDoNotExist)).collect(Collectors.toCollection(ArrayList::new)));
    }

    public static SbcouList fromDbStringValue(String dbSerializedValue) {
        ArrayList<SbcouData<?>> values = new ArrayList<>(Arrays.stream(dbSerializedValue.split(",")).mapToInt(Integer::parseInt).mapToObj(SbcouData::sbcouDataFromId).toList());
        return new SbcouList(values);
    }

    @Override
    public List<SbcouData<?>> getValue() {
        return ImmutableList.copyOf(value);
    }

    @Override
    public String getDisplayValue() {
        List<String> displayList = new ArrayList<>();
        StringBuilder display = new StringBuilder("[\r\n    ");
        for (SbcouData<?> value : value) {
            String displayLine =  value.getDisplayValue().replace("\r\n", "\r\n    ");
            displayList.add(displayLine);
        }
        display.append(String.join(",\r\n    ",  displayList)).append("\r\n]");
        return display.toString();
    }

    @Override
    protected byte[] getDbSerializedValue() {
        if (getValue() == null || getValue().isEmpty()) {
            return DbDataValues.getStringValueAsBytes("");
        }
        
        String serialized = getValue().stream()
                .mapToInt(SbcouData::getDataId)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(","));
        
        return DbDataValues.getStringValueAsBytes(serialized);
    }

    @Override
    public FlatObject flatten() {
        return FlatObject.builder(this)
                .build();
    }
}
