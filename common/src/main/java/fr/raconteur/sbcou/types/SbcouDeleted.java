package fr.raconteur.sbcou.types;

import fr.raconteur.sbcou.flatobject.FlatObject;

public class SbcouDeleted extends SbcouData<String> {
    public SbcouDeleted() {
        super(null, null);
    }

    @Override
    public String getValue() {
        throw new UnsupportedOperationException("Cannot use deleted value as regular value");
    }

    @Override
    public String getDisplayValue() {
        throw new UnsupportedOperationException("Cannot use deleted value as regular value");
    }

    @Override
    protected byte[] getDbSerializedValue() {
        throw new UnsupportedOperationException("Cannot use deleted value as regular value");
    }

    @Override
    public FlatObject flatten() {
        throw new UnsupportedOperationException("Cannot use deleted value as regular value");
    }

    @Override
    public boolean equals(SbcouData<?> other) {
        throw new UnsupportedOperationException("Cannot use deleted value as regular value");
    }

    @Override
    public int getDataId() {
        throw new UnsupportedOperationException("Cannot use deleted value as regular value");
    }
}
