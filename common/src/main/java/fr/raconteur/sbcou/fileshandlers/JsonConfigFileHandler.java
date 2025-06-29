package fr.raconteur.sbcou.fileshandlers;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class JsonConfigFileHandler extends AbstractConfigFileHandler {

  public JsonConfigFileHandler(String basePath, String relativePath) {
    super(basePath, relativePath);
  }

  @Override
  public Object read() throws Exception {
    GsonBuilder gsonBuilder = new GsonBuilder()
            //.registerTypeAdapter(TypeToken.get(Object.class).getType(), new CustomizedObjectTypeAdapter())
            /*.setObjectToNumberStrategy(in -> {
              String n = in.nextString();
              if (n.indexOf('.') != -1) {
                return new BigDecimal(n);
              }
              return new BigInteger(n);
            })*/;
    Gson gson = gsonBuilder.create();

    try (BufferedReader reader = new BufferedReader(new FileReader(configFile)); BufferedReader secondReader =
            new BufferedReader(new FileReader(configFile))) {
      JsonReader jsonReader = gson.newJsonReader(reader);
      // Object result = gson.fromJson(reader, TypeToken.get(Object.class).getType());
      Object result = (new CustomizedObjectTypeAdapter()).read(jsonReader);
      if (result == null) {
        String lines = secondReader.lines().reduce("", (a, b) -> a + b);
        if (lines.equals("null")) {
          return null;
        } else {
          throw new Exception("Invalid JSON file: " + configFile.getAbsolutePath());
        }
      }
      return result;
    }
  }

  @Override
  public void write(Object data) throws IOException {
    Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
    String json = gson.toJson(data);
    var ignored = configFile.getParentFile().mkdirs();
    var ignored2 = configFile.createNewFile();
    try (java.io.FileWriter writer = new java.io.FileWriter(configFile)) {
      writer.write(json);
    }
  }
}
