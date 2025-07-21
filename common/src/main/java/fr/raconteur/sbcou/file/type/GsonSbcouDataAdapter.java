package fr.raconteur.sbcou.file.type;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import fr.raconteur.sbcou.types.SbcouData;
import fr.raconteur.sbcou.types.nested.SbcouList;
import fr.raconteur.sbcou.types.nested.SbcouObject;
import fr.raconteur.sbcou.types.primitives.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GsonSbcouDataAdapter extends TypeAdapter<SbcouData<?>> {

    private final TypeAdapter<Object> delegate = new Gson().getAdapter(Object.class);

    @Override
    public void write(JsonWriter out, SbcouData<?> value) throws IOException {
        delegate.write(out, value);
    }

    @Override
    public SbcouData<?> read(JsonReader in) throws IOException {
        in.setStrictness(Strictness.LENIENT);
        JsonToken token = in.peek();
        switch (token) {
            case JsonToken.BEGIN_ARRAY:
                List<SbcouData<?>> sbcouList = new ArrayList<>();
                in.beginArray();
                while (in.hasNext()) {
                    sbcouList.add(read(in));
                }
                in.endArray();
                return new SbcouList(sbcouList);
            case JsonToken.BEGIN_OBJECT:
                Map<String, SbcouData<?>> sbcouMap = new HashMap<>();
                in.beginObject();
                while (in.hasNext()) {
                    sbcouMap.put(in.nextName(), read(in));
                }
                in.endObject();
                return new SbcouObject(sbcouMap);

            case JsonToken.STRING:
                return new SbcouString(in.nextString());

            case JsonToken.NUMBER:
                String n = in.nextString();
                if (n.equals("-NaN") || n.equals("NaN") || n.equals("+NaN")) {
                    return new SbcouNaN(n);
                }
                if (n.equals("-Infinity") || n.equals("Infinity") || n.equals("+Infinity")) {
                    return new SbcouInfinity(n);
                }
                if (n.indexOf('.') != -1) {
                    return new SbcouReal(n);
                }
                return new SbcouInteger(n);

            case JsonToken.BOOLEAN:
                return new SbcouBoolean(in.nextBoolean());

            case JsonToken.NULL:
                in.nextNull();
                return null;

            default:
                throw new IllegalStateException();
        }
    }
}

