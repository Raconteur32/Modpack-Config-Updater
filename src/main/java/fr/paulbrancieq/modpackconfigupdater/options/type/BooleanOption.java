package fr.paulbrancieq.modpackconfigupdater.options.type;

import fr.paulbrancieq.modpackconfigupdater.Backup;
import fr.paulbrancieq.modpackconfigupdater.exceptions.OptionException;
import fr.paulbrancieq.modpackconfigupdater.options.Option;
import fr.paulbrancieq.modpackconfigupdater.path.OptionPath;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

public class BooleanOption extends Option<Boolean> {
  public BooleanOption(OptionPath optionPath, Backup backup, Boolean value, CollectionOption<?> parent) {
    super(optionPath, backup, value, parent);
  }

  @Override
  public Object serializableValue() {
    return getValue();
  }

  @Override
  public void merge(Option<?> option) throws OptionException.CantMergeOption {
    throw new OptionException.CantMergeOption(this, option, "BooleanOptions cannot be merged");
  }

  @Override
  @MustBeInvokedByOverriders
  public Boolean isSameAs(Option<?> option) {
    return super.isSameAs(option) && value.equals(((BooleanOption) option).value);
  }

  @Override
  public BooleanOption deepCopy(CollectionOption<?> parent) {
    return new BooleanOption(optionPath, getBackup().orElse(null), value, parent);
  }
}
