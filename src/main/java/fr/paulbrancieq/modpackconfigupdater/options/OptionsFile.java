package fr.paulbrancieq.modpackconfigupdater.options;

import org.jetbrains.annotations.MustBeInvokedByOverriders;

import java.io.File;
import java.util.Optional;

public interface OptionsFile<T> {
  File getFile();

  default Runnable getFileRemover() {
    return () -> {
      try {
        boolean fileDeleted = getFile().delete();
        if (!fileDeleted) {
          throw new RuntimeException("The file was not deleted: " + getFile().getAbsolutePath());
        }
      } catch (Exception e) {
        throw new RuntimeException("Can't remove the file.", e);
      }
    };
  }
}
