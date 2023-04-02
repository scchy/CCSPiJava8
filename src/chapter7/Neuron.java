package chapter7;

import java.util.function.DoubleUnaryOperator;

/**
 * @author Scc_hy
 * @version 1.0.0
 * @ClassName Neuron.java
 * @Description
 * @createTime 2023年03月26日 19:19:00
 */
public class Neuron {
    public double[] weights;
    public final double learningRate;
    public double outputCache;
    public double delta;
    public final DoubleUnaryOperator activationFunction;
    public final DoubleUnaryOperator derivativeActivationFunction;

    public Neuron(double[] weights, double learningRate, DoubleUnaryOperator actFunc, DoubleUnaryOperator derivativeActFunc){
        this.weights = weights;
        this.learningRate = learningRate;
        outputCache = 0.0;
        delta = 0.0;
        this.activationFunction = actFunc;
        this.derivativeActivationFunction = derivativeActFunc;
    }

    public double output(double[] inputs){
        outputCache = Util.dotProduct(inputs, weights);
        return activationFunction.applyAsDouble(outputCache);
    }
}
