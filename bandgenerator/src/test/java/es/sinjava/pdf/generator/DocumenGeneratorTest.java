package es.sinjava.pdf.generator;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.sinjava.model.BandSelloOrgano;
import es.sinjava.model.BandTemplate;
import es.sinjava.model.FieldContainer;
import es.sinjava.pdf.model.PdfTemplate;
import es.sinjava.util.TemplateProvider;

public class DocumenGeneratorTest {

	// Logger para rendimiento
	private final static Logger logger = LoggerFactory.getLogger(DocumenGeneratorTest.class);
	// Lo que van a compartir los test
	private static PdfTemplate pdfTemplate;
	private static FieldContainer fieldContainer;

	@Test
	public void testBuildAsFile() throws IOException {
		logger.debug("Begin testBuildAsFile");
		File destFile = File.createTempFile("testBuildAsFile", ".pdf");
		DocumentGenerator.documentoFromTemplate(pdfTemplate, fieldContainer, destFile);
		Assert.assertTrue(destFile.canRead());
		logger.debug("End testBuildAsFile");
	}
	
	@Before
	public void setUp() throws Exception {
	}

	// Carga antes de los test
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		File templatePDF = new File(
				DocumenGeneratorTest.class.getClassLoader().getResource("document.xml").getFile());
		pdfTemplate = TemplateProvider.retrievePdfTemplate(templatePDF);
		// datos parametrizados para la plantilla de pdf
		fieldContainer = new FieldContainer();
		Map<String, String> container = new HashMap<>();
		container.put("nombre", "Andrés Gaudioso");
		container.put("param", "Andrés$Beatriz$Tomás$Carmen");
		fieldContainer.setContainer(container);
	}

}
