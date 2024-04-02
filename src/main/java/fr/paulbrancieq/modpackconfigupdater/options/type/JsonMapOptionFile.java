package fr.paulbrancieq.modpackconfigupdater.options.type;

import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import fr.paulbrancieq.modpackconfigupdater.Backup;
import fr.paulbrancieq.modpackconfigupdater.CustomizedObjectTypeAdapter;
import fr.paulbrancieq.modpackconfigupdater.exceptions.OptionException;
import fr.paulbrancieq.modpackconfigupdater.options.Option;
import fr.paulbrancieq.modpackconfigupdater.path.OptionPath;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class JsonMapOptionFile extends MapOption {
  private Boolean removed = false;
  private final String basePath;

  public JsonMapOptionFile(String basePath, OptionPath optionPath, Backup backup, Map<?, Option<?>> value, CollectionOption<?> parent) {
    super(optionPath, backup, value, parent);
    this.basePath = basePath;
  }

  @SuppressWarnings({"unused","Duplicates"})
  @Override
  public void save() throws OptionException.FileException.CantWriteFile, OptionException.FileException.CantDeleteFile, OptionException.CantSaveOption {
    backup();
    if (removed) {
      try {
        File file = new File(Path.of(this.basePath, optionPath.getFilePath()).toString());
        boolean check = file.delete();
        return;
      } catch (SecurityException e) {
        throw new OptionException.FileException.CantDeleteFile(optionPath.getFilePath(), e);
      }
    }
    Gson gson = new GsonBuilder().registerTypeAdapter(new TypeToken<Map<String, Object>>(){}.getType(),
            new CustomizedObjectTypeAdapter())
        .registerTypeAdapter(new TypeToken<List<Option<?>>>(){}.getType(), new CustomizedObjectTypeAdapter())
        .create();
    try {
      File file = new File(Path.of(this.basePath, optionPath.getFilePath()).toString());
      Boolean check = file.getParentFile().mkdirs();
      java.io.FileWriter writer = new java.io.FileWriter(file);
      gson.toJson(value, writer);
      writer.close();
    } catch (java.io.IOException e) {
      throw new OptionException.FileException.CantWriteFile(Path.of(this.basePath, optionPath.getFilePath()).toString(), e);
    }
  }

  @Override
  public void remove() {
    super.remove();
    removed = true;
  }

  public static JsonMapOptionFile fromFile(String basePath, OptionPath optionPath, Backup backup) throws OptionException.FileException.CantReadFile {
    if (!optionPath.getInFileOptionPathParts().isEmpty()) {
      throw new IllegalArgumentException("optionPath should only have the file path");
    }
    try {
      File file = new File(Path.of(basePath, optionPath.getFilePath()).toString());
      Gson gson = new GsonBuilder().registerTypeAdapter(new TypeToken<Map<String, Object>>(){}.getType(),
              new CustomizedObjectTypeAdapter())
          .registerTypeAdapter(new TypeToken<List<Option<?>>>(){}.getType(), new CustomizedObjectTypeAdapter())
          .create();
      BufferedReader reader = new BufferedReader(new java.io.FileReader(file));
      Map<String, Object> value = gson.fromJson(reader, new TypeToken<Map<String, Object>>(){}.getType());
      return new JsonMapOptionFile(basePath, optionPath, backup, serializableMapToOptionMap(optionPath, backup, value), null);
    } catch (FileNotFoundException | JsonParseException e) {
      throw new OptionException.FileException.CantReadFile(Path.of(basePath, optionPath.getFilePath()).toString(), e);
    }
  }

  @Override
  public JsonMapOptionFile deepCopy(CollectionOption<?> parent) {
    throw new UnsupportedOperationException("Use deepCopyFile instead of deepCopy for JsonMapOptionFile");
  }

  public JsonMapOptionFile deepCopyFile(String basePath, CollectionOption<?> parent) {
    return new JsonMapOptionFile(basePath, optionPath, getBackup().orElse(null), value, parent);
  }
}
