package fr.paulbrancieq.modpackconfigupdater.options.type;

import fr.paulbrancieq.modpackconfigupdater.Backup;
import fr.paulbrancieq.modpackconfigupdater.path.OptionPath;

public class FloatOption extends NumberOption<Float> {
  public FloatOption(OptionPath optionPath, Backup backup, Float value, CollectionOption<?> parent) {
    super(optionPath, backup, value, parent);
  }

  @Override
  public FloatOption deepCopy(CollectionOption<?> parent) {
    return new FloatOption(optionPath, getBackup().orElse(null), value, parent);
  }
}
