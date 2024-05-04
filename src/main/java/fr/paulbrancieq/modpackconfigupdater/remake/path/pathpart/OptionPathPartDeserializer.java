package fr.paulbrancieq.modpackconfigupdater.remake.path.pathpart;

import com.google.gson.*;
import fr.paulbrancieq.modpackconfigupdater.remake.mcufile.AnnotatedTypeAdapterFactory;
import fr.paulbrancieq.modpackconfigupdater.remake.path.pathpart.filters.FilterOptionPathPart;

import java.lang.reflect.Type;

public class OptionPathPartDeserializer implements JsonDeserializer<OptionPathPart> {
  @Override
  public OptionPathPart deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
        .registerTypeAdapterFactory(new AnnotatedTypeAdapterFactory()).create();
    if (json.isJsonObject()) {
      return gson.fromJson(json, FilterOptionPathPart.class);
    }
    if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isString()) {
      return new SimpleOptionPathPart(json.getAsString());
    }
    throw new JsonParseException(json + " is not a valid OptionPathPart. It must be a JsonObject, or String.");
  }
}
