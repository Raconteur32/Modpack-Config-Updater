package fr.raconteur.sbcou.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import fr.raconteur.sbcou.file.type.GsonSbcouDataAdapter;
import fr.raconteur.sbcou.types.SbcouData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class JsonConfigFileHandler extends AbstractConfigFileHandler {

  public JsonConfigFileHandler(String basePath, String relativePath) {
    super(basePath, relativePath);
  }

  @Override
  protected String getDefaultEncoding() {
    return "utf-8";
  }

  @Override
  public SbcouData<?> read() throws Exception {
    GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson = gsonBuilder.create();

    try (BufferedReader reader = new BufferedReader(new FileReader(configFile, Charset.forName(getEncoding())))) {
      JsonReader jsonReader = gson.newJsonReader(reader);
      SbcouData<?> result = (new GsonSbcouDataAdapter()).read(jsonReader);
      if (result == null) {
        throw new Exception("Invalid JSON file: " + configFile.getAbsolutePath());
      }
      return result;
    }
  }

  @Override
  public void write(SbcouData<?> data) throws IOException {
    Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
    String json = gson.toJson(data);
    var ignored = configFile.getParentFile().mkdirs();
    var ignored2 = configFile.createNewFile();
    try (java.io.FileWriter writer = new java.io.FileWriter(configFile, Charset.forName(getEncoding()))) {
      writer.write(json);
    }
  }
}
