package fr.next.numericalimage.service;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import fr.next.numericalimage.color.ColorDifference;
import fr.next.numericalimage.image.StaticImageFunctions;
import fr.next.numericalimage.quantize.Quantize;

public class QuantizeService implements Service {

	public static final String NAME = "quantize";

	@Override
	public Map<String, Object> process(Map<String, Object> parameters, Map<String, Object> args) {
		// parameters
		String workingDirectory = (String) args.get("workingDirectory");
		BufferedImage img = (BufferedImage) args.get("bufferedImage");
		
		int nbColor = (int) parameters.get("nb_colors");

		// processing
		int[][] result = StaticImageFunctions.convertTo2DUsingGetRGB(img);

		int[] colorPalette = Quantize.quantizeImage(result, nbColor);

		int pixelsQ[] = null;
		BufferedImage quantize = null;
		try {
			pixelsQ = getPixels(img);

			for (int m = 0, l = pixelsQ.length; m < l; m++) {
				double bestDistance = Double.MAX_VALUE;
				int bestIndex = -1;
				int pixel = pixelsQ[m];
				int r1 = (pixel >> 16) & 0xFF;
				int g1 = (pixel >> 8) & 0xFF;
				int b1 = (pixel >> 0) & 0xFF;
				for (int k = 0; k < colorPalette.length; k++) {
					int pixel2 = colorPalette[k];
					int r2 = (pixel2 >> 16) & 0xFF;
					int g2 = (pixel2 >> 8) & 0xFF;
					int b2 = (pixel2 >> 0) & 0xFF;
					double dist = ColorDifference.findDifference(r1, g1, b1, r2, g2, b2);
					if (dist < bestDistance) {
						bestDistance = dist;
						bestIndex = k;
					}
				}
				pixelsQ[m] = colorPalette[bestIndex];
			}

			quantize = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
			quantize.getWritableTile(0, 0).setDataElements(0, 0, img.getWidth(), img.getHeight(), pixelsQ);
			ImageIO.write(quantize, "png", new File(workingDirectory + File.separator + "transform_quantize.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Map<String, Object> res = new HashMap<>();
		res.put("bufferedImage", quantize);
		res.put("workingDirectory", workingDirectory);
		return res;
	}

	public static int[] getPixels(Image image) throws IOException {
		int w = image.getWidth(null);
		int h = image.getHeight(null);
		int pix[] = new int[w * h];
		PixelGrabber grabber = new PixelGrabber(image, 0, 0, w, h, pix, 0, w);

		try {
			if (grabber.grabPixels() != true) {
				throw new IOException("Grabber returned false: " + grabber.status());
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return pix;
	}

}
