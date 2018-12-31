package es.sinjava;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.sinjava.model.FieldContainer;
import es.sinjava.pdf.generator.BeaGenerator;
import es.sinjava.pdf.model.PdfTemplate;
import es.sinjava.util.DraftFactory;
import es.sinjava.util.TemplateProvider;

public class GenerateDraftPDFA {

	private final static Logger logger = LoggerFactory.getLogger(GenerateDraftPDFA.class);

	public static void main(String[] args) throws InvalidPasswordException, IOException, JAXBException {

		logger.info("Begin main");

		File template = new File(DGAMultiBand.class.getClassLoader().getResource("draft.xml").getFile());
		PdfTemplate preview = TemplateProvider.retrievePdfTemplate(template);
		FieldContainer fieldContainer = new FieldContainer();
		Map<String, String> container = new HashMap<>();
		container.put("nombre", "Andrés Gaudioso");
		container.put("param", "Andrés$Beatriz$Tomás$Idoia");
		fieldContainer.setContainer(container);

		PdfTemplate pdfTemplate = DraftFactory.getDraft(preview, fieldContainer);

		File tempFile = File.createTempFile("Draft", ".pdf");
		boolean requirePDFA = true;

		BeaGenerator.getInstance().writePDFFile(pdfTemplate.getStoreContentList(), tempFile, requirePDFA);

		logger.info("End main");

	}
}
