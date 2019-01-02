package es.sinjava.util;

import java.io.StringWriter;

import es.sinjava.model.Band;
import es.sinjava.model.BandTemplate;
import es.sinjava.model.FieldContainer;
import es.sinjava.model.Template;

public class BandFactory {

	public static Band getBand(BandTemplate bandTemplate, FieldContainer fieldContainer) {
		Band newBand = new Band();
		newBand.setPosition(bandTemplate.getPosition());

		String line = populateValues(bandTemplate.getLineOne(), fieldContainer);

		Template template = new Template(bandTemplate.getName());
		template.put(Template.HEADER, line);

		line = populateValues(bandTemplate.getLineTwo(), fieldContainer);
		template.put(Template.BODY, line);

		line = populateValues(bandTemplate.getLineTree(), fieldContainer);
		template.put(Template.FOOTER, line);
		
		newBand.setTemplate(template);
		return newBand;
	}

	private static String populateValues(String line, FieldContainer fieldContainer) {
		String[] words = line.split(" ");
		StringWriter stringWritter = new StringWriter();

		for (String word : words) {

			if (word.startsWith("${") && word.endsWith("}")) {
				String key = word.substring(2, word.length() - 1);
				stringWritter.append(fieldContainer.getContainer().get(key)).append(" ");
			} else {
				stringWritter.append(word).append(" ");
			}
		}
		return stringWritter.toString();
	}

}
