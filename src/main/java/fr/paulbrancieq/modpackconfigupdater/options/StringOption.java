package fr.paulbrancieq.modpackconfigupdater.options;

import fr.paulbrancieq.modpackconfigupdater.Backup;
import fr.paulbrancieq.modpackconfigupdater.path.OptionPath;

public class StringOption extends Option<String> {
  String value;
  public StringOption(OptionPath optionPath, Backup backup, String value, CollectionOption<?> parent) {
    super(optionPath, value, backup, parent);
  }

  @Override
  public String getValue() {
    return value;
  }

  @Override
  public StringOption deepCopy(CollectionOption<?> parent) {
    return new StringOption(optionPath, backup, value, parent);
  }
}
