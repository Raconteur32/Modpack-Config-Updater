package fr.paulbrancieq.modpackconfigupdater.remake.path.filters;

import com.google.gson.*;

import java.lang.reflect.Type;

public class FilterOptionPathPartJsonDeserializer implements JsonDeserializer<FilterOptionPathPart> {
  // TODO: Implement the deserialize method (need a finished version of FilterOptionPathPart and sub types of FilterOptionPathPart first)
  @Override
  public FilterOptionPathPart deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    JsonObject jsonObject = json.getAsJsonObject();
    FilterTarget target = context.deserialize(jsonObject.get("target"), FilterTarget.class);
    return null;
  }
}
