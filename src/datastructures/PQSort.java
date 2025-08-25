package datastructures;

import java.util.*;

public class PQSort {

    public static <E extends Comparable<? super E>> List<E> sort(Collection<E> data) {
        PriorityQueue<E> pq = new PriorityQueue<>(data);
        List<E> sorted = new ArrayList<>(pq.size());
        while (!pq.isEmpty()) {
            sorted.add(pq.poll());
        }
        return sorted;
    }

    public static <E extends Comparable<? super E>> void sort(E[] array) {
        PriorityQueue<E> pq = new PriorityQueue<>(Arrays.asList(array));
        for (int i = 0; i < array.length; i++) {
            array[i] = pq.poll();
        }
    }

}
