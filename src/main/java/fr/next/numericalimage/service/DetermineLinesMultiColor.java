package fr.next.numericalimage.service;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetermineLinesMultiColor implements Service {

	public static final String NAME = "determine_lines_multi_color";
	
	@Override
	public Map<String, Object> process(Map<String, Object> parameters, Map<String, Object> args) {
		// parameters
		String workingDirectory = (String) args.get("workingDirectory");
		BufferedImage img = (BufferedImage) args.get("bufferedImage");
		
		Color neutralColorParam = DetermineLines.parseRgb((String) parameters.get("neutral_color"));
		int[] neutralColor =  new int[] { neutralColorParam.getRed(), neutralColorParam.getGreen(), neutralColorParam.getBlue()};
		int maxSize = (int) parameters.get("line_max_size");

		// processing
		List<Integer> colors = new ArrayList<>();
		int width = img.getWidth();
		int height = img.getHeight();
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				int colorRgb = img.getRGB(col, row);
				if (!colors.contains(colorRgb)) {
					colors.add(colorRgb);
				}
			}
		}

		ImageToNumericConverter imgConverter = new ImageToNumericConverter();
		String[][] origin = new String[width][height];
		for (int color : colors) {
			// determine lines
			int colorToTreat = color;
			List<String> lines = imgConverter.determineLines(1, img, neutralColor, colorToTreat, maxSize);
			int w = img.getWidth();
			int h = img.getHeight();
			imgConverter.linesToImage2DArray(origin, color, lines, w, h);
		}
		
		Map<String, Object> res = new HashMap<>();
		res.put("imageArray", origin);
		res.put("workingDirectory", workingDirectory);
		res.put("bufferedImage", img);
		return res;
	}

}
