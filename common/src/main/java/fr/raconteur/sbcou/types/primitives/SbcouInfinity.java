package fr.raconteur.sbcou.types.primitives;

import fr.raconteur.sbcou.db.versions.DbDataValues;
import fr.raconteur.sbcou.types.SbcouPrimitives;

import java.util.ArrayList;
import java.util.List;

public class SbcouInfinity extends SbcouPrimitives<String> {

    public SbcouInfinity(String value) {
        super("INFINITY", value);
        List<String> acceptedNaNs = new ArrayList<String>() {
            {
                add("+Infinity");
                add("Infinity");
                add("-Infinity");
            }
        };
        if (!acceptedNaNs.contains(value)) {
            throw new RuntimeException(String.format("The value %s is not supported for Infinity type", value));
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