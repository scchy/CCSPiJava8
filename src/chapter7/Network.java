package chapter7;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
/**
 * @author Scc_hy
 * @version 1.0.0
 * @ClassName Network.java
 * @Description
 * @createTime 2023年03月28日 10:48:00
 */
public class Network<T> {
    private List<Layer> layers = new ArrayList<>();

    public Network(int[] layerStructure, double lr,
                   DoubleUnaryOperator activationFunc,
                   DoubleUnaryOperator derivativeActivationFunc){
        if(layerStructure.length < 3){
            throw  new IllegalArgumentException("Error: Should be at least 3 layers (1 input 1 hidden 1 output)");
        }
        // 输入
        Layer inputLayer = new Layer(Optional.empty(), layerStructure[0], lr, activationFunc, derivativeActivationFunc);
        layers.add(inputLayer);
        // hidden
        for (int i = 1; i < layerStructure.length; i++) {
            Layer nextLayer = new Layer(Optional.of(layers.get(i - 1)), layerStructure[i], lr,
                    activationFunc,
                    derivativeActivationFunc);
            layers.add(nextLayer);
        }
    }

    // 前向传播
    private double[] outputs(double[] input){
        double[] res = input;
        for(Layer l: layers){
            res = l.outputs(res);
        }
        return res;
    }

    // 反向传播
    private void backward(double[] expected){
        int lastLayer = layers.size() - 1;
        layers.get(lastLayer).calculateDeltasForOutputLayer(expected);
        for (int i = lastLayer - 1; i >= 0; i--) {
            layers.get(i).calculateDeltasHiddenLayer(layers.get(i + 1));
        }
    }

    // 更新参数
    private void updateWeights(){
        for(Layer l: layers.subList(1, layers.size())){
            for(Neuron n: l.neurons){
                for(int w = 0; w < n.weights.length; w++){
                    n.weights[w] = n.weights[w] + (n.learningRate *
                            l.preLayer.get().outputCache[w] * n.delta);
                }
            }
        }
    }

    public void train(List<double[]> inputs, List<double[]> expecteds){
        for (int i = 0; i < inputs.size(); i++) {
            double[] xs = inputs.get(i);
            double[] ys = expecteds.get(i);
            double[] xsPred = outputs(xs);
            backward(ys);
            updateWeights();
        }
    }

    public class Results{
        public final int correct;
        public final int samples;
        public final double percentage;

        public Results(int correct, int samples, double percentage) {
            this.correct = correct;
            this.samples = samples;
            this.percentage = percentage;
        }
    }

    public Results validate(List<double[]> inputs, List<T> expecteds, Function<double[], T> interpret){
        int correct = 0;
        for(int i=0; i < inputs.size(); i++){
            double[] input = inputs.get(i);
            T expected = expecteds.get(i);
            T res = interpret.apply(outputs(input));
            if(res.equals(expected)){
                correct++;
            }
        }
        double pct = (double) correct / (double) inputs.size();
        return new Results(correct, inputs.size(), pct);
    }
}
