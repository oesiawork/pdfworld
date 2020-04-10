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

public class DocumentAndBand {

	private static Logger logger = LoggerFactory.getLogger(DocumentAndBand.class);

	public static void main(String... args) throws Exception {
		logger.info("Begin Process");

		File destFile = File.createTempFile("documentoBand", ".pdf");
		destFile.deleteOnExit(); // Documento intermedio

		File templatePDF = new File(DocumentAndBand.class.getClassLoader().getResource("document.xml").getFile());
		PdfTemplate pdfTemplate = TemplateProvider.retrievePdfTemplate(templatePDF);
		// datos parametrizados para la plantilla de pdf
		FieldContainer fieldContainer = new FieldContainer();
		Map<String, String> container = new HashMap<>();
		container.put("nombre", "Andrés Gaudioso");
		container.put("param", "Andrés$Beatriz$Tomás$Carmen");
		fieldContainer.setContainer(container);
		DocumentGenerator.documentBandFromTemplate(pdfTemplate, fieldContainer, destFile);

		File outFile = new File("DocumentoFinalMontado.pdf");

		// Recuperamos la banda
		File bandTemplateFile = new File(
				DocumentAndBand.class.getClassLoader().getResource("bandOrganismo.xml").getFile());
		BandTemplate bandTemplate = TemplateProvider.retrieveBandTemplate(bandTemplateFile);

		FieldContainer fc = BandSelloOrgano.build("CSV8976450048556", "Andrés Gaudioso Simón",
				"https://aplicaciones.aragon.es/ccsv_pub/", "13/07/2009", "Instituto Aragonés de la Mujer");
		bandTemplate.setQrCode("Jovencillo emponzoñado de Wisky que figurota exhibe");

		WaterBandGenerator.overlapBand(destFile, outFile, bandTemplate, fc, true);
		logger.info("End Process");
	}

}
