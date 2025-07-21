package fr.raconteur.sbcou.types;

import fr.raconteur.sbcou.db.DbDataValues;
import fr.raconteur.sbcou.flatobject.FlatObject;
import fr.raconteur.sbcou.types.nested.SbcouList;
import fr.raconteur.sbcou.types.nested.SbcouObject;
import fr.raconteur.sbcou.types.primitives.*;
import oshi.annotation.concurrent.Immutable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

public abstract class SbcouData<T> {
    protected final T value;
    protected final DbDataValues dbDataValue;

    protected SbcouData(String stringType, T value) {
        this.value = value;
        if (stringType != null) {
            this.dbDataValue = DbDataValues.get(stringType, getDbSerializedValue());
        }
        else {
            this.dbDataValue = null;
        }

    }

    public static  SbcouData<?> sbcouDataFromId(Integer id) {
        Optional<DbDataValues> dbValue = DbDataValues.getFromDb(id);
        DbDataValues dbDataValue;

        if (dbValue.isPresent()) {
            dbDataValue = dbValue.get();
        } else {
          throw new RuntimeException("Value with id " + id + " not found");
        }

        switch (dbDataValue.getDataType()) {
            case "INTEGER" -> {
                return new SbcouInteger(new BigInteger(dbDataValue.getValueAsString()));
            }
            case "REAL" -> {
                return new SbcouReal(new BigDecimal(dbDataValue.getValueAsString()));
            }
            case "NAN" -> {
                return new SbcouNaN(dbDataValue.getValueAsString());
            }
            case "INFINITY" -> {
                return new SbcouInfinity(dbDataValue.getValueAsString());
            }
            case "NULL" -> {
                return new SbcouNull();
            }
            case "BOOLEAN" -> {
                return new SbcouBoolean(dbDataValue.getValueAsString());
            }
            case "STRING" -> {
                return new SbcouString(dbDataValue.getValueAsString());
            }
            case "LIST" -> {
                return SbcouList.fromDbStringValue(dbDataValue.getValueAsString());
            }
            case "OBJECT" -> {
                return SbcouObject.fromDbStringValue(dbDataValue.getValueAsString());
            }
            default -> throw new RuntimeException("Unknown datatype " + dbDataValue.getDataType());
        }
    }

    /**
     *
     * @return T Immutable version or copy of value
     */
    public abstract T getValue();

    public abstract String getDisplayValue();

    public boolean equals(SbcouData<?> other) {
        return this.dbDataValue.getId() == other.dbDataValue.getId();
    }

    /**
     * Returns the id of dbDataValue
     *
     * @return The identifier of the value in the database
     */
    public int getDataId() {
        return dbDataValue.getId();
    }

    public String getStringType() {
        return dbDataValue.getDataType();
    }

    protected abstract byte[] getDbSerializedValue();

    public abstract FlatObject flatten();
}
