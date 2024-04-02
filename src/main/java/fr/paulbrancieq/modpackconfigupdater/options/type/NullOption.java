package fr.paulbrancieq.modpackconfigupdater.options.type;

import fr.paulbrancieq.modpackconfigupdater.Backup;
import fr.paulbrancieq.modpackconfigupdater.exceptions.OptionException;
import fr.paulbrancieq.modpackconfigupdater.options.Option;
import fr.paulbrancieq.modpackconfigupdater.path.OptionPath;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

public class NullOption extends Option<Object> {

  public NullOption(OptionPath optionPath, Backup backup, CollectionOption<?> parent) {
    super(optionPath, backup, null, parent);
  }

  @Override
  public Object serializableValue() {
    return getValue();
  }

  @Override
  public void merge(Option<?> option) throws OptionException.CantMergeOption {
    throw new OptionException.CantMergeOption(this, option, "NullOptions cannot be merged");
  }

  @Override
  @MustBeInvokedByOverriders
  public Boolean isSameAs(Option<?> option) {
    return super.isSameAs(option) && value.equals(((NullOption) option).value);
  }

  @Override
  public Option<Object> deepCopy(CollectionOption<?> parent) {
    return new NullOption(optionPath, getBackup().orElse(null), parent);
  }
}
