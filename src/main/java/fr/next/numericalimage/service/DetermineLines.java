package fr.next.numericalimage.service;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetermineLines  implements Service {

	public static final String NAME = "determine_lines";
	
	@Override
	public Map<String, Object> process(Map<String, Object> parameters, Map<String, Object> args) {
		//parameters
		String workingDirectory = (String) args.get("workingDirectory");
		BufferedImage img = (BufferedImage) args.get("bufferedImage");

		Color neutralColorParam = parseRgb((String) parameters.get("neutral_color"));
		int[] neutralColor =  new int[] { neutralColorParam.getRed(), neutralColorParam.getGreen(), neutralColorParam.getBlue()};
		Color colorToTreatParam = parseRgb((String) parameters.get("color_to_treat"));
		int colorToTreat = colorToTreatParam.getRGB();
		int maxSize = (int) parameters.get("line_max_size"); 
				
		//processing
		ImageToNumericConverter imgConverter = new ImageToNumericConverter();
		List<String> lines = imgConverter.determineLines(1, img, neutralColor, colorToTreat, maxSize);
		
		int w = img.getWidth();
		int h = img.getHeight();
		String[][] origin = new String[w][h];
		imgConverter.linesToImage2DArray(origin, colorToTreat, lines, w, h);
		
		
		Map<String, Object> res = new HashMap<>();
		res.put("imageArray", origin);
		res.put("workingDirectory", workingDirectory);
		res.put("bufferedImage", img);
		return res;
	}
	
	public static Color parseRgb(String param) {
		param = param.trim();
		if(param.startsWith("rgb")) {
			String[] colorRGB = param.substring(4, param.length() - 1).split(",");
			return new Color(Integer.valueOf(colorRGB[0].trim()), Integer.valueOf(colorRGB[1].trim()), Integer.valueOf(colorRGB[2].trim()));
		}
		throw new IllegalArgumentException("Not recognized parameter " + param);
	}

}
