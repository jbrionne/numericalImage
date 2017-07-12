package fr.next.numericalimage;

import java.util.HashMap;
import java.util.Map;

import fr.next.numericalimage.service.BlackToWhite;
import fr.next.numericalimage.service.Border;
import fr.next.numericalimage.service.ConvertToHtml;
import fr.next.numericalimage.service.DetermineLines;
import fr.next.numericalimage.service.DetermineLinesMultiColor;
import fr.next.numericalimage.service.QuantizeService;
import fr.next.numericalimage.service.Reduce;
import fr.next.numericalimage.service.Scale;
import fr.next.numericalimage.service.Service;

public class StaticRegistry {
	
	public static Map<String, Service> registry = new HashMap<>();
	
	static {
		registry.put(BlackToWhite.NAME, new BlackToWhite());
		registry.put(Reduce.NAME, new Reduce());
		registry.put(Scale.NAME, new Scale());
		registry.put(Border.NAME, new Border());
		registry.put(QuantizeService.NAME, new QuantizeService());
		
		registry.put(DetermineLines.NAME, new DetermineLines());
		registry.put(DetermineLinesMultiColor.NAME, new DetermineLinesMultiColor());
		
		registry.put(ConvertToHtml.NAME, new ConvertToHtml());
	}
	
}
