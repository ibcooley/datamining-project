package AlgorithmObjects.Shared;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by craig on 10/31/14.
 */
public class EquivalenceClass<TItemset, TTransaction> {
    private List<TItemset> prefix;
    private List<VerticalItemset<TItemset, TTransaction>> verticalItemsets;

    public EquivalenceClass() {
        setPrefix(new ArrayList<TItemset>());
        setVerticalItemsets(new ArrayList<VerticalItemset<TItemset, TTransaction>>());
    }

    public EquivalenceClass(List<TItemset> prefix) {
        setPrefix(prefix);
        setVerticalItemsets(new ArrayList<VerticalItemset<TItemset, TTransaction>>());
    }

    public EquivalenceClass(List<TItemset> prefix, List<VerticalItemset<TItemset, TTransaction>> verticalItemsets) {
        setPrefix(prefix);
        setVerticalItemsets(verticalItemsets);
    }

    public List<TItemset> getPrefix() {
        return prefix;
    }

    public void setPrefix(List<TItemset> prefix) {
        this.prefix = prefix;
    }

    public List<VerticalItemset<TItemset, TTransaction>> getVerticalItemsets() {
        return verticalItemsets;
    }

    public void setVerticalItemsets(List<VerticalItemset<TItemset, TTransaction>> verticalItemsets) {
        this.verticalItemsets = verticalItemsets;
    }
}
