package chapter4;

import sun.plugin.services.WIExplorerBrowserService;

import java.util.*;
import java.util.function.IntConsumer;

/**
 * @author Scc_hy
 * @version 1.0.0
 * @ClassName WeightedGraph.java
 * @Description
 * @createTime 2023年03月09日 10:17:00
 */
public class WeightedGraph<V> extends Graph<V, WeightedEdge> {
    public WeightedGraph(List<V> vertices){
        super(vertices);
    }

    public void addEdge(WeightedEdge e){
        edges.get(e.from).add(e);
        edges.get(e.to).add(e.reversed());
    }

    public void addEdge(int from, int to, float weight) {
        addEdge(new WeightedEdge(from, to, weight));
    }

    public void addEdge(V first, V second, float weight) {
        addEdge(indexOf(first), indexOf(second), weight);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0;i < getVertexCount(); i++){
            sb.append(vertexAt(i));
            sb.append(" -> ");
            sb.append(Arrays.toString(edgesOf(i).stream()
                    .map(we -> "(" + vertexAt(we.to) + ", " + we.weight + ")").toArray()));
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    public static double totalWeight(List<WeightedEdge> path){
        return path.stream().mapToDouble(we -> we.weight).sum();
    }

    // 使用 Jarnik's algorithm 寻找最小生成树
    public List<WeightedEdge> mst(int start){
        LinkedList<WeightedEdge> res = new LinkedList<>();
        if(start < 0 || start > (getVertexCount() -1)){
            return res;
        }
        PriorityQueue<WeightedEdge> pq = new PriorityQueue<>();
        boolean[] visited = new boolean[getVertexCount()];

        IntConsumer visit = idx -> {
            visited[idx] = true;
            for(WeightedEdge e: edgesOf(idx)){
                if(!visited[e.to]){
                    pq.offer(e);
                }
            }
        };

        visit.accept(start);
        while(!pq.isEmpty()){
            WeightedEdge e = pq.poll();
            if (visited[e.to]){
                continue;
            }
            res.add(e);
            visit.accept(e.to);
        }
        return res;
    }

    public void printWeightedPath(List<WeightedEdge> wp) {
        for (WeightedEdge edge : wp) {
            System.out.println(vertexAt(edge.from) + " " + edge.weight + "> " + vertexAt(edge.to));
        }
        System.out.println("Total Weight: " + totalWeight(wp));
    }

    public static final class DijkstraNode implements Comparable<DijkstraNode>{
        public final int vertex;
        public final double distance;

        public DijkstraNode(int vertex, double distance) {
            this.vertex = vertex;
            this.distance = distance;
        }

        @Override
        public int compareTo(DijkstraNode other) {
            Double mine = distance;
            Double theirs = other.distance;
            return mine.compareTo(theirs);
        }
    }

    public static final class DijkstraResult {
        public final double[] distances;
        public final Map<Integer, WeightedEdge> pathMap;

        public DijkstraResult(double[] distances, Map<Integer, WeightedEdge> pathMap){
            this.distances = distances;
            this.pathMap = pathMap;
        }
    }

    public DijkstraResult dijkstra(V root){
        int first = indexOf(root);
        double[] distances = new double[getVertexCount()];
        distances[first] = 0;
        boolean[] visited = new boolean[getVertexCount()];
        visited[first] = true;
        HashMap<Integer, WeightedEdge> pathMap = new HashMap<>();
        PriorityQueue<DijkstraNode> pq = new PriorityQueue<>();
        pq.offer(new DijkstraNode(first, 0));
        while(!pq.isEmpty()){
            int u = pq.poll().vertex;
            double distU = distances[u];
            for(WeightedEdge e: edgesOf(u)){
                double distV = distances[e.to];
                double pathWeight = e.weight + distU;
                if(!visited[e.to] || (distV > pathWeight)){
                    visited[e.to] = true;
                    distances[e.to] = pathWeight;
                    pathMap.put(e.to, e);
                    pq.offer(new DijkstraNode(e.to, pathWeight));
                }
            }
        }
        return new DijkstraResult(distances, pathMap);
    }

    public Map<V, Double> distanceArrayToDistanceMap(double[] distances) {
        HashMap<V, Double> distanceMap = new HashMap<>();
        for (int i = 0; i < distances.length; i++) {
            distanceMap.put(vertexAt(i), distances[i]);
        }
        return distanceMap;
    }

    public static List<WeightedEdge> pathMapToPath(int start, int end, Map<Integer, WeightedEdge> pathMap) {
        if (pathMap.size() == 0){
            return Collections.emptyList();
        }

        LinkedList<WeightedEdge> path = new LinkedList<>();
        WeightedEdge edge = pathMap.get(end);
        path.add(edge);
        while(edge.from != start){
            edge = pathMap.get(edge.to);
            path.add(edge);
        }
        Collections.reverse(path);
        return path;
    }

    public static void main(String[] args){
        WeightedGraph<String> cityGraph2 = new WeightedGraph<>(
                Arrays.asList("Seattle", "San Francisco", "Los Angeles", "Riverside", "Phoenix", "Chicago", "Boston",
                        "New York", "Atlanta", "Miami", "Dallas", "Houston", "Detroit", "Philadelphia", "Washington")
        );
        cityGraph2.addEdge("Seattle", "Chicago", 1737);
        cityGraph2.addEdge("Seattle", "San Francisco", 678);
        cityGraph2.addEdge("San Francisco", "Riverside", 386);
        cityGraph2.addEdge("San Francisco", "Los Angeles", 348);
        cityGraph2.addEdge("Los Angeles", "Riverside", 50);
        cityGraph2.addEdge("Los Angeles", "Phoenix", 357);
        cityGraph2.addEdge("Riverside", "Phoenix", 307);
        cityGraph2.addEdge("Riverside", "Chicago", 1704);
        cityGraph2.addEdge("Phoenix", "Dallas", 887);
        cityGraph2.addEdge("Phoenix", "Houston", 1015);
        cityGraph2.addEdge("Dallas", "Chicago", 805);
        cityGraph2.addEdge("Dallas", "Atlanta", 721);
        cityGraph2.addEdge("Dallas", "Houston", 225);
        cityGraph2.addEdge("Houston", "Atlanta", 702);
        cityGraph2.addEdge("Houston", "Miami", 968);
        cityGraph2.addEdge("Atlanta", "Chicago", 588);
        cityGraph2.addEdge("Atlanta", "Washington", 543);
        cityGraph2.addEdge("Atlanta", "Miami", 604);
        cityGraph2.addEdge("Miami", "Washington", 923);
        cityGraph2.addEdge("Chicago", "Detroit", 238);
        cityGraph2.addEdge("Detroit", "Boston", 613);
        cityGraph2.addEdge("Detroit", "Washington", 396);
        cityGraph2.addEdge("Detroit", "New York", 482);
        cityGraph2.addEdge("Boston", "New York", 190);
        cityGraph2.addEdge("New York", "Philadelphia", 81);
        cityGraph2.addEdge("Philadelphia", "Washington", 123);

        System.out.println(cityGraph2);
        List<WeightedEdge> mst = cityGraph2.mst(0);
        cityGraph2.printWeightedPath(mst);

        System.out.println();

        DijkstraResult dijkstraResult = cityGraph2.dijkstra("Los Angeles");
        Map<String, Double> nameDistance = cityGraph2.distanceArrayToDistanceMap(dijkstraResult.distances);
        System.out.println("Distance from Los Angeles: ");
        nameDistance.forEach((n, d) -> System.out.println(n + ":" + d));
        System.out.println();

        System.out.println("Shortest path from Los Angeles to Boston: ");
        List<WeightedEdge> path = pathMapToPath(cityGraph2.indexOf("Los Angeles"), cityGraph2.indexOf("Boston"),
                dijkstraResult.pathMap);
        cityGraph2.printWeightedPath(path);

    }

}
