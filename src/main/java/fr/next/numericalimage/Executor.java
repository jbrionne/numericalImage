package fr.next.numericalimage;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import fr.next.numericalimage.service.Service;

/**
 * Execute services and gives new arguments to the next service.
 * A service must be stateless. The executor finds the service by 'type' property. 
 * The first service receives 'workingDirectory' (String) and 'bufferedImage' (BufferedImage) arguments.
 * 
 */
public class Executor {

	public void executeServices(String workingDirectory, BufferedImage img, Map o,
			Map<String, Object> defaultConfiguration) {
		List<Map<String, Object>> pipeline = (List<Map<String, Object>>) o.get("pipeline");
		if (StaticCommandProperties.VERBOSE) {
			System.out.println(pipeline);
		}

		Map<String, Object> args = new HashMap<>();
		args.put("workingDirectory", workingDirectory);
		args.put("bufferedImage", img);
		
		for (Map<String, Object> j : pipeline) {
			System.out.println(j.get("label"));
			j.get("type");

			@SuppressWarnings("unchecked")
			Map<String, Object> defaultConfigurationForType = (Map<String, Object>) defaultConfiguration
					.get(j.get("type"));
			if (defaultConfigurationForType == null) {
				defaultConfigurationForType = new HashMap<>();
			}
			for (Entry<String, Object> e : j.entrySet()) {
				defaultConfigurationForType.put(e.getKey(), e.getValue());
			}
			
			if (StaticCommandProperties.VERBOSE) {
				System.out.println(defaultConfigurationForType);
			}

			Service service = StaticRegistry.registry.get(j.get("type"));
			args = service.process(defaultConfigurationForType, args);
		}
	}
}
