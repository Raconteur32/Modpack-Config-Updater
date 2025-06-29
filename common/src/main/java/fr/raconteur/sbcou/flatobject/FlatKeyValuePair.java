package fr.raconteur.sbcou.flatobject;

import fr.raconteur.sbcou.OptionChange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Map;
import java.util.Objects;

@Unmodifiable
public class FlatKeyValuePair implements Map.Entry<FlatKey, Object> {
    private final FlatKey flatKey;
    private final Object value;

    public FlatKeyValuePair(@NotNull String key, Object value) {
        this.flatKey = new FlatKey(key);
        this.value = value;
    }

    @Override
    public FlatKey getKey() {
        return flatKey;
    }

    public String getFlatKeyString() {
        return flatKey.toString();
    }

    @Unmodifiable
    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public Object setValue(Object value) {
        throw new UnsupportedOperationException("FlatKeyValuePair is immutable");
    }

    public OptionChange compare(FlatKeyValuePair other) {
        if (other == null || !Objects.equals(this.getFlatKeyString(), other.getFlatKeyString())) {
            throw new IllegalArgumentException("Keys must be identical for comparison");
        }
        return Objects.equals(this.value, other.getValue()) ? OptionChange.EQUAL : OptionChange.MODIFIED;
    }
}
