package Helpers;

import java.util.*;

/**
 * Created by craig on 11/14/14.
 */
public class ListHelpers {
    // http://stackoverflow.com/questions/12548312/find-all-subsets-of-length-k-in-an-array
    private static <T> void getSubsets(List<T> superSet, int k, int idx, List<T> current, List<List<T>> solution) {
        //successful stop clause
        if (current.size() == k) {
            solution.add(new ArrayList<T>(current));
            return;
        }
        //unsuccessful stop clause
        if (idx == superSet.size()) return;
        T x = superSet.get(idx);
        current.add(x);
        //"guess" x is in the subset
        getSubsets(superSet, k, idx + 1, current, solution);
        current.remove(x);
        //"guess" x is not in the subset
        getSubsets(superSet, k, idx + 1, current, solution);
    }

    public static <T> List<List<T>> getSubsets(List<T> superSet, int k) {
        List<List<T>> res = new ArrayList<List<T>>();
        getSubsets(superSet, k, 0, new ArrayList<T>(), res);
        return res;
    }

    public static <T> List<T> union(List<T> list1, List<T> list2) {
        Set<T> set = new TreeSet<T>();
        set.addAll(list1);
        set.addAll(list2);
        return new ArrayList<T>(set);
    }

    public static <T> List<T> intersection(List<T> list1, List<T> list2) {
        List<T> list = new ArrayList<T>();
        for (T t : list1) {
            if(list2.contains(t)) {
                list.add(t);
            }
        }
        return list;
    }

    public static <T> List<T> subtract(List<T> list1, List<T> list2) {
        List<T> result = new ArrayList<T>();
        Set<T> set2 = new HashSet<T>(list2);
        for (T t1 : list1) {
            if (!set2.contains(t1)) {
                result.add(t1);
            }
        }
        return result;
    }

}
