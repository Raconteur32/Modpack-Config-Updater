package fr.paulbrancieq.modpackconfigupdater.options.type;

import fr.paulbrancieq.modpackconfigupdater.Backup;
import fr.paulbrancieq.modpackconfigupdater.exceptions.OptionException;
import fr.paulbrancieq.modpackconfigupdater.options.Option;
import fr.paulbrancieq.modpackconfigupdater.path.OptionPath;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

public class StringOption extends Option<String> {
  public StringOption(OptionPath optionPath, Backup backup, String value, CollectionOption<?> parent) {
    super(optionPath, backup, value, parent);
  }

  @Override
  public Object serializableValue() {
    return getValue();
  }

  @Override
  public void merge(Option<?> option) throws OptionException.CantMergeOption {
    throw new OptionException.CantMergeOption(this, option, "StringOptions cannot be merged");
  }

  @Override
  @MustBeInvokedByOverriders
  public Boolean isSameAs(Option<?> option) {
    return super.isSameAs(option) && value.equals(((StringOption) option).value);
  }

  @Override
  public StringOption deepCopy(CollectionOption<?> parent) {
    return new StringOption(optionPath, getBackup().orElse(null), value, parent);
  }
}
