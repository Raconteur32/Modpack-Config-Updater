package fr.paulbrancieq.modpackconfigupdater.remake.mcufile;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnnotatedTypeAdapterFactory implements TypeAdapterFactory {
  private Field[] getFields(Class<?> clazz) {
    List<Field> fields = new ArrayList<>();
    while (clazz != Object.class && clazz != null) {
      fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
      clazz = clazz.getSuperclass();
    }
    return fields.toArray(new Field[0]);
  }
  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
    Class<? super T> rawType = typeToken.getRawType();

    Set<Field> requiredFields = Stream.of(getFields(rawType))
        .filter(f -> f.getAnnotation(JsonRequired.class) != null)
        .collect(Collectors.toSet());

    if (requiredFields.isEmpty()) {
      return null;
    }

    final TypeAdapter<T> baseAdapter = (TypeAdapter<T>) gson.getAdapter(rawType);

    return new TypeAdapter<T>() {

      @Override
      public void write(JsonWriter jsonWriter, T o) throws IOException {
        baseAdapter.write(jsonWriter, o);
      }

      @Override
      public T read(JsonReader in) throws IOException {
        JsonElement jsonElement = Streams.parse(in);

        if (jsonElement.isJsonObject()) {
          ArrayList<String> missingFields = new ArrayList<>();
          for (Field field : requiredFields) {
            if (!jsonElement.getAsJsonObject().has(field.getName())) {
              missingFields.add(field.getName());
            }
          }
          if (!missingFields.isEmpty()) {
            throw new JsonParseException(
                String.format("Missing required fields %s for %s",
                    missingFields, rawType.getName()));
          }
        }

        TypeAdapter<T> delegate = gson.getDelegateAdapter(AnnotatedTypeAdapterFactory.this, typeToken);
        return delegate.fromJsonTree(jsonElement);
      }
    };
  }
}
