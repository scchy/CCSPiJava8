package chapter6;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author Scc_hy
 * @version 1.0.0
 * @ClassName Statistics.java
 * @Description
 * @createTime 2023年03月26日 18:50:00
 */
public final class Statistics {
    private List<Double> list;
    private DoubleSummaryStatistics dss;

    public Statistics(List<Double> list){
        this.list = list;
        dss = list.stream().collect(
                Collectors.summarizingDouble(d -> d)
        );
    }

    public double sum(){
        return dss.getSum();
    }

    public double mean(){
        return dss.getAverage();
    }

    public double variance(){
        double m = mean();
        return list.stream().mapToDouble(x -> Math.pow((x - m), 2))
                .average().getAsDouble();
    }

    public double std(){
        return Math.sqrt(variance());
    }

    // (x - mean) / std
    public List<Double> zscored(){
        double mean = mean();
        double std = std();
        return list.stream()
                .map(x -> std != 0 ? ((x - mean)/std) : 0.0)
                .collect(Collectors.toList());
    }

    public double max() {
        return dss.getMax();
    }

    public double min() {
        return dss.getMin();
    }


}
