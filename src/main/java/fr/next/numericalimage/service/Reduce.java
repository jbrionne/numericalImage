package fr.next.numericalimage.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class Reduce implements Service {

	public static final String NAME = "reduce";
	
	@Override
	public Map<String, Object> process(Map<String, Object> parameters, Map<String, Object> args) {
		// parameters
		String workingDirectory = (String) args.get("workingDirectory");
		BufferedImage img = (BufferedImage) args.get("bufferedImage");
		
		int pixSize = (int) parameters.get("pix_size");
		String imageType = (String) parameters.get("image_type");

		// processing
		BufferedImage reduce = null;
		if("int_rgb".equals(imageType)) {
			reduce = reduceTypeInt(pixSize, img);
		} else if("byte_gray".equals(imageType)) {
			reduce = reduceTypeByte(pixSize, img);
		} else {
			throw new IllegalArgumentException();
		}
				
		try {
			ImageIO.write(reduce, "png", new File(workingDirectory + File.separator +  "transform_reduce.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Map<String, Object> res = new HashMap<>();
		res.put("bufferedImage", reduce);
		res.put("workingDirectory", workingDirectory);
		return res;
	}
	private static BufferedImage reduceTypeInt(int pixSize, BufferedImage img) {
		int h = img.getHeight();
		int w = img.getWidth();
		int newH = h / pixSize + 1;
		int newW = w / pixSize + 1;

		BufferedImage reduce = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_RGB);
		int[] pixels = new int[newW * newH];
		int indexY = 0;
		for (int y = 0; y < h; y += pixSize) {
			int indexX = 0;
			for (int x = 0; x < w; x += pixSize) {
				int rgb = img.getRGB(x, y);
				pixels[indexY * newW + indexX] = rgb;
				indexX++;
			}
			indexY++;
		}
		
		reduce.getWritableTile(0, 0).setDataElements(0, 0, newW, newH, pixels);
		return reduce;
	}
	
	
	private BufferedImage reduceTypeByte(int pixSize, BufferedImage img) {
		int h = img.getHeight();
		int w = img.getWidth();
		int newH = h / pixSize + 1;
		int newW = w / pixSize + 1;

		byte[] pixels = new byte[newW * newH];
		int indexY = 0;
		for (int y = 0; y < h; y += pixSize) {
			int indexX = 0;
			for (int x = 0; x < w; x += pixSize) {
				int[] pixelDest = new int[3];
				pixelDest = img.getData().getPixel(x, y, pixelDest);
				if (pixelDest[0] == 255) {
					pixels[indexY * newW + indexX] = -1;
				} else {
					pixels[indexY * newW + indexX] = 0;
				}
				indexX++;
			}
			indexY++;
		}
		BufferedImage reduce = new BufferedImage(newW, newH, BufferedImage.TYPE_BYTE_GRAY);
		reduce.getWritableTile(0, 0).setDataElements(0, 0, newW, newH, pixels);
		return reduce;
	}

}
