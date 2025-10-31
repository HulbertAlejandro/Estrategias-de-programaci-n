package voracious_algorithms;

import java.util.*;

public class ContainerOptimization {
    static class Item {
        int id;
        double value;
        double weight;
        int qty;
        double criterion;

        Item(int id, double v, double w, int q) {
            this.id = id;
            value = v;
            weight = w;
            qty = q;
            criterion = 0.0;
        }
    }

    // COMPLEJIDAD TEMPORAL: O(n log n) - por el ordenamiento
    // COMPLEJIDAD ESPACIAL: O(n) - para almacenar los items
    public static Result runHeuristic(int heuristic, List<Item> itemsInput, double capacity) {
        // Copiar items para no modificar la lista original - O(n)
        List<Item> items = new ArrayList<>();
        for (Item it : itemsInput) {
            items.add(new Item(it.id, it.value, it.weight, it.qty));
        }

        // Calcular criterio - O(n)
        for (Item it : items) {
            if (heuristic == 1) it.criterion = it.value;
            else if (heuristic == 2) it.criterion = 1.0 / it.weight;
            else it.criterion = it.value / it.weight;
        }

        // Ordenar decreciente por criterio - O(n log n)
        items.sort((a, b) -> Double.compare(b.criterion, a.criterion));

        double remaining = capacity;
        double totalValue = 0.0;
        double totalWeight = 0.0;
        List<Selection> selection = new ArrayList<>();

        // Proceso de llenado - O(n)
        for (Item it : items) {
            if (remaining <= 1e-12) {
                selection.add(new Selection(it.id, 0, 0, 0));
                continue;
            }

            double maxFullWeight = it.weight * it.qty;
            if (remaining >= maxFullWeight - 1e-12) {
                // Tomar todas las unidades
                double takeUnits = it.qty;
                double takenWeight = maxFullWeight;
                double takenValue = it.value * takeUnits;
                remaining -= takenWeight;
                totalValue += takenValue;
                totalWeight += takenWeight;
                selection.add(new Selection(it.id, takeUnits, takenWeight, takenValue));
            } else {
                // Tomar fracción
                double unitsCanTake = remaining / it.weight;
                if (unitsCanTake > it.qty) unitsCanTake = it.qty;
                double takenWeight = unitsCanTake * it.weight;
                double takenValue = unitsCanTake * it.value;
                remaining -= takenWeight;
                totalValue += takenValue;
                totalWeight += takenWeight;
                selection.add(new Selection(it.id, unitsCanTake, takenWeight, takenValue));
            }
        }

        return new Result(totalValue, totalWeight, selection);
    }

    static class Selection {
        int id;
        double unitsTaken;
        double weightTaken;
        double valueTaken;

        Selection(int id, double units, double weight, double value) {
            this.id = id;
            unitsTaken = units;
            weightTaken = weight;
            valueTaken = value;
        }
    }

    static class Result {
        double totalValue;
        double totalWeight;
        List<Selection> selection;

        Result(double value, double weight, List<Selection> sel) {
            totalValue = value;
            totalWeight = weight;
            selection = sel;
        }
    }

    public static void main(String[] args) {
        // Datos del problema
        List<Item> items = Arrays.asList(
                new Item(1, 15, 210, 3),
                new Item(2, 50, 230, 2),
                new Item(3, 20, 150, 4),
                new Item(4, 55, 40, 5),
                new Item(5, 92, 80, 3)
        );
        double capacity = 520.0;

        // Ejecutar las 3 heurísticas
        Result result1 = runHeuristic(1, items, capacity); // Mayor valor
        Result result2 = runHeuristic(2, items, capacity); // Menor peso  
        Result result3 = runHeuristic(3, items, capacity); // Mayor valor/peso

        // Mostrar resultados
        System.out.println("Comparativa de Heurísticas:");
        System.out.printf("Mayor valor primero:     %.2f\n", result1.totalValue);
        System.out.printf("Menor peso primero:      %.2f\n", result2.totalValue);
        System.out.printf("Mayor valor/peso primero: %.2f\n", result3.totalValue);
    }
}