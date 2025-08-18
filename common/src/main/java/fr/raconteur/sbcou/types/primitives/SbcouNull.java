package fr.raconteur.sbcou.types.primitives;

import fr.raconteur.sbcou.db.versions.DbDataValues;
import fr.raconteur.sbcou.types.SbcouPrimitives;

public class SbcouNull extends SbcouPrimitives<String> {

    public SbcouNull() {
        super("NULL", null);
    }

    @Override
    public String getValue() {
        return null;
    }

    @Override
    protected byte[] getDbSerializedValue() {
        return DbDataValues.getStringValueAsBytes("null");
    }

    @Override
    public String getDisplayValue() {
        return "null";
    }
}