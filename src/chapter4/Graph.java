package chapter4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Scc_hy
 * @version 1.0.0
 * @ClassName Graph.java
 * @Description
 * @createTime 2023年03月08日 12:46:00
 */
public abstract class Graph<V, E extends Edge> {
    private ArrayList<V> vertices = new ArrayList<>();
    protected ArrayList<ArrayList<E>> edges = new ArrayList<>();

    public Graph(){};
    public Graph(List<V> vertices) {
        this.vertices.addAll(vertices);
        for (V vertex : vertices) {
            edges.add(new ArrayList<>());
        }
    }
    // Number of vertices
    public int getVertexCount() {
        return vertices.size();
    }

    // Number of edges
    public int getEdgeCount() {
        return edges.stream().mapToInt(ArrayList::size).sum();
    }

    // Add a vertex to the graph and return its index
    public int addVertex(V vertex) {
        vertices.add(vertex);
        edges.add(new ArrayList<>());
        return getVertexCount() - 1;
    }

    // 基于idx寻找v
    public V vertexAt(int index) {
        return vertices.get(index);
    }

    // 图中 vertex的index
    public int indexOf(V vertex) {
        return vertices.indexOf(vertex);
    }

    // idx对应v 的邻居 v
    public List<V> neighborsOf(int index) {
        return edges.get(index).stream()
                .map(edge -> vertexAt(edge.to))
                .collect(Collectors.toList());
    }

    public List<V> neighborsOf(V vertex) {
        return neighborsOf(indexOf(vertex));
    }

    // idx对应v 的所有邻边
    public List<E> edgesOf(int index) {
        return edges.get(index);
    }
    public List<E> edgesOf(V vertex) {
        return edgesOf(indexOf(vertex));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < getVertexCount(); i++) {
            sb.append(vertexAt(i));
            sb.append(" -> ");
            sb.append(Arrays.toString(neighborsOf(i).toArray()));
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }
}
