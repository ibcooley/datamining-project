package AlgorithmObjects.FPTree;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Created by Ben on 10/25/2014.
 * FPTree_Node
 */
public class FPTree_Node<T> implements IFPTree_Node<T> {

    private final HashMap<T, IFPTree_Node> Nodes;
    private int Count = 0;
    private T name;
    private FPTree_Tree<T> tree;
    private IFPTree_Node<T> parentNode;
    private IFPTree_Node<T> Link;

    public FPTree_Node (FPTree_Tree<T> tree, IFPTree_Node<T> parent, T nodeName) {
        if (parent.getClass().equals(FPTree_Node.class)) {
            this.parentNode = parent;
        }
        Nodes = new HashMap<T, IFPTree_Node>();
        this.name = nodeName;
        this.tree = tree;
    }

    @Override
    public HashMap<LinkedList<IFPTree_Node<T>>, Integer> getAncestors () {
        HashMap<LinkedList<IFPTree_Node<T>>, Integer> ancestors = new HashMap<LinkedList<IFPTree_Node<T>>, Integer>();

        Collection<LinkedList<IFPTree_Node<T>>> items = getAncestors(this);

        for (LinkedList<IFPTree_Node<T>> list : items) {
            if (list.size() > 0) {
                ancestors.put(list, this.getCount());
            }
        }
        return ancestors;
    }

    private Collection<LinkedList<IFPTree_Node<T>>> getAncestors(IFPTree_Node<T> node) {
        HashSet<LinkedList<IFPTree_Node<T>>> ancestors = new HashSet<LinkedList<IFPTree_Node<T>>>();
        LinkedList<IFPTree_Node<T>> items = new LinkedList<IFPTree_Node<T>>();

        if (node != null) {
            IFPTree_Node<T> ancestor = node;
            while (ancestor != null) {
                items.addFirst(ancestor);
                if (node == this)
                    ancestors.add(new LinkedList<IFPTree_Node<T>>(items));
                ancestor = ancestor.getParent();
            }
            if (node != this) {
                ancestors.add(new LinkedList<IFPTree_Node<T>>(items));
            }

            // Collection<LinkedList<IFPTree_Node<T>>> lists = getAncestors(node.getParent());

            //for (LinkedList<IFPTree_Node<T>> list : lists) {
            //    if (list.size() > 0) {
            //        ancestors.add(list);
            //    }
            //}
        }


        return ancestors;
    }

    @Override
    public IFPTree_Node<T> getLink () {
        return Link;
    }

    @Override
    public void setLink (IFPTree_Node<T> link) {
        this.Link = link;
    }

    public IFPTree_Node<T> getParent () {
        return parentNode;
    }

    public HashMap<T, IFPTree_Node> getNodes () {
        return Nodes;
    }

    public int getCount () {
        return this.Count;
    }

    public void setCount (int newCount) {
        this.Count = newCount;
    }

    public void updateCount () {
        this.setCount(this.getCount() + 1);
        if (this.parentNode != null)
            this.parentNode.updateCount();
    }

    @Override
    public int getPatternCount () {
        int ret = 0;
        if (Nodes.size() == 0) return 1;

        for (IFPTree_Node<T> node : Nodes.values()) {
            ret += node.getPatternCount();
        }
        return ret;
    }

    @Override
    public T getName () {
        return this.name;
    }
}