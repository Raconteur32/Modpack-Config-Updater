package fr.paulbrancieq.modpackconfigupdater.options.type;

import fr.paulbrancieq.modpackconfigupdater.Backup;
import fr.paulbrancieq.modpackconfigupdater.options.Option;
import fr.paulbrancieq.modpackconfigupdater.path.OptionPath;

public abstract class CollectionOption<T> extends Option<T> {
  public CollectionOption(OptionPath optionPath, Backup backup, T value, CollectionOption<?> parent) {
    super(optionPath, backup, value, parent);
  }

  public abstract void removeChild(Option<?> option);

  public abstract void overrideChild(Option<?> option, Option<?> newOption);
}
