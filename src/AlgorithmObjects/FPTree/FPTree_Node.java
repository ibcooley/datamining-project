package AlgorithmObjects.FPTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Ben on 10/25/2014.
 * FPTree_Node
 */
public class FPTree_Node<T> implements IFPTree_Node<T> {

    private final HashMap<T, IFPTree_Node> Nodes;
    private int Count = 0;
    private T name;
    private IFPTree_Node<T> parentNode;

    public FPTree_Node(IFPTree_Node<T> parent, T nodeName) {
        this.parentNode = parent;
        Nodes = new HashMap<T, IFPTree_Node>();
        this.name = nodeName;
    }

    public IFPTree_Node<T> getParentNode() {
        return parentNode;
    }

    public HashMap<T, IFPTree_Node> getNodes() {
        return Nodes;
    }

    public void Prune(int minSupport) {
        List<T> toRemove = new ArrayList<T>();
        for (T rowCol : Nodes.keySet()) {
            Nodes.get(rowCol).Prune(minSupport);
            if (Nodes.get(rowCol).getCount() < minSupport) {
                toRemove.add(rowCol);
            }
        }
        for (T obj : toRemove) {
            Nodes.remove(obj);
        }
    }

    public int getCount() {
        return this.Count;
    }

    public void setCount(int newCount) {
        if (parentNode != null) {
            parentNode.setCount(parentNode.getCount() + 1);
        }
        this.Count = newCount;
    }

    @Override
    public String toString(String prefix) {
        String out = "";
        List<T> keys = new ArrayList<T>(Nodes.keySet());
        for (int i = 0; i < Nodes.size(); i++) {
            out += Nodes.get(keys.get(i)).toString(prefix + keys.get(i).toString() + " -> ");
        }

        if (Nodes.size() == 0 && prefix != "") {
            out = prefix + getCount() + "\r\n";
        }

        return out;
    }

    @Override
    public T getName() {
        return this.name;
    }
}