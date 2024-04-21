package fr.paulbrancieq.modpackconfigupdater.remake.exception;

public class McuException extends Exception {
  public McuException(String message) {
    super(message);
  }

  public McuException(String message, Throwable cause) {
    super(message, cause);
  }
}
