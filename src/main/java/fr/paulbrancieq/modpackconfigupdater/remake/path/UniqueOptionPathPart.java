package fr.paulbrancieq.modpackconfigupdater.remake.path;

public class UniqueOptionPathPart extends OptionPathPart {
  private final String stringPathPart;

  public UniqueOptionPathPart(String stringPathPart) {
    this.stringPathPart = stringPathPart;
  }

  @Override
  public boolean match(Object otherStringPathPart) {
    if (!(otherStringPathPart instanceof String)) {
      return stringPathPart.equals(otherStringPathPart.toString());
    }
    return stringPathPart.equals(otherStringPathPart);
  }

  public String getStringPathPart() {
    return stringPathPart;
  }
}
