package datastructures;

import java.util.*;

public class SortComparison {

    public enum Pattern {
        RANDOM, NEARLY_SORTED, REVERSE_SORTED
    }

    public static void main(String[] args) {

        int[] sizes = {100, 500, 1000, 3000};
        Pattern[] patterns = Pattern.values();

        System.out.printf("%-15s %-15s %-20s %-20s %-20s %-20s %-20s%n", "Pattern", "Size", "TreapSort (ns)", "PQSort (ns)", "QuickSort (ns)", "MergeSort (ns)", "CollectionsSort (ns)");

        for (Pattern pattern : patterns) {
            for (int size : sizes) {
                int[] base = generateArray(size, pattern);

                Integer[] treapArr = toIntegerArray(base);
                Integer[] pqArr = toIntegerArray(base);
                int[] quickArr = Arrays.copyOf(base, base.length);
                int[] mergeArr = Arrays.copyOf(base, base.length);
                List<Integer> colList = toIntegerList(base);

                System.gc();

                long treapTime = measure(() -> TreapSort.sort(treapArr));
                long pqTime    = measure(() -> PQSort.sort(pqArr));
                long quickTime = measure(() -> QuickSort.quickSort(quickArr, 0, quickArr.length - 1));
                long mergeTime = measure(() -> MergeSort.mergeSort(mergeArr));
                long colTime = measure(() -> Collections.sort(colList));

                System.out.printf("%-15s %-15d %-20d %-20d %-20d %-20d %-20d%n", pattern, size, treapTime, pqTime, quickTime, mergeTime, colTime);

            }
        }
    }

    @FunctionalInterface
    private interface SortAction {
        void run();
    }

    private static long measure(SortAction action) {
        long start = System.nanoTime();
        action.run();
        long end = System.nanoTime();
        return end - start;
    }

    private static int[] generateArray(int size, Pattern pattern) {
        int[] arr = new int[size];
        Random rand = new Random(42);
        switch (pattern) {
            case RANDOM:
                for (int i = 0; i < size; i++) arr[i] = i;
                for (int i = size - 1; i > 0; i--) {
                    int j = rand.nextInt(i + 1);
                    int tmp = arr[i]; arr[i] = arr[j]; arr[j] = tmp;
                }
                break;
            case NEARLY_SORTED:
                for (int i = 0; i < size; i++) arr[i] = i;
                int swaps = size / 100;
                for (int i = 0; i < swaps; i++) {
                    int a = rand.nextInt(size);
                    int b = rand.nextInt(size);
                    int tmp = arr[a]; arr[a] = arr[b]; arr[b] = tmp;
                }
                break;
            case REVERSE_SORTED:
                for (int i = 0; i < size; i++) arr[i] = size - 1 - i;
                break;
        }
        return arr;
    }

    private static Integer[] toIntegerArray(int[] arr) {
        Integer[] result = new Integer[arr.length];
        for (int i = 0; i < arr.length; i++) result[i] = arr[i];
        return result;
    }
    private static List<Integer> toIntegerList(int[] arr) {
        List<Integer> list = new ArrayList<>(arr.length);
        for (int value : arr) list.add(value);
        return list;
    }

}
