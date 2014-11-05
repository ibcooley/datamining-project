package Algorithms;

import AlgorithmObjects.FPTree.FPTree_Node;
import AlgorithmObjects.FPTree.FPTree_Tree;
import AlgorithmObjects.FPTree.IFPTree_Node;
import AlgorithmObjects.Shared.Order;
import Helpers.SortMap;
import Helpers.WalmartCSVReader;

import java.io.IOException;
import java.util.*;

/**
 * Created by Ben on 11/3/2014.
 * Algorithms
 * This is to include the association of commonly-purchased-together items
 */
public class FPTree_CategoricalFrequencies {

    private final List<Order> initial_orders;
    private final int minSupport;
    private Map<String, Integer> OrderedAttributes;
    private FPTree_Tree results_found;

    public FPTree_CategoricalFrequencies(List<Order> orders, int minSupport) {
        this.initial_orders = orders;
        this.minSupport = minSupport;
        this.results_found = null;
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Hello from FPTree!");

        List<Order> orders = WalmartCSVReader.GetOrders();

        FPTree_CategoricalFrequencies FP = new FPTree_CategoricalFrequencies(orders, 5);
        FP.start();
        IFPTree_Node<String> tree = FP.findFrequentItemSets();
        System.out.println(tree.toString(""));
    }

    public void start() {
        Map<String, Integer> attributeCount = new HashMap<String, Integer>();
        for (int i = 0; i < initial_orders.size(); i++) {
            List<String> items = initial_orders.get(i).getItemSet();
            for (int j = 0; j < items.size(); j++) {
                Integer count = 0;
                if (attributeCount.containsKey(items.get(j))) {
                    count = attributeCount.get(items.get(j));
                }
                count++;
                attributeCount.put(items.get(j), count);
            }
        }

        String[] items = attributeCount.keySet().toArray(new String[attributeCount.keySet().size()]);
        for (int i = 0; i < items.length; i++) {
            if (attributeCount.containsKey(items[i])) {
                if (attributeCount.get(items[i]) < minSupport) {
                    attributeCount.remove(items[i]);
                    i--;
                }
            }
        }

        OrderedAttributes = SortMap.GetMapSortedByValue(attributeCount, SortMap.SortOrder.DESC);
    }

    public IFPTree_Node findFrequentItemSets() {
        if (results_found != null) return results_found;
        IFPTree_Node<String> root = new FPTree_Tree<String>();
        IFPTree_Node<String> currentNode, parentNode;

        //foreach row of the data
        for (int i = 0; i < initial_orders.size(); i++) {
            List<String> row = initial_orders.get(i).getItemSet();
            ArrayList<String> orderedRowAttributes = new ArrayList<String>(GetOrderedAttributesForRow(row));
            currentNode = null;

            for (String attribute : orderedRowAttributes) {

                //if null, set root as currentNode.
                if (currentNode == null) {
                    currentNode = root;
                }
                if (currentNode.getNodes().containsKey(attribute)) {
                    currentNode = currentNode.getNodes().get(attribute);
                } else {
                    FPTree_Node node = new FPTree_Node(currentNode, attribute);
                    currentNode.getNodes().put(attribute, node);
                    currentNode = node;
                }

                currentNode.setCount(currentNode.getCount() + 1);
            }
        }
        root.Prune(minSupport);
        return root;
    }

    protected Set<String> GetOrderedAttributesForRow(List<String> rowData) {
        Map<String, Integer> matchedColumns = new HashMap<String, Integer>();
        for (int attrNum = 0; attrNum < rowData.size(); attrNum++) {
            String attr = rowData.get(attrNum);
            if (OrderedAttributes.containsKey(attr)) {
                matchedColumns.put(attr, OrderedAttributes.get(attr));
            }
        }

        return SortMap.GetMapSortedByValue(matchedColumns, SortMap.SortOrder.DESC).keySet();
    }
}
