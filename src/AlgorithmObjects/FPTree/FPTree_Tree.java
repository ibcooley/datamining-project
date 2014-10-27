package AlgorithmObjects.FPTree;

import AlgorithmObjects.Shared.RowColumnPair;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by Ben on 10/25/2014.
 * FPTree_Tree
 */
public class FPTree_Tree implements IFPTree_Node {

    private final HashMap<RowColumnPair, IFPTree_Node> Nodes;

    public FPTree_Tree() {
        Nodes = new HashMap<RowColumnPair, IFPTree_Node>();
    }

    public HashMap<RowColumnPair, IFPTree_Node> getNodes() {
        return Nodes;
    }

    public void Prune(int minSupport) {
        Set<RowColumnPair> keys = Nodes.keySet();
        for (RowColumnPair rowCol : keys) {
            Nodes.get(rowCol).Prune(minSupport);
            if (Nodes.get(rowCol).getCount() < minSupport) {
                Nodes.remove(Nodes.get(rowCol));
            }
        }
    }

    public int getCount() {
        int ret = 0;
        for (IFPTree_Node node : Nodes.values()) {
            ret += node.getCount();
        }
        return ret;
    }

    public void setCount(int count) {
    }
}