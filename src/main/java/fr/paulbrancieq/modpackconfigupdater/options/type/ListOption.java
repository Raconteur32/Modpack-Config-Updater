package fr.paulbrancieq.modpackconfigupdater.options.type;

import fr.paulbrancieq.modpackconfigupdater.Backup;
import fr.paulbrancieq.modpackconfigupdater.exceptions.OptionException;
import fr.paulbrancieq.modpackconfigupdater.options.Option;
import fr.paulbrancieq.modpackconfigupdater.path.OptionPath;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

import java.util.ArrayList;
import java.util.List;

public class ListOption extends CollectionOption<List<Option<?>>> {
  public ListOption(OptionPath optionPath, Backup backup, List<Option<?>> value, CollectionOption<?> parent) {
    super(optionPath, backup, new ArrayList<>(value), parent);
    List<Option<?>> newValue = new ArrayList<>();
    for (Option<?> option : value) {
      newValue.add(option.deepCopy(this));
    }
    this.value = newValue;
  }

  public static ListOption fromSerializableList(OptionPath optionPath, Backup backup, List<Object> list, CollectionOption<?> parent) {
    return new ListOption(optionPath, backup, serializableListToOptionList(optionPath, backup, list), parent);
  }

  protected static List<Option<?>> serializableListToOptionList(OptionPath optionPath, Backup backup, List<Object> list) {
    List<Option<?>> value = new ArrayList<>();
    for (int i = 0; i < list.size(); i++) {
      value.add(Option.fromSerializableValue(optionPath.getSubPath(i), backup, list.get(i), null));
    }
    return value;
  }

  @Override
  public List<Object> serializableValue() {
    List<Object> list = new ArrayList<>();
    for (Option<?> option : value) {
      list.add(option.serializableValue());
    }
    return list;
  }

  @SuppressWarnings("Duplicates")
  @Override
  public List<Option<?>> getSubOptions(List<OptionPath.InFileOptionPathPart> parts) {
    if (parts.isEmpty()) {
      return List.of(this);
    }
    List<Option<?>> directSubOptions;
    List<Option<?>> subOptions = new ArrayList<>();
    OptionPath.InFileOptionPathPart part = parts.get(0);
    List<OptionPath.InFileOptionPathPart> subParts = parts.subList(1, parts.size());
    if (part.isFilter()) {
      directSubOptions = part.getFilter().filter(this);
    } else {
      try {
        int index = Integer.parseInt(part.getBaseString());
        directSubOptions = List.of(value.get(index));
      } catch (Exception e) {
        directSubOptions = List.of();
      }
    }
    for (Option<?> directSubOption : directSubOptions) {
      if (directSubOption instanceof CollectionOption<?> collectionOption) {
        subOptions.addAll(collectionOption.getSubOptions(subParts));
      } else if (subParts.isEmpty()) {
        subOptions.add(directSubOption);
      }
    }
    return subOptions;
  }

  @Override
  public void removeChild(Option<?> option) {
    value.remove(option);
  }

  @Override
  public void overrideChild(Option<?> option, Option<?> newOption) {
    value.set(value.indexOf(option), newOption);
  }

  @Override
  public void merge(Option<?> option) throws OptionException.CantMergeOption {
    throw new OptionException.CantMergeOption(this, option, "ListOptions cannot be merged");
  }

  @Override
  @MustBeInvokedByOverriders
  public Boolean isSameAs(Option<?> option) {
    if (!super.isSameAs(option)) {
      return false;
    }
    List<Option<?>> otherValue = ((ListOption) option).value;
    if (value.size() != otherValue.size()) {
      return false;
    }
    for (int i = 0; i < value.size(); i++) {
      if (!value.get(i).isSameAs(otherValue.get(i))) {
        return false;
      }
    }
    return true;
  }

  @Override
  public ListOption deepCopy(CollectionOption<?> parent) {
    return new ListOption(optionPath, getBackup().orElse(null), value, parent);
  }
}
