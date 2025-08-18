package fr.raconteur.sbcou.types.primitives;

import fr.raconteur.sbcou.db.versions.DbDataValues;
import fr.raconteur.sbcou.types.SbcouPrimitives;

public class SbcouString extends SbcouPrimitives<String> {
    
    public SbcouString(String value) {
        super("STRING", value);
    }
    
    public SbcouString(Object value) {
        super("STRING", value.toString());
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