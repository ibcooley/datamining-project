package AlgorithmObjects.FPTree;

import Helpers.SortMap;
import com.sun.deploy.util.StringUtils;

import java.util.*;

/**
 * Created by Ben on 10/25/2014.
 * FPTree_Tree
 */
public class FPTree_Tree<T> implements IFPTree_Node<T> {

	private final HashMap<T, IFPTree_Node> Nodes;
	HashMap<String, Integer> itemsets;
	private int count;
	private T name;
	private LinkedHashMap<T, IFPTree_Node<T>> HeadNodes;
	private LinkedHashMap<T, IFPTree_Node<T>> TailNodes;

	public FPTree_Tree () {
		Nodes = new HashMap<T, IFPTree_Node>();
		HeadNodes = new LinkedHashMap<T, IFPTree_Node<T>>();
		TailNodes = new LinkedHashMap<T, IFPTree_Node<T>>();
	}

	public LinkedHashMap<T, IFPTree_Node<T>> getHeadNodes () {
		return HeadNodes;
	}

	public int getPatternCount () {
		int ret = 0;
		for (IFPTree_Node<T> node : Nodes.values()) {
			ret += node.getPatternCount();
		}
		return ret;
	}

	public HashMap<T, IFPTree_Node> getNodes () {
		return Nodes;
	}

	public int getCount () {
		int ret = 0;
		for (IFPTree_Node node : Nodes.values()) {
			ret += node.getCount();
		}
		return ret;
	}

	public void setCount (int count) {
		this.count = count;
	}

	public void updateCount () {

	}

	public String toString (int minSupport) {
		Map<String, Integer> items = SortMap.GetMapSortedByKey(getFrequestItemSets(), SortMap.SortOrder.ASC);
		String ret = "";

		for (String name : items.keySet()) {
			if (items.get(name) >= minSupport) {
				ret += String.format("%1$5s", items.get(name)) + ":\t" + name + "\r\n";
			}
		}
		return ret;
	}

	public void fixNodes (IFPTree_Node<T> node) {
		IFPTree_Node<T> tail = this.TailNodes.get(node.getName());

		if (tail != null) {
			tail.setLink(node);
		}

		this.TailNodes.put(node.getName(), node);

		if (this.HeadNodes.get(node.getName()) == null) {
			this.HeadNodes.put(node.getName(), node);
		}
	}

	public HashMap<String, Integer> getFrequestItemSets (int minSupport) {
		HashMap<String, Integer> items = getFrequestItemSets();
		HashMap<String, Integer> toReturn = new HashMap<String, Integer>();

		for (Map.Entry<String, Integer> entry : items.entrySet()) {
			if (entry.getValue() >= minSupport) {
				toReturn.put(entry.getKey(), entry.getValue());
			}
		}
		return toReturn;
	}

	public HashMap<String, Integer> getFrequestItemSets () {
		if (itemsets != null) return itemsets;
		itemsets = new HashMap<String, Integer>();
		for (IFPTree_Node<T> header : this.getHeadNodes().values()) {
			IFPTree_Node<T> item = header;
			while (item != null) {
				HashMap<LinkedList<IFPTree_Node<T>>, Integer> ancestors = item.getAncestors();
				for (LinkedList<IFPTree_Node<T>> list : ancestors.keySet()) {
					List<String> names = new ArrayList<String>();
					for (int i = 0; i < list.size(); i++) {
						names.add(list.get(i).getName().toString());
					}
					String prefix = StringUtils.join(names, ", ");
					if (itemsets.containsKey(prefix)) {
						itemsets.put(prefix, itemsets.get(prefix) + ancestors.get(list));
					} else {
						itemsets.put(prefix, ancestors.get(list));
					}
				}
				item = item.getLink();
			}
		}
		return itemsets;
	}

	@Override
	public T getName () {
		return this.name;
	}

	@Override
	public IFPTree_Node<T> getLink () {
		return null;
	}

	@Override
	public void setLink (IFPTree_Node<T> link) {

	}

	@Override
	public IFPTree_Node<T> getParent () {
		return null;
	}

	@Override
	public HashMap<LinkedList<IFPTree_Node<T>>, Integer> getAncestors () {
		return null;
	}
}