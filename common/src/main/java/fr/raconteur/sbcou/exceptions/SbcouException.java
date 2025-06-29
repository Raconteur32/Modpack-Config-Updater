package fr.raconteur.sbcou.exceptions;

public class SbcouException extends Exception {
  public SbcouException(String message) {
    super(message);
  }

  public SbcouException(String message, Throwable cause) {
    super(message, cause);
  }
}
