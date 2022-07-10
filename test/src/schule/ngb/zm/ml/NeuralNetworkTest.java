package schule.ngb.zm.ml;

import org.junit.jupiter.api.Test;
import schule.ngb.zm.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class NeuralNetworkTest {

	@Test
	void learnCalc() {
		Log.enableGlobalDebugging();

		int INPUT_SIZE = 50;
		int PREDICT_SIZE = 4;
		int TRAINING_CYCLES = 40000;
		CalcType OPERATION = CalcType.SUB;

		// Create neural network with layer1: 4 neurones, layer2: 1 neuron
		NeuralNetwork net = new NeuralNetwork(2, 8, 4, 1);

		List<TestData> trainingData = createTrainingSet(INPUT_SIZE, OPERATION);

		double[][] inputs = new double[INPUT_SIZE][2];
		double[][] outputs = new double[INPUT_SIZE][1];
		for( int i = 0; i < trainingData.size(); i++ ) {
			inputs[i][0] = trainingData.get(i).a;
			inputs[i][1] = trainingData.get(i).b;
			outputs[i][0] = trainingData.get(i).result;
		}

		System.out.println("Training the neural net to learn "+OPERATION+"...");
		net.train(inputs, outputs, TRAINING_CYCLES);
		System.out.println("    finished training");

		for( int i = 1; i <= net.getLayerCount(); i++ ) {
			System.out.println("Layer " +i + " weights");
			System.out.println(net.getLayer(i).weights);
		}

		// calculate the predictions on unknown data
		List<TestData> predictionSet = createTrainingSet(PREDICT_SIZE, OPERATION);
		for( TestData t : predictionSet ) {
			predict(t, net);
		}
	}

	public static void predict( TestData data, NeuralNetwork net ) {
		double[][] testInput = new double[][]{{data.a, data.b}};
		net.predict(testInput);

		// then
		System.out.printf(
			"Prediction on data (%.2f, %.2f) was %.4f, expected %.2f (of by %.4f)\n",
			data.a, data.b,
			net.getOutput()[0][0],
			data.result,
			net.getOutput()[0][0] - data.result
		);
	}

	private List<TestData> createTrainingSet( int trainingSetSize, CalcType operation ) {
		Random random = new Random();
		List<TestData> tuples = new ArrayList<>();

		for( int i = 0; i < trainingSetSize; i++ ) {
			double s1 = random.nextDouble() * 0.5;
			double s2 = random.nextDouble() * 0.5;

			switch( operation ) {
				case ADD:
					tuples.add(new AddData(s1, s2));
					break;
				case SUB:
					tuples.add(new SubData(s1, s2));
					break;
				case MUL:
					tuples.add(new MulData(s1, s2));
					break;
				case DIV:
					tuples.add(new DivData(s1, s2));
					break;
				case MOD:
					tuples.add(new ModData(s1, s2));
					break;
			}
		}

		return tuples;
	}


	private static enum CalcType {
		ADD, SUB, MUL, DIV, MOD
	}

	private static abstract class TestData {

		double a;
		double b;
		double result;
		CalcType type;

		TestData( double a, double b ) {
			this.a = a;
			this.b = b;
		}

	}

	private static final class AddData extends TestData {

		CalcType type = CalcType.ADD;

		public AddData( double a, double b ) {
			super(a, b);
			result = a + b;
		}

	}

	private static final class SubData extends TestData {

		CalcType type = CalcType.SUB;

		public SubData( double a, double b ) {
			super(a, b);
			result = a - b;
		}

	}

	private static final class MulData extends TestData {

		CalcType type = CalcType.MUL;

		public MulData( double a, double b ) {
			super(a, b);
			result = a * b;
		}

	}

	private static final class DivData extends TestData {

		CalcType type = CalcType.DIV;

		public DivData( double a, double b ) {
			super(a, b);
			if( b == 0.0 ) {
				b = .1;
			}
			result = a / b;
		}

	}

	private static final class ModData extends TestData {

		CalcType type = CalcType.MOD;

		public ModData( double b, double a ) {
			super(b, a);
			result = a % b;
		}

	}

}
