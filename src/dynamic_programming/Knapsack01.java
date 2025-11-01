package dynamic_programming;

import java.util.*;

/**
 * Knapsack 0/1: implementación con Memoización (Top-Down) y Tabulación (Bottom-Up).
 * Incluye reconstrucción de la solución (lista de índices de objetos seleccionados).
 *
 * Ejemplo incluido:
 * v = [2,5,10,14,15], w = [1,3,4,5,7], capacidad = 8 -> valor óptimo = 19 (objetos B y D)
 */
public class Knapsack01 {

    static class Result {
        int value;
        List<Integer> items; // índices de objetos tomados (0-based)

        Result(int value, List<Integer> items) {
            this.value = value;
            this.items = items;
        }
    }

    // ---------- M E M O I Z A C I Ó N (Top-Down) ----------
    private static int[][] memo;
    private static int[] vals, wts;
    private static int N;

    public static Result knapsackMemo(int[] values, int[] weights, int capacity) {
        vals = values;
        wts = weights;
        N = vals.length;
        memo = new int[N + 1][capacity + 1];
        for (int i = 0; i <= N; i++) Arrays.fill(memo[i], -1);

        int best = dpMemo(0, capacity);

        // Reconstrucción: desde i=0, cap=capacity
        List<Integer> taken = new ArrayList<>();
        int cap = capacity;
        for (int i = 0; i < N && cap >= 0; i++) {
            int curr = getMemo(i, cap);
            int without = getMemo(i + 1, cap); // valor si no tomo i
            if (curr == without) {
                // no tomado
                continue;
            } else {
                // tomado
                taken.add(i);
                cap -= wts[i];
            }
        }
        return new Result(best, taken);
    }

    // dp(i, cap) = mejor valor considerando objetos i..N-1 con capacidad cap
    private static int dpMemo(int i, int cap) {
        if (i == N) return 0;
        if (memo[i][cap] != -1) return memo[i][cap];

        int without = dpMemo(i + 1, cap);
        int with = 0;
        if (wts[i] <= cap) {
            with = vals[i] + dpMemo(i + 1, cap - wts[i]);
        }
        memo[i][cap] = Math.max(without, with);
        return memo[i][cap];
    }

    // helper: devuelve memo[i][cap] si existe, o calcula (asegura que dpMemo fue ejecutado)
    private static int getMemo(int i, int cap) {
        if (i >= N) return 0;
        if (memo[i][cap] == -1) return dpMemo(i, cap);
        return memo[i][cap];
    }

    // ---------- T A B U L A C I Ó N (Bottom-Up) ----------
    public static Result knapsackTab(int[] values, int[] weights, int capacity) {
        int n = values.length;
        int[][] dp = new int[n + 1][capacity + 1];

        // dp[0][*] = 0 por defecto
        for (int i = 1; i <= n; i++) {
            for (int c = 1; c <= capacity; c++) {
                int without = dp[i - 1][c];
                int with = Integer.MIN_VALUE;
                if (weights[i - 1] <= c) {
                    with = values[i - 1] + dp[i - 1][c - weights[i - 1]];
                }
                dp[i][c] = Math.max(without, with);
            }
        }

        int best = dp[n][capacity];

        // Reconstrucción: retroceder en la tabla dp
        List<Integer> taken = new ArrayList<>();
        int c = capacity;
        for (int i = n; i >= 1; i--) {
            if (dp[i][c] != dp[i - 1][c]) {
                // se tomó el objeto i-1
                taken.add(i - 1);
                c -= weights[i - 1];
            }
            // else no se tomó
        }
        Collections.reverse(taken); // para mostrar en orden ascendente de índices
        return new Result(best, taken);
    }

    // Método utilitario para imprimir la tabla DP (para prueba de escritorio)
    public static void printDpTable(int[] values, int[] weights, int capacity) {
        int n = values.length;
        int[][] dp = new int[n + 1][capacity + 1];
        for (int i = 1; i <= n; i++) {
            for (int c = 1; c <= capacity; c++) {
                int without = dp[i - 1][c];
                int with = Integer.MIN_VALUE;
                if (weights[i - 1] <= c) with = values[i - 1] + dp[i - 1][c - weights[i - 1]];
                dp[i][c] = Math.max(without, with);
            }
        }

        System.out.println("Tabla DP (filas=0..n, cols=0..capacity):");
        for (int i = 0; i <= n; i++) {
            System.out.printf("i=%d: ", i);
            for (int c = 0; c <= capacity; c++) System.out.printf("%3d ", dp[i][c]);
            System.out.println();
        }
    }

    // ---------- MAIN: ejemplo y pruebas ----------
    public static void main(String[] args) {
        int[] values = {2, 5, 10, 14, 15};   // v
        int[] weights = {1, 3, 4, 5, 7};     // w
        int capacity = 8;

        System.out.println("Ejemplo: v=" + Arrays.toString(values) + ", w=" + Arrays.toString(weights) + ", capacidad=" + capacity);
        System.out.println();

        // Tabulación
        Result rTab = knapsackTab(values, weights, capacity);
        System.out.println("Tabulación (Bottom-Up):");
        System.out.println("  Valor óptimo = " + rTab.value);
        System.out.println("  Ítems tomados (indices 0-based): " + rTab.items);
        System.out.println("  Detalle de ítems tomados:");
        for (int idx : rTab.items) {
            System.out.printf("    #%d -> valor=%d, peso=%d%n", idx, values[idx], weights[idx]);
        }
        System.out.println();

        // Memoización
        Result rMemo = knapsackMemo(values, weights, capacity);
        System.out.println("Memoización (Top-Down):");
        System.out.println("  Valor óptimo = " + rMemo.value);
        System.out.println("  Ítems tomados (indices 0-based): " + rMemo.items);
        System.out.println("  Detalle de ítems tomados:");
        for (int idx : rMemo.items) {
            System.out.printf("    #%d -> valor=%d, peso=%d%n", idx, values[idx], weights[idx]);
        }

        System.out.println();
        System.out.println("Prueba de escritorio (tabla DP usada en tabulación):");
        printDpTable(values, weights, capacity);
    }
}
