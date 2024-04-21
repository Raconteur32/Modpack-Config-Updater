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
  private final Backup backup;
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
      this.optionName =
          optionPath.getInFileOptionPathParts().get(optionPath.getInFileOptionPathParts().size() - 1).getBaseString();
    }
  }

  public <O extends Option<?>> O as(Class<O> optionClass) throws OptionException.OptionCantBeCasted {
    if (optionClass.isInstance(this)) {
      return optionClass.cast(this);
    } else {
      throw new OptionException.OptionCantBeCasted(this, optionClass);
    }
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public static Option<?> fromSerializableValue(OptionPath optionPath, Backup backup, Object value,
                                                CollectionOption<?> parent) {
    if (value instanceof Boolean) {
      return new BooleanOption(optionPath, backup, (Boolean) value, parent);
    } else if (value instanceof Integer) {
      return new IntegerOption(optionPath, backup, (Integer) value, parent);
    } else if (value instanceof Double) {
      return new DoubleOption(optionPath, backup, (Double) value, parent);
    } else if (value instanceof Float) {
      return new FloatOption(optionPath, backup, (Float) value, parent);
    } else if (value instanceof Long) {
      return new LongOption(optionPath, backup, (Long) value, parent);
    } else if (value instanceof Short) {
      return new ShortOption(optionPath, backup, (Short) value, parent);
    } else if (value instanceof Byte) {
      return new ByteOption(optionPath, backup, (Byte) value, parent);
    } else if (value instanceof String) {
      return new StringOption(optionPath, backup, (String) value, parent);
    } else if (value instanceof List) {
      return ListOption.fromSerializableList(optionPath, backup, (List) value, parent);
    } else if (value instanceof Map) {
      return MapOption.fromSerializableMap(optionPath, backup, (Map) value, parent);
    } else if (value == null) {
      return new NullOption(optionPath, backup, parent);
    } else {
      throw new IllegalArgumentException("Unsupported type: " + value.getClass().getSimpleName());
    }
  }

  public void save() throws OptionException.FileException.CantWriteFile, OptionException.FileException.CantDeleteFile,
      OptionException.CantSaveOption {
    if (getParent().isPresent()) {
      getParent().get().save();
    }
  }

  public abstract Object serializableValue();

  public Optional<CollectionOption<?>> getParent() {
    return Optional.ofNullable(parent);
  }

  public Optional<Backup> getBackup() {
    return Optional.ofNullable(backup);
  }

  public OptionPath getOptionPath() {
    return optionPath;
  }

  public T getValue() {
    return value;
  }

  public final List<Option<?>> getSubOptions(OptionPath optionPath) throws OptionException.OptionDoesNotHaveChildren {
    return getSubOptions(optionPath.getInFileOptionPathParts());
  }

  public List<Option<?>> getSubOptions(List<OptionPath.InFileOptionPathPart> parts) {
    return List.of();
  }

  public abstract void merge(Option<?> option) throws OptionException.CantMergeOption;

  @MustBeInvokedByOverriders
  public void remove() {
    getParent().ifPresent(parent -> parent.removeChild(this));
  }

  @MustBeInvokedByOverriders
  public void override(Option<?> newOption) {
    getParent().ifPresent(parent ->parent.overrideChild(this, newOption));
  }
  
  public boolean isUntargetable() {
    return (getParent().isPresent() && getParent().get().isUntargetable()) ||
        (getParent().isPresent() && getParent().get() instanceof ListOption);
  }

  protected void backup() throws OptionException.CantSaveOption {
    if (getBackup().isEmpty()) {
      throw new OptionException.CantSaveOption(this, "No backup provided");
    }
    backup.add(optionPath);
  }

  @MustBeInvokedByOverriders
  public Boolean isSameAs(Option<?> option) {
    return option.getClass().equals(this.getClass());
  }

  public abstract Option<T> deepCopy(CollectionOption<?> parent);
}
