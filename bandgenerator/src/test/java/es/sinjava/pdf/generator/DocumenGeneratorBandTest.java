package es.sinjava.pdf.generator;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

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

public class DocumenGeneratorBandTest {

	// Logger para rendimiento
	private final static Logger logger = LoggerFactory.getLogger(DocumenGeneratorBandTest.class);
	// Lo que van a compartir los test
	private static PdfTemplate pdfTemplate;
	private static FieldContainer fieldContainer;

	private File inFile;

	@Test
	public void addBand() throws IOException, JAXBException {
		logger.debug("Begin addBand");
		File destFile = File.createTempFile("band", ".pdf");
		destFile.deleteOnExit();
		File bandTemplateFile = new File(
				DocumenGeneratorBandTest.class.getClassLoader().getResource("bandOrganismo.xml").getFile());
		BandTemplate bandTemplate = TemplateProvider.retrieveBandTemplate(bandTemplateFile);

		FieldContainer fc = BandSelloOrgano.build("CSV8976450048556", "Andrés Gaudioso Simón",
				"https://aplicaciones.aragon.es/ccsv_pub/", "13/07/2009", "Instituto Aragonés de la Mujer");
		bandTemplate.setQrCode("Jovencillo emponzoñado de Wisky que figurota exhibe");

		WaterBandGenerator.addBand(inFile, destFile, bandTemplate, fc, false);

		Assert.assertTrue(destFile.canRead());
		logger.debug("End testBuildAsFile");
	}

	@Test
	public void addBandPDFA() throws IOException, JAXBException {
		logger.debug("Begin testBuildAsFile");
		File destFile = File.createTempFile("band", ".pdf");
		destFile.deleteOnExit();
		File bandTemplateFile = new File(
				DocumenGeneratorBandTest.class.getClassLoader().getResource("bandOrganismo.xml").getFile());
		BandTemplate bandTemplate = TemplateProvider.retrieveBandTemplate(bandTemplateFile);

		FieldContainer fc = BandSelloOrgano.build("CSV8976450048556", "Andrés Gaudioso Simón",
				"https://aplicaciones.aragon.es/ccsv_pub/", "13/07/2009", "Instituto Aragonés de la Mujer");
		bandTemplate.setQrCode("Jovencillo emponzoñado de Wisky que figurota exhibe");

		WaterBandGenerator.addBand(inFile, destFile, bandTemplate, fc, true);

		Assert.assertTrue(destFile.canRead());
		logger.debug("End testBuildAsFile");
	}
	
	@Test
	public void overlapBand() throws IOException, JAXBException {
		logger.debug("Begin addBand");
		File destFile = File.createTempFile("band", ".pdf");
		destFile.deleteOnExit();
		File bandTemplateFile = new File(
				DocumenGeneratorBandTest.class.getClassLoader().getResource("bandOrganismo.xml").getFile());
		BandTemplate bandTemplate = TemplateProvider.retrieveBandTemplate(bandTemplateFile);

		FieldContainer fc = BandSelloOrgano.build("CSV8976450048556", "Andrés Gaudioso Simón",
				"https://aplicaciones.aragon.es/ccsv_pub/", "13/07/2009", "Instituto Aragonés de la Mujer");
		bandTemplate.setQrCode("Jovencillo emponzoñado de Wisky que figurota exhibe");

		WaterBandGenerator.overlapBand(inFile, destFile, bandTemplate, fc, false);

		Assert.assertTrue(destFile.canRead());
		logger.debug("End testBuildAsFile");
	}

	@Test
	public void overlapBandPDFA() throws IOException, JAXBException {
		logger.debug("Begin testBuildAsFile");
		File destFile = File.createTempFile("band", ".pdf");
		destFile.deleteOnExit();
		File bandTemplateFile = new File(
				DocumenGeneratorBandTest.class.getClassLoader().getResource("bandOrganismo.xml").getFile());
		BandTemplate bandTemplate = TemplateProvider.retrieveBandTemplate(bandTemplateFile);

		FieldContainer fc = BandSelloOrgano.build("CSV8976450048556", "Andrés Gaudioso Simón",
				"https://aplicaciones.aragon.es/ccsv_pub/", "13/07/2009", "Instituto Aragonés de la Mujer");
		bandTemplate.setQrCode("Jovencillo emponzoñado de Wisky que figurota exhibe");

		WaterBandGenerator.overlapBand(inFile, destFile, bandTemplate, fc, true);

		Assert.assertTrue(destFile.canRead());
		logger.debug("End testBuildAsFile");
	}

	
	
	
	

	@Before
	public void setUp() throws Exception {
		inFile = File.createTempFile("documentoBand", ".pdf");
		inFile.deleteOnExit();
		DocumentGenerator.documentBandFromTemplate(pdfTemplate, fieldContainer, inFile);

	}

	// Carga antes de los test
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		File templatePDF = new File(
				DocumenGeneratorBandTest.class.getClassLoader().getResource("document.xml").getFile());
		pdfTemplate = TemplateProvider.retrievePdfTemplate(templatePDF);
		// datos parametrizados para la plantilla de pdf
		fieldContainer = new FieldContainer();
		Map<String, String> container = new HashMap<>();
		container.put("nombre", "Andrés Gaudioso");
		container.put("param", "Andrés$Beatriz$Tomás$Carmen");
		fieldContainer.setContainer(container);
	}

}
