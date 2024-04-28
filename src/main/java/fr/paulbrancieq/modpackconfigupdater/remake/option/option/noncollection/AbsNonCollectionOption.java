package fr.paulbrancieq.modpackconfigupdater.remake.option.option.noncollection;

import fr.paulbrancieq.modpackconfigupdater.remake.option.CollectionOptionChildOperationException;
import fr.paulbrancieq.modpackconfigupdater.remake.option.containers.OptionContainer;
import fr.paulbrancieq.modpackconfigupdater.remake.option.option.Option;
import fr.paulbrancieq.modpackconfigupdater.remake.option.option.OptionBasicImpl;
import fr.paulbrancieq.modpackconfigupdater.remake.path.OptionPathPart;
import fr.paulbrancieq.modpackconfigupdater.remake.path.UniqueOptionPathPart;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class AbsNonCollectionOption<T> extends OptionBasicImpl<T> {
  public AbsNonCollectionOption(@NotNull T value, @NotNull OptionContainer container) {
    super(value, container);
  }

  @Override
  public List<OptionContainer> getChildContainerListFromPathPart(@NotNull OptionPathPart pathPart)
          throws CollectionOptionChildOperationException {
    throw new CollectionOptionChildOperationException.NotACollectionOption();
  }

  @Override
  public OptionContainer getChildContainerFromUniquePathPart(@NotNull UniqueOptionPathPart pathPart)
          throws CollectionOptionChildOperationException.NotACollectionOption {
    throw new CollectionOptionChildOperationException.NotACollectionOption();
  }


  @Override
  public void defaultOption(@NotNull Option<?> refOption)
          throws CollectionOptionChildOperationException.NotACollectionOption {
    throw new CollectionOptionChildOperationException.NotACollectionOption();
  }

  @Override
  public void defaultChildOption(@NotNull Option<?> refOption)
          throws CollectionOptionChildOperationException.NotACollectionOption {
    throw new CollectionOptionChildOperationException.NotACollectionOption();
  }

  @Override
  public void removeChildOption(@NotNull OptionPathPart pathPart)
          throws CollectionOptionChildOperationException {
    throw new CollectionOptionChildOperationException.NotACollectionOption();
  }
}
