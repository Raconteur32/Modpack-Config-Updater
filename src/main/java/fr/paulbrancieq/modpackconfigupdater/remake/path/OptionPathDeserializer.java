package fr.paulbrancieq.modpackconfigupdater.remake.path;

import com.google.gson.*;
import fr.paulbrancieq.modpackconfigupdater.remake.mcufile.AnnotatedTypeAdapterFactory;
import fr.paulbrancieq.modpackconfigupdater.remake.path.pathpart.OptionPathPart;
import fr.paulbrancieq.modpackconfigupdater.remake.path.pathpart.SimpleOptionPathPart;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class OptionPathDeserializer implements JsonDeserializer<OptionPath> {
  @Override
  public OptionPath deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
          throws JsonParseException {
    Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
            .registerTypeAdapterFactory(new AnnotatedTypeAdapterFactory()).create();
    if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isString()) {
      return new SimpleOptionPath(json.getAsString());
    } else if (json.isJsonArray()) {
      List<OptionPathPart> optionPathParts = new ArrayList<>();
      for (JsonElement jsonElement : json.getAsJsonArray()) {
        if (jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isString()) {
          optionPathParts.addAll(SimpleOptionPathPart.fromMultiPartString(jsonElement.getAsString()));
        } else {
          optionPathParts.add(gson.fromJson(jsonElement, OptionPathPart.class));
        }
      }
      return new OptionPath(optionPathParts);
    }
    throw new JsonParseException(json + " is not a valid OptionPath. It must be a JsonArray or String.");
  }
}
