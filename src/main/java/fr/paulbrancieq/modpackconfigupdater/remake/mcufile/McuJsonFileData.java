package fr.paulbrancieq.modpackconfigupdater.remake.mcufile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class McuJsonFileData {
  @JsonRequired
  @SerializedName("version")
  @Expose
  public Integer version;

  protected List<VersionTransition> versionTransitions = new ArrayList<>();

  protected void addVersionTransition(Integer from, Integer to, Consumer<McuJsonFileData> transition) {
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
    protected Consumer<McuJsonFileData> transition;
    public VersionTransition(Integer from, Integer to, Consumer<McuJsonFileData> transition) {
      this.from = from;
      this.to = to;
      this.transition = transition;
    }

    public void apply(McuJsonFileData data) {
      transition.accept(data);
      data.version = to;
    }
  }
}
