package divide_and_conquer;

import java.util.Arrays;

public class CountSmallerDivideConquer {

    // par valor + índice
    private static class Pair {
        int val;
        int idx;
        Pair(int v, int i) { val = v; idx = i; }
    }

    public static int[] countSmaller(int[] nums) {
        int n = nums.length;
        int[] result = new int[n];
        Pair[] pairs = new Pair[n];
        for (int i = 0; i < n; i++) pairs[i] = new Pair(nums[i], i);
        Pair[] aux = new Pair[n];
        sortCount(pairs, aux, 0, n - 1, result);
        return result;
    }

    private static void sortCount(Pair[] pairs, Pair[] aux, int l, int r, int[] result) {
        if (l >= r) return;
        int mid = l + (r - l) / 2;
        sortCount(pairs, aux, l, mid, result);
        sortCount(pairs, aux, mid + 1, r, result);
        merge(pairs, aux, l, mid, r, result);
    }

    private static void merge(Pair[] pairs, Pair[] aux, int l, int mid, int r, int[] result) {
        for (int i = l; i <= r; i++) aux[i] = pairs[i];
        int i = l, j = mid + 1, k = l;
        int rightCountTaken = 0; // número de elementos del lado derecho ya colocados en pairs

        while (i <= mid && j <= r) {
            if (aux[j].val < aux[i].val) {
                // right element is smaller -> it goes before aux[i]
                pairs[k++] = aux[j++];
                rightCountTaken++;
            } else {
                // aux[i].val <= aux[j].val, so all right elements taken so far are smaller than aux[i]
                result[aux[i].idx] += rightCountTaken;
                pairs[k++] = aux[i++];
            }
        }
        // si quedan elementos en la izquierda, cada uno debe acumular rightCountTaken
        while (i <= mid) {
            result[aux[i].idx] += rightCountTaken;
            pairs[k++] = aux[i++];
        }
        // el resto del derecho simplemente se copian
        while (j <= r) {
            pairs[k++] = aux[j++];
        }
    }

    // prueba con el ejemplo
    public static void main(String[] args) {
        int[] nums = {5, 2, 6, 1, 3};
        int[] res = countSmaller(nums);
        System.out.println("Array: " + Arrays.toString(nums));
        System.out.println("Smaller elements count to right: " + Arrays.toString(res));
    }
}
