package fr.paulbrancieq.modpackconfigupdater.remake.path.pathpart;

import com.google.gson.annotations.JsonAdapter;
import fr.paulbrancieq.modpackconfigupdater.remake.option.option.Option;

@JsonAdapter(OptionPathPartJsonDeserializer.class)
public abstract class OptionPathPart {
  protected abstract boolean match(Object objectToMatch);

  public boolean match(UniqueOptionPathPart otherOptionPathPart) {
    return match(otherOptionPathPart.getStringPathPart());
  }

  public abstract boolean match(Option<?> option);
}
