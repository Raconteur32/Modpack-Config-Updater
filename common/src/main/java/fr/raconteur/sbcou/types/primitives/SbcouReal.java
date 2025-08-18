package fr.raconteur.sbcou.types.primitives;

import fr.raconteur.sbcou.db.versions.DbDataValues;
import fr.raconteur.sbcou.types.SbcouPrimitives;

import java.math.BigDecimal;

public class SbcouReal extends SbcouPrimitives<BigDecimal> {
    
    public SbcouReal(BigDecimal value) {
        super("REAL", value);
    }
    
    public SbcouReal(int value) {
        super("REAL", BigDecimal.valueOf(value));
    }
    
    public SbcouReal(long value) {
        super("REAL", BigDecimal.valueOf(value));
    }
    
    public SbcouReal(double value) {
        super("REAL", BigDecimal.valueOf(value));
    }
    
    public SbcouReal(float value) {
        super("REAL", BigDecimal.valueOf(value));
    }
    
    public SbcouReal(String value) {
        super("REAL", new BigDecimal(value));
    }

    @Override
    public BigDecimal getValue() {
        return value;
    }

    @Override
    protected byte[] getDbSerializedValue() {
        return DbDataValues.getStringValueAsBytes(getValue().toString());
    }
} 