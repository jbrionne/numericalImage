package fr.next.numericalimage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.yaml.snakeyaml.Yaml;

/**
 * Parse command line parameters and execute pipelined services. Pipeline is an
 * ordered sequence of services.
 * Example of command :
 * numericalimage -w /Users/user/Documents/numericalImage/cat_quantize -i cat_quantize.png -v
 * 
 */
public class MainNumericalImage {

	public static void main(String[] args) {
		Options options = new Options();
		options.addOption("i", "image", true, "the input image file");
		options.addOption("w", "working-directory", true, "the working directory");
		options.addOption("p", "pipeline", true, "the pipeline file");
		options.addOption("v", "verbose", false, "verbose");

		String workingDirectory = null;
		String img = null;
		String pipelineFile = null;
		try {
			CommandLine line = new DefaultParser().parse(options, args);
			if (line.hasOption("v")) {
				StaticCommandProperties.VERBOSE = true;
			}

			if (line.hasOption("w")) {
				workingDirectory = line.getOptionValue("w");
			} else {
				System.out.println("Working directory argument is mandatory");
				return;
			}

			if (line.hasOption("i")) {
				img = line.getOptionValue("i");
				if (!img.contains(File.separator)) {
					img = workingDirectory + File.separator + img;
				}
			} else {
				System.out.println("Input image argument is mandatory");
				return;
			}

			if (line.hasOption("p")) {
				pipelineFile = line.getOptionValue("p");
			} else {
				pipelineFile = workingDirectory + "/pipeline.yaml";
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return;
		}

		try {
			Yaml yaml = new Yaml();
			@SuppressWarnings("unchecked")
			Map<String, Object> pipeline = (Map<String, Object>) yaml.load(new FileInputStream(new File(pipelineFile)));

			@SuppressWarnings("unchecked")
			Map<String, Object> defaultConfiguration = (Map<String, Object>) yaml.load(
					Thread.currentThread().getContextClassLoader().getResourceAsStream("default_configuration.yaml"));

			BufferedImage imgBuffer = ImageIO.read(new File(img));

			new Executor().executeServices(workingDirectory, imgBuffer, pipeline, defaultConfiguration);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

}
