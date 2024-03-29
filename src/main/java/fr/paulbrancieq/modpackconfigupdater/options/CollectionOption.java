package fr.paulbrancieq.modpackconfigupdater.options;

import fr.paulbrancieq.modpackconfigupdater.Backup;
import fr.paulbrancieq.modpackconfigupdater.path.OptionPath;
import fr.paulbrancieq.modpackconfigupdater.path.filter.Filter;

import java.util.Collection;
import java.util.List;

public abstract class CollectionOption<T extends Collection<Option<?>>> extends Option<T> {
  public CollectionOption(OptionPath optionPath, Backup backup, T value, CollectionOption<?> parent) {
    super(optionPath, value, backup, parent);
  }

  public abstract void removeChild(Option<?> option);

  public abstract void overrideChild(Option<?> option, Option<?> newOption);

  public List<Option<?>> filter(Filter filter) {
    // TODO
    return null;
  }
}
