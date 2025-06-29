package fr.raconteur.sbcou.fileshandlers.types;

import com.google.gson.internal.LinkedTreeMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class StrictlyComparableMap implements Map<String, Object> {

  LinkedTreeMap<String, Object> map = new LinkedTreeMap<>();

  public StrictlyComparableMap() {
  }

  @Override
  public boolean equals(Object other) {
    // The original HashMap is reused, but
    // We ensure that the compared Object is a StrictlyComparableMap
    if (!(other instanceof StrictlyComparableMap otherMap)) {
      return false;
    }
    if (this.size() != otherMap.size()) {
      return false;
    }
    for (Entry<String, Object> entry : this.entrySet()) {
      Object thisElement = entry.getValue();
      Object otherElement = otherMap.get(entry.getKey());
      if (!Utils.isValidType(thisElement) || !Utils.isValidType(otherElement)) {
        throw new RuntimeException("Invalid type in StrictlyComparableArrayList");
      }
      if (!thisElement.getClass().isArray() && !otherElement.getClass().isArray() && !Objects.equals(thisElement, otherElement)) {
        return false;
      } else if (thisElement.getClass().isArray() && otherElement.getClass().isArray() && !Arrays.deepEquals((Object[]) thisElement, (Object[]) otherElement)) {
        return false;
      } else if (thisElement.getClass().isArray() || otherElement.getClass().isArray()) {
        return false;
      }
    }
    return true;
  }

  @Override
  public int hashCode() {
    // hashCode like ArraysList
    int hashCode = 1;
    for (Map.Entry<String, Object> entry : this.entrySet()) {
      hashCode = 31 * hashCode + (entry == null ? 0 : entry.getKey().hashCode() + entry.getValue().hashCode() * 2);
    }
    return hashCode;
  }

  @Override
  public int size() {
    return this.map.size();
  }

  @Override
  public boolean isEmpty() {
    return this.map.isEmpty();
  }

  @Override
  public boolean containsKey(Object key) {
    return this.map.containsKey(key);
  }

  @Override
  public boolean containsValue(Object value) {
    return this.map.containsValue(value);
  }

  @Override
  public Object get(Object key) {
    return this.map.get(key);
  }

  @Nullable
  @Override
  public Object put(String key, Object value) {
    return this.map.put(key, Utils.toValidType(value));
  }

  @Override
  public Object remove(Object key) {
    return this.map.remove(key);
  }

  @Override
  public void putAll(@NotNull Map<? extends String, ? extends Object> m) {
    for (Map.Entry<? extends String, ?> entry : m.entrySet()) {
      put(entry.getKey(), Utils.toValidType(entry.getValue()));
    }
  }

  @Override
  public void clear() {
    this.map.clear();
  }

  @NotNull
  @Override
  public Set<String> keySet() {
    return this.map.keySet();
  }

  @NotNull
  @Override
  public Collection<Object> values() {
    return this.map.values();
  }

  @NotNull
  @Override
  public Set<Entry<String, Object>> entrySet() {
    return this.map.entrySet();
  }
}
