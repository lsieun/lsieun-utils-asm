package lsieun.utils.ds.pair;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.function.Function;

import static java.util.stream.Collectors.*;

public class PairBuddy {
    public static <T, S, K, V> void printByGroup(List<Pair<T, S>> pairList,
                                                 Function<Pair<T,S>, K> keyFunc,
                                                 Function<Pair<T,S>, V> valueFunc) {
        Map<K, List<V>> map = pairList.stream()
                .collect(groupingBy(keyFunc, HashMap::new, mapping(valueFunc, toList())));
        map.forEach((key, list) -> {
            System.out.println(key);
            for (V value : list) {
                System.out.println("    " + value);
            }
        });
    }
}
