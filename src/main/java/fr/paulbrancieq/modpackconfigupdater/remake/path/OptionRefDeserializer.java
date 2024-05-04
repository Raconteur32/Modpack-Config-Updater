package fr.paulbrancieq.modpackconfigupdater.remake.path;

import com.google.gson.*;
import fr.paulbrancieq.modpackconfigupdater.remake.mcufile.AnnotatedTypeAdapterFactory;

import java.lang.reflect.Type;
import java.nio.file.Path;

public class OptionRefDeserializer implements JsonDeserializer<OptionRef> {

  @Override
  public OptionRef deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
          throws JsonParseException {
    Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
            .registerTypeAdapterFactory(new AnnotatedTypeAdapterFactory()).create();
    if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isString()) {
      try {
        return new OptionRef(json.getAsString());
      } catch (IllegalArgumentException e) {
        throw new JsonParseException(e.getMessage());
      }
    } else if (json.isJsonObject()) {
      if (!json.getAsJsonObject().has("filepath") || !json.getAsJsonObject().get("filepath").isJsonPrimitive() ||
              !json.getAsJsonObject().get("filepath").getAsJsonPrimitive().isString()) {
        throw new JsonParseException("Option ref object must have a 'filepath' field that is a string.");
      }
      if (!json.getAsJsonObject().has("optionpath")) {
        throw new JsonParseException("Option ref object must have an 'optionpath' field.");
      }
        return new OptionRef(Path.of(json.getAsJsonObject().get("filepath").getAsString()),
                gson.fromJson(json.getAsJsonObject().get("optionpath"), OptionPath.class));
    }
    throw new JsonParseException(json + " is not a valid OptionRef. It must be a JsonObject or String.");
  }
}
