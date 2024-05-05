package fr.paulbrancieq.modpackconfigupdater.remake.option.containers;

import fr.paulbrancieq.modpackconfigupdater.remake.option.OrphanException;
import fr.paulbrancieq.modpackconfigupdater.remake.option.option.Option;
import fr.paulbrancieq.modpackconfigupdater.remake.path.OptionPath;
import fr.paulbrancieq.modpackconfigupdater.remake.path.pathpart.SimpleOptionPathPart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.List;

public abstract class FileContainer extends OptionContainerBasicImpl {
  protected Path aPath;
  protected Path rPath;

  protected FileContainer(Path aPath, Path rPath, @Nullable Object newOptionValue) {
    super(newOptionValue, null, "");
    this.aPath = aPath;
    this.rPath = rPath;
  }

  @Override
  public @NotNull Option<?> getParentOption() throws OrphanException.CantGetParentOptionFromContainer {
    throw new OrphanException.CantGetParentOptionFromContainer();
  }

  @Override
  public @NotNull SimpleOptionPathPart getPathPart() {
    throw new UnsupportedOperationException();
  }

  @Override
  public @NotNull OptionPath getPath() {
    return new OptionPath(List.of());
  }

  public abstract void save();
}
