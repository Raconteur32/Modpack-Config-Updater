package fr.paulbrancieq.modpackconfigupdater.options.type;

import com.google.gson.Gson;
import fr.paulbrancieq.modpackconfigupdater.Backup;
import fr.paulbrancieq.modpackconfigupdater.exceptions.OptionException;
import fr.paulbrancieq.modpackconfigupdater.options.Option;
import fr.paulbrancieq.modpackconfigupdater.path.OptionPath;

import java.io.File;
import java.util.List;

public class JsonListOptionFile extends ListOption {
  public JsonListOptionFile(OptionPath optionPath, Backup backup, List<Option<?>> value, CollectionOption<?> parent) {
    super(optionPath, backup, value, parent);
  }

  @SuppressWarnings({"unused","Duplicates"})
  @Override
  public void save() throws OptionException.FileException.CantWriteFile {
    Gson gson = new Gson();
    try {
      File file = new File(optionPath.getFilePath());
      Boolean check = file.getParentFile().mkdirs();
      java.io.FileWriter writer = new java.io.FileWriter(file);
      gson.toJson(value, writer);
      writer.close();
    } catch (java.io.IOException e) {
      throw new OptionException.FileException.CantWriteFile(optionPath.getFilePath(), e);
    }
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static JsonListOptionFile fromFile(OptionPath optionPath, Backup backup) throws OptionException.FileException.CantReadFile {
    Gson gson = new Gson();
    try {
      File file = new File(optionPath.getFilePath());
      java.io.FileReader reader = new java.io.FileReader(file);
      List value = gson.fromJson(reader, List.class);
      reader.close();
      return new JsonListOptionFile(optionPath, backup, serializableListToOptionList(optionPath, backup, value), null);
    } catch (java.io.IOException e) {
      throw new OptionException.FileException.CantReadFile(optionPath.getFilePath(), e);
    }
  }

  @Override
  public JsonListOptionFile deepCopy(CollectionOption<?> parent) {
    return new JsonListOptionFile(optionPath, backup, value, parent);
  }
}
