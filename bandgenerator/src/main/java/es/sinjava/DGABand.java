package es.sinjava;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.sinjava.model.Band;
import es.sinjava.model.Band.Position;
import es.sinjava.model.BandSelloOrgano;
import es.sinjava.model.BandTemplate;
import es.sinjava.model.FieldContainer;
import es.sinjava.model.Template;
import es.sinjava.util.BandFactory;
import es.sinjava.util.BandGenerator;
import es.sinjava.util.TemplateProvider;

public class DGABand {

	private final static Logger logger = LoggerFactory.getLogger(DGABand.class);

	private static File entrada = new File(DGABand.class.getClassLoader().getResource("toembed.pdf").getFile());

	public static void main(String[] args) throws InvalidPasswordException, IOException, JAXBException {
		// Leer el archivo a empotrar

		logger.debug("Begin main");
		PDDocument documentoBase = PDDocument.load(entrada);

		File bandTemplateFile = new File(
				GenerateDraftWithBanner.class.getClassLoader().getResource("bandTemplate.xml").getFile());

		BandTemplate bandTemplate = TemplateProvider.retrieveBandTemplate(bandTemplateFile);

		FieldContainer fc = BandSelloOrgano.build("CSV8976450048556", "Andrés Gaudioso Simón",
				"https://aplicaciones.aragon.es/ccsv_pub/", "13/07/2009",
				"Colegio Profesional de Ingenieros Técnicos en Informática de Aragón");

		Band band = BandFactory.getBand(bandTemplate, fc);

		band.setPosition(Position.BOTTON);
		BandGenerator.getInstance(documentoBase, band).buildAsTempFile();
		// añadirle la banda
		logger.debug("End main");

	}
}
