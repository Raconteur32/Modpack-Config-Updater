package fr.raconteur.sbcou.flatobject;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import fr.raconteur.sbcou.OptionChange;

public class FlatObject implements Map<String, Object> {
  private final Map<String, FlatKeyValuePair> flatMap;

  public FlatObject() {
    this.flatMap = new HashMap<>();
  }

  public static FlatObject fromMap(Map<String, Object> map) {
    FlatObject flatObject = new FlatObject();
    Map<String, Object> flattenedMap = flatten(map);
    flatObject.putAll(flattenedMap);
    return flatObject;
  }

  public static FlatObject distinctFlatObject(FlatObject flatObject) {
    FlatObject distinctFlatObject = new FlatObject();
    Map<String, FlatKeyValuePair> flatKeyValueMap = flatObject.getFlatMap();
    for (FlatKeyValuePair pair : flatKeyValueMap.values()) {
      if (!(pair.getValue() instanceof Map) || ((Map<?, ?>) pair.getValue()).isEmpty()) {
        distinctFlatObject.put(pair.getKey().toString(), pair.getValue());
      }
    }
    return distinctFlatObject;
  }

  public Map<String, Object> toMap() {
    Map<String, Object> expandedMap = new HashMap<>();
    for (Map.Entry<String, FlatKeyValuePair> entry : flatMap.entrySet()) {
      expandedMap.put(entry.getKey(), entry.getValue().getValue());
    }
    return expand(expandedMap);
  }

  public Map<String, FlatKeyValuePair> getFlatMap() {
    return new HashMap<>(flatMap);
  }

  @Override
  public int size() {
    return flatMap.size();
  }

  @Override
  public boolean isEmpty() {
    return flatMap.isEmpty();
  }

  @Override
  public boolean containsKey(Object key) {
    return flatMap.containsKey(key);
  }

  @Override
  public boolean containsValue(Object value) {
    for (FlatKeyValuePair pair : flatMap.values()) {
      if (pair.getValue().equals(value)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public Object get(Object key) {
    FlatKeyValuePair pair = flatMap.get(key);
    return pair != null ? pair.getValue() : null;
  }

  @Override
  public Object put(String key, Object value) {
    FlatKeyValuePair oldPair = flatMap.put(key, new FlatKeyValuePair(key, value));
    return oldPair != null ? oldPair.getValue() : null;
  }

  @Override
  public Object remove(Object key) {
    FlatKeyValuePair removedPair = flatMap.remove(key);
    return removedPair != null ? removedPair.getValue() : null;
  }

  @Override
  public void putAll(Map<? extends String, ?> m) {
    for (Map.Entry<? extends String, ?> entry : m.entrySet()) {
      put(entry.getKey(), entry.getValue());
    }
  }

  @Override
  public void clear() {
    flatMap.clear();
  }

  @Override
  public @NotNull Set<String> keySet() {
    return flatMap.keySet();
  }

  @Override
  public @NotNull Collection<Object> values() {
    return flatMap.values().stream()
            .map(FlatKeyValuePair::getValue)
            .toList();
  }

  public static class Entry implements Map.Entry<String, Object> {
    private final String key;
    private final Object value;

    public Entry(String key, Object value) {
      this.key = key;
      this.value = value;
    }

    @Override
    public String getKey() {
      return key;
    }

    @Override
    public Object getValue() {
      return value;
    }

    @Override
    public Object setValue(Object value) {
      throw new UnsupportedOperationException("Entry is immutable");
    }
  }

  @Override
  public @NotNull Set<Map.Entry<String, Object>> entrySet() {
    return flatMap.entrySet().stream()
            .map(entry -> (Map.Entry<String, Object>) (new Entry(entry.getKey(), entry.getValue().getValue())))
            .collect(Collectors.toSet());
  }

  public List<FlatKeyValuePair> getAllParents(String key) {
    FlatKey flatKey = new FlatKey(key);
    return flatKey.getAllParents().stream()
            .filter(this::containsKey)
            .map(flatMap::get)
            .collect(Collectors.toList());
  }

  @Nullable
  public FlatKeyValuePair getDirectParent(String key) {
    FlatKey flatKey = new FlatKey(key);
    String directParentKey = flatKey.getDirectParent();
    if (directParentKey == null) {
      return null;
    }
    return flatMap.get(directParentKey);
  }

  public List<FlatKeyValuePair> getAllChildren(String key) {
    return flatMap.entrySet().stream()
            .filter(entry -> entry.getKey().startsWith(key + "."))
            .map(Map.Entry::getValue)
            .collect(Collectors.toList());
  }

  public List<FlatKeyValuePair> getDirectChildren(String key) {
    FlatKey parentKey = new FlatKey(key);
    int parentKeySize = parentKey.getSize();
    return flatMap.values().stream()
            .filter(flatKeyValuePair -> {
              FlatKey entryKey = flatKeyValuePair.getKey();
              return entryKey.toString().startsWith(parentKey.toString()) && entryKey.getSize() == parentKeySize + 1;
            })
            .collect(Collectors.toList());
  }

  public List<FlatKeyValuePair> getDirectChildren() {
    return flatMap.values().stream()
            .filter(pair -> pair.getKey().getSize() == 1)
            .collect(Collectors.toList());
  }

  public List<FlatKeyValuePair> getNeighbors(String key) {
    FlatKeyValuePair directParent = getDirectParent(key);
    if (directParent == null) {
      return new ArrayList<>();
    }
    return getDirectChildren(directParent.getKey().toString());
  }

  public static Map<String, Object> expand(Map<String, Object> input, Map<String, Object> baseObject) {
    Map<String, Object> result = new HashMap<>(baseObject);
    for (Map.Entry<String, Object> entry : input.entrySet()) {
      expandRecursive(result, splitKey(entry.getKey()), entry.getValue());
    }
    return result;
  }

  public static Map<String, Object> expand(Map<String, Object> input) {
    return expand(input, new HashMap<>());
  }

  @SuppressWarnings("unchecked")
  private static void expandRecursive(Map<String, Object> current, ArrayList<String> keys, Object value) {
    String key = keys.getFirst();
    if (keys.size() == 1) {
      current.put(key, value);
    } else {
      if (!current.containsKey(key) || !(current.get(key) instanceof Map)) {
        current.put(key, new HashMap<String, Object>());
      }
      expandRecursive((Map<String, Object>) current.get(key), new ArrayList<>(keys.subList(1, keys.size())), value);
    }
  }

  public static ArrayList<String> splitKey(String key) {
    ArrayList<String> result = new ArrayList<>();
    Pattern pattern = Pattern.compile("(?<!\\\\)\\.");
    String[] parts = pattern.split(key);
    for (String part : parts) {
      result.add(unescapeKey(part.replaceAll("^\"|\"$", "")));
    }
    return result;
  }

  private static String unescapeKey(String key) {
    return key.replace("\\\\", "\\").replace("\\\"", "\"").replace("\\.", ".");
  }

  public static Map<String, Object> flatten(Map<String, Object> input) {
    Map<String, Object> result = new HashMap<>();
    flattenRecursive(input, "", result);
    return result;
  }

  @SuppressWarnings("unchecked")
  private static void flattenRecursive(Map<String, Object> input, String prefix, Map<String, Object> result) {
    for (Map.Entry<String, Object> entry : input.entrySet()) {
      String key = entry.getKey();
      Object value = entry.getValue();

      String escapedKey = escapeKey(key);
      String newKey = prefix + "\"" + escapedKey + "\"";

      if (value instanceof Map) {
        result.put(newKey, value);
        flattenRecursive((Map<String, Object>) value, newKey + ".", result);
      } else {
        result.put(newKey, value);
      }
    }
  }

  private static String escapeKey(String key) {
    return key.replace("\\", "\\\\").replace("\"", "\\\"").replace(".", "\\.");
  }

  public Map<String, OptionChange> getChangeTo(FlatObject other) {
    Map<String, OptionChange> changes = new HashMap<>();

    // Check for new and modified keys
    for (String key : other.keySet()) {
      if (!this.containsKey(key)) {
        changes.put(key, OptionChange.NEW);
      } else if (!Objects.equals(this.get(key), other.get(key))) {
        changes.put(key, OptionChange.MODIFIED);
      } else {
        changes.put(key, OptionChange.EQUAL);
      }
    }

    // Check for deleted keys
    for (String key : this.keySet()) {
      if (!other.containsKey(key)) {
        changes.put(key, OptionChange.DELETED);
      }
    }

    return changes;
  }
}
