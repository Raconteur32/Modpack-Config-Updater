package fr.paulbrancieq.modpackconfigupdater.remake.option.option.noncollection;

import fr.paulbrancieq.modpackconfigupdater.remake.option.containers.OptionContainer;
import fr.paulbrancieq.modpackconfigupdater.remake.option.option.Option;
import org.jetbrains.annotations.NotNull;

public abstract class AbsImmutableValueOption<T> extends AbsNonCollectionOption<T> {
  public AbsImmutableValueOption(@NotNull T value, @NotNull OptionContainer container) {
    super(value, container);
  }

  @Override
  public boolean equals(@NotNull Option<?> otherOpt) {
    return value.equals(otherOpt.getRawValue());
  }

  @Override
  public @NotNull T getRawValue() {
    return value;
  }
}
