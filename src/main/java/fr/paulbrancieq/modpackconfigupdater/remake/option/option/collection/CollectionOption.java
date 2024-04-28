package fr.paulbrancieq.modpackconfigupdater.remake.option.option.collection;

import fr.paulbrancieq.modpackconfigupdater.remake.option.containers.OptionContainer;
import fr.paulbrancieq.modpackconfigupdater.remake.option.option.OptionBasicImpl;
import org.jetbrains.annotations.NotNull;

public abstract class CollectionOption<T> extends OptionBasicImpl<T> {
  public CollectionOption(@NotNull T value, @NotNull OptionContainer container) {
    super(value, container);
  }
}
