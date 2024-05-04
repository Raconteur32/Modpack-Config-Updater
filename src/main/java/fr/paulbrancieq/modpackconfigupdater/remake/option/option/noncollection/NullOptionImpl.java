package fr.paulbrancieq.modpackconfigupdater.remake.option.option.noncollection;

import fr.paulbrancieq.modpackconfigupdater.remake.option.CollectionOptionChildOperationException;
import fr.paulbrancieq.modpackconfigupdater.remake.option.containers.OptionContainer;
import fr.paulbrancieq.modpackconfigupdater.remake.option.option.Option;
import fr.paulbrancieq.modpackconfigupdater.remake.option.option.OptionContextVisitor;
import fr.paulbrancieq.modpackconfigupdater.remake.path.OptionPath;
import fr.paulbrancieq.modpackconfigupdater.remake.path.pathpart.OptionPathPart;
import fr.paulbrancieq.modpackconfigupdater.remake.path.pathpart.SimpleOptionPathPart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record NullOptionImpl(OptionContainer container) implements Option<Object> {
  public NullOptionImpl(@NotNull OptionContainer container) {
    this.container = container;
  }

  @Override
  public @Nullable Object getRawValue() {
    return null;
  }

  @Override
  public boolean equals(@NotNull Option<?> otherOpt) {
    return otherOpt.getRawValue() == null;
  }

  @Override
  public Option<Object> deepCopy(@NotNull OptionContainer newContainer) {
    return new NullOptionImpl(newContainer);
  }

  @Override
  public OptionContainer getContainer() {
    return container;
  }

  @Override
  public List<OptionContainer> getChildContainerListFromPathPart(@NotNull OptionPathPart pathPart)
          throws CollectionOptionChildOperationException {
    throw new CollectionOptionChildOperationException.NotACollectionOption();
  }

  @Override
  public OptionContainer getChildContainerFromUniquePathPart(@NotNull SimpleOptionPathPart pathPart)
          throws CollectionOptionChildOperationException {
    throw new CollectionOptionChildOperationException.NotACollectionOption();
  }

  @Override
  public void defaultOption(@NotNull Option<?> refOption) throws CollectionOptionChildOperationException {
    throw new CollectionOptionChildOperationException.NotACollectionOption();
  }

  @Override
  public void defaultChildOption(@NotNull Option<?> refOption) throws CollectionOptionChildOperationException {
    throw new CollectionOptionChildOperationException.NotACollectionOption();
  }

  @Override
  public void removeChildOption(@NotNull OptionPathPart pathPart)
          throws CollectionOptionChildOperationException {
    throw new CollectionOptionChildOperationException.NotACollectionOption();
  }

  @Override
  public @NotNull List<Option<?>> getFromPath(@NotNull OptionContextVisitor visitor,
                                              @NotNull OptionPath path) {
    return visitor.visitOption(this, path);
  }
}
