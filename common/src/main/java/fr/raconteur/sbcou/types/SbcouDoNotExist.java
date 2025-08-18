package fr.raconteur.sbcou.types;

import fr.raconteur.sbcou.flatobject.FlatObject;

public class SbcouDoNotExist extends SbcouData<String> {
    public SbcouDoNotExist() {
        super(null, null);
    }

    @Override
    public String getValue() {
        throw new UnsupportedOperationException("Cannot use non-existent value as regular value");
    }

    @Override
    public String getDisplayValue() {
        throw new UnsupportedOperationException("Cannot use non-existent value as regular value");
    }

    @Override
    protected byte[] getDbSerializedValue() {
        throw new UnsupportedOperationException("Cannot use non-existent value as regular value");
    }

    @Override
    public FlatObject flatten() {
        return FlatObject.builder(this)
                .build();
    }

    @Override
    public boolean equals(SbcouData<?> other) {
        throw new UnsupportedOperationException("Cannot use non-existent value as regular value");
    }

    @Override
    public int getDataId() {
        return -1;
    }
} 