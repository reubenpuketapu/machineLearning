package perceptron;

public class Image {

	private int[][] pixels;

	private int width;
	private int height;

	private String classed;

	public Image(int[][] pixels, int width, int height, String classed) {

		this.pixels = pixels;
		this.width = width;
		this.height = height;
		this.classed = classed;
	}

	public int[][] getPixels() {
		return pixels;
	}

	public String getClassed() {
		return classed;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

}
