package fr.paulbrancieq.modpackconfigupdater.remake.option.containers.filetypes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import fr.paulbrancieq.modpackconfigupdater.CustomizedObjectTypeAdapter;
import fr.paulbrancieq.modpackconfigupdater.remake.option.containers.FileContainer;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.List;
import java.util.function.Function;

public class JsonFileContainer extends FileContainer {

  protected JsonFileContainer(Path aPath, Path rPath, @Nullable Object newOptionValue) {
    super(aPath, rPath, newOptionValue);
  }

  public static JsonFileContainer fromPath(Path bPath, Path rPath) throws FileNotFoundException {
    // Gson with default JsonElement deserializer (to be able to read any json file and keep it as JsonElement)
    // and custom type adapter for all objects to be able to read the correct type of number (BigDecimal or BigInteger)
    GsonBuilder gsonBuilder = new GsonBuilder().registerTypeAdapter(TypeToken.get(Object.class).getType(),
            new CustomizedObjectTypeAdapter()).setObjectToNumberStrategy(in -> {
      String n = in.nextString();
      if (n.indexOf('.') != -1) {
        return new BigDecimal(n);
      }
      return new BigInteger(n);
    });
    //.registerTypeAdapter(JsonElement.class, (JsonDeserializer<JsonElement>) (json, typeOfT, context) -> json);
    Gson gson = gsonBuilder.create();
    Path aPath = bPath.resolve(rPath);
    BufferedReader reader = new BufferedReader(new FileReader(aPath.toFile()));
    //JsonElement jsonElement = gson.fromJson(reader, JsonElement.class);
    Object jsonElement = gson.fromJson(reader, TypeToken.get(Object.class).getType());
    return new JsonFileContainer(aPath, rPath, jsonElement);
  }

  public static Object serializableFromJsonElement(JsonElement jsonElement) {
    List<AbstractMap.SimpleImmutableEntry<Function<JsonElement, Boolean>, Function<JsonElement, Object>>> functionMap =
            List.of(new AbstractMap.SimpleImmutableEntry<>(JsonElement::isJsonArray, JsonElement::getAsJsonArray),
                    new AbstractMap.SimpleImmutableEntry<>(JsonElement::isJsonObject, JsonElement::getAsJsonObject),
                    new AbstractMap.SimpleImmutableEntry<>((jsonElement1) -> jsonElement1.isJsonPrimitive() &&
                            jsonElement1.getAsJsonPrimitive().isString(),
                            (jsonElement1) -> jsonElement1.getAsJsonPrimitive().getAsString()),
                    new AbstractMap.SimpleImmutableEntry<>(JsonFileContainer::isJsonElementIntegral,
                            (jsonElement1) -> jsonElement1.getAsJsonPrimitive().getAsBigInteger()),
                    new AbstractMap.SimpleImmutableEntry<>((jsonElement1) -> jsonElement1.isJsonPrimitive() &&
                            jsonElement1.getAsJsonPrimitive().isNumber(),
                            (jsonElement1) -> jsonElement1.getAsJsonPrimitive().getAsBigDecimal()),
                    new AbstractMap.SimpleImmutableEntry<>((jsonElement1) -> jsonElement1.isJsonPrimitive() &&
                            jsonElement1.getAsJsonPrimitive().isBoolean(),
                            (jsonElement1) -> jsonElement1.getAsJsonPrimitive().getAsBoolean()),
                    new AbstractMap.SimpleImmutableEntry<>(JsonElement::isJsonNull, (jsonElement1) -> null));
    for (AbstractMap.SimpleImmutableEntry<Function<JsonElement, Boolean>, Function<JsonElement, Object>> entry :
            functionMap) {
      if (entry.getKey().apply(jsonElement)) {
        return entry.getValue().apply(jsonElement);
      }
    }
    throw new RuntimeException("JsonElement is not a valid JsonElement");
  }

  public static Boolean isJsonElementIntegral(JsonElement jsonElement) {
    // Purposely exclude floating point numbers AND bytes
    if (!jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isNumber()) {
      return false;
    }
    Number number = jsonElement.getAsJsonPrimitive().getAsNumber();
    return number instanceof Integer || number instanceof Long || number instanceof Short ||
            number instanceof BigInteger;
  }

  @Override
  public void save() {

  }
}
