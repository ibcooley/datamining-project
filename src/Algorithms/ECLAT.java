package Algorithms;

import AlgorithmObjects.Shared.EquivalenceClass;
import AlgorithmObjects.Shared.Order;
import AlgorithmObjects.Shared.VerticalItemset;
import Helpers.WalmartCSVReader;
import java.io.IOException;
import java.util.*;

public class ECLAT {

    private List<Order> orders;
    private int minSupport;
    private List<VerticalItemset<String, String>> frequentItemsets;

    public ECLAT(List<Order> orders, int minSupport) {
        this.orders = orders;
        this.minSupport = minSupport;
        frequentItemsets = new ArrayList<VerticalItemset<String, String>>();
    }

    public void start() {
        List<VerticalItemset<String, String>> frequentKMinusOneItemsets = findFrequentOneItemsets();
        EquivalenceClass<String, String> ec = new EquivalenceClass<String, String>(new ArrayList<String>(), frequentKMinusOneItemsets);
        eclatAlgorithm(ec);
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
            Collections.sort(p);

            EquivalenceClass<String, String> ecPrime = new EquivalenceClass<String, String>(p);

            for (int j = i + 1; j < ec.getVerticalItemsets().size(); j++) {
                VerticalItemset<String, String> vItemset2 = ec.getVerticalItemsets().get(j);
                List<String> tmpT = intersection(vItemset1.getTransactionIDSet(), vItemset2.getTransactionIDSet());
                if (tmpT.size() >= minSupport) {
                    VerticalItemset<String, String> newVItemset = new VerticalItemset<String, String>();
                    newVItemset.getItemset().addAll(p);
                    newVItemset.getItemset().add(vItemset2.getItemset().get(vItemset2.getItemset().size() - 1));
                    newVItemset.getTransactionIDSet().addAll(tmpT);
                    Collections.sort(newVItemset.getItemset());
                    ecPrime.getVerticalItemsets().add(newVItemset);
                    frequentItemsets.add(newVItemset);
                }
            }

            if (ecPrime.getVerticalItemsets().size() != 0) {
                eclatAlgorithm(ecPrime);
            }
        }
    }

    public <T> List<T> union(List<T> list1, List<T> list2) {
        Set<T> set = new HashSet<T>();
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

    public static void main(String[] args) throws IOException {
        System.out.println("Hello from ECLAT!");

        List<Order> orders = WalmartCSVReader.GetOrders();

        ECLAT eclat = new ECLAT(orders, 5);
        eclat.start();
    }
}
