package fr.raconteur.sbcou.flatobject;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import oshi.annotation.concurrent.Immutable;

import java.util.*;
import java.util.regex.Pattern;

@Immutable
public class FlatKey {
    private @NotNull final String key;
    private static final Map<String, ArrayList<String>> splitKeyCache = new HashMap<>();
    private static final Map<String, String> unescapeKeyCache = new HashMap<>();
    private static final Pattern pattern = Pattern.compile("(?<!\\\\)\\.");
    public static FlatKey EMPTY = new FlatKey("");

    public FlatKey(@NotNull String key) {
        this.key = key;
    }

    private ArrayList<String> rawSplitKey() {
        return splitKeyCache.computeIfAbsent(key, k -> {
            ArrayList<String> result = new ArrayList<>();
            String[] parts = pattern.split(k);
            Collections.addAll(result, parts);
            return result;
        });
    }

    @NotNull
    public ArrayList<String> splitKey() {
        ArrayList<String> rawParts = rawSplitKey();
        ArrayList<String> result = new ArrayList<>();
        for (String part : rawParts) {
            result.add(unescapeKey(part.replaceAll("^\"|\"$", "")));
        }
        return result;
    }

    private static String unescapeKey(String key) {
        return unescapeKeyCache.computeIfAbsent(key, k ->
            k.replace("\\\\", "\\").replace("\\\"", "\"").replace("\\.", ".")
        );
    }

    @NotNull
    public String getElementAt(int index) {
        ArrayList<String> splitKey = splitKey();
        if (index >= 0 && index < splitKey.size()) {
            return splitKey.get(index);
        }
        throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for the split key.");
    }

    public int getSize() {
        return key.isEmpty() ? 0 : splitKey().size();
    }

    @NotNull
    public List<String> getAllParents() {
        ArrayList<String> keys = rawSplitKey();
        List<String> parents = new ArrayList<>();
        for (int i = 1; i < keys.size(); i++) {
            ArrayList<String> subList = new ArrayList<>(keys.subList(0, i));
            parents.add(String.join(".", subList));
        }
        return parents;
    }

    @NotNull
    public List<FlatKey> getAllParentsAsFlatKeys() {
        ArrayList<String> parentStrings = new ArrayList<>(getAllParents());
        List<FlatKey> parents = new ArrayList<>();
        for (String parent : parentStrings) {
            parents.add(new FlatKey(parent));
        }
        return parents;
    }

    @Nullable
    public String getDirectParent() {
        ArrayList<String> keys = rawSplitKey();
        if (keys.size() > 1) {
            return String.join(".", keys.subList(0, keys.size() - 1));
        }
        return null;
    }

    @Override
    public String toString() {
        return key;
    }

    public String getName() {
        return splitKey().getLast();
    }

    public boolean isEmpty() {
        return key.isEmpty();
    }
}