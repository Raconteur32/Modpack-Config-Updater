package fr.paulbrancieq.modpackconfigupdater.remake.path.pathpart.filters.validators.type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class FilterValidatorDeserializer implements JsonDeserializer<FilterValidatorType> {
  @Override
  public FilterValidatorType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
          throws JsonParseException {
    try {
      return FilterValidatorType.fromString(json.getAsString());
    } catch (Exception e) {
      throw new JsonParseException("Invalid FilterValidatorType: " + json.getAsString());
    }
  }
}
