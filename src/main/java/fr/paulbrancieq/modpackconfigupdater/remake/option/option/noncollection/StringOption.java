package fr.paulbrancieq.modpackconfigupdater.remake.option.option.noncollection;

import fr.paulbrancieq.modpackconfigupdater.remake.option.containers.OptionContainer;
import fr.paulbrancieq.modpackconfigupdater.remake.option.option.Option;
import org.jetbrains.annotations.NotNull;

public class StringOption extends AbsImmutableValueOption<String> {
  public StringOption(@NotNull String value, @NotNull OptionContainer container) {
    super(value, container);
  }

  @Override
  public Option<String> deepCopy(@NotNull OptionContainer newContainer) {
    return new StringOption(value, newContainer);
  }
}
