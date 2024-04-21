package fr.paulbrancieq.modpackconfigupdater.options.context;

import fr.paulbrancieq.modpackconfigupdater.Backup;
import fr.paulbrancieq.modpackconfigupdater.options.Option;
import fr.paulbrancieq.modpackconfigupdater.path.OptionPath;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

public class OptionContext {
  private final Path basePath;
  private final Backup backup;
  private final List<OptionFileLoader> optionFileLoaders = List.of(new OptionFileLoader.Json());
  private final Map<Path, Option<?>> optionsMap = new HashMap<>();
  public OptionContext(String basePath, @Nullable Backup backup, List<String> rPathsToIgnore) {
    this.basePath = Path.of(basePath).normalize();
    this.backup = backup;
    if (!new File(basePath).isDirectory()) {
      throw new IllegalArgumentException("basePath for Option Context must be a directory");
    }
    List<Path> absolutePathsToIgnore = rPathsToIgnore.stream().map((rPath) -> Path.of(basePath, rPath)).toList();
    List<Path> listOfFilesRelativeToBasePath =
        FileUtils.listFiles(new File(basePath), null, true).stream()
            .filter(file -> !absolutePathsToIgnore.contains(file.toPath()))
            .map(file -> {
              try {
                return Path.of(basePath).relativize(Path.of(file.getCanonicalPath())).normalize();
              } catch (Exception e) {
                throw new RuntimeException(e);
              }
            }).toList();
    buildOptionsMap(listOfFilesRelativeToBasePath);
  }

  private void buildOptionsMap(List<Path> listOfFilesRelativeToBasePath) {
    for (Path path : listOfFilesRelativeToBasePath) {
      Option<?> option = null;
      for (OptionFileLoader optionFileLoader : optionFileLoaders) {
        Optional<Option<?>> optionalOption = optionFileLoader.tryLoad(basePath.toString(), path.toString(), backup);
        if (optionalOption.isPresent()) {
          option = optionalOption.get();
          break;
        }
      }
      if (option != null) {
        optionsMap.put(path, option);
      }
    }
  }

  public List<Option<?>> getOptionsFromOptionPath(OptionPath optionPath) {
    List<Option<?>> options = new ArrayList<>();
    if (!optionsMap.containsKey(Path.of(optionPath.getFilePath()).normalize())) {
      return options;
    }
    Option<?> optionFile = optionsMap.get(Path.of(optionPath.getFilePath()).normalize());
    return optionFile.getSubOptions(optionPath.getInFileOptionPathParts());
  }
}
