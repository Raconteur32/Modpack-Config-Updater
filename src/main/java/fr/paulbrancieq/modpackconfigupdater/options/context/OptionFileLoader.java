package fr.paulbrancieq.modpackconfigupdater.options.context;

import fr.paulbrancieq.modpackconfigupdater.Backup;
import fr.paulbrancieq.modpackconfigupdater.ModpackConfigurationUpdater;
import fr.paulbrancieq.modpackconfigupdater.exceptions.OptionException;
import fr.paulbrancieq.modpackconfigupdater.options.Option;
import fr.paulbrancieq.modpackconfigupdater.options.type.JsonListOptionFile;
import fr.paulbrancieq.modpackconfigupdater.options.type.JsonMapOptionFile;
import fr.paulbrancieq.modpackconfigupdater.path.OptionPath;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public abstract class OptionFileLoader {
  protected final List<String> acceptedExtensions;
  public OptionFileLoader(List<String> acceptedExtensions) {
    this.acceptedExtensions = acceptedExtensions;
  }
  public Optional<Option<?>> tryLoad(String aPath, String rPath, Backup backup) {
    if (acceptedExtensions.stream().anyMatch(rPath::endsWith)) {
      return load(aPath, rPath, backup);
    }
    return Optional.empty();
  }
  public Optional<Option<?>> forceLoad(String aPath, String rPath, Backup backup) {
    return load(aPath, rPath, backup);
  }
  protected abstract Optional<Option<?>> load(String basePath, String rPath, Backup backup);

  public static class Json extends OptionFileLoader {
    public Json() {
      super(List.of(".json", ".json5"));
    }
    @Override
    protected Optional<Option<?>> load(String basePath, String rPath, Backup backup) {
      OptionPath optionPath = new OptionPath(rPath + ":");
      Path absolutePath = Path.of(basePath, rPath);
      try {
        JsonMapOptionFile optionFile = JsonMapOptionFile.fromFile(basePath, optionPath, backup);
        return Optional.of(optionFile);
      } catch (OptionException.FileException.CantReadFile e) {
        try {
          JsonListOptionFile optionFile = JsonListOptionFile.fromFile(basePath, optionPath, backup);
          return Optional.of(optionFile);
        } catch (OptionException.FileException.CantReadFile ex) {
          ModpackConfigurationUpdater.LOGGER.warn("Couldn't read file: " + absolutePath);
        }
      }
      return Optional.empty();
    }
  }
}
