package AlgorithmObjects.Shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VerticalItemset<TItem, TTransactionID> implements Serializable {
    private List<TItem> itemset;
    private List<TTransactionID> transactionIDSet;

    public VerticalItemset() {
        itemset = new ArrayList<TItem>();
        transactionIDSet = new ArrayList<TTransactionID>();
    }

    public VerticalItemset(List<TItem> itemset, List<TTransactionID> transactionIDSet) {
        this.itemset = itemset;
        this.transactionIDSet = transactionIDSet;
    }

    public List<TItem> getItemset() {
        return itemset;
    }

    public void setItem(List<TItem> itemset) {
        this.itemset = itemset;
    }

    public List<TTransactionID> getTransactionIDSet() {
        return transactionIDSet;
    }

    public void setTransactionIDSet(List<TTransactionID> transactionIDSet) {
        this.transactionIDSet = transactionIDSet;
    }
}
