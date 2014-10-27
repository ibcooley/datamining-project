package Helpers;

import java.util.*;

/**
 * Created by Ben on 10/25/2014.
 * SortMap
 */
public class SortMap {

    public static <K, V extends Comparable<V>> Map<K, V> GetMapSortedByValue(Map<K, V> initialMap) {
        return GetMapSortedByValue(initialMap, SortOrder.ASC);
    }

    public static <K, V extends Comparable<V>> Map<K, V> GetMapSortedByValue(Map<K, V> initialMap, SortOrder order) {
        List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(initialMap.entrySet());

        final SortOrder _order = order;
        // Sorting the list based on values
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> o1,
                               Map.Entry<K, V> o2) {
                if (_order == SortOrder.ASC) {
                    return o1.getValue().compareTo(o2.getValue());
                } else {
                    return o2.getValue().compareTo(o1.getValue());
                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<K, V> sortedMap = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    public enum SortOrder {
        ASC,
        DESC
    }
}
