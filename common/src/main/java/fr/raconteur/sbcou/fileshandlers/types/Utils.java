package fr.raconteur.sbcou.fileshandlers.types;

import java.math.BigDecimal;
import java.util.Map;

public class Utils {
  public static boolean isValidType(Object obj) {
    return obj instanceof String ||
            obj instanceof java.math.BigInteger ||
            obj instanceof java.math.BigDecimal ||
            obj instanceof Boolean ||
            obj instanceof StrictlyComparableArrayList ||
            obj instanceof StrictlyComparableMap ||
            obj instanceof Byte[] ||
            obj instanceof Double && ((Double) obj).isNaN() ||
            obj instanceof Float && ((Float) obj).isNaN() ||
            obj == null;
  }

  @SuppressWarnings("unchecked")
  public static Object toValidType(Object obj) {
    switch (obj) {
      case String s -> {
        return s;
      }
      case java.math.BigInteger bigInteger -> {
        return bigInteger;
      }
      case Integer i -> {
        return java.math.BigInteger.valueOf(i);
      }
      case Long l -> {
        return java.math.BigInteger.valueOf(l);
      }
      case Double d -> {
        if (d.isNaN()) {
          return d;
        }
        return java.math.BigDecimal.valueOf(d);
      }
      case java.math.BigDecimal bigDecimal -> {
        return bigDecimal;
      }
      case Boolean b -> {
        return b;
      }
      case StrictlyComparableArrayList strictlyComparableArrayList -> {
        return strictlyComparableArrayList;
      }
      case StrictlyComparableMap strictlyComparableMap -> {
        return strictlyComparableMap;
      }
      case Byte[] bytes -> {
        return bytes;
      }
      case null -> {
        return null;
      }
      case Map<?, ?> map1 -> {
        StrictlyComparableMap map = new StrictlyComparableMap();
        map.putAll((Map<? extends String, ?>) map1);
        return map;
      }
      case java.util.List<?> objects -> {
        StrictlyComparableArrayList list = new StrictlyComparableArrayList();
        list.addAll(objects);
        return list;
      }
      default -> throw new RuntimeException("Invalid type in Utils.toValidType, type: " + obj.getClass().getName());
    }
  }
}
