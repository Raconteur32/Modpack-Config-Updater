package fr.paulbrancieq.modpackconfigupdater.exceptions;

import fr.paulbrancieq.modpackconfigupdater.options.Option;

public class OptionException extends Exception {
  public OptionException(String message) {
    super(message);
  }

  public OptionException(String message, Throwable cause) {
    super(message, cause);
  }

  public static class CantMergeOption extends OptionException {
    public CantMergeOption(Option<?> baseOption, Option<?> optionToMerge, String reason) {
      super("Can't merge option " + optionToMerge.getOptionPath().getFullPath() +
          "(" + optionToMerge.getValue().getClass() + ") into " + baseOption.getOptionPath().getFullPath()
          + "(" + baseOption.getValue().getClass() + "): " + reason);
    }

    public CantMergeOption(Option<?> baseOption, Option<?> optionToMerge, Throwable cause) {
      super("Can't merge option " + optionToMerge.getOptionPath().getFullPath() + "(" + optionToMerge.getClass() +
          ") into " + baseOption.getOptionPath().getFullPath() + "(" + baseOption.getClass() + ")", cause);
    }
  }

  public static class CantSaveOption extends OptionException {
    public CantSaveOption(Option<?> option, String message) {
      super("Can't save option " + option.getOptionPath().getFullPath() + "(" + option.getValue().getClass() + "): " + message);
    }
  }

  public static class FileException extends OptionException {
    public FileException(String message) {
      super(message);
    }

    public FileException(String message, Throwable cause) {
      super(message, cause);
    }

    public static class CantReadFile extends FileException {
      public CantReadFile(String path) {
        super("Can't read file at path " + path);
      }

      public CantReadFile(String path, Throwable cause) {
        super("Can't read file at path " + path, cause);
      }
    }

    public static class CantWriteFile extends FileException {
      public CantWriteFile(String path) {
        super("Can't write file at path " + path);
      }

      public CantWriteFile(String path, Throwable cause) {
        super("Can't write file at path " + path, cause);
      }
    }

    public static class CantDeleteFile extends FileException {
      public CantDeleteFile(String path) {
        super("Can't delete file at path " + path);
      }

      public CantDeleteFile(String path, Throwable cause) {
        super("Can't delete file at path " + path, cause);
      }
    }
  }
}
