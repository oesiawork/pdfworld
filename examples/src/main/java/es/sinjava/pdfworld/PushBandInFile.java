package es.sinjava.pdfworld;

import java.io.File;

import es.sinjava.model.BandSelloOrgano;
import es.sinjava.model.BandTemplate;
import es.sinjava.model.FieldContainer;
import es.sinjava.pdf.generator.DocumentBandGenerator;
import es.sinjava.util.TemplateProvider;

public class PushBandInFile {

	public static void main(String... args) throws Exception {
		File inputFile = new File("Sherlock_Holmes.pdf");
		File outFile = new File("Band_Sherlock_Holmes.pdf");

		// Recuperamos la banda
		File bandTemplateFile = new File(PushBandInFile.class.getClassLoader().getResource("bandOrganismo.xml").getFile());
		BandTemplate bandTemplate = TemplateProvider.retrieveBandTemplate(bandTemplateFile);

		// Rellenamos los datos de la banda con la clase de utilidad que nos marca los
		// campos. Patrón Builder

		FieldContainer fc = BandSelloOrgano.build("CSV8976450048556", "Andrés Gaudioso Simón",
				"https://aplicaciones.aragon.es/ccsv_pub/", "13/07/2009",
				"Instituto Aragonés de la Mujer");
		bandTemplate.setQrCode("Jovencillo emponzoñado de Wisky que figurota exhibe");

		 DocumentBandGenerator.addBand(inputFile, outFile, bandTemplate, fc);
	}

}
