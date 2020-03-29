package es.sinjava.pdfworld;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

import org.apache.pdfbox.pdmodel.PDDocument;

import es.sinjava.docx.DocPDFConverter;
import es.sinjava.model.FieldContainer;

public class GenerateDocumentFromWord {

	public static void main(String[] args) throws Exception {
		InputStream docInPut = new FileInputStream(
				GenerateDocumentFromWord.class.getClassLoader().getResource("solicitudbase.docx").getFile());
		FieldContainer fieldContainer = new FieldContainer();
		fieldContainer.setContainer(new HashMap<String, String>());
		fieldContainer.getContainer().put("csvDocument", "CSV45998995556BENT");
		fieldContainer.getContainer().put("listItem", "Tocomocho");
		fieldContainer.getContainer().put("date", "28 de Diciembre de 2018");
		PDDocument documentoBase = DocPDFConverter.getPDF(docInPut, fieldContainer);
		File tempFile =  new File("Word.pdf");
		documentoBase.save(tempFile);
	}

}
