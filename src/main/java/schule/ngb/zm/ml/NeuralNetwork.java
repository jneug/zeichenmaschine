package schule.ngb.zm.ml;

import schule.ngb.zm.util.Log;

public class NeuralNetwork {

	private NeuronLayer[] layers;

	private double[][] output;

	private double learningRate = 0.1;

	public NeuralNetwork( int inputs, int layer1, int outputs ) {
		this(new NeuronLayer(layer1, inputs), new NeuronLayer(outputs, layer1));
	}

	public NeuralNetwork( int inputs, int layer1, int layer2, int outputs ) {
		this(new NeuronLayer(layer1, inputs), new NeuronLayer(layer2, layer1), new NeuronLayer(outputs, layer2));
	}

	public NeuralNetwork( NeuronLayer layer1, NeuronLayer layer2 ) {
		this.layers = new NeuronLayer[2];
		this.layers[0] = layer1;
		this.layers[1] = layer2;
		layer1.connect(null, layer2);
		layer2.connect(layer1, null);
	}

	public NeuralNetwork( NeuronLayer layer1, NeuronLayer layer2, NeuronLayer layer3 ) {
		this.layers = new NeuronLayer[3];
		this.layers[0] = layer1;
		this.layers[1] = layer2;
		this.layers[2] = layer3;
		layer1.connect(null, layer2);
		layer2.connect(layer1, layer3);
		layer3.connect(layer2, null);
	}

	public int getLayerCount() {
		return layers.length;
	}

	public NeuronLayer getLayer( int i ) {
		return layers[i - 1];
	}

	public double getLearningRate() {
		return learningRate;
	}

	public void setLearningRate( double pLearningRate ) {
		this.learningRate = pLearningRate;
	}

	public double[][] getOutput() {
		return output;
	}

	public double[][] predict( double[][] inputs ) {
		//this.output = layers[1].apply(layers[0].apply(inputs));
		this.output = layers[0].apply(inputs);
		return this.output;
	}

	public void learn( double[][] expected ) {
		layers[layers.length-1].backprop(expected, learningRate);
	}

	public void train( double[][] inputs, double[][] expected, int iterations/*, double minChange, int timeout */ ) {
		for( int i = 0; i < iterations; i++ ) {
			// pass the training set through the network
			predict(inputs);
			// start backpropagation through all layers
			learn(expected);

			if( i % 10000 == 0 ) {
				LOG.trace("Training iteration %d of %d", i, iterations);
			}
		}
	}

	private static final Log LOG = Log.getLogger(NeuralNetwork.class);

}
