package es.sinjava.pdf.generator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.pdfbox.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.sinjava.model.Band.Position;
import es.sinjava.model.BandSelloOrgano;
import es.sinjava.model.BandTemplate;
import es.sinjava.model.FieldContainer;
import es.sinjava.model.Template;
import es.sinjava.pdf.model.PdfTemplate;
import es.sinjava.util.TemplateProvider;

public class DocumentBandGeneratorTest {

	// Logger para rendimiento
	private final static Logger logger = LoggerFactory.getLogger(DocumentBandGeneratorTest.class);
	// Lo que van a compartir los test
	private static PdfTemplate pdfTemplate;
	private static FieldContainer fieldContainer;
	private static BandTemplate bandTemplate;
	private static FieldContainer fcBanda;
	private static File senuelo;
	private static byte[] senueloByte;

	@Test
	public void testBuildAsFile() throws IOException {
		logger.debug("Begin testBuildAsFile");
		File orquestationFile = File.createTempFile("testBuildAsFile", ".pdf");
		DocumentBandGenerator.buildAsFile(orquestationFile, pdfTemplate, fieldContainer, bandTemplate, fcBanda);
		orquestationFile.deleteOnExit();
		Assert.assertTrue(orquestationFile.canRead());
		logger.debug("End testBuildAsFile");
	}

	@Test
	public void testBuildAsFileWithoutBand() throws IOException {
		logger.debug("Begin testBuildAsFile");
		File orquestationFile = File.createTempFile("testBuildAsFileWithoutBand", ".pdf");
		orquestationFile.deleteOnExit();
		DocumentBandGenerator.buildAsFile(orquestationFile, pdfTemplate, fieldContainer, null, null);
		Assert.assertTrue(orquestationFile.canRead());
		logger.debug("End testBuildAsFile");
	}

	@Test
	public void testBuildAsByteArray() throws IOException {
		logger.debug("Begin testBuildAsByteArray");
		fcBanda.getContainer().put(Template.QR, "1828-1598");
		byte[] document = DocumentBandGenerator.buildAsByteArray(pdfTemplate, fieldContainer, bandTemplate, fcBanda);
		Assert.assertTrue(document.length > 25);
		logger.debug("End testBuildAsByteArray");
	}

	@Test
	public void testBuildAsByteArrayWhithoutBand() throws IOException {
		logger.debug("Begin testBuildAsByteArrayWhithoutBand");
		byte[] document = DocumentBandGenerator.buildAsByteArray(pdfTemplate, fieldContainer, null, null);
		Assert.assertTrue(document.length > 25);
		logger.debug("End testBuildAsByteArrayWhithoutBand");
	}

	@Test
	public void testAddBandByteArrayFileBandTemplateFieldContainer() throws IOException {
		File orquestationFile = File.createTempFile("testAddBandByteArrayFileBandTemplateFieldContainer", ".pdf");
		orquestationFile.deleteOnExit();
		DocumentBandGenerator.addBand(senuelo, orquestationFile, bandTemplate, fcBanda);
		Assert.assertTrue(orquestationFile.canRead());
	}

	@Test
	public void testAddBandFileFileBandTemplateFieldContainer() throws IOException {
		File orquestationFile = File.createTempFile("Test", ".pdf");
		orquestationFile.deleteOnExit();
		DocumentBandGenerator.addBand(senueloByte, orquestationFile, bandTemplate, fcBanda);
		Assert.assertTrue(orquestationFile.canRead());
	}
	
	@Test
	public void testAddBandHorizontalFile() throws IOException {
		File orquestationFile = File.createTempFile("Horizontal", ".pdf");
//		orquestationFile.deleteOnExit();
		InputStream in = DocumentBandGeneratorTest.class.getClassLoader().getResourceAsStream("Horizontal.pdf");
		byte[] horizontal = IOUtils.toByteArray(in );
		DocumentBandGenerator.addBand(horizontal, orquestationFile, bandTemplate, fcBanda);
		Assert.assertTrue(orquestationFile.canRead());
	}
	
	@Test
	public void testAddBanHorizontalInHorizontalFile() throws IOException {
		File orquestationFile = File.createTempFile("Horizontal", ".pdf");
//		orquestationFile.deleteOnExit();
		InputStream in = DocumentBandGeneratorTest.class.getClassLoader().getResourceAsStream("Horizontal.pdf");
		byte[] horizontal = IOUtils.toByteArray(in );
		bandTemplate.setPosition(Position.BOTTON);
		DocumentBandGenerator.addBand(horizontal, orquestationFile, bandTemplate, fcBanda);
		bandTemplate.setPosition(Position.LEFT);
		Assert.assertTrue(orquestationFile.canRead());
	}
	
	
	

	@Before
	public void setUp() throws Exception {
	}

	// Carga antes de los test
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		File templatePDF = new File(
				DocumentBandGeneratorTest.class.getClassLoader().getResource("orquest.xml").getFile());
		pdfTemplate = TemplateProvider.retrievePdfTemplate(templatePDF);
		// datos parametrizados para la plantilla de pdf
		fieldContainer = new FieldContainer();
		Map<String, String> container = new HashMap<>();
		container.put("nombre", "Andrés Gaudioso");
		container.put("param", "Andrés$Beatriz$Tomás$Carmen");
		fieldContainer.setContainer(container);

		File bandTemplateFile = new File(
				DocumentBandGeneratorTest.class.getClassLoader().getResource("bandTemplate.xml").getFile());
		bandTemplate = TemplateProvider.retrieveBandTemplate(bandTemplateFile);

		fcBanda = BandSelloOrgano.build("CSV8976450048556", "Andrés Gaudioso Simón",
				"https://aplicaciones.aragon.es/ccsv_pub/", "13/07/2009",
				"Colegio Profesional de Ingenieros Técnicos en Informática de Aragón");
		senuelo = File.createTempFile("senuelo", ".pdf");
		DocumentBandGenerator.buildAsFile(senuelo, pdfTemplate, fieldContainer, null, null);

		senueloByte = DocumentBandGenerator.buildAsByteArray(pdfTemplate, fieldContainer, null, null);
	}

}
