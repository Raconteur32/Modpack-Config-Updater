package fr.paulbrancieq.modpackconfigupdater.remake.path.filters;

public enum FilterTarget {
  OPTION_VALUE("value"), INDEX("index");
  private final String target;

  FilterTarget(String target) {
    this.target = target;
  }

  public String getTargetString() {
    return target;
  }
}
