package fr.paulbrancieq.modpackconfigupdater.newtests.mcujsonfiletest.subtypetestfiles;

import com.google.gson.annotations.Expose;
import fr.paulbrancieq.modpackconfigupdater.remake.mcufile.JsonRequired;

public class SubType {
  @Expose
  @JsonRequired
  public String id;
  @Expose
  public String name;
}
