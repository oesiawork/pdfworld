package es.sinjava;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.sinjava.model.Band;
import es.sinjava.model.Band.Position;
import es.sinjava.model.FieldContainer;
import es.sinjava.model.Template;
import es.sinjava.util.BandGenerator;
import es.sinjava.util.TemplateProvider;

public class App {

	private final static Logger logger = LoggerFactory.getLogger(App.class);

	private final static File entrada = new File(".\\src\\main\\resources\\toembed.pdf");

	public static void main(String[] args) throws InvalidPasswordException, IOException {
		// Leer el archivo a empotrar
		logger.debug("Begin main");
		PDDocument documentoBase = PDDocument.load(entrada);
		// Band band =Band.getDefaultBand();
		Map<String, Template> templates = TemplateProvider.getLoadedTemplates();

		Template templateOne = templates.get("templateOne");

		Band band = Band.getBand(templateOne);
		band.setPosition(Position.LEFT);
		band.setQrCode("CSV8976450048556");

		// Aqui viene la parte delicada, debería saber que tiene el template
		FieldContainer fc = band.getTemplate().getFieldContainer();

		if (logger.isInfoEnabled()) {
			for (Entry<String, String> current : fc.getContainer().entrySet()) {
				logger.debug(current.getKey());
			}
		}

		FieldContainer fcInput = new FieldContainer();
		Map<String, String> valoresInput = new HashMap<>();
		valoresInput.put("colita", "Alea Jacta Est");
		fcInput.setContainer(valoresInput);

		templateOne.setFieldContainer(fcInput);

		band.setTemplate(templateOne);

		BandGenerator.getInstance(documentoBase, band).buildAsTempFile();
		// añadirle la banda
		logger.debug("End main");

	}
}
