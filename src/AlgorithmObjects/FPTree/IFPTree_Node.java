package AlgorithmObjects.FPTree;

import AlgorithmObjects.Shared.RowColumnPair;

import java.util.Map;

/**
 * Created by Ben on 10/27/2014.
 * IFPTree_Node
 */
public interface IFPTree_Node {
    void Prune(int minSupport);

    Map<RowColumnPair, IFPTree_Node> getNodes();

    int getCount();

    void setCount(int newCount);
}
