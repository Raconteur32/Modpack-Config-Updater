package fr.paulbrancieq.modpackconfigupdater.remake.path;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class InFileUniqueOptionPath extends InFileOptionPath {

  public InFileUniqueOptionPath(List<UniqueOptionPathPart> uniqueOptionPathParts) {
    super(uniqueOptionPathParts);
  }

  public InFileUniqueOptionPath(String multiPartString) {
    super(UniqueOptionPathPart.fromMultiPartString(multiPartString));
  }

  @SuppressWarnings("unchecked")
  public String getPathString() {
    return ((List<UniqueOptionPathPart>) optionPathParts).stream().map(UniqueOptionPathPart::getStringPathPart)
        .collect(Collectors.joining("."));
  }

  @SuppressWarnings("unchecked")
  @Override
  public @NotNull
  List<UniqueOptionPathPart> getOptionPathParts() {
    return (List<UniqueOptionPathPart>) optionPathParts;
  }
}
