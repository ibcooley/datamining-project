package Algorithms;

import AlgorithmObjects.FPTree.FPTree_Node;
import AlgorithmObjects.FPTree.FPTree_Tree;
import AlgorithmObjects.FPTree.IFPTree_Node;
import AlgorithmObjects.Shared.OrdersTable;
import AlgorithmObjects.Shared.RowColumnPair;
import Helpers.SortMap;
import Helpers.TimeAndMemoryRecorder;

import java.io.PrintWriter;
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
    private TimeAndMemoryRecorder recorder;

    public FPTree(OrdersTable orders, int minSupport) {
        this.initial_orders = orders;
        this.minSupport = minSupport;
        this.results_found = null;
        recorder = new TimeAndMemoryRecorder();
    }

    public static void main (String[] args) throws Exception {
        System.out.println("Hello from FPTree!");

        OrdersTable table = new OrdersTable("./in/walmart_sorted.csv");
        FPTree FP = new FPTree(table, 200);
        FP.start();
        FPTree_Tree<String> tree = FP.findFrequentItemSets();

        PrintWriter pw = new PrintWriter("FPTree_frequent_itemsets.txt");
        pw.print(tree.toString(200));
        pw.close();

        String timeMemoryResults = "elapsed time, used memory, frequent itemset count\n";
        for (int i = 0; i < FP.recorder.getTimes().size(); i++) {
            String line = FP.recorder.getTimes().get(i) + ", " + FP.recorder.getMemories().get(i) + ", " + FP.recorder.getFrequentItemsetCounts().get(i) + "\n";
            timeMemoryResults += line;
        }

        PrintWriter pw2 = new PrintWriter("FPTree_time_memory_results.txt");
        pw2.print(timeMemoryResults);
        pw2.close();
        System.out.println(tree.toString(200));
    }

    public TimeAndMemoryRecorder getRecorder () {
        return recorder;
    }

    public void start () throws Exception {
        this.recorder.start();
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
                if (attributeCount.get(pairs[i]) <= minSupport) {
                    attributeCount.remove(pairs[i]);
                    i--;
                }
            }
        }

        OrderedAttributes = SortMap.GetMapSortedByValue(attributeCount, SortMap.SortOrder.DESC);
        recorder.poll(0);
    }

    public FPTree_Tree findFrequentItemSets () {
        if (results_found != null) return results_found;
        FPTree_Tree<RowColumnPair> root = new FPTree_Tree<RowColumnPair>();
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
                    FPTree_Node node = new FPTree_Node(root, currentNode, attribute);
                    currentNode.getNodes().put(attribute, node);
                    currentNode = node;
                    root.fixNodes(currentNode);
                    recorder.poll(root.getPatternCount());
                }


            }
            if (currentNode != null) {
                currentNode.updateCount();
            }

        }
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
