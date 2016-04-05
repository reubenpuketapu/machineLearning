package perceptron;

import java.util.Random;

public class Feature {

	static int num = 4;

	int[] row = new int[num];
	int[] col = new int[num];
	boolean[] sgn = new boolean[num];

	public Feature() {
		Random r = new Random();
		for (int i = 0; i < num; i++) {
			row[i] = r.nextInt(10);
			col[i] = r.nextInt(10);
			sgn[i] = r.nextBoolean();
		}
	}

	public double value(Image image) {

		int sum = 0;
		for (int i = 0; i < num; i++) {
			if ((image.getPixels()[row[i]][col[i]] == 1) == sgn[i]) {
				sum++;
			}
		}
		return sum >= 3 ? 1 : 0;

	}

	public void print() {
		System.out.println("row: {" + row[0] + ", " + row[1] + ", " + row[2] + ", " + row[3] + "}");
		System.out.println("col: {" + col[0] + ", " + col[1] + ", " + col[2] + ", " + col[3] + "}");
		System.out.println("sgn: {" + sgn[0] + ", " + sgn[1] + ", " + sgn[2] + ", " + sgn[3] + "}");
	}
}
