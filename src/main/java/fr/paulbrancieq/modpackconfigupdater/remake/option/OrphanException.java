package fr.paulbrancieq.modpackconfigupdater.remake.option;

public class OrphanException extends Exception {
  public OrphanException(String message) {
    super(message + " This exception should never be thrown or always be caught. Contact the developer.");
  }

  public static class CantGetParentOptionFromContainer extends OrphanException {
    public CantGetParentOptionFromContainer() {
      super("Can't get parent option from container.");
    }
  }
}
