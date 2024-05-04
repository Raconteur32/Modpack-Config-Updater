package fr.paulbrancieq.modpackconfigupdater.remake.path;

import com.google.gson.annotations.JsonAdapter;
import fr.paulbrancieq.modpackconfigupdater.remake.path.pathpart.OptionPathPart;
import fr.paulbrancieq.modpackconfigupdater.remake.path.pathpart.SimpleOptionPathPart;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@JsonAdapter(OptionPathDeserializer.class)
public class OptionPath {
  protected final @NotNull List<? extends OptionPathPart> optionPathParts;

  protected OptionPath(@NotNull List<? extends OptionPathPart> optionPathParts) {
    this.optionPathParts = new ArrayList<>(optionPathParts);
  }

  public boolean match(@NotNull SimpleOptionPath otherOptionPath) {
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

  public @NotNull OptionPath resolve(@NotNull SimpleOptionPathPart uniqueOptionPathPart) {
    List<OptionPathPart> newOptionPathParts = new ArrayList<>(optionPathParts);
    newOptionPathParts.add(uniqueOptionPathPart);
    return new OptionPath(newOptionPathParts);
  }

  public boolean isRoot() {
    return optionPathParts.isEmpty();
  }

  public @NotNull OptionPath getParent() throws PathException.ReachedPathRoot {
    if (optionPathParts.isEmpty()) {
      throw new PathException.ReachedPathRoot();
    }
    List<OptionPathPart> newOptionPathParts = new ArrayList<>(optionPathParts);
    newOptionPathParts.remove(newOptionPathParts.size() - 1);
    return new OptionPath(newOptionPathParts);
  }

  public @NotNull OptionPathPart getFirstPart() throws PathException.ReachedPathRoot {
    if (optionPathParts.isEmpty()) {
      throw new PathException.ReachedPathRoot();
    }
    return optionPathParts.get(0);
  }

  public @NotNull OptionPath getConsumedPath() {
    return new OptionPath(new ArrayList<>(optionPathParts.subList(1, optionPathParts.size())));
  }
}
