package chapter4;

/**
 * @author Scc_hy
 * @version 1.0.0
 * @ClassName WeightedEdge.java
 * @Description
 * @createTime 2023年03月08日 13:05:00
 */
public class WeightedEdge extends Edge implements Comparable<WeightedEdge>{
    public final double weight;

    public WeightedEdge(int f, int t, double weight){
        super(f, t);
        this.weight = weight;
    }

    @Override
    public WeightedEdge reversed(){
        return new WeightedEdge(to, from, weight);
    }

    @Override
    public int compareTo(WeightedEdge oth){
        return Double.compare(weight, oth.weight);
    }

    @Override
    public String toString() {
        return from + " " + weight + "> " + to;
    }
}
