package fr.paulbrancieq.modpackconfigupdater.mcufiles;

import com.google.gson.*;
import fr.paulbrancieq.modpackconfigupdater.ModpackConfigurationUpdater;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.stream.Stream;

public abstract class McuJsonFile<T extends McuJsonData> {
  protected final File file;
  protected final Class<T> dataType;
  T data;

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
          .registerTypeAdapter(this.getDataType(), new AnnotatedDeserializer<>()).create();
      StringBuilder json = new StringBuilder();
      Stream<String> stream = java.nio.file.Files.lines(this.file.toPath());
      stream.forEach(json::append);
      stream.close();
      this.data = gson.fromJson(json.toString(), this.getDataType());
      this.data.applyTransitionsIfNeeded();
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

  protected static class AnnotatedDeserializer<T> implements JsonDeserializer<T>
  {

    public T deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException
    {
      T pojo = new Gson().fromJson(je, type);

      Field[] fields = pojo.getClass().getFields();
      for (Field f : fields)
      {
        if (f.getAnnotation(JsonRequired.class) != null)
        {
          try
          {
            f.setAccessible(true);
            if (f.get(pojo) == null)
            {
              throw new JsonParseException("Missing field in JSON: " + f.getName());
            }
          }
          catch (IllegalArgumentException | IllegalAccessException ex)
          {
            ModpackConfigurationUpdater.LOGGER.error("Can't deserialize file version.", ex);
          }
        }
      }
      return pojo;
    }
  }
}
