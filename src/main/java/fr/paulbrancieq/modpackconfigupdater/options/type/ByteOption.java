package fr.paulbrancieq.modpackconfigupdater.options.type;

import fr.paulbrancieq.modpackconfigupdater.Backup;
import fr.paulbrancieq.modpackconfigupdater.path.OptionPath;

public class ByteOption extends NumberOption<Byte> {
  public ByteOption(OptionPath optionPath, Backup backup, Byte value, CollectionOption<?> parent) {
    super(optionPath, backup, value, parent);
  }

  @Override
  public ByteOption deepCopy(CollectionOption<?> parent) {
    return new ByteOption(optionPath, getBackup().orElse(null), value, parent);
  }
}
