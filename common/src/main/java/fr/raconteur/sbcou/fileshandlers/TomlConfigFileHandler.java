package fr.raconteur.sbcou.fileshandlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import fr.raconteur.sbcou.exceptions.FileHandlerException;
import fr.raconteur.sbcou.fileshandlers.types.StrictlyComparableMap;
import io.github.wasabithumb.jtoml.JToml;


public class TomlConfigFileHandler extends AbstractConfigFileHandler {
  private String relativePath;

  public TomlConfigFileHandler(String basePath, String relativePath) {
    super(basePath, relativePath);
    this.relativePath = relativePath;
  }

  @Override
  public Map<String, Object> read() throws FileHandlerException {
    // get config string file content
    StrictlyComparableMap configMap = new StrictlyComparableMap();
    JToml jToml = JToml.jToml();
    try (BufferedReader reader = new BufferedReader(new java.io.FileReader(configFile))) {
      // Parse the TOML file
      HashMap<String, Object> serialized = new Gson().fromJson(jToml.serialize(JsonObject.class, jToml.read(reader)),
              HashMap.class);
      configMap.putAll(serialized);
    } catch (IOException e) {
      throw new FileHandlerException("Error reading TOML file: " + configFile.getAbsolutePath(), e);
    } catch (Exception e) {
      throw new FileHandlerException("Error parsing TOML file: " + configFile.getAbsolutePath(), e);
    }

    return configMap;
  }

  @Override
  public void write(Object value) throws IOException {
    if (!(value instanceof Map)) {
      throw new IllegalArgumentException("Value must be a Map");
    }
    boolean ignored = configFile.getParentFile().mkdirs();
    boolean ignored2 = configFile.createNewFile();

    JToml toml = JToml.jToml();
    try {
      String jsonString = new Gson().toJson(value);
      JsonObject jsonObject = new Gson().fromJson(jsonString, JsonObject.class);
      toml.write(configFile, toml.deserialize(JsonObject.class, jsonObject));
    } catch (Exception e) {
      throw new IOException("Error serializing data to TOML format: " + configFile.getAbsolutePath(), e);
    }
  }
}
