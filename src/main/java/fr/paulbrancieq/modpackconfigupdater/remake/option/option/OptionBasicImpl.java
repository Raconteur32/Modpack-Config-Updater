package fr.paulbrancieq.modpackconfigupdater.remake.option.option;

import fr.paulbrancieq.modpackconfigupdater.remake.option.containers.OptionContainer;
import fr.paulbrancieq.modpackconfigupdater.remake.path.InFileOptionPath;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class OptionBasicImpl<T> implements Option<T> {
  protected final @NotNull T value;
  private final @NotNull OptionContainer container;

  public OptionBasicImpl(@NotNull T value, @NotNull OptionContainer container) {
    this.value = value;
    this.container = container;
  }

  @Override
  public @NotNull OptionContainer getContainer() {
    return container;
  }

  @Override
  public @NotNull List<Option<?>> getFromPath(@NotNull OptionContextVisitor visitor, @NotNull InFileOptionPath path) {
    return visitor.visitOption(this, path);
  }
}
