package es.sinjava.pdfworld;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.sinjava.model.BandSelloOrgano;
import es.sinjava.model.BandTemplate;
import es.sinjava.model.FieldContainer;
import es.sinjava.pdf.generator.WaterBandGenerator;
import es.sinjava.util.TemplateProvider;

public class WaterBandFileOverlaping {
	
	private static Logger logger = LoggerFactory.getLogger(WaterBandFileOverlaping.class);

	public static void main(String... args) throws Exception {
		logger.info("Begin Process");
		File inputFile = new File("Sherlock_Holmes.pdf");
		
		File outFile = new File("Over_Sherlock_Holmes.pdf");

		// Recuperamos la banda
		File bandTemplateFile = new File(WaterBandFileOverlaping.class.getClassLoader().getResource("bandOrganismo.xml").getFile());
		BandTemplate bandTemplate = TemplateProvider.retrieveBandTemplate(bandTemplateFile);
		
		FieldContainer fc = BandSelloOrgano.build("CSV8976450048556", "Andrés Gaudioso Simón",
				"https://aplicaciones.aragon.es/ccsv_pub/", "13/07/2009",
				"Instituto Aragonés de la Mujer");
		bandTemplate.setQrCode("Jovencillo emponzoñado de Wisky que figurota exhibe");

		 WaterBandGenerator.overlapBand(inputFile, outFile, bandTemplate, fc, false);
		 logger.info("End Process");
	}

}
