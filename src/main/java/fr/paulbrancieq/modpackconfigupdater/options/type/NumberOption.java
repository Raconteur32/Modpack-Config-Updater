package fr.paulbrancieq.modpackconfigupdater.options.type;

import fr.paulbrancieq.modpackconfigupdater.Backup;
import fr.paulbrancieq.modpackconfigupdater.exceptions.OptionException;
import fr.paulbrancieq.modpackconfigupdater.options.Option;
import fr.paulbrancieq.modpackconfigupdater.path.OptionPath;

public class NumberOption<T extends Number> extends Option<T> {
  public NumberOption(OptionPath optionPath, Backup backup, T value, CollectionOption<?> parent) {
    super(optionPath, backup, value, parent);
  }

  @Override
  public Object serializableValue() {
    return getValue();
  }


  @Override
  public T getValue() {
    return value;
  }

  @Override
  public void merge(Option<?> option) throws OptionException.CantMergeOption {
    throw new OptionException.CantMergeOption(this, option, "NumberOptions cannot be merged");
  }

  @Override
  public NumberOption<T> deepCopy(CollectionOption<?> parent) {
    return new NumberOption<>(optionPath, getBackup().orElse(null), value, parent);
  }
}
