package fr.paulbrancieq.modpackconfigupdater.remake.option.containers;

import fr.paulbrancieq.modpackconfigupdater.remake.option.option.Option;
import fr.paulbrancieq.modpackconfigupdater.remake.option.OrphanException;
import fr.paulbrancieq.modpackconfigupdater.remake.path.InFileOptionPath;
import fr.paulbrancieq.modpackconfigupdater.remake.path.UniqueOptionPathPart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OptionContainerBasicImpl implements OptionContainer {
  private final @NotNull Option<?> option;

  private @Nullable Option<?> parentOption;

  private final @NotNull UniqueOptionPathPart pathPart;

  public OptionContainerBasicImpl(@NotNull Option<?> option, @NotNull Option<?> parentOption,
                                  @NotNull String pathPart) {
    this.option = option.deepCopy(this);
    this.parentOption = parentOption;
    this.pathPart = new UniqueOptionPathPart(pathPart);
  }

  public OptionContainerBasicImpl(@Nullable Object newOptionValue, @Nullable Option<?> parentOption,
                                  @NotNull String pathPart) {
    this.option = Option.fromSerializableValue(newOptionValue, this);
    this.parentOption = parentOption;
    this.pathPart = new UniqueOptionPathPart(pathPart);
  }

  @Override
  public @NotNull Option<?> getOption() {
    return option;
  }

  @Override
  public @NotNull Option<?> getParentOption() throws OrphanException.CantGetParentOptionFromContainer {
    if (parentOption == null) {
      throw new OrphanException.CantGetParentOptionFromContainer();
    }
    return parentOption;
  }

  @Override
  public void makeOrphan() {
    parentOption = null;
  }

  @Override
  public @NotNull UniqueOptionPathPart getPathPart() {
    return pathPart;
  }

  @Override
  public @NotNull InFileOptionPath getPath() throws OrphanException.CantGetParentOptionFromContainer {
    return getParentOption().getContainer().getPath().resolve(pathPart);
  }
}
