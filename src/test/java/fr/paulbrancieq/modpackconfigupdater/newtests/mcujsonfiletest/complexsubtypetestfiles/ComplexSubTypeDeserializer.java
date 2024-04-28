package fr.paulbrancieq.modpackconfigupdater.newtests.mcujsonfiletest.complexsubtypetestfiles;

import com.google.gson.*;

import java.lang.reflect.Type;

public class ComplexSubTypeDeserializer implements JsonDeserializer<ComplexSubType> {
  @Override
  public ComplexSubType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    JsonObject jsonObject = json.getAsJsonObject();
    ComplexSubType complexSubType = new ComplexSubType();
    complexSubType.id = jsonObject.get("id").getAsString();
    complexSubType.name = jsonObject.get("name").getAsString();
    complexSubType.complexField = complexSubType.name + complexSubType.id;
    return complexSubType;
  }
}
