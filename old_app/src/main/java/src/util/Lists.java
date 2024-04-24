package src.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Lists {

  public static <T> T choose(List<T> array, Random rng) {
    return array.get(rng.nextInt(array.size()));
  }

  public static boolean found(List<?> array) {
    return array.size() > 0;
  }

  public static <T> List<T> create(int size, T value) {
    List<T> list = new ArrayList<T>();
    for (int i = 0; i < size; i++) list.add(value);
    return list;
  }
}
