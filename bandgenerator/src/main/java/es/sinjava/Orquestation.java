package es.sinjava;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.sinjava.model.Band.Position;
import es.sinjava.factory.DraftFactory;
import es.sinjava.model.BandSelloOrgano;
import es.sinjava.model.BandTemplate;
import es.sinjava.model.FieldContainer;
import es.sinjava.pdf.generator.DocumentBandGenerator;
import es.sinjava.pdf.model.PdfTemplate;
import es.sinjava.util.TemplateProvider;

public class Orquestation {

	private final static Logger logger = LoggerFactory.getLogger(Orquestation.class);

	public static void main(String[] args) throws InvalidPasswordException, IOException, JAXBException {

		logger.info("Begin main");

		// plantilla para el pdf y relleno de datos
		File templatePDF = new File(Orquestation.class.getClassLoader().getResource("orquest.xml").getFile());
		PdfTemplate pdfTemplate = TemplateProvider.retrievePdfTemplate(templatePDF);

		// datos parametrizados para la plantilla de pdf
		FieldContainer fieldContainer = new FieldContainer();
		Map<String, String> container = new HashMap<>();
		container.put("nombre", "Andrés Gaudioso");
		container.put("param", "Andrés$Beatriz$Tomás$Idoia");
		fieldContainer.setContainer(container);

		// creamos la DraftDel Pdf
		PdfTemplate pdfDraft = DraftFactory.getDraft(pdfTemplate, fieldContainer);

		// no lo volcaremos a disco
		File tempFile = File.createTempFile("Multi", ".pdf");
		// BeaGenerator.getInstance().writePDFFile(pdfTemplate.getStoreContentList(),
		// tempFile, requirePDFA);
		logger.info("End main");

		// recuperamos la plantilla de la banda

		File bandTemplateFile = new File(Orquestation.class.getClassLoader().getResource("bandTemplate.xml").getFile());
		BandTemplate bandTemplate = TemplateProvider.retrieveBandTemplate(bandTemplateFile);

		// bandTemplate.setPosition(Position.BOTTON);

		// creamos el contenido de la banda

		FieldContainer fc = BandSelloOrgano.build("CSV8976450048556", "Andrés Gaudioso Simón",
				"https://aplicaciones.aragon.es/ccsv_pub/", "13/07/2009",
				"Colegio Profesional de Ingenieros Técnicos en Informática de Aragón");

		File orquestationFile = File.createTempFile("Orquest", ".pdf");

		DocumentBandGenerator.buildAsTempFile(orquestationFile, pdfTemplate, fieldContainer, bandTemplate, fc);

		File noband = File.createTempFile("NoBand", ".pdf");
		DocumentBandGenerator.buildAsTempFile(noband, pdfTemplate, fieldContainer, null, fc);

		File withBand = File.createTempFile("NoBand", ".pdf");
		DocumentBandGenerator.addBand(noband, withBand, bandTemplate, fc);

	}
}
