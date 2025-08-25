package datastructures;

import java.util.*;

public class TreapSort {

    public static <E extends Comparable<? super E>> List<E> sort(Collection<E> data) {
        UzmaTreap<E, E> treap = new UzmaTreap<>();
        for (E elem : data) {
            treap.put(elem, elem);
        }
        List<E> sorted = new ArrayList<>(treap.size());
        for (Map.Entry<E, E> entry : treap.entrySet()) {
            sorted.add(entry.getKey());
        }
        return sorted;
    }

    public static <E extends Comparable<? super E>> void sort(E[] array) {
        List<E> sortedList = sort(Arrays.asList(array));
        for (int i = 0; i < array.length; i++) {
            array[i] = sortedList.get(i);
        }
    }


}
