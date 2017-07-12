package fr.next.numericalimage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.next.numericalimage.image.Point2D;
import fr.next.numericalimage.service.ImageToNumericConverter;
import junit.framework.Assert;
import junit.framework.TestCase;

public class PathTest extends TestCase {

	public void testAllPath() {
		ImageToNumericConverter l = new ImageToNumericConverter();
		int X = 0;
		int Y = 0;
		int i = 1;
		int j = 1;
		int width = 2;
		int height = 2;
		int size = 3;

		List<List<Point2D>> paths = l.findAllPath(X, Y, i, j, width, height, size);
		Assert.assertEquals("[[[x=0, y=0], [x=1, y=0], [x=1, y=1]], [[x=0, y=0], [x=0, y=1], [x=1, y=1]]]",
				Arrays.toString(paths.toArray()));
	}

	public void testAllPathMultiple() {
		ImageToNumericConverter l = new ImageToNumericConverter();
		int X = 0;
		int Y = 0;
		int i = 2;
		int j = 2;
		int width = 3;
		int height = 3;
		int size = 5;

		List<List<Point2D>> paths = l.findAllPath(X, Y, i, j, width, height, size);
		Assert.assertEquals(
				"[[[x=0, y=0], [x=1, y=0], [x=2, y=0], [x=2, y=1], [x=2, y=2]], [[x=0, y=0], [x=1, y=0], [x=1, y=1], [x=2, y=1], [x=2, y=2]], [[x=0, y=0], [x=1, y=0], [x=1, y=1], [x=1, y=2], [x=2, y=2]], [[x=0, y=0], [x=0, y=1], [x=1, y=1], [x=2, y=1], [x=2, y=2]], [[x=0, y=0], [x=0, y=1], [x=1, y=1], [x=1, y=2], [x=2, y=2]], [[x=0, y=0], [x=0, y=1], [x=0, y=2], [x=1, y=2], [x=2, y=2]]]",
				Arrays.toString(paths.toArray()));
	}

	public void testAllPathMultipleLong() {
		ImageToNumericConverter l = new ImageToNumericConverter();
		int X = 0;
		int Y = 0;
		int i = 2;
		int j = 2;
		int width = 3;
		int height = 3;
		int size = 9;

		List<List<Point2D>> paths = l.findAllPath(X, Y, i, j, width, height, size);
		Assert.assertEquals(
				"[[[x=0, y=0], [x=1, y=0], [x=2, y=0], [x=2, y=1], [x=1, y=1], [x=0, y=1], [x=0, y=2], [x=1, y=2], [x=2, y=2]]]",
				Arrays.toString(paths.toArray()));
	}

	public void testAreaOfPath() {
		ImageToNumericConverter l = new ImageToNumericConverter();
		List<Point2D> firstPath = new ArrayList<Point2D>();
		firstPath.add(new Point2D(0, 0));
		firstPath.add(new Point2D(1, 0));
		firstPath.add(new Point2D(2, 0));
		firstPath.add(new Point2D(2, 1));
		firstPath.add(new Point2D(1, 1));
		firstPath.add(new Point2D(0, 1));
		firstPath.add(new Point2D(0, 2));
		firstPath.add(new Point2D(1, 2));
		firstPath.add(new Point2D(2, 2));
		List<Point2D> secondPath = new ArrayList<Point2D>();
		secondPath.add(new Point2D(0, 0));
		secondPath.add(new Point2D(0, 1));
		secondPath.add(new Point2D(0, 2));
		secondPath.add(new Point2D(1, 2));
		secondPath.add(new Point2D(1, 1));
		secondPath.add(new Point2D(1, 0));
		secondPath.add(new Point2D(2, 0));
		secondPath.add(new Point2D(2, 1));
		secondPath.add(new Point2D(2, 2));
		Assert.assertTrue(l.compareAreaOfPath(firstPath, secondPath));
	}
}
