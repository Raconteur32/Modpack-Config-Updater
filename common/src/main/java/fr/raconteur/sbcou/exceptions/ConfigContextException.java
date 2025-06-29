package fr.raconteur.sbcou.exceptions;

public class ConfigContextException extends SbcouException {
  public ConfigContextException(String message) {
    super(message);
  }

  public ConfigContextException(String message, Throwable cause) {
    super(message, cause);
  }
}
