package fr.next.numericalimage.service;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConvertToHtml implements Service {

	public static final String NAME = "convert_to_html";

	@Override
	public Map<String, Object> process(Map<String, Object> parameters, Map<String, Object> args) {
		// parameters
		String workingDirectory = (String) args.get("workingDirectory");
		BufferedImage img = (BufferedImage) args.get("bufferedImage");
		String[][] grids = (String[][]) args.get("imageArray");
		
		boolean multicolor = (boolean) parameters.get("multicolor");

		// processing
		List<Integer> colors = new ArrayList<>();
		if (multicolor) {
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
		}

		StringBuilder result = GridToHtmlConverter.convertToHtml(grids, colors);
		GridToHtmlConverter.writeToFile(workingDirectory, result);

		return new HashMap<String, Object>();
	}

}
