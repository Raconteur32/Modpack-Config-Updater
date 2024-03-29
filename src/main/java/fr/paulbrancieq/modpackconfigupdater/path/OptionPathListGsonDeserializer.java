package fr.paulbrancieq.modpackconfigupdater.path;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class OptionPathListGsonDeserializer implements JsonDeserializer<List<OptionPath>> {
  @Override
  public List<OptionPath> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    List<OptionPath> optionPaths = new ArrayList<>();
    json.getAsJsonArray().forEach(jsonElement -> optionPaths.add(new OptionPath(jsonElement.getAsString())));
    return optionPaths;
  }
}
