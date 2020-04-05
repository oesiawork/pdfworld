package es.sinjava.pdf.custom;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.sinjava.model.BandSelloOrgano;
import es.sinjava.model.BandTemplate;
import es.sinjava.model.FieldContainer;
import es.sinjava.pdf.generator.DocumentBandGenerator;
import es.sinjava.pdf.model.PdfTemplate;
import es.sinjava.util.TemplateProvider;

public class CustomTemplatesTest {

	private final static Logger logger = LoggerFactory.getLogger(CustomTemplatesTest.class);

	@Test
	public void testBuildAsFile() throws IOException, JAXBException {
		logger.debug("Begin testBuildAsFile");
		File oficioRemision = File.createTempFile("oficioRemision", ".pdf");

		// Paso 1
		// Seleccionamos la plantilla del pdf: el oficio
		File templatePDF = new File(CustomTemplatesTest.class.getClassLoader().getResource("oficio.xml").getFile());
		PdfTemplate pdfTemplate = TemplateProvider.retrievePdfTemplate(templatePDF);

		// Paso 2
		// Seleccionamos los datos del documentos. Que tienen que mapear contra los de
		// la plantilla
		FieldContainer fieldContainer = new FieldContainer();
		Map<String, String> container = new HashMap<>();
		container.put("csvDocument", "CSV8976450048556BENT");
		container.put("nombre", "Andrés Gaudioso");
		List<String> inputs= new ArrayList<>();
		inputs.add("Expediente -CSV: CSV7W1573D14L130BENT");
		inputs.add("Registro -CSV: CSV7W1573D14L130SRT");
		inputs.add("Documentación Justificativa -CSV: CSV7W1573D14L130PFI");
		inputs.add("Documentación Justificativa -CSV: CSV7W1573D14L130TTO");

		container.put("param", StringUtils.join(inputs, "$" ));
		fieldContainer.setContainer(container);

		// Paso 3
		// Seleccionamos la plantilla de la banda
		File bandTemplateFile = new File(
				CustomTemplatesTest.class.getClassLoader().getResource("bandOrganismo.xml").getFile());
		BandTemplate bandTemplate = TemplateProvider.retrieveBandTemplate(bandTemplateFile);

		// Paso 4
		// Seleccionamos los datos de la banda
		FieldContainer fcBanda = BandSelloOrgano.build("CSV8976450048556BENT", "Andrés Gaudioso Simón",
				"https://aplicaciones.aragon.es/ccsv_pub/", "13/07/2009", "Bandeja de entrada mola");

		DocumentBandGenerator.buildAsFile(oficioRemision, pdfTemplate, fieldContainer, bandTemplate, fcBanda);

//		oficioRemision.deleteOnExit();
		Assert.assertTrue(oficioRemision.canRead());
		logger.debug("End testBuildAsFile");
	}

	@Test
	public void testOficio() throws IOException, JAXBException {
		logger.debug("Begin testBuildAsFile");
		File oficioRemision = File.createTempFile("oficioRemisionSinBanda", ".pdf");

		// Paso 1
		// Seleccionamos la plantilla del pdf: el oficio
		File templatePDF = new File(CustomTemplatesTest.class.getClassLoader().getResource("oficio.xml").getFile());
		PdfTemplate pdfTemplate = TemplateProvider.retrievePdfTemplate(templatePDF);

		// Paso 2
		// Seleccionamos los datos del documentos. Que tienen que mapear contra los de
		// la plantilla
		FieldContainer fieldContainer = new FieldContainer();
		Map<String, String> container = new HashMap<>();
		container.put("csvDocument", "CSV8976450048556BENT");
		container.put("nombre", "Andrés Gaudioso");
		container.put("param", "Andrés$Beatriz$Tomás$Carmen");
		fieldContainer.setContainer(container);

		DocumentBandGenerator.buildAsFile(oficioRemision, pdfTemplate, fieldContainer, null, null);
//		oficioRemision.deleteOnExit();
		Assert.assertTrue(oficioRemision.canRead());
		logger.debug("End testBuildAsFile");
	}

}
