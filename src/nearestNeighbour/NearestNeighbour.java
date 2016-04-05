package nearestNeighbour;

import java.io.*;
import java.util.*;

public class NearestNeighbour {

	private Set<Node> trainingSet;
	private Set<Node> testSet;
	private List<Double> ranges;

	private static int k;

	public NearestNeighbour(String testName, String trainingName) {
		trainingSet = new HashSet<>();
		testSet = new HashSet<>();
		ranges = new ArrayList<Double>();

		runAlgorithm(testName, trainingName);
	}

	private void runAlgorithm(String testName, String trainingName) {

		scanK();

		scanNodes(trainingName, trainingSet);

		findRangeValues(trainingSet);

		scanNodes(testName, testSet);

		System.out.println("\nIncorrect values: ");
		System.out.println("--------------");

		System.out.println("   Actual\t\t   Outcome");
		classNodes();

	}

	private void findRangeValues(Set<Node> learningSet) {

		double[] minMaxValues = new double[8];

		// initialize array with appropriate compare values
		for (int i = 0; i < 8; i++) {
			if (i % 2 == 0) {
				minMaxValues[i] = Double.MAX_VALUE;
			} else {
				minMaxValues[i] = Double.MIN_VALUE;
			}
		}

		// iterate through training set comparing the max / min values
		for (Node node : learningSet) {

			if (node.getsLength() < minMaxValues[0]) {
				minMaxValues[0] = node.getsLength();
			}
			if (node.getsLength() > minMaxValues[1]) {
				minMaxValues[1] = node.getsLength();
			}
			if (node.getsWidth() < minMaxValues[2]) {
				minMaxValues[2] = node.getsWidth();
			}
			if (node.getsWidth() > minMaxValues[3]) {
				minMaxValues[3] = node.getsWidth();
			}
			if (node.getpLength() < minMaxValues[4]) {
				minMaxValues[4] = node.getpLength();
			}
			if (node.getpLength() > minMaxValues[5]) {
				minMaxValues[5] = node.getpLength();
			}
			if (node.getpWidth() < minMaxValues[6]) {
				minMaxValues[6] = node.getpWidth();
			}
			if (node.getpWidth() > minMaxValues[7]) {
				minMaxValues[7] = node.getpWidth();
			}

		}

		// calculate ranges with min / max values
		int j = 0;
		for (int i = 0; i < 4; i++) {
			ranges.add(Math.abs(minMaxValues[j] - minMaxValues[j + 1]));
			j += 2;
		}

	}

	private void classNodes() {

		double accuracy = 0;

		// Iterate through all of the test nodes and classify them into class
		// instances
		

		for (Node testNode : testSet) {

			ArrayList<Node> nodes = new ArrayList<>();
			nodes.addAll(trainingSet);

			// Sort the test set based on the given comparator
			Collections.sort(nodes, comparator(testNode));

			// Now order the top 'k' classes in order of frequency into a tree
			// map
			TreeMap<String, Integer> classes = new TreeMap<>();
			for (int i = 0; i < k; i++) {
				if (classes.containsKey(nodes.get(i).getClassName())) {

					int count = classes.get(nodes.get(i).getClassName());
					classes.replace(nodes.get(i).getClassName(), count + 1);

				} else {
					classes.put(nodes.get(i).getClassName(), 1);
				}
			}

			// Iterate through the tree finding the most common class
			int highest = 0;
			String name = null;

			for (Map.Entry<String, Integer> entry : classes.entrySet()) {
				if (entry.getValue() > highest) {
					highest = entry.getValue();
					name = entry.getKey();
				}
			}

			// set the test outcome to the most commonly occurring class in the
			// top 'k' instances
			testNode.setTestOutcome(name);
			
			
			if (!testNode.getClassName().equalsIgnoreCase(testNode.getTestOutcome())) {
				accuracy++;
				System.out.println(testNode.getClassName() + "\t\t" + testNode.getTestOutcome());

			}
		}
		
		System.out.println("--------------");

		System.out.printf("\nAccuracy: %.2f", (testSet.size() - accuracy) / testSet.size() * 100);
		System.out.println("%");
		System.out.println((int) accuracy + " out of " + testSet.size() + " classes were incorrectly assumed");

	}

	private Comparator<Node> comparator(Node testNode) {

		// create a new comparator based on the distance vector algorithm from
		// two nodes to the given test node
		return new Comparator<Node>() {
			@Override
			public int compare(Node o1, Node o2) {

				double node1 = Math
						.sqrt((Math.pow((o1.getsLength() - testNode.getsLength()), 2)) / Math.pow(ranges.get(0), 2)
								+ (Math.pow(o1.getsWidth() - testNode.getsWidth(), 2)) / Math.pow(ranges.get(1), 2)
								+ (Math.pow(o1.getpLength() - testNode.getpLength(), 2)) / Math.pow(ranges.get(2), 2)
								+ (Math.pow(o1.getpWidth() - testNode.getpWidth(), 2)) / Math.pow(ranges.get(3), 2));

				double node2 = Math
						.sqrt((Math.pow((o2.getsLength() - testNode.getsLength()), 2)) / Math.pow(ranges.get(0), 2)
								+ (Math.pow(o2.getsWidth() - testNode.getsWidth(), 2)) / Math.pow(ranges.get(1), 2)
								+ (Math.pow(o2.getpLength() - testNode.getpLength(), 2)) / Math.pow(ranges.get(2), 2)
								+ (Math.pow(o2.getpWidth() - testNode.getpWidth(), 2)) / Math.pow(ranges.get(3), 2));

				if (node1 < node2) {
					return -1;
				} else if (node1 > node2) {
					return 1;
				} else {
					return 0;
				}

			}
		};
	}

	private void scanK() {

		Scanner scanner = new Scanner(System.in);
		System.out.printf("Please enter value for k: ");
		k = scanner.nextInt();
		scanner.close();

	}

	/*
	 * Scan through the file and add the new nodes to the given set
	 */
	private void scanNodes(String filename, Set<Node> set) {

		try {

			Scanner sc = new Scanner(new File(filename));

			while (sc.hasNext()) {
				double sLength = sc.nextDouble();
				double sWidth = sc.nextDouble();
				double pLength = sc.nextDouble();
				double pWidth = sc.nextDouble();
				String type = sc.next();

				set.add(new Node(sLength, sWidth, pLength, pWidth, type));

			}

			sc.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		new NearestNeighbour(args[1], args[0]);
	}

}
