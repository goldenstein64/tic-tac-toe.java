package src.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class BoardCountCollector
  implements Collector<Integer, ArrayList<Integer>, List<Integer>> {

  @Override
  public Supplier<ArrayList<Integer>> supplier() {
    return () -> {
      ArrayList<Integer> result = new ArrayList<>(9);
      for (int i = 0; i < 9; i++) result.add(0);
      return result;
    };
  }

  @Override
  public BiConsumer<ArrayList<Integer>, Integer> accumulator() {
    return (list, value) -> list.set(value, list.get(value) + 1);
  }

  @Override
  public BinaryOperator<ArrayList<Integer>> combiner() {
    return (list1, list2) -> {
      assert list1.size() == 9 : "list1.size() != 9";
      assert list2.size() == 9 : "list2.size() != 9";
      for (int i = 0; i < 9; i++) {
        list1.set(i, list1.get(i) + list2.get(i));
      }
      return list1;
    };
  }

  @Override
  public Function<ArrayList<Integer>, List<Integer>> finisher() {
    return list -> List.copyOf(list);
  }

  @Override
  public Set<Collector.Characteristics> characteristics() {
    return Set.of(Collector.Characteristics.IDENTITY_FINISH);
  }
}
