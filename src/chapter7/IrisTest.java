package chapter7;

import java.util.*;

/**
 * @author Scc_hy
 * @version 1.0.0
 * @ClassName IrisTest.java
 * @Description
 * @createTime 2023年03月28日 11:09:00
 */
public class IrisTest {
    public static final String IRIS_SETOSA = "Iris-setosa";
    public static final String IRIS_VERSICOLOR = "Iris-versicolor";
    public static final String IRIS_VIRGINICA = "Iris-virginica";

    private List<double[]> irisParameters = new ArrayList<>();
    private List<double[]> irisClassifications = new ArrayList<>();
    private List<String> irisSpecies = new ArrayList<>();

    public IrisTest(){
        List<String[]> irisDataset = Util.loadCSV("/chapter7/data/iris.csv");
        // 乱序
        Collections.shuffle(irisDataset);
        for(String[] iris: irisDataset){
            double[] parameters = Arrays.stream(iris)
                    .limit(4)
                    .mapToDouble(Double::parseDouble)
                    .toArray();
            irisParameters.add(parameters);
            String species = iris[4];
            switch (species) {
                case IRIS_SETOSA:
                    irisClassifications.add(new double[] { 1.0, 0.0, 0.0 });
                    break;
                case IRIS_VERSICOLOR:
                    irisClassifications.add(new double[] { 0.0, 1.0, 0.0 });
                    break;
                default:
                    irisClassifications.add(new double[] { 0.0, 0.0, 1.0 });
                    break;
            }
            irisSpecies.add(species);
        }
        // 数据标准化-基于列
        Util.normalizeByFeatureScaling(irisParameters);
    }

    public String irisInterpretOutput(double[] output){
        double max = Util.max(output);
        if (max == output[0]) {
            return IRIS_SETOSA;
        } else if (max == output[1]) {
            return IRIS_VERSICOLOR;
        } else {
            return IRIS_VIRGINICA;
        }
    }

    public Network<String>.Results classify() {
        // 4个特征， 一个隐含层6个nerous, 输出3
        Network<String> irisNet = new Network<>(new int[]{4, 6, 3}, 0.3, Util::sigmoid, Util::derivativeSigmoid);
        // 训练集
        List<double[]> trX = irisParameters.subList(0, 140);
        List<double[]> trY = irisClassifications.subList(0, 140);
        int nIters = 50;
        for(int i=0; i< nIters; i++){
            irisNet.train(trX, trY);
        }
        List<double[]> teX = irisParameters.subList(140, 150);
        List<String> teY = irisSpecies.subList(140, 150);
        return irisNet.validate(teX, teY, this::irisInterpretOutput);
    }

    public static void main(String[] args){
        IrisTest irisTest = new IrisTest();
        Network<String>.Results results = irisTest.classify();
        System.out.println(results.correct + " correct of " + results.samples + " = " +
                results.percentage * 100 + "%");
    }

}
