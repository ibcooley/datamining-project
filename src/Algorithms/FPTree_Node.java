package Algorithms;

import java.util.HashMap;

/**
 * Created by Ben on 10/25/2014.
 */
class FPTree_Node {

    private final HashMap<RowColumnPair, FPTree_Node> Nodes;
    private int Count = 0;

    public FPTree_Node() {
        Nodes = new HashMap<RowColumnPair, FPTree_Node>();
    }

    public HashMap<RowColumnPair, FPTree_Node> getNodes() {
        return Nodes;
    }

    public int incrementCount() {
        return Count++;
    }
}