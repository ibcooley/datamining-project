package Algorithms;

import Helpers.SortMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ben on 10/22/2014.
 */
class FPTree {

    private final OrdersTable initial_orders;
    private final int minSupport;
    private Map<RowColumnPair, Integer> OrderedAttributes;

    public FPTree(OrdersTable orders, int minSupport) {
        this.initial_orders = orders;
        this.minSupport = minSupport;
    }

    public void start() {
        Map<RowColumnPair, Integer> attributeCount = new HashMap<RowColumnPair, Integer>();
        for (int i = 0; i < initial_orders.getColumnCount(); i++) {
            for (int j = 0; j < initial_orders.getRowCount(); j++) {
                RowColumnPair pair = new RowColumnPair(initial_orders.getColumnName(i), initial_orders.getValueAt(i, j));
                Integer count = 0;
                if (attributeCount.containsKey(pair)) {
                    count = attributeCount.get(pair);
                }
                count++;
                attributeCount.put(pair, count);
            }
        }

        RowColumnPair[] pairs = (RowColumnPair[]) attributeCount.keySet().toArray();
        for (int i = 0; i < pairs.length; i++) {
            if (attributeCount.get(pairs[i]) < minSupport) {
                attributeCount.remove(pairs[i]);
                i--;
            }
        }

        OrderedAttributes = SortMap.GetMapSortedByValue(attributeCount, SortMap.SortOrder.DESC);
    }

    public FPTree_Tree findFrequentItemSets() {
        //TODO: Implement tree algorithm
        OrderedAttributes.size();
        return null;
    }


}
