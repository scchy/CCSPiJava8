package chapter6;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Scc_hy
 * @version 1.0.0
 * @ClassName DataPoint.java
 * @Description
 * @createTime 2023年03月21日 17:00:00
 */
public class DataPoint {
    public final int numDimensions;
    private List<Double> originals;
    public List<Double> dimensions;
    public DataPoint(List<Double> init){
        originals = init;
        dimensions = new ArrayList<>(init);
        numDimensions = dimensions.size();
    }

    // 欧式距离
    public double distance(DataPoint other){
        double diff = 0.0;
        for(int i = 0; i < numDimensions; i++){
            double error = dimensions.get(i) - other.dimensions.get(i);
            diff += Math.pow(error, 2);
        }
        return Math.sqrt(diff);
    }

    @Override
    public String toString(){
        return originals.toString();
    }
}
