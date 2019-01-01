package es.sinjava;

import es.sinjava.model.Band;
import es.sinjava.model.BandTemplate;
import es.sinjava.model.FieldContainer;
import es.sinjava.model.Template;

public class BandFactory {

	public static Band getBand(BandTemplate bandTemplate, FieldContainer fieldContainer) {
		Band newBand = new Band();
		newBand.setPosition(bandTemplate.getPosition());
		Template template = new Template(bandTemplate.getName());
		template.put(Template.HEADER, bandTemplate.getLineOne());
		template.put(Template.BODY, bandTemplate.getLineTwo());
		template.put(Template.FOOTER, bandTemplate.getLineTree());
		newBand.setTemplate(template);
		return newBand;
	}

}
