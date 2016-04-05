package decisionTree;

public class NonLeafNode implements Node {

	private Node left;
	private Node right;

	private String attributeName;

	public NonLeafNode(Node left, Node right, String attributeName) {
		this.left = left;
		this.right = right;
		this.attributeName = attributeName;
	}

	public Node getLeft() {
		return left;
	}

	public Node getRight() {
		return right;
	}

	public String getAttribute() {
		return attributeName;
	}

	public void report(String indent) {
		System.out.format("%s%s = True:\n", indent, attributeName);
		left.report(indent + " ");
		System.out.format("%s%s = False:\n", indent, attributeName);
		right.report(indent + " ");
	}

}
