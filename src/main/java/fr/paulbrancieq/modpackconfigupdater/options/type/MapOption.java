package fr.paulbrancieq.modpackconfigupdater.options.type;

import fr.paulbrancieq.modpackconfigupdater.Backup;
import fr.paulbrancieq.modpackconfigupdater.exceptions.OptionException;
import fr.paulbrancieq.modpackconfigupdater.options.Option;
import fr.paulbrancieq.modpackconfigupdater.path.OptionPath;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MapOption extends CollectionOption<Map<Object, Option<?>>> {
  public MapOption(OptionPath optionPath, Backup backup, Map<Object, Option<?>> value, CollectionOption<?> parent) {
    super(optionPath, backup, new HashMap<>(value), parent);
    Map<Object, Option<?>> newValue = new HashMap<>();
    for (Map.Entry<Object, Option<?>> entry : value.entrySet()) {
      newValue.put(entry.getKey(), entry.getValue().deepCopy(this));
    }
    this.value = newValue;
  }

  public static MapOption fromSerializableMap(OptionPath optionPath, Backup backup, Map<Object, Object> map, CollectionOption<?> parent) {
    return new MapOption(optionPath, backup, serializableMapToOptionMap(optionPath, backup, map), parent);
  }

  protected static Map<Object, Option<?>> serializableMapToOptionMap(OptionPath optionPath, Backup backup, Map<Object, Object> map) {
    Map<Object, Option<?>> value = new HashMap<>();
    for (Map.Entry<Object, Object> entry : map.entrySet()) {
      value.put(entry.getKey(), Option.fromSerializableValue(optionPath.getSubPath(entry.getKey()), backup, entry.getValue(), null));
    }
    return value;
  }

  @Override
  public Map<Object, Object> serializableValue() {
    Map<Object, Object> map = new HashMap<>();
    for (Map.Entry<Object, Option<?>> entry : value.entrySet()) {
      map.put(entry.getKey(), entry.getValue().serializableValue());
    }
    return map;
  }

  @Override
  public void removeChild(Option<?> option) {
    Optional<?> optional = value.entrySet().stream().filter(entry -> entry.getValue().equals(option)).findFirst();
    optional.ifPresent(o -> value.remove(o));
  }

  @Override
  public void overrideChild(Option<?> option, Option<?> newOption) {
    Optional<?> optional = value.entrySet().stream().filter(entry -> entry.getValue().equals(option)).findFirst();
    optional.ifPresent(o -> value.put(o, newOption.deepCopy(this)));
  }

  @Override
  public Map<Object, Option<?>> getValue() {
    return null;
  }

  @Override
  public void merge(Option<?> option) throws OptionException.CantMergeOption {
    if (!(option instanceof MapOption mapOption)) {
      throw new OptionException.CantMergeOption(this, option, "Can't merge a MapOption with a non-MapOption");
    }
    for (Map.Entry<?, Option<?>> entry : mapOption.getValue().entrySet()) {
      value.put(entry.getKey(), entry.getValue().deepCopy(this));
    }
  }

  @Override
  public MapOption deepCopy(CollectionOption<?> parent) {
    return new MapOption(optionPath, getBackup().orElse(null), value, parent);
  }
}
