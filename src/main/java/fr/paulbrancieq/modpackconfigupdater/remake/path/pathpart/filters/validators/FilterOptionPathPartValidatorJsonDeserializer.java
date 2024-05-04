package fr.paulbrancieq.modpackconfigupdater.remake.path.pathpart.filters.validators;

import com.google.gson.*;
import fr.paulbrancieq.modpackconfigupdater.remake.mcufile.AnnotatedTypeAdapterFactory;
import fr.paulbrancieq.modpackconfigupdater.remake.path.pathpart.filters.validators.type.FilterValidatorType;

import java.lang.reflect.Type;

public class FilterOptionPathPartValidatorJsonDeserializer implements JsonDeserializer<FilterOptionPathPartValidator> {
  @Override
  public FilterOptionPathPartValidator deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    JsonObject jsonObject = json.getAsJsonObject();
    FilterValidatorType type = context.deserialize(jsonObject.get("type"), FilterValidatorType.class);
    Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
        .registerTypeAdapterFactory(new AnnotatedTypeAdapterFactory()).create();
    return gson.fromJson(jsonObject, type.getFilterValidatorOptionPathPartClass());
  }
}
