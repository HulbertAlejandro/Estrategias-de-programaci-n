package voracious_algorithms;

import java.util.*;

public class MSTAlgorithms {

    static class Edge implements Comparable<Edge> {
        int u, v;
        int w;

        Edge(int u, int v, int w) {
            this.u = u;
            this.v = v;
            this.w = w;
        }

        public int compareTo(Edge o) {
            return Integer.compare(this.w, o.w);
        }
    }

    static class UnionFind {
        int[] parent, rank;

        UnionFind(int n) {
            parent = new int[n + 1];
            rank = new int[n + 1];
            for (int i = 1; i <= n; i++) {
                parent[i] = i;
                rank[i] = 0;
            }
        }

        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        boolean union(int a, int b) {
            int ra = find(a);
            int rb = find(b);
            if (ra == rb) return false;

            if (rank[ra] < rank[rb]) {
                parent[ra] = rb;
            } else if (rank[ra] > rank[rb]) {
                parent[rb] = ra;
            } else {
                parent[rb] = ra;
                rank[ra]++;
            }
            return true;
        }
    }

    public static List<Edge> kruskal(int n, List<Edge> edges) {
        List<Edge> sortedEdges = new ArrayList<>(edges);
        Collections.sort(sortedEdges);

        UnionFind uf = new UnionFind(n);
        List<Edge> mst = new ArrayList<>();

        for (Edge edge : sortedEdges) {
            if (uf.union(edge.u, edge.v)) {
                mst.add(edge);
                if (mst.size() == n - 1) break;
            }
        }

        return mst;
    }

    static class PrimEdge implements Comparable<PrimEdge> {
        int u, v, w;

        PrimEdge(int u, int v, int w) {
            this.u = u;
            this.v = v;
            this.w = w;
        }

        public int compareTo(PrimEdge o) {
            return Integer.compare(this.w, o.w);
        }
    }

    public static List<Edge> prim(int n, List<List<Edge>> adj) {
        boolean[] visited = new boolean[n + 1];
        PriorityQueue<PrimEdge> pq = new PriorityQueue<>();
        List<Edge> mst = new ArrayList<>();

        visited[1] = true;
        for (Edge edge : adj.get(1)) {
            pq.add(new PrimEdge(1, edge.v, edge.w));
        }

        while (!pq.isEmpty() && mst.size() < n - 1) {
            PrimEdge minEdge = pq.poll();

            if (visited[minEdge.v]) continue;

            visited[minEdge.v] = true;
            mst.add(new Edge(minEdge.u, minEdge.v, minEdge.w));

            for (Edge edge : adj.get(minEdge.v)) {
                if (!visited[edge.v]) {
                    pq.add(new PrimEdge(minEdge.v, edge.v, edge.w));
                }
            }
        }

        return mst;
    }

    public static int calculateTotalCost(List<Edge> edges) {
        int total = 0;
        for (Edge edge : edges) {
            total += edge.w;
        }
        return total;
    }

    public static List<List<Edge>> buildAdjacencyList(int n, List<Edge> edges) {
        List<List<Edge>> adj = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            adj.add(new ArrayList<>());
        }

        for (Edge edge : edges) {
            adj.get(edge.u).add(new Edge(edge.u, edge.v, edge.w));
            adj.get(edge.v).add(new Edge(edge.v, edge.u, edge.w));
        }

        return adj;
    }

    public static void main(String[] args) {
        int n = 7;
        List<Edge> edges = Arrays.asList(
                new Edge(1, 2, 1000000),
                new Edge(2, 3, 2000000),
                new Edge(4, 5, 3000000),
                new Edge(6, 7, 3000000),
                new Edge(1, 4, 4000000),
                new Edge(2, 5, 4000000),
                new Edge(4, 7, 4000000),
                new Edge(3, 5, 5000000),
                new Edge(2, 4, 6000000),
                new Edge(3, 6, 6000000),
                new Edge(5, 7, 7000000),
                new Edge(5, 6, 8000000)
        );

        List<List<Edge>> adj = buildAdjacencyList(n, edges);

        List<Edge> kruskalResult = kruskal(n, edges);
        List<Edge> primResult = prim(n, adj);

        int kruskalCost = calculateTotalCost(kruskalResult);
        int primCost = calculateTotalCost(primResult);

        System.out.println("Conexiones seleccionadas por Kruskal:");
        for (Edge edge : kruskalResult) {
            System.out.printf("Municipio %d -- Municipio %d (Costo: $%,d COP)%n",
                    edge.u, edge.v, edge.w);
        }
        System.out.printf("Costo total Kruskal: $%,d COP%n", kruskalCost);

        System.out.println("\nConexiones seleccionadas por Prim:");
        for (Edge edge : primResult) {
            System.out.printf("Municipio %d -- Municipio %d (Costo: $%,d COP)%n",
                    edge.u, edge.v, edge.w);
        }
        System.out.printf("Costo total Prim: $%,d COP%n", primCost);
    }
}