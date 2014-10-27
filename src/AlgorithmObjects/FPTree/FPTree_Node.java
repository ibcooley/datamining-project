package AlgorithmObjects.FPTree;

import AlgorithmObjects.Shared.RowColumnPair;

import java.util.HashMap;

/**
 * Created by Ben on 10/25/2014.
 * FPTree_Node
 */
public class FPTree_Node implements IFPTree_Node {

    private final HashMap<RowColumnPair, IFPTree_Node> Nodes;
    private int Count = 0;

    public FPTree_Node() {
        Nodes = new HashMap<RowColumnPair, IFPTree_Node>();
    }

    public HashMap<RowColumnPair, IFPTree_Node> getNodes() {
        return Nodes;
    }

    public void Prune(int minSupport) {
        for (RowColumnPair rowCol : Nodes.keySet()) {
            Nodes.get(rowCol).Prune(minSupport);
            if (Nodes.get(rowCol).getCount() < minSupport) {
                Nodes.remove(Nodes.get(rowCol));
            }
        }
    }

    public int getCount() {
        return this.Count;
    }

    public void setCount(int newCount) {
        this.Count = newCount;
    }


}