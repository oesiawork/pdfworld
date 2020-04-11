package es.sinjava.pdfworld;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.sinjava.model.BandSelloOrgano;
import es.sinjava.model.BandTemplate;
import es.sinjava.model.FieldContainer;
import es.sinjava.pdf.generator.DocumentGenerator;
import es.sinjava.pdf.generator.WaterBandGenerator;
import es.sinjava.pdf.model.PdfTemplate;
import es.sinjava.util.TemplateProvider;

public class DocumentExample {

	private static Logger logger = LoggerFactory.getLogger(DocumentExample.class);

	public static void main(String... args) throws Exception {
		logger.info("Begin Process");

		File destFile = new File("documentoSimple.pdf");
		File templatePDF = new File(DocumentExample.class.getClassLoader().getResource("document.xml").getFile());
		PdfTemplate pdfTemplate = TemplateProvider.retrievePdfTemplate(templatePDF);
		// datos parametrizados para la plantilla de pdf
		FieldContainer fieldContainer = new FieldContainer();
		Map<String, String> container = new HashMap<>();
		container.put("nombre", "Andrés Gaudioso");
		container.put("param", "Andrés$Beatriz$Tomás$Carmen");
		fieldContainer.setContainer(container);
		DocumentGenerator.documentBandFromTemplate(pdfTemplate, fieldContainer, destFile);

		logger.info("End Process");
	}

}
