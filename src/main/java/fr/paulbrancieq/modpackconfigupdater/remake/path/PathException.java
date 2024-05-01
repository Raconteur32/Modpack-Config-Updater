package fr.paulbrancieq.modpackconfigupdater.remake.path;

public
class PathException extends Exception {
  public PathException(String message) {
    super(message);
  }

  public static class ReachedPathRoot extends PathException {
    public ReachedPathRoot() {
      super("Reached the beginning of the path.");
    }
  }
}
