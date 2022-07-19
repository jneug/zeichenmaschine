package schule.ngb.zm.ml;

import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;

public class NeuronLayer implements Function<MLMatrix, MLMatrix> {

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

	MLMatrix weights;

	MLMatrix biases;

	NeuronLayer previous, next;

	DoubleUnaryOperator activationFunction, activationFunctionDerivative;

	MLMatrix lastOutput, lastInput;

	public NeuronLayer( int neurons, int inputs ) {
		weights = MatrixFactory
			.create(inputs, neurons)
			.initializeRandom();

		biases = MatrixFactory
			.create(1, neurons)
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

	public MLMatrix getWeights() {
		return weights;
	}

	public MLMatrix getBiases() {
		return biases;
	}

	public int getNeuronCount() {
		return weights.columns();
	}

	public int getInputCount() {
		return weights.rows();
	}

	public MLMatrix getLastOutput() {
		return lastOutput;
	}

	public void setWeights( MLMatrix newWeights ) {
		weights = newWeights.duplicate();
	}

	@Override
	public String toString() {
		return weights.toString() + "\n" + biases.toString();
	}

	@Override
	public MLMatrix apply( MLMatrix inputs ) {
		lastInput = inputs.duplicate();
		lastOutput = inputs
			.multiplyAddBias(weights, biases)
			.applyInPlace(activationFunction);

		if( next != null ) {
			return next.apply(lastOutput);
		} else {
			return lastOutput;
		}
	}

	@Override
	public <V> Function<V, MLMatrix> compose( Function<? super V, ? extends MLMatrix> before ) {
		return ( in ) -> apply(before.apply(in));
	}

	@Override
	public <V> Function<MLMatrix, V> andThen( Function<? super MLMatrix, ? extends V> after ) {
		return ( in ) -> after.apply(apply(in));
	}

	public void backprop( MLMatrix expected, double learningRate ) {
		MLMatrix error, adjustment;
		if( next == null ) {
			error = expected.sub(lastOutput);
		} else {
			error = expected.multiplyTransposed(next.weights);
		}

		error.scaleInPlace(
			lastOutput.apply(this.activationFunctionDerivative)
		);
		// Hier schon leraningRate anwenden?
		// See https://towardsdatascience.com/understanding-and-implementing-neural-networks-in-java-from-scratch-61421bb6352c
		//delta = MLMath.matrixApply(delta, ( x ) -> learningRate * x);
		if( previous != null ) {
			previous.backprop(error, learningRate);
		}

		biases.addInPlace(
			error.colSums().scaleInPlace(
				-learningRate / (double) error.rows()
			)
		);

		adjustment = lastInput.transposedMultiplyAndScale(error, learningRate);
		weights.addInPlace(adjustment);
	}

}
