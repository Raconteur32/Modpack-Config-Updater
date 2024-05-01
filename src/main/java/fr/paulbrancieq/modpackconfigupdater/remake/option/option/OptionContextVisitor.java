package fr.paulbrancieq.modpackconfigupdater.remake.option.option;

import fr.paulbrancieq.modpackconfigupdater.remake.path.InFileOptionPath;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface OptionContextVisitor {
  @NotNull List<Option<?>> visitContext(@NotNull OptionContext optionContext);

  @NotNull List<Option<?>> visitOption(@NotNull Option<?> option, @NotNull InFileOptionPath path);
}
