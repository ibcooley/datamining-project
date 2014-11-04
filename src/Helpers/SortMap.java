package Helpers;

import java.util.*;

public class SortMap {
    public static <K, V extends Comparable<V>> Map<K, V> GetMapSortedByValue(Map<K, V> initialMap) {
        return GetMapSortedByValue(initialMap, SortOrder.ASC);
    }

    public static <K, V extends Comparable<V>> Map<K, V> GetMapSortedByValue(Map<K, V> initialMap, final SortOrder order) {
        List<Map.Entry<K, V>> list = new LinkedList(initialMap.entrySet());


        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                if (order == SortMap.SortOrder.ASC) {
                    return ((Comparable) o1.getValue()).compareTo(o2.getValue());
                }
                return ((Comparable) o2.getValue()).compareTo(o1.getValue());
            }
        });
        Map<K, V> sortedMap = new LinkedHashMap();
        for (Map.Entry<K, V> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    public static enum SortOrder {
        ASC, DESC;

        private SortOrder() {
        }
    }
}
