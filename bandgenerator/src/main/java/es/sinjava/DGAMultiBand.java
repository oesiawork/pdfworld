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

public class DGAMultiBand {

	private final static Logger logger = LoggerFactory.getLogger(DGAMultiBand.class);

	private static File entrada = new File(DGAMultiBand.class.getClassLoader().getResource("eclipse.pdf").getFile());

	public static void main(String[] args) throws InvalidPasswordException, IOException {
		// Leer el archivo a empotrar

		logger.info("Begin main");
		final PDDocument documentoBase = PDDocument.load(entrada);
		// String urlVerification, String date, String organismName)
		
		for ( int index = 0; index < 2; index++) {

			Thread thread = new Thread(new Runnable() {
				
				
				@Override
				public void run() {
					String name = Thread.currentThread().getName();
					logger.debug(" Ejecuto el runnable " + name );
					
					
					Band band = new BandSelloOrgano("CSV897"+System.currentTimeMillis(), "Andrés Gaudioso Simón",
							"https://aplicaciones.aragon.es/ccsv_pub/", "13/07/2009",
							"Colegio Profesional de Ingenieros Técnicos en Informática de Aragón");
					band.setPosition(Position.BOTTON);
					BandGenerator.getInstance(documentoBase, band).buildAsTempFile();
					logger.debug(" End el runnable "+name);
				}
			});
			thread.start();

		}

		// añadirle la banda
		logger.info("End main");

	}
}
