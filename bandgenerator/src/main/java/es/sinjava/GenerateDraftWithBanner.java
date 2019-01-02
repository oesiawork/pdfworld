package es.sinjava;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.sinjava.model.Band;
import es.sinjava.model.BandSelloOrgano;
import es.sinjava.model.BandTemplate;
import es.sinjava.model.FieldContainer;
import es.sinjava.pdf.generator.BeaGenerator;
import es.sinjava.pdf.model.PdfTemplate;
import es.sinjava.util.BandFactory;
import es.sinjava.util.BandGenerator;
import es.sinjava.util.DraftFactory;
import es.sinjava.util.TemplateProvider;

public class GenerateDraftWithBanner {

	private final static Logger logger = LoggerFactory.getLogger(GenerateDraftWithBanner.class);

	public static void main(String[] args) throws InvalidPasswordException, IOException, JAXBException {

		logger.info("Begin main");

		File template = new File(GenerateDraftWithBanner.class.getClassLoader().getResource("banner.xml").getFile());

		// plantilla para el pdf y relleno de datos

		PdfTemplate preview = TemplateProvider.retrievePdfTemplate(template);
		FieldContainer fieldContainer = new FieldContainer();
		Map<String, String> container = new HashMap<>();
		container.put("nombre", "Andrés Gaudioso");
		container.put("param", "Andrés$Beatriz$Tomás$Idoia");
		fieldContainer.setContainer(container);
		PdfTemplate pdfTemplate = DraftFactory.getDraft(preview, fieldContainer);

		File tempFile = File.createTempFile("Banner", ".pdf");
		boolean requirePDFA = true;

		BeaGenerator.getInstance().writePDFFile(pdfTemplate.getStoreContentList(), tempFile, requirePDFA);
		logger.info("End main");

		PDDocument documentoBase = PDDocument.load(tempFile);

		File bandTemplateFile = new File(
				GenerateDraftWithBanner.class.getClassLoader().getResource("bandTemplate.xml").getFile());

		BandTemplate bandTemplate = TemplateProvider.retrieveBandTemplate(bandTemplateFile);
		
		FieldContainer fc = BandSelloOrgano.build("CSV8976450048556", "Andrés Gaudioso Simón",
				"https://aplicaciones.aragon.es/ccsv_pub/", "13/07/2009",
				"Colegio Profesional de Ingenieros Técnicos en Informática de Aragón");

		Band band = BandFactory.getBand(bandTemplate, fc );
		band.setQrCode("https://aplicaciones.aragon.es/ccsv_pub/CSV8976450048556");
		BandGenerator.getInstance(documentoBase, band).buildAsTempFile();

	}
}
