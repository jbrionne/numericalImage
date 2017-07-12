package fr.next.numericalimage.service;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class BlackToWhite  implements Service {

	public static final String NAME = "blacktowhite";
	
	@Override
	public Map<String, Object> process(Map<String, Object> parameters, Map<String, Object> args) {
		//parameters
		String workingDirectory = (String) args.get("workingDirectory");
		BufferedImage img = (BufferedImage) args.get("bufferedImage");
		
		int pixSize = (int) parameters.get("pix_size");
		
		//processing
		BufferedImage pixelated = blackAndWhitePixelisation(pixSize, img);
		try {
			ImageIO.write(pixelated, "png", new File(workingDirectory + File.separator + "transform_pixelated.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//send
		Map<String, Object> res = new HashMap<>();
		res.put("bufferedImage", pixelated);
		res.put("workingDirectory", workingDirectory);
		return res;
	}

	private BufferedImage blackAndWhitePixelisation(int pixSize, BufferedImage imgEdges) {

		BufferedImage convertedImg = new BufferedImage(imgEdges.getWidth(), imgEdges.getHeight(),
				BufferedImage.TYPE_BYTE_GRAY);
		convertedImg.getGraphics().drawImage(imgEdges, 0, 0, null);
		convertedImg.getGraphics().dispose();

		// Black & White
		int w = convertedImg.getWidth();
		int h = convertedImg.getHeight();
		Raster src = convertedImg.getData();
		WritableRaster dest = src.createCompatibleWritableRaster();
		int[] white = new int[] { 255, 255, 255 };
		int[] black = new int[] { 0, 0, 0 };
		for (int y = 0; y < h; y += pixSize) {
			for (int x = 0; x < w; x += pixSize) {
				boolean hasAtLeastOneWhite = tryToFindWhiteBorder(pixSize, src, y, x);
				for (int yd = y; (yd < y + pixSize) && (yd < src.getHeight()); yd++) {
					for (int xd = x; (xd < x + pixSize) && (xd < src.getWidth()); xd++) {
						if (hasAtLeastOneWhite) {
							// we put a border, canny is inversed (black/White)
							dest.setPixel(xd, yd, black);
						} else {
							dest.setPixel(xd, yd, white);
						}
					}
				}
			}
		}
		BufferedImage pixelated = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
		pixelated.setData(dest);
		return pixelated;
	}

	private boolean tryToFindWhiteBorder(int pixSize, Raster src, int y, int x) {
		for (int yd = y; (yd < y + pixSize) && (yd < src.getHeight()); yd++) {
			for (int xd = x; (xd < x + pixSize) && (xd < src.getWidth()); xd++) {
				int[] pixel = new int[3];
				pixel = src.getPixel(xd, yd, pixel);
				// Only if type is byte gray
				if (pixel[0] != 0 || pixel[1] != 0 || pixel[2] != 0) {
					return true;
				}
			}
		}
		return false;
	}
}
