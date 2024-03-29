package fr.paulbrancieq.modpackconfigupdater.options;

import fr.paulbrancieq.modpackconfigupdater.Backup;
import fr.paulbrancieq.modpackconfigupdater.path.OptionPath;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

public abstract class Option<T> {
  protected final OptionPath optionPath;
  protected final String optionName;
  protected final Backup backup;
  protected final CollectionOption<?> parent;

  protected T value;

  public Option(OptionPath optionPath, T value, Backup backup, CollectionOption<?> parent) {
    this.backup = backup;
    this.parent = parent;
    this.value = value;
    this.optionPath = optionPath;
    if (optionPath.getInFileOptionPathParts().isEmpty()) {
      this.optionName = optionPath.getFilePath();
    } else {
      this.optionName = optionPath.getInFileOptionPathParts().get(optionPath.getInFileOptionPathParts().size() - 1).getPartName();
    }
  }

  public CollectionOption<?> getParent() {
    return parent;
  }

  public abstract T getValue();

  @MustBeInvokedByOverriders
  public void merge(Option<?> option) {
    backup();
  }

  @MustBeInvokedByOverriders
  public void remove() {
    backup();
    getParent().removeChild(this);
  }

  @MustBeInvokedByOverriders
  public void override(Option<?> option) {
    backup();
    getParent().overrideChild(this, option);
  }

  void backup() {
    backup.add(optionPath);
  }

  public abstract Option<T> deepCopy(CollectionOption<?> parent);
}
