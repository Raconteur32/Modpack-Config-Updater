package fr.paulbrancieq.modpackconfigupdater.mcujsonfiletest.basictestfiles;

import com.google.gson.annotations.Expose;
import fr.paulbrancieq.modpackconfigupdater.remake.mcufile.JsonRequired;
import fr.paulbrancieq.modpackconfigupdater.remake.mcufile.McuJsonFileData;

public class McuFileData extends McuJsonFileData {
  @JsonRequired
  @Expose
  private String id;

  @Expose
  private String name;

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
