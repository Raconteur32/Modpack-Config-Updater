package fr.paulbrancieq.modpackconfigupdater.exceptions;

public class FilterException extends Exception {
  public FilterException(String message) {
    super(message);
  }

  public FilterException(String message, Throwable cause) {
    super(message, cause);
  }

  public static class InvalidFilterString extends FilterException {
    public InvalidFilterString(String message) {
      super(message);
    }

    public InvalidFilterString(String message, Throwable cause) {
      super(message, cause);
    }
  }

  public static class InvalidValueToFilter extends FilterException {
    public InvalidValueToFilter(String value, String[] validValues) {
      super("Invalid value to filter: " + value + ". Valid values are: " + String.join(", ", validValues));
    }

    public InvalidValueToFilter(String value, String[] validValues, Throwable cause) {
      super("Invalid value to filter: " + value + ". Valid values are: " + String.join(", ", validValues), cause);
    }
  }
}
