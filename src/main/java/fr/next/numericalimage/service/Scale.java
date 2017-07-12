package fr.next.numericalimage.service;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class Scale  implements Service {

	public static final String NAME = "scale";
	
	@Override
	public Map<String, Object> process(Map<String, Object> parameters, Map<String, Object> args) {
		//parameters
		String workingDirectory = (String) args.get("workingDirectory");
		BufferedImage img = (BufferedImage) args.get("bufferedImage");
		
		int scaleFactor = (int) parameters.get("scale_factor");
		
		//processing
		BufferedImage scale = scale(img, img.getWidth() / scaleFactor, img.getHeight() / scaleFactor);

		try {
			ImageIO.write(scale, "png", new File(workingDirectory + File.separator + "transform_scale.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Map<String, Object> res = new HashMap<>();
		res.put("bufferedImage", scale);
		res.put("workingDirectory", workingDirectory);
		return res;
	}

	public BufferedImage scale(BufferedImage img, int targetWidth, int targetHeight) {
		int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB
				: BufferedImage.TYPE_INT_ARGB;
		BufferedImage ret = img;
		BufferedImage scratchImage = null;
		Graphics2D g2 = null;
		int w = img.getWidth();
		int h = img.getHeight();
		int prevW = w;
		int prevH = h;

		do {
			if (w > targetWidth) {
				w /= 2;
				w = (w < targetWidth) ? targetWidth : w;
			}

			if (h > targetHeight) {
				h /= 2;
				h = (h < targetHeight) ? targetHeight : h;
			}

			if (scratchImage == null) {
				scratchImage = new BufferedImage(w, h, type);
				g2 = scratchImage.createGraphics();
			}

			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2.drawImage(ret, 0, 0, w, h, 0, 0, prevW, prevH, null);

			prevW = w;
			prevH = h;
			ret = scratchImage;
		} while (w != targetWidth || h != targetHeight);

		if (g2 != null) {
			g2.dispose();
		}

		if (targetWidth != ret.getWidth() || targetHeight != ret.getHeight()) {
			scratchImage = new BufferedImage(targetWidth, targetHeight, type);
			g2 = scratchImage.createGraphics();
			g2.drawImage(ret, 0, 0, null);
			g2.dispose();
			ret = scratchImage;
		}
		return ret;
	}
	
}
