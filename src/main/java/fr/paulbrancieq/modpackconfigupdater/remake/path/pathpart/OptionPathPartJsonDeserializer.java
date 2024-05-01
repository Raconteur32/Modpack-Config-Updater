package fr.paulbrancieq.modpackconfigupdater.remake.path.pathpart;

import com.google.gson.*;
import fr.paulbrancieq.modpackconfigupdater.remake.mcufile.AnnotatedTypeAdapterFactory;
import fr.paulbrancieq.modpackconfigupdater.remake.path.pathpart.filters.FilterOptionPathPart;

import java.lang.reflect.Type;

public class OptionPathPartJsonDeserializer implements JsonDeserializer<OptionPathPart> {
  @Override
  public OptionPathPart deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
        .registerTypeAdapterFactory(new AnnotatedTypeAdapterFactory()).create();
    if (json.isJsonObject() || json.isJsonArray()) {
      return gson.fromJson(json, FilterOptionPathPart.class);
    }
    if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isString()) {
      return new UniqueOptionPathPart(json.getAsString());
    }
    throw new JsonParseException(json + " is not a valid OptionPathPart. It must be a JsonObject, JsonArray or String.");
  }
}
