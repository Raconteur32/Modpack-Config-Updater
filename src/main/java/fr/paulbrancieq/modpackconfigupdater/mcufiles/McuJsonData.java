package fr.paulbrancieq.modpackconfigupdater.mcufiles;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class McuJsonData {
  @JsonRequired
  @SerializedName("version")
  @Expose
  public Integer version;

  protected List<VersionTransition> versionTransitions = new ArrayList<>();

  protected void addVersionTransition(Integer from, Integer to, Consumer<McuJsonData> transition) {
    versionTransitions.add(new VersionTransition(from, to, transition));
  }

  protected void applyTransitionsIfNeeded() {
    versionTransitions.stream().filter(
        versionTransition -> versionTransition.from.equals(version))
        .forEach(versionTransition -> versionTransition.apply(this));
  }

  public static class VersionTransition {
    protected Integer from;
    protected Integer to;
    protected Consumer<McuJsonData> transition;
    public VersionTransition(Integer from, Integer to, Consumer<McuJsonData> transition) {
      this.from = from;
      this.to = to;
      this.transition = transition;
    }

    public void apply(McuJsonData data) {
      transition.accept(data);
      data.version = to;
    }
  }
}
