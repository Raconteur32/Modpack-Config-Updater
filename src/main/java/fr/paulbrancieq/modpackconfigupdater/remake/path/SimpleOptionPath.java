package fr.paulbrancieq.modpackconfigupdater.remake.path;

import fr.paulbrancieq.modpackconfigupdater.remake.path.pathpart.SimpleOptionPathPart;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class SimpleOptionPath extends OptionPath {

  public SimpleOptionPath(List<SimpleOptionPathPart> uniqueOptionPathParts) {
    super(uniqueOptionPathParts);
  }

  public SimpleOptionPath(String multiPartString) {
    super(SimpleOptionPathPart.fromMultiPartString(multiPartString));
  }

  @SuppressWarnings("unchecked")
  public String getPathString() {
    return ((List<SimpleOptionPathPart>) optionPathParts).stream().map(SimpleOptionPathPart::getStringPathPart)
        .collect(Collectors.joining("."));
  }

  @SuppressWarnings("unchecked")
  @Override
  public @NotNull
  List<SimpleOptionPathPart> getOptionPathParts() {
    return (List<SimpleOptionPathPart>) optionPathParts;
  }
}
