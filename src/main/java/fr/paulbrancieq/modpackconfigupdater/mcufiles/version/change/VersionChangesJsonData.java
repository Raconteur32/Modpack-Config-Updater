package fr.paulbrancieq.modpackconfigupdater.mcufiles.version.change;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import fr.paulbrancieq.modpackconfigupdater.mcufiles.McuJsonData;
import fr.paulbrancieq.modpackconfigupdater.path.OptionPath;
import fr.paulbrancieq.modpackconfigupdater.path.OptionPathListGsonDeserializer;

import java.util.ArrayList;
import java.util.List;

public class VersionChangesJsonData extends McuJsonData {
  @JsonAdapter(OptionPathListGsonDeserializer.class)
  @Expose
  @SerializedName("remove")
  public List<OptionPath> toRemove = new ArrayList<>();

  @JsonAdapter(OptionPathListGsonDeserializer.class)
  @Expose
  @SerializedName("merge")
  public List<OptionPath> toMerge = new ArrayList<>();

  @JsonAdapter(OptionPathListGsonDeserializer.class)
  @Expose
  @SerializedName("override")
  public List<OptionPath> toOverride = new ArrayList<>();
}
