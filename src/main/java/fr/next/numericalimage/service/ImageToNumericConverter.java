package fr.next.numericalimage.service;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.next.numericalimage.image.Point2D;
import fr.next.numericalimage.image.StaticImageFunctions;

public class ImageToNumericConverter {

	public List<String> determineLines(int pixSize, BufferedImage img, int[] neutralColor, int colorToTreat, int maxSize) {
		Raster src = img.getData();
		BufferedImage dest = StaticImageFunctions.deepCopy(img);
		
		List<String> lines = new ArrayList<>();

		String found = "START";
		while (!found.isEmpty()) {
			found = foundRandomPixelWithColor(pixSize, dest, colorToTreat);
			int size = 0;
			if (!found.isEmpty()) {
				// find a black case not already done
				String[] dot = found.split(",");
				int X = Integer.valueOf(dot[0]);
				int Y = Integer.valueOf(dot[1]);
				List<String> newCur = new ArrayList<>();
				String newCurBis = X + "," + Y;
				newCur.add(newCurBis);
				dest.getRaster().setPixel(X, Y, neutralColor);
				StringBuilder linesAsString = new StringBuilder();
				linesAsString.append(X + "," + Y);
				size = 1;
				while (newCurBis != null) {
					String[] coord = newCurBis.split(",");
					int i = Integer.valueOf(coord[0]);
					int j = Integer.valueOf(coord[1]);
					if(size < maxSize) {
						newCurBis = filledCurrent(src, dest.getRaster(), neutralColor, X, Y, i, j, colorToTreat, false, size + 1);
					} else {
						newCurBis = null;
					}
					if (newCurBis != null) {
						String[] coordNext = newCurBis.split(",");
						int iNext = Integer.valueOf(coordNext[0]);
						int jNext = Integer.valueOf(coordNext[1]);
						dest.getRaster().setPixel(iNext, jNext, neutralColor);
						linesAsString.append(":");
						linesAsString.append(newCurBis);
						size++;
					}
				}
				lines.add(linesAsString.toString());
			}
		}
		return lines;
	}

	private String foundRandomPixelWithColor(int pixSize, BufferedImage dest, int colorRGB) {
		int randH = (int) (Math.random() * dest.getHeight());
		int moduloH = randH % pixSize;
		
		int randV = (int) (Math.random() * dest.getWidth());
		int moduloV = randV % pixSize;
		
		
		int initialY = randH - moduloH;
		int initialX = randV - moduloV;
		
		for (int y = initialY; y < dest.getHeight(); y += pixSize) {
			for (int x = 0; x < dest.getWidth(); x += pixSize) {
				if((y > initialY) || (y == initialY && x >= initialX))  {
					int pixel = dest.getRGB(x, y);
					if (pixel == colorRGB) {
						return x + "," + y;
					}
				}
			}
		}
		
		for (int y = initialY; y >= 0; y -= pixSize) {
			for (int x = dest.getWidth() - 1; x >= 0; x -= pixSize) {
				if((y < initialY) || (y == initialY && x < initialX))  {
					int pixel = dest.getRGB(x, y);
					if (pixel == colorRGB) {
						return x + "," + y;
					}
				}
			}
		}
		return "";
	}
	
	private String filledCurrent(Raster src, WritableRaster dest, int[] grey, int X, int Y, int x, int y,
			int colorToTreatRGB, boolean noEnveloped, int size) {
		String cur = null;
		List<Point2D> pHori = searchHori(src, dest, X, Y, x, y, colorToTreatRGB, size);
		List<Point2D> pVerti = searchVerti(src, dest, X, Y, x, y, colorToTreatRGB, size);
		pHori.addAll(pVerti);
		if(pHori.size() == 0) {
			return null;
		} else {
			ArrayList<Integer> numbers = generateRandomListOfNumberWithNoDuplicates(pHori);
			for(Integer randIndex : numbers) {
				Point2D p = pHori.get(randIndex);
				List<List<Point2D>> paths = findAllPath(X, Y, p.getX(), p.getY(), src.getWidth(), src.getHeight(), size);
				if (paths.size() == 0) {
					return null;
				} else if (paths.size() >= 1) {
					return p.getX() + "," + p.getY();
				} else {
					boolean allPathsAreFullBlackPoint = true;
					for(List<Point2D> path : paths) {
						boolean allBlack = true;
						for(Point2D pPoint : path) {
							int[] pixelCurrent = new int[3];
							pixelCurrent = src.getPixel(pPoint.getX(), pPoint.getY(), pixelCurrent);
							int pixelCurrentColor = new Color(pixelCurrent[0], pixelCurrent[1], pixelCurrent[2]).getRGB();
							if (pixelCurrentColor != colorToTreatRGB) {
								allBlack = false;
								break;
							}
						}
						allPathsAreFullBlackPoint = allPathsAreFullBlackPoint && allBlack;
						if(!allPathsAreFullBlackPoint) {
							break;
						}
					}
					if(allPathsAreFullBlackPoint) {
						return p.getX() + "," + p.getY();
					}
				}
			}
		}
		return cur;
	}

	private ArrayList<Integer> generateRandomListOfNumberWithNoDuplicates(List<Point2D> pHori) {
		ArrayList<Integer> numbers = new ArrayList<Integer>();   
		Random randomGenerator = new Random();
		while (numbers.size() < pHori.size()) {
		    int random = randomGenerator .nextInt(pHori.size());
		    if (!numbers.contains(random)) {
		        numbers.add(random);
		    }
		}
		return numbers;
	}

	private List<Point2D> searchVerti(Raster src, WritableRaster dest, int X, int Y, int x, int y, int colorToTreatRGB, int size) {
		List<Point2D> possibilities = new ArrayList<>();
		for (int j = y - 1; j <= y + 1; j++) {
			int i = x;
			if (i >= 0 && j >= 0 && j < src.getHeight() && i < src.getWidth()) {
				int[] pixelCurrent = new int[3];
				pixelCurrent = src.getPixel(i, j, pixelCurrent);
				int pixelCurrentColor = new Color(pixelCurrent[0], pixelCurrent[1], pixelCurrent[2]).getRGB();
				int[] pixelDest = new int[3];
				pixelDest = dest.getPixel(i, j, pixelDest);
				int pixelDestColor = new Color(pixelDest[0], pixelDest[1], pixelDest[2]).getRGB();
				if (pixelCurrentColor == colorToTreatRGB && pixelDestColor == colorToTreatRGB) {
					possibilities.add(new Point2D(i, j));
				}
			}
		}
		return possibilities;
	}

	private List<Point2D> searchHori(Raster src, WritableRaster dest, int X, int Y, int x, int y, int colorToTreatRGB, int size) {
		List<Point2D> possibilities = new ArrayList<>();
		for (int i = x - 1; i <= x + 1; i++) {
			int j = y;
			if (i >= 0 && j >= 0 && j < src.getHeight() && i < src.getWidth()) {
				int[] pixelCurrent = new int[3];
				pixelCurrent = src.getPixel(i, j, pixelCurrent);
				int pixelCurrentColor = new Color(pixelCurrent[0], pixelCurrent[1], pixelCurrent[2]).getRGB();
				int[] pixelDest = new int[3];
				pixelDest = dest.getPixel(i, j, pixelDest);
				int pixelDestColor = new Color(pixelDest[0], pixelDest[1], pixelDest[2]).getRGB();
				if (pixelCurrentColor == colorToTreatRGB && pixelDestColor == colorToTreatRGB) {
					possibilities.add(new Point2D(i, j));
				}
			}
		}
		return possibilities;
	}

	public List<List<Point2D>> findAllPath(int X, int Y, int i, int j, int width, int height, int size) {
		Point2D p = new Point2D(X, Y);
		List<List<Point2D>> paths = new ArrayList<>();
		List<Point2D> firstPath = new ArrayList<>();
		firstPath.add(p);
		paths.add(firstPath);
		if (size == 0 && X == i && Y == j) {
			return paths;
		}
		for (int m = 0; m < size - 1; m++) {
			List<List<Point2D>> allNewPaths = new ArrayList<>();
			for (List<Point2D> path : paths) {
				Point2D endOfPath = path.get(path.size() - 1);
				for (int k = endOfPath.getX() - 1; k <= endOfPath.getX() + 1; k++) {
					if (k >= 0 && k < width && endOfPath.getY() >= 0 && endOfPath.getY() < height
							&& k != endOfPath.getX()) {
						Point2D pC = new Point2D(k, endOfPath.getY());
						if (!contains(path, pC)) {
							List<Point2D> newPath = new ArrayList<>(path);
							newPath.add(pC);
							allNewPaths.add(newPath);
						}
					}
				}
				for (int l = endOfPath.getY() - 1; l <= endOfPath.getY() + 1; l++) {
					if (endOfPath.getX() >= 0 && endOfPath.getX() < width && l >= 0 && l < height
							&& l != endOfPath.getY()) {
						Point2D pC = new Point2D(endOfPath.getX(), l);
						if (!contains(path, pC)) {
							List<Point2D> newPath = new ArrayList<>(path);
							newPath.add(pC);
							allNewPaths.add(newPath);
						}
					}
				}
			}
			paths.clear();
			paths = allNewPaths;
		}

		// eliminate path with the end different from the destination
		List<List<Point2D>> pathWithDestinationPoint = new ArrayList<>();
		for (List<Point2D> path : paths) {
			Point2D last = path.get(path.size() - 1);
			if (last.getX() == i && last.getY() == j) {
				pathWithDestinationPoint.add(path);
			}
		}

		// remove identical area ! (same Point2D)
		List<List<Point2D>> keepPath = new ArrayList<>();
		for (List<Point2D> originPath : pathWithDestinationPoint) {
			boolean alreadyExist = false;
			for (List<Point2D> secondPath : keepPath) {
				if (!originPath.equals(secondPath)) {
					if(compareAreaOfPath(originPath, secondPath)) {
						alreadyExist = true;
						break;
					}
				}
			}
			if(!alreadyExist) {
				keepPath.add(originPath);
			}
		}
		return keepPath;
	}

	public boolean compareAreaOfPath(List<Point2D> originPath, List<Point2D> secondPath) {
		if (!originPath.equals(secondPath)) {
			if(originPath.size() != secondPath.size()) {
				return false;
			}
			for (Point2D pOrigin : originPath) {
				if (!contains(secondPath, pOrigin)) {
					return false;
				}
			}
			for (Point2D pOrigin : secondPath) {
				if (!contains(originPath, pOrigin)) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean contains(List<Point2D> path, Point2D p) {
		for (Point2D c : path) {
			if (c.getX() == p.getX() && c.getY() == p.getY()) {
				return true;
			}
		}
		return false;
	}
	
	public void linesToImage2DArray(String[][] result, int colorRgb, List<String> lines, int w, int h) {
		for (String line : lines) {
			String[] dots = line.split(":");
			int size = dots.length;
			int index = 0;
			for (String dot : dots) {
				try {
					if (index == 0) {
						String[] coords = dot.split(",");
						int x = Integer.valueOf(coords[0]);
						int y = Integer.valueOf(coords[1]);
						result[x][y] = colorRgb + "," + size;
					} else if (index == size - 1) {
						String[] coordsUV = dot.split(",");
						int u = Integer.valueOf(coordsUV[0]);
						int v = Integer.valueOf(coordsUV[1]);
						result[u][v] = colorRgb + "," + size;
					} else {
						String[] coordsUV = dot.split(",");
						int u = Integer.valueOf(coordsUV[0]);
						int v = Integer.valueOf(coordsUV[1]);
						result[u][v] = " ";
					}
					index++;
				} catch (NumberFormatException e) {
					throw new AssertionError("dot " + dot, e);
				}
			}

			String[] coords = dots[0].split(",");
			int x = Integer.valueOf(coords[0]);
			int y = Integer.valueOf(coords[1]);
			result[x][y] = colorRgb + "," + size;
			if (size > 1) {
				String[] coordsUV = dots[size - 1].split(",");
				int u = Integer.valueOf(coordsUV[0]);
				int v = Integer.valueOf(coordsUV[1]);
				result[u][v] = colorRgb + "," + size;
			}

		}
	}

}
