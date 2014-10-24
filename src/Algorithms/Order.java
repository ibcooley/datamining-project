package Algorithms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Craig on 10/24/2014.
 */
public class Order implements Serializable {
    public Order() {
        orderID = "";
        itemSet = new ArrayList<String>();
    }

    private String orderID;
    private List<String> itemSet;

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public List<String> getItemSet() {
        return itemSet;
    }

    public void setItemSet(List<String> itemSet) {
        this.itemSet = itemSet;
    }
}
