package AlgorithmObjects.Shared;

import Helpers.ListHelpers;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by craig on 11/14/14.
 */
public class AssociationFinder {

    private List<Order> orders;
    private double minConf;
    private List<Association> associations;

    public AssociationFinder(List<Order> orders, double minConf) {
        this.orders = orders;
        this.minConf = minConf;
        associations = new ArrayList<Association>();
    }

    public List<Association> find(List<Itemset<String>> frequentItemsets) {
        for (Itemset<String> itemset : frequentItemsets) {
            // Itemsets of size 1 cannot have associations
            if (itemset.getItemSet().size() > 1) {
                List<List<String>> subsets = ListHelpers.getSubsets(itemset.getItemSet(), itemset.getItemSet().size() - 1);

                for (List<String> subset : subsets) {
                    List<String> diff = ListHelpers.subtract(itemset.getItemSet(), subset);

                    if (diff.size() > 0) {
                        // Find confidence: conf(A => B) = sup_count(A U B) / sup_count(A)
                        // Need to find sup_count(A) - we already have sup_count(A U B)
                        int supCountA = 0;
                        for (Order order : orders) {
                            List<String> orderItems = order.getItemSet();
                            if (orderItems.containsAll(subset)) {
                                supCountA++;
                            }
                        }
                        double conf = (double)itemset.getSupportCount() / (double)supCountA;

                        // If conf >= minConf then we've found an association (subset => diff)
                        if (conf >= minConf) {
                            Association association = new Association(subset, diff, (double)itemset.getSupportCount() / (double)orders.size(), conf, 0);
                            associations.add(association);
                        }
                    }
                }
            }
        }

        return associations;
    }

    public void printToFile() throws FileNotFoundException {
        String results = "";
        for (Association association : associations) {
            String line = association.getA() + " => " + association.getB()
                    + " [support = " + String.format("%.4f", association.getSupport())
                    + "%, confidence = " + String.format("%.4f", association.getConfidence()) + "%]\n";
            results += line;
        }
        PrintWriter pw = new PrintWriter("association_results.txt");
        pw.print(results);
        pw.close();
    }
}
