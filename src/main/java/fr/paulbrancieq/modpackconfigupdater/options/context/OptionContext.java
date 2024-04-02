package fr.paulbrancieq.modpackconfigupdater.options.context;

import fr.paulbrancieq.modpackconfigupdater.Backup;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class OptionContext {
  public OptionContext(String basePath, @Nullable Backup backup, List<String> rPathsToIgnore) {
    if (!new File(basePath).isDirectory()) {
      throw new IllegalArgumentException("basePath for Option Context must be a directory");
    }
    List<Path> absolutePathsToIgnore = rPathsToIgnore.stream().map((rPath) -> Path.of(basePath, rPath)).toList();
    List<String> listOfFilesRelativeToBasePath =
        FileUtils.listFiles(new File(basePath), null, true).stream()
            .filter(file -> !absolutePathsToIgnore.contains(file.toPath()))
            .map(file -> {
              try {
                return Path.of(basePath).relativize(Path.of(file.getCanonicalPath())).toString();
              } catch (Exception e) {
                throw new RuntimeException(e);
              }
            }).toList();
  }
}
