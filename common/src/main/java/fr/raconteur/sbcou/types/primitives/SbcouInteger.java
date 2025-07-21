package fr.raconteur.sbcou.types.primitives;

import fr.raconteur.sbcou.db.DbDataValues;
import fr.raconteur.sbcou.types.SbcouPrimitives;

import java.math.BigInteger;

public class SbcouInteger extends SbcouPrimitives<BigInteger> {
    
    public SbcouInteger(BigInteger value) {
        super("INTEGER", value);
    }
    
    public SbcouInteger(int value) {
        super("INTEGER", BigInteger.valueOf(value));
    }
    
    public SbcouInteger(long value) {
        super("INTEGER", BigInteger.valueOf(value));
    }
    
    public SbcouInteger(String value) {
        super("INTEGER", new BigInteger(value));
    }

    @Override
    public BigInteger getValue() {
        return value;
    }

    @Override
    protected byte[] getDbSerializedValue() {
        return DbDataValues.getStringValueAsBytes(getValue().toString());
    }
}