package Algorithms;

import java.util.HashMap;


/**
 * Created by Ben on 10/25/2014.
 */
public class FPTree_Tree {

    private final HashMap<RowColumnPair, FPTree_Node> Nodes;

    public FPTree_Tree() {
        Nodes = new HashMap<RowColumnPair, FPTree_Node>();
    }

    public HashMap<RowColumnPair, FPTree_Node> getNodes() {
        return Nodes;
    }
}