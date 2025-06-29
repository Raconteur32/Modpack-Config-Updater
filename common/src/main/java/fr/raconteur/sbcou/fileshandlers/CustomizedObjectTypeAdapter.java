package fr.raconteur.sbcou.fileshandlers;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import fr.raconteur.sbcou.fileshandlers.types.StrictlyComparableArrayList;
import fr.raconteur.sbcou.fileshandlers.types.StrictlyComparableMap;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public class CustomizedObjectTypeAdapter extends TypeAdapter<Object> {

  private final TypeAdapter<Object> delegate = new Gson().getAdapter(Object.class);

  @Override
  public void write(JsonWriter out, Object value) throws IOException {
    delegate.write(out, value);
  }

  @Override
  public Object read(JsonReader in) throws IOException {
    in.setStrictness(Strictness.LENIENT);
    JsonToken token = in.peek();
    switch (token) {
      case JsonToken.BEGIN_ARRAY:
        List<Object> list = new StrictlyComparableArrayList();
        in.beginArray();
        while (in.hasNext()) {
          list.add(read(in));
        }
        in.endArray();
        return list;

      case JsonToken.BEGIN_OBJECT:
        Map<String, Object> map = new StrictlyComparableMap();
        in.beginObject();
        while (in.hasNext()) {
          map.put(in.nextName(), read(in));
        }
        in.endObject();
        return map;

      case JsonToken.STRING:
        return in.nextString();

      case JsonToken.NUMBER:
        //return in.nextDouble();
        String n = in.nextString();
        if (n.indexOf('.') != -1) {
          return new BigDecimal(n);
        }
        return new BigInteger(n);

      case JsonToken.BOOLEAN:
        return in.nextBoolean();

      case JsonToken.NULL:
        in.nextNull();
        return null;

      default:
        throw new IllegalStateException();
    }
  }
}

