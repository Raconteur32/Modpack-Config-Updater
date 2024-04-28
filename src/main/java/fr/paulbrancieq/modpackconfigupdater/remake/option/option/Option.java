package fr.paulbrancieq.modpackconfigupdater.remake.option.option;

import fr.paulbrancieq.modpackconfigupdater.remake.option.CollectionOptionChildOperationException;
import fr.paulbrancieq.modpackconfigupdater.remake.option.containers.OptionContainer;
import fr.paulbrancieq.modpackconfigupdater.remake.option.option.noncollection.NullOption;
import fr.paulbrancieq.modpackconfigupdater.remake.option.option.noncollection.StringOption;
import fr.paulbrancieq.modpackconfigupdater.remake.path.OptionPathPart;
import fr.paulbrancieq.modpackconfigupdater.remake.path.UniqueOptionPathPart;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Option<ValueT> {
  @Nullable ValueT getRawValue();

  boolean equals(@NotNull Option<?> otherOpt);

  Option<ValueT> deepCopy(@NotNull OptionContainer newContainer);

  OptionContainer getContainer();

  List<OptionContainer> getChildContainerListFromPathPart(@NotNull OptionPathPart pathPart)
          throws CollectionOptionChildOperationException;

  OptionContainer getChildContainerFromUniquePathPart(@NotNull UniqueOptionPathPart pathPart)
          throws CollectionOptionChildOperationException;

  void defaultOption(@NotNull Option<?> refOption) throws CollectionOptionChildOperationException;

  OptionContainer removeChildOption(@NotNull OptionPathPart pathPart) throws CollectionOptionChildOperationException;

  @FunctionalInterface
  static interface OptionFactory {
    Option<?> create(@NotNull Object value, @NotNull OptionContainer container);
  }

  static Option<?> fromSerializableValue(@Nullable Object value, @NotNull OptionContainer container) {
    if (value == null) {
      return new NullOption(container);
    }
    Map<Class<?>, OptionFactory> factories = new HashMap<>() {{
      // TODO: Implement the rest of the types
      put(Boolean.class, (v, c) -> {
        throw new NotImplementedException("BooleanOption");
      });
      put(Integer.class, (v, c) -> {
        throw new NotImplementedException("IntegerOption");
      });
      put(Double.class, (v, c) -> {
        throw new NotImplementedException("DoubleOption");
      });
      put(Float.class, (v, c) -> {
        throw new NotImplementedException("FloatOption");
      });
      put(Long.class, (v, c) -> {
        throw new NotImplementedException("LongOption");
      });
      put(Short.class, (v, c) -> {
        throw new NotImplementedException("ShortOption");
      });
      put(Byte.class, (v, c) -> {
        throw new NotImplementedException("ByteOption");
      });
      put(String.class, (v, c) -> new StringOption((String) v, c));
      put(List.class, (v, c) -> {
        throw new NotImplementedException("ListOption");
      });
      put(Map.class, (v, c) -> {
        throw new NotImplementedException("MapOption");
      });
    }};
    for (Map.Entry<Class<?>, OptionFactory> entry : factories.entrySet()) {
      if (entry.getKey().isInstance(value)) {
        return entry.getValue().create(value, container);
      }
    }
    throw new IllegalArgumentException("Unsupported type: " + value.getClass().getSimpleName());
  }
}
