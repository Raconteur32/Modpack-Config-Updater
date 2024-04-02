package fr.paulbrancieq.modpackconfigupdater.options.type;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.paulbrancieq.modpackconfigupdater.Backup;
import fr.paulbrancieq.modpackconfigupdater.CustomizedObjectTypeAdapter;
import fr.paulbrancieq.modpackconfigupdater.exceptions.OptionException;
import fr.paulbrancieq.modpackconfigupdater.options.Option;
import fr.paulbrancieq.modpackconfigupdater.path.OptionPath;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class JsonListOptionFile extends ListOption {
  private Boolean removed = false;
  private final String basePath;

  public JsonListOptionFile(String basePath, OptionPath optionPath, Backup backup, List<Option<?>> value, CollectionOption<?> parent) {
    super(optionPath, backup, value, parent);
    this.basePath = basePath;
  }

  @SuppressWarnings({"unused","Duplicates"})
  @Override
  public void save() throws OptionException.FileException.CantWriteFile, OptionException.FileException.CantDeleteFile {
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

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static JsonListOptionFile fromFile(String basePath, OptionPath optionPath, Backup backup) throws OptionException.FileException.CantReadFile {
    Gson gson = new GsonBuilder().registerTypeAdapter(new TypeToken<Map<String, Object>>(){}.getType(),
        new CustomizedObjectTypeAdapter())
        .registerTypeAdapter(new TypeToken<List<Option<?>>>(){}.getType(), new CustomizedObjectTypeAdapter())
        .create();
    try {
      File file = new File(Path.of(basePath, optionPath.getFilePath()).toString());
      java.io.FileReader reader = new java.io.FileReader(file);
      List value = gson.fromJson(reader, List.class);
      reader.close();
      return new JsonListOptionFile(basePath, optionPath, backup, serializableListToOptionList(optionPath, backup, value), null);
    } catch (Exception e) {
      throw new OptionException.FileException.CantReadFile(Path.of(basePath, optionPath.getFilePath()).toString(), e);
    }
  }

  @Override
  public JsonListOptionFile deepCopy(CollectionOption<?> parent) {
    throw new UnsupportedOperationException("Use deepCopyFile instead of deepCopy for JsonListOptionFile.");
  }

  public JsonListOptionFile deepCopyFile(String basePath, CollectionOption<?> parent) {
    return new JsonListOptionFile(basePath, optionPath, getBackup().orElse(null), value, parent);
  }
}
