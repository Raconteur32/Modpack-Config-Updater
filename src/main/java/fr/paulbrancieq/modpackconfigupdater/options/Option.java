package fr.paulbrancieq.modpackconfigupdater.options;

import fr.paulbrancieq.modpackconfigupdater.Backup;
import fr.paulbrancieq.modpackconfigupdater.exceptions.OptionException;
import fr.paulbrancieq.modpackconfigupdater.options.type.*;
import fr.paulbrancieq.modpackconfigupdater.path.OptionPath;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class Option<T> {
  protected final OptionPath optionPath;
  protected final String optionName;
  protected final Backup backup;
  protected CollectionOption<?> parent;

  protected T value;

  public Option(OptionPath optionPath, Backup backup, T value, CollectionOption<?> parent) {
    this.backup = backup;
    this.parent = parent;
    this.value = value;
    this.optionPath = optionPath;
    if (optionPath.getInFileOptionPathParts().isEmpty()) {
      this.optionName = optionPath.getFilePath();
    } else {
      this.optionName = optionPath.getInFileOptionPathParts().get(optionPath.getInFileOptionPathParts().size() - 1).getBaseString();
    }
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public static Option<?> fromSerializableValue(OptionPath optionPath, Backup backup, Object value, CollectionOption<?> parent) {
    return switch (value.getClass().getSimpleName()) {
      case "Boolean" -> new BooleanOption(optionPath, backup, (Boolean) value, parent);
      case "Integer" -> new NumberOption<>(optionPath, backup, (Integer) value, parent);
      case "Double" -> new NumberOption<>(optionPath, backup, (Double) value, parent);
      case "Float" -> new NumberOption<>(optionPath, backup, (Float) value, parent);
      case "Long" -> new NumberOption<>(optionPath, backup, (Long) value, parent);
      case "Short" -> new NumberOption<>(optionPath, backup, (Short) value, parent);
      case "Byte" -> new NumberOption<>(optionPath, backup, (Byte) value, parent);
      case "String" -> new StringOption(optionPath, backup, (String) value, parent);
      case "List" -> ListOption.fromSerializableList(optionPath, backup, (List) value, parent);
      case "Map" -> MapOption.fromSerializableMap(optionPath, backup, (Map) value, parent);
      default -> throw new IllegalArgumentException("Unsupported type: " + value.getClass().getSimpleName());
    };
  }

  public void save() throws OptionException.FileException.CantWriteFile {
    if (getParent().isPresent()) {
      getParent().get().save();
    }
  }

  public abstract Object serializableValue();

  public Optional<CollectionOption<?>> getParent() {
    return Optional.ofNullable(parent);
  }

  public OptionPath getOptionPath() {
    return optionPath;
  }

  public abstract T getValue();

  @MustBeInvokedByOverriders
  public abstract void merge(Option<?> option) throws OptionException.CantMergeOption;

  @MustBeInvokedByOverriders
  public void remove() {
    getParent().ifPresent(parent -> parent.removeChild(this));
  }

  @MustBeInvokedByOverriders
  public void override(Option<?> newOption) {
    getParent().ifPresent(parent -> parent.overrideChild(this, newOption));
  }

  void backup() {
    backup.add(optionPath);
  }

  public abstract Option<T> deepCopy(CollectionOption<?> parent);
}
