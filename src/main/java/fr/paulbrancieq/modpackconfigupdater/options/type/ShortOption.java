package fr.paulbrancieq.modpackconfigupdater.options.type;

import fr.paulbrancieq.modpackconfigupdater.Backup;
import fr.paulbrancieq.modpackconfigupdater.path.OptionPath;

public class ShortOption extends NumberOption<Short> {
  public ShortOption(OptionPath optionPath, Backup backup, Short value, CollectionOption<?> parent) {
    super(optionPath, backup, value, parent);
  }

  @Override
  public ShortOption deepCopy(CollectionOption<?> parent) {
    return new ShortOption(optionPath, getBackup().orElse(null), value, parent);
  }
}
