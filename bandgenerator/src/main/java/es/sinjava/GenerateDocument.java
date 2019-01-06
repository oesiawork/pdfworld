package es.sinjava;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.sinjava.pdf.generator.BeaGenerator;
import es.sinjava.pdf.model.PdfTemplate;
import es.sinjava.util.TemplateProvider;

public class GenerateDocument {

	private final static Logger logger = LoggerFactory.getLogger(GenerateDocument.class);


	public static void main(String[] args) throws InvalidPasswordException, IOException, JAXBException {
		// Leer el archivo a empotrar

		logger.info("Begin main");

		File template = new File(DGAMultiBand.class.getClassLoader().getResource("templateList.xml").getFile());
		PdfTemplate newPDFTemplate = TemplateProvider.retrievePdfTemplate(template);

		File tempFile = File.createTempFile("Generator", ".pdf");
		boolean isPdfA= false;
		BeaGenerator.getInstance().writePDFFile(newPDFTemplate.getStoreContentList(), tempFile, isPdfA);

		logger.info("End main");

	}
}
