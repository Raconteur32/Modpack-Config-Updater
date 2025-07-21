package fr.raconteur.sbcou.types;

public abstract class SbcouNested<T> extends SbcouData<T> {
    public SbcouNested(String stringType, T value) {
        super(stringType, value);
    }
}
