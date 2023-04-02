package chapter7;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.DoubleUnaryOperator;

/**
 * @author Scc_hy
 * @version 1.0.0
 * @ClassName Layer.java
 * @Description
 * @createTime 2023年03月28日 10:06:00
 */
public class Layer {
    public Optional<Layer> preLayer;
    public List<Neuron> neurons = new ArrayList<>();
    public double[] outputCache;

    public Layer(Optional<Layer> preLayer, int numNeurons, double lr, DoubleUnaryOperator activationFunc,
                 DoubleUnaryOperator derivativeActivationFunc){
        this.preLayer = preLayer;
        Random rd = new Random();
        for(int i = 0; i< numNeurons; i++){
            double[] randomWeights = null;
            if(preLayer.isPresent()){
                randomWeights = rd.doubles(preLayer.get().neurons.size()).toArray();
            }
            Neuron neuron = new Neuron(randomWeights, lr, activationFunc, derivativeActivationFunc);
            neurons.add(neuron);
        }
        outputCache = new double[numNeurons];
    }

    // 进行 act(linear(inputs))
    public double[] outputs(double[] inputs){
        if(preLayer.isPresent()){
            return neurons.stream().mapToDouble(n -> n.output(inputs)).toArray();
        }
        return inputs;
    }

    public void calculateDeltasForOutputLayer(double[] expected){
        for(int n = 0; n < neurons.size(); n++) {
            neurons.get(n).delta = neurons.get(n).derivativeActivationFunction.applyAsDouble(neurons.get(n).outputCache)
                    * (expected[n] - outputCache[n]);
        }
    }

    public void calculateDeltasHiddenLayer(Layer nextLayer){
        for(int i = 0; i < neurons.size(); i++) {
            int idx = i;
            double[] nextWeights = nextLayer.neurons.stream().mapToDouble(n->n.weights[idx]).toArray();
            double[] nextDeltas = nextLayer.neurons.stream().mapToDouble(n->n.delta).toArray();
            double sumWeightsAndDeltas = Util.dotProduct(nextWeights, nextDeltas);
            neurons.get(i).delta = neurons.get(i).derivativeActivationFunction
                    .applyAsDouble(neurons.get(i).outputCache) * sumWeightsAndDeltas;
        }
    }

}
