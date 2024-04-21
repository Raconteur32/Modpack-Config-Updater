package fr.paulbrancieq.modpackconfigupdater.remake.path;

import com.google.gson.*;
import fr.paulbrancieq.modpackconfigupdater.remake.mcufile.AnnotatedTypeAdapterFactory;
import fr.paulbrancieq.modpackconfigupdater.remake.path.filters.FilterOptionPathPart;

import java.lang.reflect.Type;

public class OptionPathPartJsonDeserializer implements JsonDeserializer<OptionPathPart> {
  @Override
  public OptionPathPart deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
        .registerTypeAdapterFactory(new AnnotatedTypeAdapterFactory()).create();
    try {
      return gson.fromJson(json, UniqueOptionPathPart.class);
    } catch (JsonParseException ignored) {
    }
    try {
      return gson.fromJson(json, FilterOptionPathPart.class);
    } catch (JsonParseException ignored) {
    }
    throw new JsonParseException(json.toString() + "is not a valid option path part. It should either be a string or a filter (unique or array of validator objects)");
  }
}
