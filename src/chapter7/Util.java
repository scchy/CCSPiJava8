package chapter7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Scc_hy
 * @version 1.0.0
 * @ClassName Util.java
 * @Description
 * @createTime 2023年03月26日 19:06:00
 */
public final class Util {

    // 一维向量点积
    public static double dotProduct(double[] xs, double[] ys){
        double sum = 0.0;
        for(int i = 0; i < xs.length; i++){
            sum += xs[i] * ys[i];
        }
        return sum;
    }

    public static double sigmoid(double x){
        return 1.0 / (1.0 + Math.exp(-x));
    }

    // sigmoid 导数
    public static double derivativeSigmoid(double x) {
        double sig = sigmoid(x);
        return sig * (1.0 - sig);
    }
    // 列标准化 (c - c.min) / (c.max - c.min)
    public static void normalizeByFeatureScaling(List<double []> dataset){
        for(int col = 0; col < dataset.get(0).length; col++){
            List<Double> column = new ArrayList<>();
            for(double[] row: dataset){
                column.add(row[col]);
            }
            double max = Collections.max(column);
            double min = Collections.min(column);
            double diff = max - min;
            for(double[] row: dataset){
                row[col] = (row[col] - min) / diff;
            }
        }
    }

    // 从csv读取数据
    public static List<String[]> loadCSV(String filename){
        try(InputStream ips = Util.class.getResourceAsStream(filename)){
            InputStreamReader ipsReader = new InputStreamReader(ips);
            BufferedReader bfReader = new BufferedReader(ipsReader);
            return bfReader.lines().map(l -> l.split(","))
                    .collect(Collectors.toList());
        }
        catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static double max(double[] num){
        return Arrays.stream(num).max().orElse(Double.MIN_VALUE);
    }


}
