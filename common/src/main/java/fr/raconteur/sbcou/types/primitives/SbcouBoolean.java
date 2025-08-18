package fr.raconteur.sbcou.types.primitives;

import fr.raconteur.sbcou.db.versions.DbDataValues;
import fr.raconteur.sbcou.types.SbcouPrimitives;

public class SbcouBoolean extends SbcouPrimitives<Boolean> {
    
    public SbcouBoolean(Boolean value) {
        super("BOOLEAN", value);
    }
    
    public SbcouBoolean(boolean value) {
        super("BOOLEAN", value);
    }
    
    public SbcouBoolean(String value) {
        super("BOOLEAN", Boolean.parseBoolean(value));
    }

    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    protected byte[] getDbSerializedValue() {
        return DbDataValues.getStringValueAsBytes(getValue().toString());
    }
} 