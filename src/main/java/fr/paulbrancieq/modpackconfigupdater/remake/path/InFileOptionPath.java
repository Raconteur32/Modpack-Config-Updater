package fr.paulbrancieq.modpackconfigupdater.remake.path;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class InFileOptionPath {
  protected final @NotNull List<? extends OptionPathPart> optionPathParts;

  protected InFileOptionPath(@NotNull List<? extends OptionPathPart> optionPathParts) {
    this.optionPathParts = new ArrayList<>(optionPathParts);
  }

  public boolean match(@NotNull InFileUniqueOptionPath otherOptionPath) {
    if (optionPathParts.size() != otherOptionPath.getOptionPathParts().size()) {
      return false;
    }
    for (int i = 0; i < optionPathParts.size(); i++) {
      if (!optionPathParts.get(i).match(otherOptionPath.getOptionPathParts().get(i))) {
        return false;
      }
    }
    return true;
  }

  public @NotNull List<? extends OptionPathPart> getOptionPathParts() {
    return optionPathParts;
  }

  public @NotNull InFileOptionPath resolve(@NotNull UniqueOptionPathPart uniqueOptionPathPart) {
    List<OptionPathPart> newOptionPathParts = new ArrayList<>(optionPathParts);
    newOptionPathParts.add(uniqueOptionPathPart);
    return new InFileOptionPath(newOptionPathParts);
  }

  public boolean isRoot() {
    return optionPathParts.isEmpty();
  }

  public @NotNull InFileOptionPath getParent() throws PathException.ReachedPathRoot {
    if (optionPathParts.isEmpty()) {
      throw new PathException.ReachedPathRoot();
    }
    List<OptionPathPart> newOptionPathParts = new ArrayList<>(optionPathParts);
    newOptionPathParts.remove(newOptionPathParts.size() - 1);
    return new InFileOptionPath(newOptionPathParts);
  }

  public @NotNull OptionPathPart getFirstPart() throws PathException.ReachedPathRoot {
    if (optionPathParts.isEmpty()) {
      throw new PathException.ReachedPathRoot();
    }
    return optionPathParts.get(0);
  }

  public @NotNull InFileOptionPath getConsumedPath() {
    return new InFileOptionPath(new ArrayList<>(optionPathParts.subList(1, optionPathParts.size())));
  }
}
