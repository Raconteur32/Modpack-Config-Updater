package fr.paulbrancieq.modpackconfigupdater.remake.option;

public abstract class CollectionOptionChildOperationException extends Exception {
  public CollectionOptionChildOperationException(String message) {
    super(message);
  }

  public static class NotACollectionOption extends CollectionOptionChildOperationException {
    public NotACollectionOption() {
      super("Cannot perform operation on a sub-option of a non-collection option.");
    }
  }

  public static class NotADefaultableCollectionOption extends CollectionOptionChildOperationException {
    public NotADefaultableCollectionOption() {
      super("Can only default sub-option of a default compatible collection (Example: Maps).");
    }
  }

  public static class CannotGetDefaultOptionsFromADifferentOptionType extends CollectionOptionChildOperationException {
    public String actualType;
    public String defaultRefType;

    public CannotGetDefaultOptionsFromADifferentOptionType(Class<?> actualType, Class<?> defaultRefType) {
      super("Cannot get default options from a different option type.");
      this.actualType = actualType.getSimpleName();
      this.defaultRefType = defaultRefType.getSimpleName();
    }
  }

  public static class ChildDoesNotExist extends CollectionOptionChildOperationException {
    public ChildDoesNotExist() {
      super("The child does not exist.");
    }
  }

  public static class InvalidKeyIndex extends CollectionOptionChildOperationException {
    public InvalidKeyIndex(String message) {
      super(message);
    }
  }
}
