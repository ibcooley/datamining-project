package Algorithms;

import AlgorithmObjects.Shared.Itemset;
import AlgorithmObjects.Shared.Order;
import au.com.bytecode.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Apriori {

    private List<Order> orders;
    private int minSupport;
    private int k;

    public Apriori(List<Order> orders, int minSupport) {
        this.orders = orders;
        this.minSupport = minSupport;
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Hello World!");

        CSVReader reader = new CSVReader(new FileReader("./in/walmart.csv"));
        String[] nextLine;
        nextLine = reader.readNext(); // header

        List<Order> orders = new ArrayList<Order>();
        Order currentOrder = new Order();
        while ((nextLine = reader.readNext()) != null) {
            // 1 - Order ID
            // 18 - Product Sub-Category
            // 19 - Product Name
            // System.out.println(nextLine[1] + " " + nextLine[19]);

            String orderID = nextLine[1];
            String productSubCategory = nextLine[18];
            if (!orderID.equalsIgnoreCase(currentOrder.getOrderID())) {
                if (currentOrder.getOrderID().length() > 0) {
                    orders.add(currentOrder);
                }
                currentOrder = new Order();
                currentOrder.setOrderID(orderID);
            }

            // Always add the product to the item set
            currentOrder.getItemSet().add(productSubCategory);
        }

        Apriori apriori = new Apriori(orders, 5);
        apriori.start();
    }

    public void start() {
        List<Itemset<String>> frequentKMinusOneItemsets = findFrequentOneItemsets();
        List<Itemset<String>> frequentItemsets = new ArrayList<Itemset<String>>(frequentKMinusOneItemsets);

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

        System.out.println(frequentItemsets);
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
        }

        return candidates;
    }

    private boolean hasInfrequentSubset(Itemset<String> c, List<Itemset<String>> frequentKMinusOneItemsets) {
        List<List<String>> kMinusOneSubsets = getSubsets(c.getItemSet(), k - 1);
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

    // http://stackoverflow.com/questions/12548312/find-all-subsets-of-length-k-in-an-array
    private void getSubsets(List<String> superSet, int k, int idx, List<String> current, List<List<String>> solution) {
        //successful stop clause
        if (current.size() == k) {
            solution.add(new ArrayList<String>(current));
            return;
        }
        //unsuccessful stop clause
        if (idx == superSet.size()) return;
        String x = superSet.get(idx);
        current.add(x);
        //"guess" x is in the subset
        getSubsets(superSet, k, idx + 1, current, solution);
        current.remove(x);
        //"guess" x is not in the subset
        getSubsets(superSet, k, idx + 1, current, solution);
    }

    private List<List<String>> getSubsets(List<String> superSet, int k) {
        List<List<String>> res = new ArrayList<List<String>>();
        getSubsets(superSet, k, 0, new ArrayList<String>(), res);
        return res;
    }
}

