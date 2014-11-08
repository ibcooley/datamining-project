package Algorithms;

import AlgorithmObjects.Shared.EquivalenceClass;
import AlgorithmObjects.Shared.Order;
import AlgorithmObjects.Shared.VerticalItemset;
import Helpers.TimeAndMemoryRecorder;
import Helpers.WalmartCSVReader;
import com.sun.deploy.util.StringUtils;

import java.io.PrintWriter;
import java.util.*;

public class ECLAT {

    private List<Order> orders;
    private int minSupport;
    private List<VerticalItemset<String, String>> frequentItemsets;
    private TimeAndMemoryRecorder recorder;

    public ECLAT(List<Order> orders, int minSupport) {
        this.orders = orders;
        this.minSupport = minSupport;
        frequentItemsets = new ArrayList<VerticalItemset<String, String>>();
        recorder = new TimeAndMemoryRecorder();
    }

    public void start() throws Exception {
        recorder.start();

        List<VerticalItemset<String, String>> frequentKMinusOneItemsets = findFrequentOneItemsets();
        frequentItemsets.addAll(frequentKMinusOneItemsets);
        recorder.poll(frequentItemsets.size());
        EquivalenceClass<String, String> ec = new EquivalenceClass<String, String>(new ArrayList<String>(), frequentKMinusOneItemsets);
        eclatAlgorithm(ec);

        String frequentItemsetResults = "";
        for (VerticalItemset<String, String> itemset : frequentItemsets) {
            String line = "";
            line += String.format("%1$5s", itemset.getTransactionIDSet().size()) + ":\t";
            line += StringUtils.join(itemset.getItemset(), ", ") + "\n";
            frequentItemsetResults += line;
        }
        PrintWriter pw = new PrintWriter("eclat_frequent_itemsets.txt");
        pw.print(frequentItemsetResults);
        pw.close();

        String timeMemoryResults = "elapsed time, used memory, frequent itemset count\n";
        for (int i = 0; i < recorder.getTimes().size(); i++) {
            String line = recorder.getTimes().get(i) + ", " + recorder.getMemories().get(i) + ", " + recorder.getFrequentItemsetCounts().get(i) + "\n";
            timeMemoryResults += line;
        }
        PrintWriter pw2 = new PrintWriter("eclat_time_memory_results.txt");
        pw2.print(timeMemoryResults);
        pw2.close();
    }

    private List<VerticalItemset<String, String>> findFrequentOneItemsets() {
        TreeMap<String, List<String>> verticalTreeMap = new TreeMap<String, List<String>>();
        List<VerticalItemset<String, String>> verticalItemsets = new ArrayList<VerticalItemset<String, String>>();

        for (Order order : orders) {
            String orderID = order.getOrderID();

            for (String item : order.getItemSet()) {
                if (verticalTreeMap.containsKey(item)) {
                    List<String> transactions = verticalTreeMap.get(item);
                    transactions.add(orderID);
                    //verticalTreeMap.put(item, transactions);
                } else {
                    List<String> transactions = new ArrayList<String>();
                    transactions.add(orderID);
                    verticalTreeMap.put(item, transactions);
                }
            }
        }

        for (String key : verticalTreeMap.keySet()) {
            if (verticalTreeMap.get(key).size() >= minSupport) {
                List<String> itemset = new ArrayList<String>();
                itemset.add(key);
                verticalItemsets.add(new VerticalItemset<String, String>(itemset, verticalTreeMap.get(key)));
            }
        }

        return verticalItemsets;
    }

    private void eclatAlgorithm(EquivalenceClass<String, String> ec) {
        for (int i = 0; i < ec.getVerticalItemsets().size(); i++) {
            VerticalItemset<String, String> vItemset1 = ec.getVerticalItemsets().get(i);

            List<String> p = new ArrayList<String>(ec.getPrefix());
            p = union(p, vItemset1.getItemset());

            EquivalenceClass<String, String> ecPrime = new EquivalenceClass<String, String>(p);

            for (int j = i + 1; j < ec.getVerticalItemsets().size(); j++) {
                VerticalItemset<String, String> vItemset2 = ec.getVerticalItemsets().get(j);
                List<String> tmpT = intersection(vItemset1.getTransactionIDSet(), vItemset2.getTransactionIDSet());
                if (tmpT.size() >= minSupport) {
                    VerticalItemset<String, String> newVItemset = new VerticalItemset<String, String>();
                    newVItemset.getItemset().addAll(p);
                    newVItemset.getItemset().add(vItemset2.getItemset().get(vItemset2.getItemset().size() - 1));
                    newVItemset.getTransactionIDSet().addAll(tmpT);
                    ecPrime.getVerticalItemsets().add(newVItemset);
                    frequentItemsets.add(newVItemset);
                }
            }

            if (ecPrime.getVerticalItemsets().size() != 0) {
                eclatAlgorithm(ecPrime);
            }
        }

        recorder.poll(frequentItemsets.size());
    }

    public <T> List<T> union(List<T> list1, List<T> list2) {
        Set<T> set = new TreeSet<T>();
        set.addAll(list1);
        set.addAll(list2);
        return new ArrayList<T>(set);
    }

    public <T> List<T> intersection(List<T> list1, List<T> list2) {
        List<T> list = new ArrayList<T>();
        for (T t : list1) {
            if(list2.contains(t)) {
                list.add(t);
            }
        }
        return list;
    }

    public static void main(String[] args) throws Exception {
        List<Order> orders = WalmartCSVReader.GetOrders();

        ECLAT eclat = new ECLAT(orders, 5);
        eclat.start();
    }
}
