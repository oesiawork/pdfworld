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

public class DocumentMultipage {

	private static Logger logger = LoggerFactory.getLogger(DocumentMultipage.class);

	public static void main(String... args) throws Exception {
		logger.info("Begin Process");

		File destFile = new File("documentoMulti.pdf");
		File templatePDF = new File(DocumentMultipage.class.getClassLoader().getResource("documentLong.xml").getFile());
		PdfTemplate pdfTemplate = TemplateProvider.retrievePdfTemplate(templatePDF);
		
		// datos parametrizados para la plantilla de pdf
		FieldContainer fieldContainer = new FieldContainer();
		Map<String, String> container = new HashMap<>();
		container.put("nombre", "Andrés Gaudioso");
		container.put("param", "Andrés$Beatriz$Tomás$Carmen");
		fieldContainer.setContainer(container);
		
		// Lo preparamos para añadirle la banda
		DocumentGenerator.documentBandFromTemplate(pdfTemplate, fieldContainer, destFile);
		
		File bandTemplateFile = new File(
				DocumentAndBand.class.getClassLoader().getResource("bandGuay.xml").getFile());
		BandTemplate bandTemplate = TemplateProvider.retrieveBandTemplate(bandTemplateFile);

		FieldContainer fc = BandSelloOrgano.build("CSV8976450048556", "Andrés Gaudioso Simón",
				"https://aplicaciones.aragon.es/ccsv_pub/", "13/07/2009", "Instituto Aragonés de la Mujer");
		bandTemplate.setQrCode("Estamos programando por encima de nuestras posibilidades");

		File outFile= new File("documentoMultiBand.pdf");
		
		WaterBandGenerator.overlapBand(destFile, outFile, bandTemplate, fc, true);
		
		
		//

		logger.info("End Process");
	}

}
