package fr.paulbrancieq.modpackconfigupdater.mcufiles.version.tree;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import fr.paulbrancieq.modpackconfigupdater.mcufiles.JsonRequired;
import fr.paulbrancieq.modpackconfigupdater.mcufiles.McuJsonData;

import java.util.List;

public class VersionsTreeJsonData extends McuJsonData {
  @JsonRequired
  @Expose
  @SerializedName("versions")
  public List<VersionJsonData> versions;

  public static class VersionJsonData extends McuJsonData {
    @Expose
    @JsonRequired
    @SerializedName("id")
    public String id;

    @Expose
    @JsonRequired
    @SerializedName("next")
    public String next;
  }
}
