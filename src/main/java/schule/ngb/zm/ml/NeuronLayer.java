package schule.ngb.zm.ml;

import java.util.Arrays;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;

public class NeuronLayer implements Function<double[][], double[][]> {

	public static NeuronLayer fromArray( double[][] weights ) {
		NeuronLayer layer = new NeuronLayer(weights[0].length, weights.length);
		for( int i = 0; i < weights[0].length; i++ ) {
			for( int j = 0; j < weights.length; j++ ) {
				layer.weights.coefficients[i][j] = weights[i][j];
			}
		}
		return layer;
	}

	public static NeuronLayer fromArray( double[][] weights, double[] biases ) {
		NeuronLayer layer = new NeuronLayer(weights[0].length, weights.length);
		for( int i = 0; i < weights[0].length; i++ ) {
			for( int j = 0; j < weights.length; j++ ) {
				layer.weights.coefficients[i][j] = weights[i][j];
			}
		}
		for( int j = 0; j < biases.length; j++ ) {
			layer.biases[j] = biases[j];
		}
		return layer;
	}
	
	Matrix weights;

	double[] biases;

	NeuronLayer previous, next;

	DoubleUnaryOperator activationFunction, activationFunctionDerivative;

	double[][] lastOutput, lastInput;

	public NeuronLayer( int neurons, int inputs ) {
		weights = new Matrix(inputs, neurons);
		weights.initializeRandom(-1, 1);

		biases = new double[neurons];
		Arrays.fill(biases, 0.0); // TODO: Random?

		activationFunction = MLMath::sigmoid;
		activationFunctionDerivative = MLMath::sigmoidDerivative;
	}

	public void connect( NeuronLayer prev, NeuronLayer next ) {
		setPreviousLayer(prev);
		setNextLayer(next);
	}

	public NeuronLayer getPreviousLayer() {
		return previous;
	}

	public boolean hasPreviousLayer() {
		return previous != null;
	}

	public void setPreviousLayer( NeuronLayer pPreviousLayer ) {
		this.previous = pPreviousLayer;
		if( pPreviousLayer != null ) {
			pPreviousLayer.next = this;
		}
	}

	public NeuronLayer getNextLayer() {
		return next;
	}

	public boolean hasNextLayer() {
		return next != null;
	}

	public void setNextLayer( NeuronLayer pNextLayer ) {
		this.next = pNextLayer;
		if( pNextLayer != null ) {
			pNextLayer.previous = this;
		}
	}

	public Matrix getWeights() {
		return weights;
	}

	public int getNeuronCount() {
		return weights.coefficients[0].length;
	}

	public int getInputCount() {
		return weights.coefficients.length;
	}

	public double[][] getLastOutput() {
		return lastOutput;
	}

	public void setWeights( double[][] newWeights ) {
		weights.coefficients = MLMath.copyMatrix(newWeights);
	}

	public void adjustWeights( double[][] adjustment ) {
		weights.coefficients = MLMath.matrixAdd(weights.coefficients, adjustment);
	}

	@Override
	public String toString() {
		return weights.toString() + "\n" + Arrays.toString(biases);
	}

	@Override
	public double[][] apply( double[][] inputs ) {
		lastInput = inputs;
		lastOutput = MLMath.matrixApply(
			MLMath.biasAdd(
				MLMath.matrixMultiply(inputs, weights.coefficients),
				biases
			),
			activationFunction
		);
		if( next != null ) {
			return next.apply(lastOutput);
		} else {
			return lastOutput;
		}
	}

	@Override
	public <V> Function<V, double[][]> compose( Function<? super V, ? extends double[][]> before ) {
		return ( in ) -> apply(before.apply(in));
	}

	@Override
	public <V> Function<double[][], V> andThen( Function<? super double[][], ? extends V> after ) {
		return ( in ) -> after.apply(apply(in));
	}

	public void backprop( double[][] expected, double learningRate ) {
		double[][] error, delta, adjustment;
		if( next == null ) {
			error = MLMath.matrixSub(expected, this.lastOutput);
		} else {
			error = MLMath.matrixMultiply(expected, MLMath.matrixTranspose(next.weights.coefficients));
		}

		delta = MLMath.matrixScale(error, MLMath.matrixApply(this.lastOutput, this.activationFunctionDerivative));
		// Hier schon leraningRate anwenden?
		// See https://towardsdatascience.com/understanding-and-implementing-neural-networks-in-java-from-scratch-61421bb6352c
		//delta = MLMath.matrixApply(delta, ( x ) -> learningRate * x);
		if( previous != null ) {
			previous.backprop(delta, learningRate);
		}

		biases = MLMath.biasAdjust(biases, MLMath.matrixApply(delta, ( x ) -> learningRate * x));

		adjustment = MLMath.matrixMultiply(MLMath.matrixTranspose(lastInput), delta);
		adjustment = MLMath.matrixApply(adjustment, ( x ) -> learningRate * x);
		this.adjustWeights(adjustment);
	}

}
