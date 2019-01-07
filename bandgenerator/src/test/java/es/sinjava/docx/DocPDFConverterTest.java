package es.sinjava.docx;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import es.sinjava.model.FieldContainer;

public class DocPDFConverterTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
//	@Ignore
	public void testGetPDFInputStream() throws Exception {
		InputStream docInPut = new FileInputStream(
				DocPDFConverterTest.class.getClassLoader().getResource("libros.docx").getFile());
		FieldContainer fieldContainer = new FieldContainer();
		fieldContainer.setContainer(new HashMap<String, String>());
		fieldContainer.getContainer().put("nombre", "Andrés");
		fieldContainer.getContainer().put("date", "28 de Diciembre de 2018");
		PDDocument documentoBase = DocPDFConverter.getPDF(docInPut, fieldContainer);
		File tempFile = File.createTempFile("Word", ".pdf");
		tempFile.deleteOnExit();
		documentoBase.save(tempFile);
		Assert.assertTrue(tempFile.canRead());
	}

	@Test
//	@Ignore
	public void testGetPDFInputStreamFieldContainer() throws Exception {
		InputStream docInPut = DocPDFConverterTest.class.getClassLoader().getResourceAsStream("libros.docx");
		PDDocument documentoBase = DocPDFConverter.getPDF(docInPut);
		File tempFile = File.createTempFile("Word", ".pdf");
		tempFile.deleteOnExit();
		documentoBase.save(tempFile);
		Assert.assertTrue(tempFile.canRead());

	}

}
