package fr.paulbrancieq.modpackconfigupdater.options.type;

import fr.paulbrancieq.modpackconfigupdater.Backup;
import fr.paulbrancieq.modpackconfigupdater.path.OptionPath;

public class IntegerOption extends NumberOption<Integer> {
  public IntegerOption(OptionPath optionPath, Backup backup, Integer value, CollectionOption<?> parent) {
    super(optionPath, backup, value, parent);
  }

  @Override
  public IntegerOption deepCopy(CollectionOption<?> parent) {
    return new IntegerOption(optionPath, getBackup().orElse(null), value, parent);
  }
}
