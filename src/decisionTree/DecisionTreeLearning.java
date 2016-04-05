package decisionTree;

import java.util.ArrayList;
import java.util.List;

import decisionTree.Helper.Instance;

public class DecisionTreeLearning {

	private Helper helper;
	private final Node root;

	public DecisionTreeLearning() {

		helper = new Helper();
		helper.readDataFile("hepatitis-training.dat");

		root = buildTree(helper.allInstances, helper.attNames);

		//testInstances((NonLeafNode) root);

		 printTree(root);

	}

	private void testInstances(NonLeafNode node) {

		Helper tester = new Helper();
		tester.readDataFile("hepatitis-test.dat");

		for (Instance instance : tester.allInstances) {

			classInstance(instance, (NonLeafNode) root);

		}

	}

	private void classInstance(Instance instance, Node node) {

		System.out.println(helper.attNames.indexOf(((NonLeafNode) node).getAttribute()));
		System.out.println("****");
		System.out.println(((NonLeafNode) node).getAttribute());
		System.out.println("Class: " + node.getClass());

		if (instance.getAtt(helper.attNames.indexOf(((NonLeafNode) node).getAttribute()))) {

			if (((NonLeafNode) node).getLeft() instanceof NonLeafNode) {
				classInstance(instance, ((NonLeafNode) node).getLeft());
			} else {
				System.out.println("Tested: " + ((LeafNode) node).getClassName() + " Actual: "
						+ helper.categoryNames.get(instance.getCategory()));
			}
		}

		else {

			if (((NonLeafNode) node).getRight() instanceof NonLeafNode) {
				classInstance(instance, ((NonLeafNode) node).getRight());
			} else {
				System.out.println("Tested: " + (((LeafNode)((NonLeafNode) node).getRight())).getClassName() + " Actual: "
						+ helper.categoryNames.get(instance.getCategory()));
			}

		}

	}

	private void printTree(Node node) {

		node.report("");

	}

	public Node buildTree(List<Instance> instances, List<String> attributes) {

		if (instances.isEmpty()) {
			System.out.println("Instances empty");

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
			System.out.println("Purity 1");
			return new LeafNode(helper.categoryNames.get(((Instance) instances.toArray()[0]).getCategory()), 1);
		}

		if (attributes.isEmpty()) {
			System.out.println("Attributes empty");
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
			System.out.println("Else");
			double bestImpurity = Double.MIN_VALUE;

			String bestAttribute = "";
			List<Instance> bestTrueInstances = new ArrayList<>();
			List<Instance> bestFalseInstances = new ArrayList<>();

			for (int index = 0; index < attributes.size(); index++) {

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

				if (bestImpurity < weightedImpurity) {
					bestImpurity = weightedImpurity;
					bestAttribute = attributes.get(index);
					bestTrueInstances = trueInstances;
					bestFalseInstances = falseInstances;

				}
			}

			List<String> newAttributesList = new ArrayList<String>();
			newAttributesList.addAll(attributes);
			newAttributesList.remove(bestAttribute);
			attributes.remove(bestAttribute);

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
		new DecisionTreeLearning();
	}

}
