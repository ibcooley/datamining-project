package Algorithms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Craig on 10/24/2014.
 */
public class Itemset<T> implements Serializable {
    public Itemset() {
        itemSet = new ArrayList<T>();
    }

    private List<T> itemSet;
    private int supportCount;

    public List<T> getItemSet() {
        return itemSet;
    }

    public void setItemSet(List<T> itemSet) {
        this.itemSet = itemSet;
    }

    public int getSupportCount() {
        return supportCount;
    }

    public void setSupportCount(int supportCount) {
        this.supportCount = supportCount;
    }

    public void incrementSupportCount() {
        supportCount++;
    }
}
