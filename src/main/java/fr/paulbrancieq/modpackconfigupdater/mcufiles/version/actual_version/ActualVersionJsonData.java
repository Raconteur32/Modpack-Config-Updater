package fr.paulbrancieq.modpackconfigupdater.mcufiles.version.actual_version;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import fr.paulbrancieq.modpackconfigupdater.mcufiles.JsonRequired;
import fr.paulbrancieq.modpackconfigupdater.mcufiles.McuJsonData;

public class ActualVersionJsonData extends McuJsonData {
  @JsonRequired
  @SerializedName("id")
  @Expose
  public String actualVersion;
}
