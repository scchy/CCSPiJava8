package chapter4;

/**
 * @author Scc_hy
 * @version 1.0.0
 * @ClassName Edge.java
 * @Description
 * @createTime 2023年03月08日 12:47:00
 */
public class Edge {
    public final int from;
    public final int to;

    public Edge(int f, int t){
        this.from = f;
        this.to = t;
    }

    public Edge reversed() {
        return new Edge(to, from);
    }

    @Override
    public String toString() {
        return from + " -> " + to;
    }

}
