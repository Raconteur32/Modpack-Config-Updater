package fr.paulbrancieq.modpackconfigupdater.remake.option.option.collection;

import fr.paulbrancieq.modpackconfigupdater.remake.option.CollectionOptionChildOperationException;
import fr.paulbrancieq.modpackconfigupdater.remake.option.containers.OptionContainer;
import fr.paulbrancieq.modpackconfigupdater.remake.option.containers.OptionContainerBasicImpl;
import fr.paulbrancieq.modpackconfigupdater.remake.option.option.Option;
import fr.paulbrancieq.modpackconfigupdater.remake.path.OptionPathPart;
import fr.paulbrancieq.modpackconfigupdater.remake.path.UniqueOptionPathPart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapOption extends CollectionOption<Map<String, OptionContainer>> {
  public MapOption(@NotNull @Unmodifiable Map<String, OptionContainer> value, @NotNull OptionContainer container) {
    super(new HashMap<>(), container);
    for (Map.Entry<String, OptionContainer> entry : value.entrySet()) {
      put(entry.getKey(), entry.getValue().getOption());
    }
  }

  public static MapOption fromSerializable(@NotNull @Unmodifiable Map<String, Object> serializable,
                                           @NotNull OptionContainer container) {
    MapOption mapOption = new MapOption(new HashMap<>(), container);
    for (Map.Entry<String, Object> entry : serializable.entrySet()) {
      mapOption.put(entry.getKey(), entry.getValue());
    }
    return mapOption;
  }

  private void put(@NotNull String key, @NotNull Option<?> option) {
    value.put(key, new OptionContainerBasicImpl(option, this, String.valueOf(key)));
  }

  private void put(@NotNull String key, @Nullable Object newValue) {
    value.put(key, new OptionContainerBasicImpl(newValue, this, String.valueOf(key)));
  }

  @Override
  public @NotNull @Unmodifiable Map<String, OptionContainer> getRawValue() {
    return Collections.unmodifiableMap(value);
  }

  @Override
  public boolean equals(@NotNull Option<?> otherOpt) {
    if (!(otherOpt instanceof MapOption)) {
      return false;
    }
    Map<String, OptionContainer> otherValue = ((MapOption) otherOpt).getRawValue();
    if (value.size() != otherValue.size()) {
      return false;
    }
    for (Map.Entry<String, OptionContainer> entry : value.entrySet()) {
      if (!entry.getValue().getOption().equals(otherValue.get(entry.getKey()).getOption())) {
        return false;
      }
    }
    return true;
  }

  @Override
  public Option<Map<String, OptionContainer>> deepCopy(@NotNull OptionContainer newContainer) {
    return new MapOption(getRawValue(), newContainer);
  }

  @Override
  public List<OptionContainer> getChildContainerListFromPathPart(@NotNull OptionPathPart pathPart) {
    return value.values().stream().filter(optionContainer -> pathPart.match(optionContainer.getOption())).toList();
  }

  @Override
  public OptionContainer getChildContainerFromUniquePathPart(@NotNull UniqueOptionPathPart pathPart)
          throws CollectionOptionChildOperationException {
    if (!value.containsKey(pathPart.getStringPathPart())) {
      throw new CollectionOptionChildOperationException.ChildDoesNotExist();
    }
    return value.get(pathPart.getStringPathPart());
  }

  @Override
  public void defaultOption(@NotNull Option<?> refOption)
          throws CollectionOptionChildOperationException.CannotGetDefaultOptionsFromADifferentOptionType {
    if (!(refOption instanceof MapOption refMapOption)) {
      throw new CollectionOptionChildOperationException.CannotGetDefaultOptionsFromADifferentOptionType(
              refOption.getClass(), MapOption.class);
    }
    for (Map.Entry<String, OptionContainer> entry : refMapOption.getRawValue().entrySet()) {
      defaultChildOption(entry.getValue().getOption());
    }
  }

  @Override
  public void defaultChildOption(@NotNull Option<?> refOption) {
    if (value.containsKey(refOption.getContainer().getPathPart().getStringPathPart())) {
      value.put(refOption.getContainer().getPathPart().getStringPathPart(),
              new OptionContainerBasicImpl(refOption, this,
                      refOption.getContainer().getPathPart().getStringPathPart()));
    }
  }

  @Override
  public void removeChildOption(@NotNull OptionPathPart pathPart) throws CollectionOptionChildOperationException {
    List<Map.Entry<String, OptionContainer>> toRemove =
            value.entrySet().stream().filter(entry -> pathPart.match(entry.getValue().getOption())).toList();
    for (Map.Entry<String, OptionContainer> entry : toRemove) {
      value.remove(entry.getKey());
    }
  }
}
