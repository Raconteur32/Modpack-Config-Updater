package fr.raconteur.sbcou.config;

import fr.raconteur.sbcou.exceptions.SbcouException;

public interface SbcouConfigFile<T> {
  class SbcouConfigFileException extends SbcouException {
    public SbcouConfigFileException(String message) {
      super(message);
    }
  }

  @FunctionalInterface
  public interface Verifier<T> {
    T verify(T data) throws SbcouConfigFileException;
  }

  T read() throws SbcouConfigFileException;
  void write(T data) throws SbcouConfigFileException;
  T verify() throws SbcouConfigFileException;
}
