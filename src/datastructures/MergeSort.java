package datastructures;

public class MergeSort {

    static void mergeSort(int[] array) {
        int length = array.length;
        if(length <= 1) return;
        int middle = length/2;
        int[] leftArray = new int[middle];
        int[] rightArray = new int[length-middle];

        System.arraycopy(array, 0, leftArray, 0, middle);
        System.arraycopy(array, middle, rightArray, 0, length - middle);

        mergeSort(leftArray);
        mergeSort(rightArray);
        merge(leftArray, rightArray, array);
    }

    static void merge(int[] leftArray, int[] rightArray, int[] array) {
        int leftSize = array.length/2;
        int rightSize = array.length - leftSize;
        int i = 0, l = 0, r = 0;

        while(l < leftSize && r < rightSize) {
            if(leftArray[l] < rightArray[r]) {
                array[i] = leftArray[l];
                i++;
                l++;
            }
            else {
                array[i] = rightArray[r];
                i++;
                r++;
            }
        }
        while(l < leftSize) {
            array[i] = leftArray[l];
            i++;
            l++;
        }
        while(r < rightSize) {
            array[i] = rightArray[r];
            i++;
            r++;
        }
    }

}
