package divide_and_conquer;

public class MajorityDivideConquer {

    // función pública que devuelve el majority element o -1 si no existe
    public static int majorityElement(int[] a) {
        if (a == null || a.length == 0) return -1;
        int candidate = majorityRec(a, 0, a.length - 1);
        // verificar que candidate es realmente majority
        int count = 0;
        for (int v : a) if (v == candidate) count++;
        return (count > a.length / 2) ? candidate : -1;
    }

    // retorna el candidato majority en a[l..r] (puede no ser realmente majority)
    private static int majorityRec(int[] a, int l, int r) {
        if (l == r) return a[l];
        int mid = l + (r - l) / 2;
        int leftCand = majorityRec(a, l, mid);
        int rightCand = majorityRec(a, mid + 1, r);
        if (leftCand == rightCand) return leftCand;

        // contar ocurrencias de cada candidato en el segmento l..r
        int leftCount = 0, rightCount = 0;
        for (int i = l; i <= r; i++) {
            if (a[i] == leftCand) leftCount++;
            else if (a[i] == rightCand) rightCount++;
        }
        return (leftCount > rightCount) ? leftCand : rightCand;
    }

    // prueba rápida con ejemplo
    public static void main(String[] args) {
        int[] arr = {2, 2, 1, 1, 1, 2, 2};
        int res = majorityElement(arr);
        System.out.println("Array: java.util.Arrays.toString(arr) -> Resultado: " + res);
        // Imprimir arreglo legible
        System.out.print("Array: [");
        for (int i=0;i<arr.length;i++){
            System.out.print(arr[i] + (i==arr.length-1? "": ", "));
        }
        System.out.println("]");
        System.out.println("Majority element (o -1 si no existe): " + res);
    }
}
