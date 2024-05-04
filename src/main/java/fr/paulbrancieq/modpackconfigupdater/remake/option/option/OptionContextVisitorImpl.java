package fr.paulbrancieq.modpackconfigupdater.remake.option.option;

import fr.paulbrancieq.modpackconfigupdater.remake.option.CollectionOptionChildOperationException;
import fr.paulbrancieq.modpackconfigupdater.remake.option.containers.OptionContainer;
import fr.paulbrancieq.modpackconfigupdater.remake.path.OptionPath;
import fr.paulbrancieq.modpackconfigupdater.remake.path.PathException;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class OptionContextVisitorImpl implements OptionContextVisitor {
  @Override
  public @NotNull List<Option<?>> visitContext(@NotNull OptionContext optionContext) {
    throw new NotImplementedException("Not implemented");
  }

  @Override
  public @NotNull List<Option<?>> visitOption(@NotNull Option<?> option, @NotNull OptionPath path) {
    if (path.isRoot()) {
      return List.of(option);
    }
    List<Option<?>> result = new ArrayList<>();
    List<? extends Option<?>> matchingDirectChildren;
    try {
      matchingDirectChildren =
              option.getChildContainerListFromPathPart(path.getFirstPart()).stream().map(OptionContainer::getOption)
                      .toList();
    } catch (CollectionOptionChildOperationException e) {
      return result;
    } catch (PathException.ReachedPathRoot e) {
      throw new RuntimeException(e);
    }
    for (Option<?> childOption : matchingDirectChildren) {
      result.addAll(childOption.getFromPath(this, path.getConsumedPath()));
    }
    return result;
  }
}
