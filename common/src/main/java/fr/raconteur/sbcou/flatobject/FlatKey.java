package fr.raconteur.sbcou.flatobject;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Pattern;

public class FlatKey implements Comparable<FlatKey> {
    private static final String rawKeyPattern = "^(?:(?:\"(?:\\\\[\"\\\\.]|[^\\\\.\"])*\"\\.)*\"(?:\\\\[\"\\\\.]|[^\\\\.\"])*\"$)?";
    private static final Pattern splitPattern = Pattern.compile("(?<!\\\\)\\.");
    private static final String doubleQuotesSuffixAndPrefixOfKeyPartPattern = "^\"|\"$";
    private static final Map<String, FlatKey> keyCache = new HashMap<>();
    public static FlatKey ROOT = new FlatKey("");

    private @NotNull final String formattedKey;
    private @NotNull final List<String> rawParts;
    private @NotNull final List<String> parts;
    private @Nullable final FlatKey directParent;
    private @NotNull final List<FlatKey> allParents;

    private FlatKey(@NotNull String formattedKey) {
        if (!Pattern.matches(rawKeyPattern, formattedKey)) {
            throw new IllegalArgumentException(formattedKey);
        }
        this.formattedKey = formattedKey;
        this.rawParts = rawSplitKey();
        this.parts = splitKey();
        this.directParent = getInitDirectParent();
        this.allParents = getInitAllParents();
        keyCache.put(formattedKey, this);
    }

    public @NotNull String getFormattedKey() {
        return formattedKey;
    }

    @NotNull
    public List<String> getRawParts() {
        return rawParts;
    }

    @NotNull
    public List<String> getParts() {
        return parts;
    }

    @Nullable
    public FlatKey getDirectParent() {
        return directParent;
    }

    @NotNull
    public List<FlatKey> getAllParents() {
        return allParents;
    }

    public int getSize() {
        return isEmpty() ? 0 : parts.size();
    }

    public String getName() {
        return splitKey().getLast();
    }

    public boolean isEmpty() {
        return formattedKey.isEmpty();
    }

    private List<String> rawSplitKey() {
        ArrayList<String> result = new ArrayList<>();
        if (isEmpty()) {
            return ImmutableList.copyOf(result);
        }
        String[] rawParts = splitPattern.split(formattedKey);
        Collections.addAll(result, rawParts);
        return ImmutableList.copyOf(result);
    }

    @NotNull
    private List<String> splitKey() {
        ArrayList<String> result = new ArrayList<>();
        if (isEmpty()) {
            return ImmutableList.copyOf(result);
        }
        for (String part : rawParts) {
            result.add(unQuoteRawPartToPart(part));
        }
        return ImmutableList.copyOf(result);
    }

    private static String unQuoteRawPartToPart(String key) {
        return key.replaceAll(doubleQuotesSuffixAndPrefixOfKeyPartPattern, "").replace("\\\\", "\\").replace("\\\"", "\"").replace("\\.", ".");
    }

    private static String quotePartToRawPart(String part) {
        return "\"" + part.replace("\\", "\\\\").replace("\"", "\\\"").replace(".", "\\.") + "\"";
    }

    @Nullable
    private FlatKey getInitDirectParent() {
        List<String> keys = rawParts;
        if (keys.size() > 1) {
            return getFlatKeyFromFormattedKey(String.join(".", keys.subList(0, keys.size() - 1)));
        }
        return null;
    }

    @NotNull
    private List<FlatKey> getInitAllParents() {
        List<FlatKey> parents = new ArrayList<>();
        if (directParent != null) {
            parents.add(directParent);
            parents.addAll(directParent.allParents);
        }
        return ImmutableList.copyOf(parents);
    }

    public static FlatKey getFlatKeyFromFormattedKey(String key) {
        if (key.isEmpty()) {
            return ROOT;
        }
        if (keyCache.containsKey(key)) {
            return keyCache.get(key);
        } else {
            return new FlatKey(key);
        }
    }

    public static FlatKey getSinglePartFlatKeyFromString(String keyPart) {
        String rawPartKey = quotePartToRawPart(keyPart);
        if (keyCache.containsKey(rawPartKey)) {
            return keyCache.get(rawPartKey);
        } else {
            return new FlatKey(rawPartKey);
        }
    }

    public FlatKey getChild(FlatKey child) {
        List<String> rawChildPart = child.getRawParts();
        List<String> newRawPartsList = new ArrayList<>() {
            {
                addAll(rawParts);
                addAll(rawChildPart);
            }
        };
        if (newRawPartsList.isEmpty()) {
            throw new RuntimeException("Created child FlatKey but got ROOT");
        }
        return getFlatKeyFromFormattedKey(String.join(".", newRawPartsList));
    }

    public FlatKey getWithoutFirstPart() {
        if (isEmpty() || rawParts.size() <= 1) {
            throw new RuntimeException("Cannot drop first part from ROOT or single-part FlatKey");
        }
        return getFlatKeyFromFormattedKey(String.join(".", rawParts.subList(1, rawParts.size())));
    }

    @Override
    public int compareTo(@NotNull FlatKey o) {
        int lenCmp = Integer.compare(this.formattedKey.length(), o.formattedKey.length());
        if (lenCmp != 0) return lenCmp;
        return this.formattedKey.compareTo(o.formattedKey);
    }
}
