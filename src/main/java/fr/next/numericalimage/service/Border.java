package fr.next.numericalimage.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import fr.next.numericalimage.service.border.CannyEdgeDetector;
import fr.next.numericalimage.service.border.CannyEdgeDetectorParam;

public class Border  implements Service {

	public static final String NAME = "border";
	
	@Override
	public Map<String, Object> process(Map<String, Object> parameters, Map<String, Object> args) {
		//parameters
		String workingDirectory = (String) args.get("workingDirectory");
		BufferedImage img = (BufferedImage) args.get("bufferedImage");
		
		Map<String, Object> cannyEdge = (Map<String, Object>) parameters.get("canny_edge_detector");
		float lowThreshold = Double.valueOf((double) cannyEdge.get("low_threshold")).floatValue();
		float highThreshold = Double.valueOf((double) cannyEdge.get("high_threshold")).floatValue();
		float gaussianKernelRadius = Double.valueOf((double) cannyEdge.get("gaussian_kernel_radius")).floatValue();
		int gaussianKernelWidth = (int) cannyEdge.get("gaussian_kernel_width");
		boolean contrastNormalized = (boolean) cannyEdge.get("contrast_normalized");
		
		CannyEdgeDetectorParam cannyEdgeDetectorParam = new CannyEdgeDetectorParam(lowThreshold, highThreshold, gaussianKernelRadius, gaussianKernelWidth, contrastNormalized);
		
		//processing
		BufferedImage border = findBorder(cannyEdgeDetectorParam, img);
		try {
			ImageIO.write(border, "png", new File(workingDirectory + File.separator + "transform_border.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Map<String, Object> res = new HashMap<>();
		res.put("bufferedImage", border);
		res.put("workingDirectory", workingDirectory);
		return res;
	}

	
	
	private BufferedImage findBorder(CannyEdgeDetectorParam cannyEdgeDetectorParam, BufferedImage image) {
		BufferedImage convertedImg = new BufferedImage(image.getWidth(), image.getHeight(),
				BufferedImage.TYPE_BYTE_GRAY);
		convertedImg.getGraphics().drawImage(image, 0, 0, null);
		convertedImg.getGraphics().dispose();
		
		CannyEdgeDetector c = new CannyEdgeDetector();
		c.setLowThreshold(cannyEdgeDetectorParam.getLowThreshold());
		c.setHighThreshold(cannyEdgeDetectorParam.getHighThreshold());
		c.setGaussianKernelRadius(cannyEdgeDetectorParam.getGaussianKernelRadius());
		c.setGaussianKernelWidth(cannyEdgeDetectorParam.getGaussianKernelWidth());
		c.setContrastNormalized(cannyEdgeDetectorParam.isContrastNormalized());
		c.setSourceImage(convertedImg);
		c.process();
		return c.getEdgesImage();
	}
	
}
