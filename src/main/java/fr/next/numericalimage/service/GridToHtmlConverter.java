package fr.next.numericalimage.service;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GridToHtmlConverter {

	public static StringBuilder convertToHtml(String[][] grids, List<Integer> colors) {
		
		
		Map<String, String> styleColors = new HashMap<>();
		Map<String, String> indexColors = new HashMap<>();
		int i = 1;
		for(Integer color : colors) {
			Color c = new Color(color);
			styleColors.put(String.valueOf(c.getRGB()), "color:rgb("+ c.getRed() +","+ c.getGreen() +","+ c.getBlue()+");");
			indexColors.put(String.valueOf(c.getRGB()), String.valueOf(i));
			i++;
		}
		
		StringBuilder strB = new StringBuilder();
		strB.append("<!DOCTYPE html>\n");
		strB.append("<html>\n");
		strB.append("<head>\n");
		strB.append("<meta charset=\"UTF-8\">\n");
		strB.append("<title>Grid</title>\n");
		strB.append("<style type=\"text/css\">\n");
		strB.append("<--\n");
		strB.append("table, tr, td {border: 1px solid rgb(230,230,230);margin: 0; padding:0;}\n");
		strB.append("td {border: 1px solid rgb(230,230,230);font-size: 1%;font-family: courier;text-align:center;}\n");
		strB.append("-->\n");
		strB.append("</style>\n");
		strB.append("</head>\n");
		strB.append("<body>\n");

		int sizeY = grids[0].length;
		int sizeX = grids.length;

		strB.append("<table style=\"border-collapse: collapse; table-layout:fixed;\">\n");
		for (int line = 0; line < sizeY; line++) {
			strB.append("<tr>\n");
			for (int col = 0; col < sizeX; col++) {
				String s = grids[col][line];
				strB.append("<td style=\"width: 8px;height: 8px;position:relative;\">");
				if(s == null || s.isEmpty() || s.equals(" ")) {
					strB.append("");
				} else {
					String[] val = s.split(",");
					strB.append("<span style=\"font-size: 8px;"+ styleColors.get(val[0]) +"\">");
					strB.append(val[1]);
					strB.append("</span>");
					if(!indexColors.isEmpty()) {
						strB.append("<span style=\"font-size: 4px;position:absolute; top:0; left:0;"+ styleColors.get(val[0]) +"\">");
						strB.append(indexColors.get(val[0]));
						strB.append("</span>");
					}
				}
				strB.append("</td>\n");
			}
			strB.append("</tr>\n");
		}
		strB.append("</table>\n");
		
		strB.append("<table style=\"border-collapse: collapse; table-layout:fixed;\">\n");
		strB.append("<tr>\n");
		int index = 1;
		for(Integer color : colors) {
			Color c = new Color(Integer.valueOf(color));
			strB.append("<td style=\"position:relative;width: 8px;height: 8px;background-color:rgb("+ c.getRed() +","+ c.getGreen() +","+ c.getBlue()+"); \">");
			if(c.getRed() > 150 && c.getGreen() > 150 && c.getBlue() > 150) {
				strB.append("<span style=\"font-size: 4px;position:absolute; top:0; left:0;color:black\">");
			} else {
				strB.append("<span style=\"font-size: 4px;position:absolute; top:0; left:0;color:white\">");
			}
			strB.append(index);
			strB.append("</span>");
			strB.append("</td>\n");
			index++;
		}
		strB.append("</tr>\n");
		strB.append("</table>\n");
		
		strB.append("</body>\n");
		strB.append("</html>\n");

		return strB;
	}
	
	public static void writeToFile(String workingDirectory, StringBuilder txt) {
		try {
			Files.write(Paths.get(workingDirectory + File.separator + "output.html"), txt.toString().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
