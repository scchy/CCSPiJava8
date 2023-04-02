package chapter6;

import com.sun.org.glassfish.external.statistics.Statistic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author Scc_hy
 * @version 1.0.0
 * @ClassName KMeans.java
 * @Description
 * @createTime 2023年03月21日 17:05:00
 */
public class KMeans<Point extends DataPoint> {

    // 类中心
    public class Cluster{
        // 类中的点
        public List<Point> points;
        // 类中心
        public DataPoint center;
        public Cluster(List<Point> points, DataPoint randPoint){
            this.points = points;
            this.center = randPoint;
        }
    }

    private List<Point> points;
    private List<Cluster> clusters;

    public KMeans(int k, List<Point> points){
        if (k < 1) {
            throw new IllegalArgumentException("k must be >= 1");
        }
        this.points = points;
        // 对数据进行归一化
        zScoreNormalize();
        // 随机类中心
        clusters = new ArrayList<>();
        for(int i = 0; i < k; i++){
            DataPoint randPoint = randomPoint();
            Cluster cluster = new Cluster(new ArrayList<Point>(), randPoint);
            clusters.add(cluster);
        }
    }

    private List<DataPoint> centroids() {
        return clusters.stream().map(c -> c.center).collect(Collectors.toList());
    }

    private List<Double> dimensionSlice(int dimension){
        return points.stream().map(x -> x.dimensions.get(dimension))
                .collect(Collectors.toList());
    }

    private void zScoreNormalize(){
        List<List<Double>> zscored = new ArrayList<>();
        for(Point p: points){
            zscored.add(new ArrayList<Double>());
        }
        for(int d = 0; d < points.get(0).numDimensions; d++){
            List<Double> dimSlice = dimensionSlice(d);
            Statistics stats = new Statistics(dimSlice);
            List<Double> zscores = stats.zscored();
            for(int idx=0; idx < zscores.size(); idx++){
                zscored.get(idx).add(zscores.get(idx));
            }
        }
        for(int i=0; i<points.size(); i++){
            points.get(i).dimensions = zscored.get(i);
        }
    }

    private DataPoint randomPoint(){
        List<Double> rdDims = new ArrayList<>();
        Random random = new Random();
        for(int d=0; d<points.get(0).numDimensions; d++){
            List<Double> v = dimensionSlice(d);
            Statistics stats = new Statistics(v);
            Double rdV = random.doubles(stats.min(), stats.max()).findFirst().getAsDouble();
            rdDims.add(rdV);
        }
        return new DataPoint(rdDims);
    }

    // 寻找每个点距离最近的中心，并将点归类
    private void assignClusters(){
        // 对每个点计算，到每个类的中心距离，并归类
        for(Point p: points){
            double lowestDistance = Double.MAX_VALUE;
            Cluster closestCluster = clusters.get(0);
            for(Cluster c: clusters){
                double centerDis = p.distance(c.center);
                if(centerDis < lowestDistance){
                    lowestDistance = centerDis;
                    closestCluster = c;
                }
            }
            closestCluster.points.add(p);
        }
    }

    // 更新类中心
    private void generateCentroids(){
        for(Cluster c: clusters){
            if(c.points.isEmpty()){
                continue;
            }
            List<Double> means = new ArrayList<>();
            for(int i = 0; i < c.points.get(0).numDimensions; i++){
                int dim = i;
                Double dimMean = c.points.stream()
                        .mapToDouble(x -> x.dimensions.get(dim)).average().getAsDouble();
                means.add(dimMean);
            }
            c.center = new DataPoint(means);
        }
    }

    // 查看两个数据是否完全一致
    private boolean listsEqual(List<DataPoint> first, List<DataPoint> second){
        if(first.size() != second.size()){
            return false;
        }
        for(int i=0; i < first.size(); i++){
            for(int j=0; j < first.get(0).numDimensions; j++){
                if(first.get(i).dimensions.get(j).doubleValue() != second.get(i).dimensions.get(j).doubleValue()){
                    return false;
                }
            }
        }
        return true;
    }

    // 进行迭代
    public List<Cluster> run(int maxIterations){
        for(int i = 0; i < maxIterations; i++){
            // 进行初始化
            for(Cluster c: clusters){
                c.points.clear();
            }
            // 点归类
            assignClusters();
            List<DataPoint> oldCenters = new ArrayList<>(centroids());
            // 更新类中心
            generateCentroids();
            // 止停条件
            if(listsEqual(oldCenters, centroids())){
                System.out.println("Converged after " + i + " iterations.");
                return clusters;
            }
        }
        return clusters;
    }

    public static void main(String[] args){
        DataPoint p1 = new DataPoint(Arrays.asList(2.0, 1.0, 1.0));
        DataPoint p2 = new DataPoint(Arrays.asList(2.0, 2.0, 5.0));
        DataPoint p3 = new DataPoint(Arrays.asList(3.0, 1.5, 2.5));
        KMeans<DataPoint> kmsTest = new KMeans<>(2, Arrays.asList(p1, p2, p3));
        List<KMeans<DataPoint>.Cluster> testClusters = kmsTest.run(100);
        for (int clusterIndex = 0; clusterIndex < testClusters.size(); clusterIndex++) {
            System.out.println("Cluster " + clusterIndex + ": "
                    + testClusters.get(clusterIndex).points);
        }
    }

}
