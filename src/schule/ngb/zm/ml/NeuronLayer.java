package schule.ngb.zm.ml;

import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;

public class NeuronLayer implements Function<double[][], double[][]> {

	Matrix weights;

	NeuronLayer previous, next;

	DoubleUnaryOperator activationFunction, activationFunctionDerivative;

	double[][] lastOutput, lastInput;

	public NeuronLayer( int neurons, int inputs ) {
		weights = new Matrix(inputs, neurons);
		weights.initializeRandom(-1, 1);

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
		return weights.toString();
	}

	@Override
	public double[][] apply( double[][] inputs ) {
		lastInput = inputs;
		lastOutput = MLMath.matrixApply(MLMath.matrixMultiply(inputs, weights.coefficients), activationFunction);
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

		delta = MLMath.matrixScale(error, MLMath.matrixApply(this.lastOutput,this.activationFunctionDerivative));
		if( previous != null ) {
			previous.backprop(delta, learningRate);
		}

		adjustment = MLMath.matrixMultiply(MLMath.matrixTranspose(lastInput), delta);
		adjustment = MLMath.matrixApply(adjustment, ( x ) -> learningRate * x);
		this.adjustWeights(adjustment);
	}

}
