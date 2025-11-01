package dynamic_programming;

import java.util.Arrays;

public class MinPathGrid {

    /**
     * Calcula el costo mínimo desde (0,0) hasta (m-1,n-1) moviéndose solo
     * a la derecha o hacia abajo. Usa O(n) espacio.
     *
     * @param grid matriz de costos (m x n)
     * @param debug si true imprime el estado de dp en cada paso (útil para prueba de escritorio)
     * @return costo mínimo total
     */
    public static int minPathSum(int[][] grid, boolean debug) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            throw new IllegalArgumentException("Matriz vacía o nula.");
        }
        int m = grid.length;
        int n = grid[0].length;

        // dp[j] = costo mínimo para llegar a la celda (i, j) en la fila actual i
        int[] dp = new int[n];

        // inicializar primera celda
        dp[0] = grid[0][0];

        // inicializar primera fila (solo se puede venir desde la izquierda)
        for (int j = 1; j < n; j++) {
            dp[j] = dp[j - 1] + grid[0][j];
        }

        if (debug) {
            System.out.println("Inicial - fila 0 -> dp = " + Arrays.toString(dp));
        }

        // procesar filas 1..m-1
        for (int i = 1; i < m; i++) {
            // la primera columna solo puede venir desde arriba (dp[0] ya tiene costo de arriba)
            dp[0] = dp[0] + grid[i][0];

            if (debug) {
                System.out.printf("Antes fila %d, dp = %s (dp[0] actualizada)= dp[0]=%d%n", i, Arrays.toString(dp), dp[0]);
            }

            // actualizar resto columnas de izquierda a derecha
            for (int j = 1; j < n; j++) {
                // dp[j] es el costo desde arriba (anterior fila), dp[j-1] es desde la izquierda (misma fila, ya actualizado)
                dp[j] = grid[i][j] + Math.min(dp[j], dp[j - 1]);
                if (debug) {
                    System.out.printf("  fila %d, col %d: cell=%d, min( arriba=%d, izq=%d ) -> dp[%d]=%d%n",
                            i, j, grid[i][j], /* arriba */ (dp[j] - grid[i][j] == dp[j-1]? dp[j-1] : dp[j] /*not used*/), dp[j-1], j, dp[j]);
                    // Nota: la impresión anterior simplifica la explicación; ver tabla formal abajo.
                }
            }

            if (debug) {
                System.out.println("Después fila " + i + " -> dp = " + Arrays.toString(dp));
            }
        }

        // dp[n-1] es el mínimo costo para llegar a (m-1, n-1)
        return dp[n - 1];
    }

    // Programa principal con el ejemplo y debug activado
    public static void main(String[] args) {
        int[][] grid = {
                {1, 3, 1},
                {1, 5, 1},
                {4, 2, 1}
        };

        System.out.println("Matriz (grid):");
        for (int[] row : grid) System.out.println(Arrays.toString(row));
        System.out.println();

        int result = minPathSum(grid, true);
        System.out.println();
        System.out.println("Resultado final: costo mínimo = " + result);
    }
}
