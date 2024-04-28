package fr.paulbrancieq.modpackconfigupdater.newtests.mcujsonfiletest.subtypetestfiles;

import com.google.gson.annotations.Expose;
import fr.paulbrancieq.modpackconfigupdater.remake.mcufile.JsonRequired;
import fr.paulbrancieq.modpackconfigupdater.remake.mcufile.McuJsonFileData;

import java.util.List;

public class McuFileData extends McuJsonFileData {
  @JsonRequired
  @Expose
  public SubType object;

  @Expose
  @JsonRequired
  public List<SubType> list;
}
