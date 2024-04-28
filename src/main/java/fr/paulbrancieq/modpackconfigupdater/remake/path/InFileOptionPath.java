package fr.paulbrancieq.modpackconfigupdater.remake.path;

import java.util.ArrayList;
import java.util.List;

public class InFileOptionPath {
  protected final List<? extends OptionPathPart> optionPathParts;

  protected InFileOptionPath(List<? extends OptionPathPart> optionPathParts) {
    this.optionPathParts = new ArrayList<>(optionPathParts);
  }

  public boolean match(InFileUniqueOptionPath otherOptionPath) {
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

  public List<? extends OptionPathPart> getOptionPathParts() {
    return optionPathParts;
  }

  public InFileOptionPath resolve(UniqueOptionPathPart uniqueOptionPathPart) {
    List<OptionPathPart> newOptionPathParts = new ArrayList<>(optionPathParts);
    newOptionPathParts.add(uniqueOptionPathPart);
    return new InFileOptionPath(newOptionPathParts);
  }
}
