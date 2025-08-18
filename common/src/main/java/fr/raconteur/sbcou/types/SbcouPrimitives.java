package fr.raconteur.sbcou.types;

import fr.raconteur.sbcou.flatobject.FlatKey;
import fr.raconteur.sbcou.flatobject.FlatObject;

public abstract class SbcouPrimitives<T> extends SbcouData<T> {
    protected SbcouPrimitives(String stringType, T value) {
        super(stringType, value);
    }

    public FlatObject flatten() {
        return FlatObject.builder(this)
                .build();
    }

    public String getDisplayValue() {
        return getValue().toString();
    }
}
