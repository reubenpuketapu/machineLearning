package decisionTree;

public class LeafNode implements Node {

	private String className;
	private double probablility;

	public LeafNode(String className, double probablility) {
		this.className = className;
		this.probablility = probablility;
	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @return the probablility
	 */
	public double getProbablility() {
		return probablility;
	}

	public void report(String indent) {
		System.out.format("%sClass %s, prob=%4.2f\n", indent, className, probablility);
	}

}
