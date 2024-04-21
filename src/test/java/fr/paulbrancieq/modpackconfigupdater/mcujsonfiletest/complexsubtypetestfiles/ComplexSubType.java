package fr.paulbrancieq.modpackconfigupdater.mcujsonfiletest.complexsubtypetestfiles;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import fr.paulbrancieq.modpackconfigupdater.remake.mcufile.JsonRequired;

@JsonAdapter(ComplexSubTypeDeserializer.class)
public class ComplexSubType {
  @Expose
  @JsonRequired
  public String id;
  @Expose
  public String name;
  public String complexField;
}
