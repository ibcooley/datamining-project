package Algorithms;

import AlgorithmObjects.Shared.Association;
import AlgorithmObjects.Shared.AssociationFinder;
import AlgorithmObjects.Shared.Itemset;
import AlgorithmObjects.Shared.Order;
import Helpers.ListHelpers;
import Helpers.TimeAndMemoryRecorder;
import Helpers.WalmartCSVReader;
import com.sun.deploy.util.StringUtils;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Apriori {

    private List<Order> orders;
    private int minSupport;
    private int k;
    private TimeAndMemoryRecorder recorder;
    private List<Itemset<String>> frequentItemsets;

    public Apriori(List<Order> orders, int minSupport) {
        this.orders = orders;
        this.minSupport = minSupport;
        recorder = new TimeAndMemoryRecorder();
        frequentItemsets = new  ArrayList<Itemset<String>>();
    }

    public static void main(String[] args) throws Exception {
        List<Order> orders = WalmartCSVReader.GetOrders();
        Apriori apriori = new Apriori(orders, 5);
        apriori.start();
    }

    public void start() throws Exception {
        recorder.start();

        List<Itemset<String>> frequentKMinusOneItemsets = findFrequentOneItemsets();
        frequentItemsets.addAll(frequentKMinusOneItemsets);
        recorder.poll(frequentItemsets.size());

        for (k = 2; frequentKMinusOneItemsets.size() != 0; k++) {
            List<Itemset<String>> c_k = aprioriGen(frequentKMinusOneItemsets);

            for (Order order : orders) {
                List<String> orderItems = order.getItemSet();

                for (Itemset<String> candidateItemset : c_k) {
                    List<String> candidateItems = candidateItemset.getItemSet();

                    if (orderItems.containsAll(candidateItems)) {
                        candidateItemset.incrementSupportCount();
                    }
                }
            }

            frequentKMinusOneItemsets = new ArrayList<Itemset<String>>();
            for (Itemset<String> candidateItemset : c_k) {
                if (candidateItemset.getSupportCount() >= minSupport) {
                    frequentKMinusOneItemsets.add(candidateItemset);
                }
            }

            frequentItemsets.addAll(frequentKMinusOneItemsets);
        }

        recorder.poll(frequentItemsets.size());

        String frequentItemsetResults = "";
        for (Itemset<String> itemset : frequentItemsets) {
            String line = "";
            line += String.format("%1$5s", itemset.getSupportCount()) + ":\t";
            line += StringUtils.join(itemset.getItemSet(), ", ") + "\n";
            frequentItemsetResults += line;
        }
        PrintWriter pw = new PrintWriter("apriori_frequent_itemsets.txt");
        pw.print(frequentItemsetResults);
        pw.close();

        String timeMemoryResults = "elapsed time, used memory, frequent itemset count\n";
        for (int i = 0; i < recorder.getTimes().size(); i++) {
            String line = recorder.getTimes().get(i) + ", " + recorder.getMemories().get(i) + ", " + recorder.getFrequentItemsetCounts().get(i) + "\n";
            timeMemoryResults += line;
        }
        PrintWriter pw2 = new PrintWriter("apriori_time_memory_results.txt");
        pw2.print(timeMemoryResults);
        pw2.close();

        AssociationFinder associationFinder = new AssociationFinder(orders, .10); // 10% min confidence
        associationFinder.find(frequentItemsets);
        associationFinder.printToFile();
    }

    private List<Itemset<String>> findFrequentOneItemsets() {
        List<Itemset<String>> frequentOneItemsets = new ArrayList<Itemset<String>>();
        TreeMap<String, Integer> c1TreeMap = new TreeMap<String, Integer>();

        // Fill in the c1 tree map
        for (Order order : orders) {
            for (String item : order.getItemSet()) {
                if (c1TreeMap.containsKey(item)) {
                    c1TreeMap.put(item, c1TreeMap.get(item) + 1);
                } else {
                    c1TreeMap.put(item, 1);
                }
            }
        }

        // Convert c1TreeMap to list of frequent itemset
        for (String key : c1TreeMap.keySet()) {
            if (c1TreeMap.get(key) >= minSupport) {
                Itemset<String> frequentItemset = new Itemset<String>();
                frequentItemset.getItemSet().add(key);
                frequentItemset.setSupportCount(c1TreeMap.get(key));
                frequentOneItemsets.add(frequentItemset);
            }
        }

        return frequentOneItemsets;
    }

    // Cross-joins L with itself and prunes
    private List<Itemset<String>> aprioriGen(List<Itemset<String>> frequentKMinusOneItemsets) {
        List<Itemset<String>> candidates = new ArrayList<Itemset<String>>();
        for (Itemset<String> itemset1 : frequentKMinusOneItemsets) {
            for (Itemset<String> itemset2 : frequentKMinusOneItemsets) {
                boolean kToKMinusOneAreEqual = true;
                for (int i = 0; kToKMinusOneAreEqual && i < itemset1.getItemSet().size() - 1; i++) {
                    if (itemset1.getItemSet().get(i).compareToIgnoreCase(itemset2.getItemSet().get(i)) != 0) {
                        kToKMinusOneAreEqual = false;
                    }
                }
                if (kToKMinusOneAreEqual && itemset1.getItemSet().get(itemset1.getItemSet().size() - 1).compareToIgnoreCase(itemset2.getItemSet().get(itemset1.getItemSet().size() - 1)) < 0) {
                    // Combine the lists - the last index should be the only one that's different
                    Itemset<String> newItemset = new Itemset<String>();
                    if (k == 2) {
                        newItemset.getItemSet().add(itemset1.getItemSet().get(0));
                        newItemset.getItemSet().add(itemset2.getItemSet().get(0));
                    } else {
                        newItemset.getItemSet().addAll(itemset1.getItemSet().subList(0, itemset1.getItemSet().size()));
                        newItemset.getItemSet().add(itemset2.getItemSet().get(itemset1.getItemSet().size() - 1));
                    }

                    // Check candidate subsets for infrequent
                    if (!hasInfrequentSubset(newItemset, frequentKMinusOneItemsets)) {
                        candidates.add(newItemset);
                    }
                }
            }

            recorder.poll(frequentItemsets.size());
        }

        return candidates;
    }

    private boolean hasInfrequentSubset(Itemset<String> c, List<Itemset<String>> frequentKMinusOneItemsets) {
        List<List<String>> kMinusOneSubsets = ListHelpers.getSubsets(c.getItemSet(), k - 1);
        for (List<String> subset : kMinusOneSubsets) {
            boolean found = false;
            for (Itemset<String> itemset : frequentKMinusOneItemsets) {
                if (itemset.getItemSet().equals(subset)) {
                    found = true;
                }
            }
            if (!found) {
                return true;
            }
        }
        return false;
    }
}

