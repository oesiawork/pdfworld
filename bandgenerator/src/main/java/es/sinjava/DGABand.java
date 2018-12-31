package es.sinjava;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.sinjava.model.Band;
import es.sinjava.model.Band.Position;
import es.sinjava.model.BandSelloOrgano;
import es.sinjava.util.BandGenerator;

public class DGABand {

	private final static Logger logger = LoggerFactory.getLogger(DGABand.class);

	private static File entrada = new File(DGABand.class.getClassLoader().getResource("toembed.pdf").getFile());

	public static void main(String[] args) throws InvalidPasswordException, IOException {
		// Leer el archivo a empotrar

		logger.debug("Begin main");
		PDDocument documentoBase = PDDocument.load(entrada);
		// String urlVerification, String date, String organismName)
		Band band = new BandSelloOrgano("CSV8976450048556", "Andrés Gaudioso Simón",
				"https://aplicaciones.aragon.es/ccsv_pub/", "13/07/2009",
				"Colegio Profesional de Ingenieros Técnicos en Informática de Aragón");
		band.setPosition(Position.LEFT);
		BandGenerator.getInstance(documentoBase, band).buildAsTempFile();
		// añadirle la banda
		logger.debug("End main");

	}
}
