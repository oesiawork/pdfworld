/*
 * 
 */
package es.sinjava.pdf.generator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.transform.TransformerException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent;
import org.apache.xmpbox.XMPMetadata;
import org.apache.xmpbox.schema.DublinCoreSchema;
import org.apache.xmpbox.schema.PDFAIdentificationSchema;
import org.apache.xmpbox.type.BadFieldValueException;
import org.apache.xmpbox.xml.XmpSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.sinjava.factory.BandFactory;
import es.sinjava.factory.DraftFactory;
import es.sinjava.model.Band;
import es.sinjava.model.BandTemplate;
import es.sinjava.model.FieldContainer;
import es.sinjava.pdf.model.PdfTemplate;
import es.sinjava.util.BeaPDFAssembler;
import es.sinjava.util.WaterBandAssembler;

/**
 * The Class DocumentBandGenerator.
 */
public class WaterBandGenerator {

	private static final String S_RGB_IEC61966_2_1 = "sRGB IEC61966-2.1";
	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(WaterBandGenerator.class);

	private WaterBandGenerator() {
	}

	/**
	 * Adds the band.
	 *
	 * @param noband       the noband
	 * @param withBand     the with band
	 * @param bandTemplate the band template
	 * @param fc           the fc
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void addBand(File noband, File withBand, BandTemplate bandTemplate, FieldContainer fc, boolean pdfa)
			throws IOException {
		WaterBandAssembler wba = new WaterBandAssembler();
		Band band = BandFactory.getBand(bandTemplate, fc);
		PDDocument document = PDDocument.load(noband);
		PDDocument returningFile = wba.insertBand(document, band);
		if (pdfa) {
			makePDFA(returningFile);
		}
		returningFile.save(withBand);
	}

	public static void overlapBand(File inputFile, File outFile, BandTemplate bandTemplate, FieldContainer fc, boolean pdfa) throws IOException {
		WaterBandAssembler wba = new WaterBandAssembler();
		Band band = BandFactory.getBand(bandTemplate, fc);
		PDDocument document = PDDocument.load(inputFile);
		
		if (pdfa) {
			makePDFA(document);
		}

		wba.overlapBand(document, band, outFile);
	}

	private static void makePDFA(PDDocument document) throws IOException {
		XMPMetadata xmp = XMPMetadata.createXMPMetadata();
		try {
			DublinCoreSchema dc = xmp.createAndAddDublinCoreSchema();
			dc.setTitle("Documento PDF");

			PDFAIdentificationSchema id = xmp.createAndAddPFAIdentificationSchema();
			id.setPart(1);
			id.setConformance("B");

			XmpSerializer serializer = new XmpSerializer();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			serializer.serialize(xmp, baos, true);

			PDMetadata metadata = new PDMetadata(document);
			metadata.importXMPMetadata(baos.toByteArray());
			document.getDocumentCatalog().setMetadata(metadata);

		} catch (BadFieldValueException | TransformerException e) {
			logger.error("Petada nueva", e);
		}

		// sRGB output intent
		InputStream colorProfile = WaterBandGenerator.class.getClassLoader().getResourceAsStream("pdfa/sRGB.icc");
		PDOutputIntent intent = new PDOutputIntent(document, colorProfile);
		intent.setInfo(S_RGB_IEC61966_2_1);
		intent.setOutputCondition(S_RGB_IEC61966_2_1);
		intent.setOutputConditionIdentifier(S_RGB_IEC61966_2_1);
		intent.setRegistryName("http://www.color.org");
		document.getDocumentCatalog().addOutputIntent(intent);
	}

}
