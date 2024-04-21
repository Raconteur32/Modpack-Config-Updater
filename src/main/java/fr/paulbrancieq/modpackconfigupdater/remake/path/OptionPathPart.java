package fr.paulbrancieq.modpackconfigupdater.remake.path;

import com.google.gson.annotations.JsonAdapter;

@JsonAdapter(OptionPathPartJsonDeserializer.class)
public abstract class OptionPathPart {
  public abstract boolean match(Object otherStringPathPart);

  public boolean match(UniqueOptionPathPart otherOptionPathPart) {
    return match(otherOptionPathPart.getStringPathPart());
  }
}
