package fr.raconteur.sbcou.types.primitives;

import fr.raconteur.sbcou.db.DbDataValues;
import fr.raconteur.sbcou.types.SbcouPrimitives;

import java.util.ArrayList;
import java.util.List;

public class SbcouNaN extends SbcouPrimitives<String> {

    public SbcouNaN(String value) {
        super("NAN", value);
        List<String> acceptedNaNs = new ArrayList<>() {
            {
                add("+NaN");
                add("NaN");
                add("-NaN");
            }
        };
        if (!acceptedNaNs.contains(value)) {
            throw new RuntimeException(String.format("The value %s is not supported for NaN type", value));
        }
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    protected byte[] getDbSerializedValue() {
        return DbDataValues.getStringValueAsBytes(getValue());
    }
}