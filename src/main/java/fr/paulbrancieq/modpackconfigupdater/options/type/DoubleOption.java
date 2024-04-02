package fr.paulbrancieq.modpackconfigupdater.options.type;

import fr.paulbrancieq.modpackconfigupdater.Backup;
import fr.paulbrancieq.modpackconfigupdater.path.OptionPath;

public class DoubleOption extends NumberOption<Double> {
  public DoubleOption(OptionPath optionPath, Backup backup, Double value, CollectionOption<?> parent) {
    super(optionPath, backup, value, parent);
  }

  @Override
  public DoubleOption deepCopy(CollectionOption<?> parent) {
    return new DoubleOption(optionPath, getBackup().orElse(null), value, parent);
  }
}
