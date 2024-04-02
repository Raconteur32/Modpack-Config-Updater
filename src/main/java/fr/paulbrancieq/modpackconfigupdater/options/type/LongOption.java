package fr.paulbrancieq.modpackconfigupdater.options.type;

import fr.paulbrancieq.modpackconfigupdater.Backup;
import fr.paulbrancieq.modpackconfigupdater.path.OptionPath;

public class LongOption extends NumberOption<Long> {
  public LongOption(OptionPath optionPath, Backup backup, Long value, CollectionOption<?> parent) {
    super(optionPath, backup, value, parent);
  }

  @Override
  public LongOption deepCopy(CollectionOption<?> parent) {
    return new LongOption(optionPath, getBackup().orElse(null), value, parent);
  }
}
