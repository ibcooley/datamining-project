package Algorithms;

import AlgorithmObjects.FPTree.FPTree_Node;
import AlgorithmObjects.FPTree.FPTree_Tree;
import AlgorithmObjects.FPTree.IFPTree_Node;
import AlgorithmObjects.Shared.OrdersTable;
import AlgorithmObjects.Shared.RowColumnPair;
import Helpers.SortMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Ben on 10/22/2014.
 * FPTree
 */
public class FPTree {

    private final OrdersTable initial_orders;
    private final int minSupport;
    private Map<RowColumnPair, Integer> OrderedAttributes;
    private FPTree_Tree results_found;

    public FPTree(OrdersTable orders, int minSupport) {
        this.initial_orders = orders;
        this.minSupport = minSupport;
        this.results_found = null;
    }

    public void start() {
        Map<RowColumnPair, Integer> attributeCount = new HashMap<RowColumnPair, Integer>();
        for (int i = 0; i < initial_orders.getColumnCount(); i++) {
            for (int j = 0; j < initial_orders.getRowCount(); j++) {
                RowColumnPair pair = new RowColumnPair(initial_orders.getColumnName(i), initial_orders.getValueAt(j, i));
                Integer count = 0;
                if (attributeCount.containsKey(pair)) {
                    count = attributeCount.get(pair);
                }
                count++;
                attributeCount.put(pair, count);
            }
        }

        RowColumnPair[] pairs = attributeCount.keySet().toArray(new RowColumnPair[attributeCount.keySet().size()]);
        for (int i = 0; i < pairs.length; i++) {
            if (attributeCount.containsKey(pairs[i])) {
                if (attributeCount.get(pairs[i]) < minSupport) {
                    attributeCount.remove(pairs[i]);
                    i--;
                }
            }
        }

        OrderedAttributes = SortMap.GetMapSortedByValue(attributeCount, SortMap.SortOrder.DESC);
    }

    public IFPTree_Node findFrequentItemSets() {
        if (results_found != null) return results_found;
        IFPTree_Node<RowColumnPair> root = new FPTree_Tree<RowColumnPair>();
        IFPTree_Node<RowColumnPair> currentNode;

        //foreach row of the data
        for (String[] row : initial_orders.getRowData()) {
            ArrayList<RowColumnPair> orderedRowAttributes = new ArrayList<RowColumnPair>(GetOrderedAttributesForRow(row));
            currentNode = null;

            for (RowColumnPair attribute : orderedRowAttributes) {

                //if null, set root as currentNode.
                if (currentNode == null) {
                    currentNode = root;
                }
                if (currentNode.getNodes().containsKey(attribute)) {
                    currentNode = currentNode.getNodes().get(attribute);
                } else {
                    FPTree_Node node = new FPTree_Node();
                    currentNode.getNodes().put(attribute, new FPTree_Node());
                    currentNode = node;
                }

                currentNode.setCount(currentNode.getCount() + 1);
            }
        }
        root.Prune(minSupport);
        return root;
    }

    protected Set<RowColumnPair> GetOrderedAttributesForRow(String[] rowData) {
        Map<RowColumnPair, Integer> matchedColumns = new HashMap<RowColumnPair, Integer>();
        for (int attrNum = 0; attrNum < rowData.length; attrNum++) {
            RowColumnPair attr = new RowColumnPair(initial_orders.getColumnName(attrNum), rowData[attrNum]);
            if (OrderedAttributes.containsKey(attr)) {
                matchedColumns.put(attr, OrderedAttributes.get(attr));
            }
        }

        return SortMap.GetMapSortedByValue(matchedColumns, SortMap.SortOrder.DESC).keySet();
    }
}
