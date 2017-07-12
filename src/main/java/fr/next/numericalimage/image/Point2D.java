package fr.next.numericalimage.image;

/**
 * 2D point.
 */
public class Point2D {

	/** abscissa **/
	private int x;

	/** ordinate **/
	private int y;

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public String toString() {
		return "[x=" + x + ", y=" + y + "]";
	}

	public Point2D(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
}
