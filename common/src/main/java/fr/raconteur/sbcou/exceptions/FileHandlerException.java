package fr.raconteur.sbcou.exceptions;

public class FileHandlerException extends SbcouException {
  public FileHandlerException(String message) {
    super(message);
  }

  public FileHandlerException(String message, Throwable cause) {
    super(message, cause);
  }
}
