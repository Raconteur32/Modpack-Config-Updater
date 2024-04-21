package fr.paulbrancieq.modpackconfigupdater.remake.mcufile;

import com.google.gson.*;

import java.io.File;
import java.util.stream.Stream;

public abstract class McuJsonFile<T extends McuJsonFileData> {
  protected final File file;
  protected final Class<T> dataType;
  private final T data;

  public McuJsonFile(String filePath, Class<T> dataType) {
    this.dataType = dataType;
    try {
      this.file = new File(filePath);
      if (!this.file.exists()) {
        throw new IllegalArgumentException("The file must exist.");
      }
    } catch (Exception e) {
      throw new IllegalArgumentException("Can't verify the file.", e);
    }
    try {
      Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
          .registerTypeAdapterFactory(new AnnotatedTypeAdapterFactory()).create();
      StringBuilder json = new StringBuilder();
      Stream<String> stream = java.nio.file.Files.lines(this.file.toPath());
      stream.forEach(json::append);
      stream.close();
      this.data = gson.fromJson(json.toString(), this.getDataType());
      this.data.applyTransitionsIfNeeded();
    } catch (JsonParseException e) {
      throw e;
    } catch (Exception e) {
      throw new IllegalArgumentException("Can't read the data from the file.", e);
    }
  }

  public void write() {
    try {
      Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
      java.nio.file.Files.write(this.file.toPath(), gson.toJson(this.data).getBytes());
    } catch (Exception e) {
      throw new IllegalArgumentException("Can't write the data to the file.", e);
    }
  }

  public Class<T> getDataType() {
    return dataType;
  }

  public T getData() {
    return data;
  }
}
