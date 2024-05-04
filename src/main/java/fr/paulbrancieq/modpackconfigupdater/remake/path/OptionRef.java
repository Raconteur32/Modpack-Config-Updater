package fr.paulbrancieq.modpackconfigupdater.remake.path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.JsonAdapter;
import fr.paulbrancieq.modpackconfigupdater.remake.mcufile.AnnotatedTypeAdapterFactory;

import java.nio.file.Path;

@JsonAdapter(OptionRefDeserializer.class)
public class OptionRef {
  private final Path filePath;
  private final OptionPath inFileOptionPath;

  public OptionRef(Path filePath, OptionPath inFileOptionPath) {
    this.filePath = filePath;
    this.inFileOptionPath = inFileOptionPath;
    if (filePath.isAbsolute()) {
      throw new IllegalArgumentException(filePath + " is not a valid relative path.");
    }
  }

  public OptionRef(String completePath) {
    Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
            .registerTypeAdapterFactory(new AnnotatedTypeAdapterFactory()).create();
    String[] split = completePath.split(":");
    if (split.length != 2) {
      throw new IllegalArgumentException(completePath + " is not a valid CompletePath. It must be a String with a " +
              "single colon ':' separating the filepath and the InFileOptionPath.");
    }
    this.filePath = Path.of(split[0]);
    this.inFileOptionPath = gson.fromJson(split[1], OptionPath.class);
  }

  public Path getFilePath() {
    return filePath;
  }

  public OptionPath getInFileOptionPath() {
    return inFileOptionPath;
  }
}
