package perceptron;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Perceptron {

	private final static int FEATURES_COUNT = 50;
	private final static int ITERATIONS_COUNT = 100;

	private List<Image> images;
	private List<Feature> features;

	private List<double[]> calculatedFeatures;
	private double[] weights;

	public Perceptron(String filename) {
		images = new ArrayList<>();
		features = new ArrayList<>();
		calculatedFeatures = new ArrayList<>();
		readFile(filename);
		setupFeatures();
		weights = new double[features.size() + 1];
		setupWeights();
		calculateFeatures();

		System.out.println("Using " + FEATURES_COUNT + " features ");
		System.out.println("Running a maximum of " + ITERATIONS_COUNT + " iterations\n-------------");

		learningAlgorithm();

		printRandomFeatures();
		printFinalWeights();

	}

	private void printFinalWeights() {
		System.out.println("Final weights: \n");
		for (double weight : weights) {
			System.out.println(weight);
		}
	}

	private void learningAlgorithm() {

		int iterations, correctCount;

		for (iterations = 0, correctCount = 0; iterations < ITERATIONS_COUNT
				&& correctCount < images.size(); iterations++) {
			for (int i = 0; i < images.size(); i++) {

				double[] imageFeatures = calculatedFeatures.get(i);
				double calculation = perceptronClassifier(imageFeatures);
				boolean exampleClass = images.get(i).getClassed().equals("#Yes");

				if ((calculation > 0 && exampleClass) || (calculation <= 0 && !exampleClass)) {
					// Perceptron is correct!! Do nothing
				}
				else if (exampleClass) { // positive example and wrong

					// add feature array from the weight array
					for (int j = 0; j < weights.length; j++) {
						weights[j] += imageFeatures[j];
					}
				}
				else if (!exampleClass) { // negative example and wrong
					// subtract feature vector from weight vector
					for (int j = 0; j < weights.length; j++) {
						weights[j] -= imageFeatures[j];
					}
				}
			}
			
			// Calculate number of correct instances
			correctCount = 0;
			for (int i = 0; i < images.size(); i++) {

				double calculation = perceptronClassifier(calculatedFeatures.get(i));
				boolean correct = images.get(i).getClassed().equals("#Yes");

				if ((calculation > 0 && correct) || (calculation <= 0 && !correct)) {
					correctCount++;
				}
			}
		}

		// Print out useful information

		System.out.println("Training cycles run " + iterations + " times");
		if (iterations == ITERATIONS_COUNT) {
			System.out.println("Stopped due to max number of iterations: " + ITERATIONS_COUNT);
		} else {
			System.out.println("Stopped becuase the perceptron is correct on all images");
		}
		System.out.println("Number of correct images: " + correctCount);
		System.out.println("Number of incorrect images: " + (images.size() - correctCount));

		System.out.println("-------------");

	}

	private void setupWeights() {

		for (int i = 0; i < weights.length; i++) {
			weights[i] = Math.random() - 0.5;
		}

	}

	private void calculateFeatures() {

		for (Image image : images) {
			calculatedFeatures.add(evalFeatures(image));
		}
	}

	private void setupFeatures() {
		for (int i = 0; i < FEATURES_COUNT; i++) {
			features.add(new Feature());
		}
	}

	private double perceptronClassifier(double[] features) {
		double p = 0;
		for (int i = 0; i < weights.length; i++) {
			p += weights[i] * features[i];
		}
		return p;
	}

	private double[] evalFeatures(Image img) {
		double[] fv = new double[features.size() + 1];
		// dummy feature
		fv[0] = 1;
		int i = 1;
		for (Feature f : features) {
			fv[i++] = f.value(img);
		}
		return fv;
	}

	private void printRandomFeatures() {
		System.out.println();
		System.out.println("Random features: \n");
		for (Feature feature : features) {
			feature.print();
			System.out.println("-------------");
		}
	}

	public void readFile(String filename) {
		Scanner scanner;
		try {
			scanner = new Scanner(new File(filename));

			while (scanner.hasNext()) {
				scanner.next();
				String classed = scanner.next();
				int width = scanner.nextInt();
				int height = scanner.nextInt();

				String pointString = scanner.next();
				pointString += scanner.next();

				int[][] pixels = new int[width][height];
				int count = 0;
				for (int i = 0; i < height; i++) {

					for (int j = 0; j < width; j++) {
						pixels[i][j] = Integer.parseInt(pointString.substring(count, ++count));
					}
				}
				images.add(new Image(pixels, width, height, classed));

			}

		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public static void main(String[] args) {
		// args[0] - image.data
		new Perceptron(args[0]);
	}

}
