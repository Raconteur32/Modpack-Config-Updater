package fr.raconteur.sbcou.config;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonArrayConfigFile<T> implements SbcouConfigFile<JsonArray> {
  private final String path;
  private final Verifier<JsonArray> verifier;

  public JsonArrayConfigFile(String path, Verifier<JsonArray> verifier) {
    this.path = path;
    this.verifier = verifier;
  }

  @Override
  public JsonArray read() throws SbcouConfigFileException {
    return verify();
  }

  public JsonArray _read() {
    try (var reader = Files.newBufferedReader(Path.of(path))) {
      return (new GsonBuilder()).create().fromJson(reader, JsonArray.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void write(JsonArray data) throws SbcouConfigFileException {
    verify();
    _write(data);
  }

  private void _write(JsonArray data) {
    try (var writer = Files.newBufferedWriter(Path.of(path))) {
      (new GsonBuilder()).setPrettyPrinting().create().toJson(data, writer);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public JsonArray verify() throws SbcouConfigFileException {
    if (!Files.exists(Path.of(path))) {
      _write(new JsonArray());
    }
    JsonArray data = _read();
    return verifier.verify(data);
  }
}
