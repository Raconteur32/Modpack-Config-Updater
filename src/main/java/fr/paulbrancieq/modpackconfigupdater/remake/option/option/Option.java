package fr.paulbrancieq.modpackconfigupdater.remake.option.option;

import fr.paulbrancieq.modpackconfigupdater.remake.option.CollectionOptionChildOperationException;
import fr.paulbrancieq.modpackconfigupdater.remake.option.containers.OptionContainer;
import fr.paulbrancieq.modpackconfigupdater.remake.option.option.collection.ListOption;
import fr.paulbrancieq.modpackconfigupdater.remake.option.option.collection.MapOption;
import fr.paulbrancieq.modpackconfigupdater.remake.option.option.noncollection.ImmutableValueOption;
import fr.paulbrancieq.modpackconfigupdater.remake.option.option.noncollection.NullOptionImpl;
import fr.paulbrancieq.modpackconfigupdater.remake.path.OptionPath;
import fr.paulbrancieq.modpackconfigupdater.remake.path.pathpart.OptionPathPart;
import fr.paulbrancieq.modpackconfigupdater.remake.path.pathpart.SimpleOptionPathPart;
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

  OptionContainer getChildContainerFromUniquePathPart(@NotNull SimpleOptionPathPart pathPart)
          throws CollectionOptionChildOperationException;

  void defaultOption(@NotNull Option<?> refOption) throws CollectionOptionChildOperationException;

  void defaultChildOption(@NotNull Option<?> refOption) throws CollectionOptionChildOperationException;

  void removeChildOption(@NotNull OptionPathPart pathPart) throws CollectionOptionChildOperationException;

  @NotNull List<Option<?>> getFromPath(@NotNull OptionContextVisitor visitor, @NotNull OptionPath path);

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
      return new NullOptionImpl(container);
    }
    OptionFactoryMap factories = new OptionFactoryMap() {{
      put(Boolean.class, ImmutableValueOption::new);
      put(Integer.class, ImmutableValueOption::new);
      put(Double.class, ImmutableValueOption::new);
      put(Float.class, ImmutableValueOption::new);
      put(Long.class, ImmutableValueOption::new);
      put(Short.class, ImmutableValueOption::new);
      put(Byte.class, ImmutableValueOption::new);
      put(String.class, ImmutableValueOption::new);
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
