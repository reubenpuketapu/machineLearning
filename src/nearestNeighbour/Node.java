package nearestNeighbour;

public class Node {

	private double sLength;
	private double sWidth;
	private double pLength;
	private double pWidth;
	private String className;
	
	private String testOutcome;

	public Node(double sLength, double sWidth, double pLength, double pWidth, String className) {
		this.sLength = sLength;
		this.sWidth = sWidth;
		this.pLength = pLength;
		this.pWidth = pWidth;
		this.className = className;
	}

	/**
	 * @return the sLength
	 */
	public double getsLength() {
		return sLength;
	}

	/**
	 * @return the sWidth
	 */
	public double getsWidth() {
		return sWidth;
	}

	/**
	 * @return the pLength
	 */
	public double getpLength() {
		return pLength;
	}

	/**
	 * @return the pWidth
	 */
	public double getpWidth() {
		return pWidth;
	}

	/**
	 * @return the type
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @return the testOutcome
	 */
	public String getTestOutcome() {
		return testOutcome;
	}

	/**
	 * @param testOutcome the testOutcome to set
	 */
	public void setTestOutcome(String testOutcome) {
		this.testOutcome = testOutcome;
	}

}
