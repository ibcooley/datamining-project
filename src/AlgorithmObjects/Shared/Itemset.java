package AlgorithmObjects.Shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Craig on 10/24/2014.
 * Itemset
 */
public class Itemset<T> implements Serializable {
    private List<T> itemSet;
    private int supportCount;

    public Itemset() {
        itemSet = new ArrayList<T>();
    }

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
