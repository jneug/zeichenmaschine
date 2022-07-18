package schule.ngb.zm.ml;

import java.util.Arrays;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;

public class NeuronLayer implements Function<Matrix, Matrix> {

	/*public static NeuronLayer fromArray( double[][] weights ) {
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
	}*/

	Matrix weights;

	Matrix biases;

	NeuronLayer previous, next;

	DoubleUnaryOperator activationFunction, activationFunctionDerivative;

	Matrix lastOutput, lastInput;

	public NeuronLayer( int neurons, int inputs ) {
		weights = MatrixFactory
			.create(inputs, neurons)
			.initializeRandom(-1, 1);

		biases = MatrixFactory
			.create(neurons, 1)
			.initializeZero();

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

	public Matrix getBiases() {
		return biases;
	}

	public int getNeuronCount() {
		return weights.columns();
	}

	public int getInputCount() {
		return weights.rows();
	}

	public Matrix getLastOutput() {
		return lastOutput;
	}

	public void setWeights( Matrix newWeights ) {
		weights = newWeights.duplicate();
	}

	public void adjustWeights( Matrix adjustment ) {
		weights.add(adjustment);
	}

	@Override
	public String toString() {
		return weights.toString() + "\n" + biases.toString();
	}

	@Override
	public Matrix apply( Matrix inputs ) {
		lastInput = inputs;
		lastOutput = inputs
			.multiplyAddBias(weights, biases)
			.apply(activationFunction);

		if( next != null ) {
			return next.apply(lastOutput);
		} else {
			return lastOutput;
		}
	}

	@Override
	public <V> Function<V, Matrix> compose( Function<? super V, ? extends Matrix> before ) {
		return ( in ) -> apply(before.apply(in));
	}

	@Override
	public <V> Function<Matrix, V> andThen( Function<? super Matrix, ? extends V> after ) {
		return ( in ) -> after.apply(apply(in));
	}

	public void backprop( Matrix expected, double learningRate ) {
		Matrix error, delta, adjustment;
		if( next == null ) {
			error = expected.duplicate().sub(lastOutput);
		} else {
			error = expected.duplicate().multiply(next.weights.transpose());
		}

		error.scale(lastOutput.duplicate().apply(this.activationFunctionDerivative));
		// Hier schon leraningRate anwenden?
		// See https://towardsdatascience.com/understanding-and-implementing-neural-networks-in-java-from-scratch-61421bb6352c
		//delta = MLMath.matrixApply(delta, ( x ) -> learningRate * x);
		if( previous != null ) {
			previous.backprop(error, learningRate);
		}

		// biases = MLMath.biasAdjust(biases, MLMath.matrixApply(delta, ( x ) -> learningRate * x));

		adjustment = lastInput.duplicate().transpose().multiply(error).apply((d) -> learningRate*d);
		this.adjustWeights(adjustment);
	}

}
