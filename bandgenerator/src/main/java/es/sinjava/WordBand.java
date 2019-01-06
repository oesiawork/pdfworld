package es.sinjava;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.sinjava.docx.DocPDFConverter;
import es.sinjava.model.Band;
import es.sinjava.model.Band.Position;
import es.sinjava.model.BandSelloOrgano;
import es.sinjava.model.FieldContainer;

public class WordBand {

	private final static Logger logger = LoggerFactory.getLogger(WordBand.class);

	public static void main(String[] args) throws Exception {
		// Leer el archivo a empotrar

		logger.debug("Begin main");
		InputStream docInPut = new FileInputStream(
				WordBand.class.getClassLoader().getResource("libros.docx").getFile());

		FieldContainer fieldContainer = new FieldContainer();
		fieldContainer.setContainer(new HashMap<String, String>());
		fieldContainer.getContainer().put("nombre", "Andrés");
		fieldContainer.getContainer().put("date", "28 de Diciembre de 2018");

		PDDocument documentoBase = DocPDFConverter.getPDF(docInPut, fieldContainer);
		documentoBase.save(File.createTempFile("Word", ".pdf"));

		logger.debug("End main");

	}
}
