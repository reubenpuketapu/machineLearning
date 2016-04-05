package decisionTree;

import java.util.ArrayList;
import java.util.List;

import decisionTree.Helper.Instance;

public class DecisionTreeLearning {

	private Helper helper;
	private final Node root;

	private double correct = 0;
	private double incorrect = 0;

	private double firstClass = 0;
	private double baseline = 0;

	public DecisionTreeLearning(String training, String test) {

		helper = new Helper();
		helper.readDataFile(training);
		calculateBaseline();
		root = buildTree(helper.allInstances, helper.attNames);

		printTree(root);

		System.out.println("\n");
		testInstances((NonLeafNode) root, test);

		System.out.printf("\n\nAccuracy: %4.2f", (correct / (correct + incorrect)));
		System.out.println("%");
		System.out.printf("Baseline classifier: %4.2f", baseline);
		System.out.println("%");


	}

	private void calculateBaseline() {

		for (Instance instance : helper.allInstances) {
			if (instance.getCategory() == 0) {
				firstClass++;
			}
		}
		this.baseline = firstClass / helper.allInstances.size();

	}

	private void testInstances(NonLeafNode node, String test) {

		Helper tester = new Helper();
		tester.readDataFile(test);

		for (Instance instance : tester.allInstances) {

			classInstance(instance, (NonLeafNode) root);

		}

	}

	private void classInstance(Instance instance, Node node) {

		String nodeAttribute = ((NonLeafNode) node).getAttribute();

		boolean branch = instance.getAtt(helper.attNames.indexOf(nodeAttribute));

		if (branch) {

			if (((NonLeafNode) node).getLeft() instanceof NonLeafNode) {

				classInstance(instance, ((NonLeafNode) node).getLeft());

			} else {

				String calculated = ((LeafNode) ((NonLeafNode) node).getLeft()).getClassName();
				String actual = helper.categoryNames.get(instance.getCategory());

				System.out.print("Calc: " + calculated + "\t Actual: " + actual);

				if (calculated.equals(actual)) {
					correct++;
					System.out.println();
				} else {
					System.out.println(" *");
					incorrect++;
				}
			}

		} else {

			if (((NonLeafNode) node).getRight() instanceof NonLeafNode) {

				classInstance(instance, ((NonLeafNode) node).getRight());

			} else {

				String calculated = ((LeafNode) ((NonLeafNode) node).getRight()).getClassName();
				String actual = helper.categoryNames.get(instance.getCategory());

				System.out.print("Calc: " + calculated + "\t Actual: " + actual + "");

				if (calculated.equals(actual)) {
					correct++;
					System.out.println();
				} else {
					System.out.println(" *");
					incorrect++;
				}
			}

		}

	}

	private void printTree(Node node) {
		node.report("");
	}

	public Node buildTree(List<Instance> instances, List<String> attributes) {

		if (instances.isEmpty()) {
			// System.out.println("Instances empty");

			double count1 = 0;
			double count2 = 0;

			for (Instance instance : helper.allInstances) {

				if (instance.getCategory() == 0) {
					count1++;
				} else {
					count2++;
				}

			}

			if (count1 > count2) {
				return new LeafNode(helper.categoryNames.get(0), count1 / (count1 + count2));
			} else {
				return new LeafNode(helper.categoryNames.get(1), count2 / (count1 + count2));
			}

		}

		if (calcPurity(instances) == 0) {
			return new LeafNode(helper.categoryNames.get(((Instance) instances.toArray()[0]).getCategory()), 1);
		}

		if (attributes.isEmpty()) {
			double count1 = 0;
			double count2 = 0;

			for (Instance instance : instances) {

				if (instance.getCategory() == 0) {
					count1++;
				} else {
					count2++;
				}

			}

			if (count1 > count2) {
				return new LeafNode(helper.categoryNames.get(0), count1 / (count1 + count2));
			} else {
				return new LeafNode(helper.categoryNames.get(1), count2 / (count1 + count2));
			}

		}

		else { // find best attribute

			double bestImpurity = Double.MAX_VALUE;

			String bestAttribute = "";
			List<Instance> bestTrueInstances = new ArrayList<>();
			List<Instance> bestFalseInstances = new ArrayList<>();

			for (int index = 0; index < helper.attNames.size(); index++) {

				List<Instance> trueInstances = new ArrayList<>();
				List<Instance> falseInstances = new ArrayList<>();

				for (Instance instance : instances) {
					if (instance.getAtt(index)) {
						trueInstances.add(instance);
					} else {
						falseInstances.add(instance);
					}
				}

				double weightedImpurity = calcWeightedImpurity(calcPurity(trueInstances), calcPurity(falseInstances),
						trueInstances.size(), falseInstances.size(), trueInstances.size() + falseInstances.size());

				if (bestImpurity > weightedImpurity) {
					bestImpurity = weightedImpurity;
					bestAttribute = helper.attNames.get(index);
					bestTrueInstances = trueInstances;
					bestFalseInstances = falseInstances;

				}
			}

			List<String> newAttributesList = new ArrayList<String>();
			newAttributesList.addAll(attributes);
			newAttributesList.remove(bestAttribute);

			Node left = buildTree(bestTrueInstances, newAttributesList);
			Node right = buildTree(bestFalseInstances, newAttributesList);

			return new NonLeafNode(left, right, bestAttribute);

		}
	}

	private double calcPurity(List<Instance> instances) {

		double m = 0;
		double n = 0;

		for (Instance instance : instances) {

			if (instance.getCategory() == 0) {
				m++;
			} else {
				n++;
			}

		}

		return (m * n) / Math.pow((m + n), 2);

	}

	private double calcWeightedImpurity(double impurityTrue, double impurityFalse, double trueInstances,
			double falseInstances, double totalInstances) {

		double weightedImpurity = trueInstances / totalInstances * impurityTrue
				+ falseInstances / totalInstances * impurityFalse;

		return weightedImpurity;
	}

	public static void main(String[] args) {
		// args[0] - training
		// args[1] - test
		new DecisionTreeLearning(args[0], args[1]);
	}

}
