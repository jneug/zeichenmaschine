package schule.ngb.zm.ml;

import schule.ngb.zm.util.Log;
import schule.ngb.zm.util.ResourceStreamProvider;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class NeuralNetwork {

	public static void saveToFile( String source, NeuralNetwork network ) {
		try(
			Writer writer = ResourceStreamProvider.getWriter(source);
			PrintWriter out = new PrintWriter(writer)
		) {
			for( NeuronLayer layer: network.layers ) {
				out.print(layer.getNeuronCount());
				out.print(' ');
				out.print(layer.getInputCount());
				out.println();

				for( int i = 0; i < layer.getInputCount(); i++ ) {
					for( int j = 0; j < layer.getNeuronCount(); j++ ) {
						out.print(layer.weights.coefficients[i][j]);
						out.print(' ');
					}
					out.println();
				}
				for( int j = 0; j < layer.getNeuronCount(); j++ ) {
					out.print(layer.biases[j]);
					out.print(' ');
				}
				out.println();
			}
			out.flush();
		} catch( IOException ex ) {
			LOG.warn(ex, "");
		}
	}

	public static NeuralNetwork loadFromFile( String source ) {
		try(
			Reader reader = ResourceStreamProvider.getReader(source);
			BufferedReader in = new BufferedReader(reader)
		) {
			List<NeuronLayer> layers = new LinkedList<>();
			String line;
			while( (line = in.readLine()) != null ) {
				String[] split = line.split(" ");
				int neurons = Integer.parseInt(split[0]);
				int inputs = Integer.parseInt(split[1]);

				NeuronLayer layer = new NeuronLayer(neurons, inputs);
				for( int i = 0; i < inputs; i++ ) {
					split = in.readLine().split(" ");
					for( int j = 0; j < neurons; j++ ) {
						layer.weights.coefficients[i][j] = Double.parseDouble(split[j]);
					}
				}
				// Load Biases
				split = in.readLine().split(" ");
				for( int j = 0; j < neurons; j++ ) {
					layer.biases[j] = Double.parseDouble(split[j]);
				}

				layers.add(layer);
			}

			return new NeuralNetwork(layers);
		} catch( IOException | NoSuchElementException ex ) {
			LOG.warn(ex, "Could not load neural network from source <%s>", source);
		}
		return null;
	}

	/*public static NeuralNetwork loadFromFile( String source ) {
		try(
			InputStream stream = ResourceStreamProvider.getInputStream(source);
			Scanner in = new Scanner(stream)
		) {
			List<NeuronLayer> layers = new LinkedList<>();
			while( in.hasNext() ) {
				int neurons = in.nextInt();
				int inputs = in.nextInt();

				NeuronLayer layer = new NeuronLayer(neurons, inputs);
				for( int i = 0; i < inputs; i++ ) {
					for( int j = 0; j < neurons; j++ ) {
						layer.weights.coefficients[i][j] = in.nextDouble();
					}
				}
				for( int j = 0; j < neurons; j++ ) {
					layer.biases[j] = in.nextDouble();
				}

				layers.add(layer);
			}

			return new NeuralNetwork(layers);
		} catch( IOException | NoSuchElementException ex ) {
			LOG.warn(ex, "Could not load neural network from source <%s>", source);
		}
		return null;
	}*/

	private NeuronLayer[] layers;

	private double[][] output;

	private double learningRate = 0.1;

	public NeuralNetwork( int inputs, int layer1, int outputs ) {
		this(new NeuronLayer(layer1, inputs), new NeuronLayer(outputs, layer1));
	}

	public NeuralNetwork( int inputs, int layer1, int layer2, int outputs ) {
		this(new NeuronLayer(layer1, inputs), new NeuronLayer(layer2, layer1), new NeuronLayer(outputs, layer2));
	}

	public NeuralNetwork( int inputs, int layer1, int layer2, int layer3, int outputs ) {
		this(new NeuronLayer(layer1, inputs), new NeuronLayer(layer2, layer1), new NeuronLayer(layer3, layer2), new NeuronLayer(outputs, layer3));
	}

	public NeuralNetwork( List<NeuronLayer> layers ) {
		this.layers = new NeuronLayer[layers.size()];
		for( int i = 0; i < layers.size(); i++ ) {
			this.layers[i] = layers.get(i);
			if( i > 0 ) {
				this.layers[i-1].setNextLayer(this.layers[i]);
			}
		}
	}

	public NeuralNetwork( NeuronLayer... layers ) {
		this.layers = new NeuronLayer[layers.length];
		for( int i = 0; i < layers.length; i++ ) {
			this.layers[i] = layers[i];
			if( i > 0 ) {
				this.layers[i-1].setNextLayer(this.layers[i]);
			}
		}
	}

	public int getLayerCount() {
		return layers.length;
	}
	public NeuronLayer[] getLayers() {
		return layers;
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
		layers[layers.length - 1].backprop(expected, learningRate);
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
