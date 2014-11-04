package AlgorithmObjects.FPTree;

import java.util.Map;

/**
 * Created by Ben on 10/27/2014.
 * IFPTree_Node
 */
public interface IFPTree_Node<T> {
    void Prune(int minSupport);

    Map<T, IFPTree_Node> getNodes();

    int getCount();

    void setCount(int newCount);

    String toString(String prefix);
}
