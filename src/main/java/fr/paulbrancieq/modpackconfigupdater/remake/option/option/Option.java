package fr.paulbrancieq.modpackconfigupdater.remake.option.option;

import fr.paulbrancieq.modpackconfigupdater.remake.option.CollectionOptionChildOperationException;
import fr.paulbrancieq.modpackconfigupdater.remake.option.containers.OptionContainer;
import fr.paulbrancieq.modpackconfigupdater.remake.option.option.collection.ListOption;
import fr.paulbrancieq.modpackconfigupdater.remake.option.option.collection.MapOption;
import fr.paulbrancieq.modpackconfigupdater.remake.option.option.noncollection.NullOption;
import fr.paulbrancieq.modpackconfigupdater.remake.option.option.noncollection.StringOption;
import fr.paulbrancieq.modpackconfigupdater.remake.path.InFileOptionPath;
import fr.paulbrancieq.modpackconfigupdater.remake.path.OptionPathPart;
import fr.paulbrancieq.modpackconfigupdater.remake.path.UniqueOptionPathPart;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import oshi.util.tuples.Pair;

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

  void defaultChildOption(@NotNull Option<?> refOption) throws CollectionOptionChildOperationException;

  void removeChildOption(@NotNull OptionPathPart pathPart) throws CollectionOptionChildOperationException;

  @NotNull List<Option<?>> getFromPath(@NotNull OptionContextVisitor visitor, @NotNull InFileOptionPath path);

  @FunctionalInterface
  interface OptionFactory<T> {
    Option<?> create(@NotNull T value, @NotNull OptionContainer container);
  }

  class OptionFactoryMap {
    private final Map<Class<?>, OptionFactory<?>> map;

    public OptionFactoryMap() {
      this.map = new HashMap<>();
    }

    public <T> void put(Class<T> key, OptionFactory<T> value) {
      map.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> OptionFactory<T> get(Class<T> key) {
      return (OptionFactory<T>) map.get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> List<Pair<Class<T>, OptionFactory<T>>> entrySet() {
      List<Pair<Class<T>, OptionFactory<T>>> pairs = new java.util.ArrayList<>(List.of());
      for (Map.Entry<Class<?>, OptionFactory<?>> entry : map.entrySet()) {
        Class<T> key = (Class<T>) entry.getKey();
        OptionFactory<T> value = (OptionFactory<T>) entry.getValue();
        pairs.add(new Pair<>(key, value));
      }
      return pairs;
    }
  }

  static Option<?> fromSerializableValue(@Nullable Object value, @NotNull OptionContainer container) {
    if (value == null) {
      return new NullOption(container);
    }
    OptionFactoryMap factories = new OptionFactoryMap() {{
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
      put(String.class, StringOption::new);
      put(List.class, ListOption::fromSerializable);
      put(Map.class, MapOption::fromSerializable);
    }};
    for (Pair<Class<Object>, OptionFactory<Object>> entry : factories.entrySet()) {
      if (entry.getA().isInstance(value)) {
        return entry.getB().create(value, container);
      }
    }
    throw new IllegalArgumentException("Unsupported type: " + value.getClass().getSimpleName());
  }
}
