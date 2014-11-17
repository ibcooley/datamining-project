package AlgorithmObjects.FPTree;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Ben on 10/27/2014.
 * IFPTree_Node
 */
public interface IFPTree_Node<T> {

    Map<T, IFPTree_Node> getNodes();

    int getCount();

    void setCount(int newCount);

    int getPatternCount ();

    T getName();

    IFPTree_Node<T> getLink ();

    void setLink (IFPTree_Node<T> link);

    IFPTree_Node<T> getParent ();

    HashMap<LinkedList<IFPTree_Node<T>>, Integer> getAncestors ();

    void updateCount ();
}
