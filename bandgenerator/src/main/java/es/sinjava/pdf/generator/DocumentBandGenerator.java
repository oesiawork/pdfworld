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
import es.sinjava.util.BeaPDFBandAssembler;

public class DocumentBandGenerator {

	private static final Logger logger = LoggerFactory.getLogger(DocumentBandGenerator.class);

	public static void buildAsFile(File orquestationFile, PdfTemplate pdfTemplate, FieldContainer fieldContainer,
			BandTemplate bandTemplate, FieldContainer fc) throws IOException {

		logger.info("Begin buildAsTempFile");
		PdfTemplate pdfDraft = DraftFactory.getDraft(pdfTemplate, fieldContainer);
		BeaPDFAssembler beapdfAssembler = new BeaPDFAssembler();
		Band band =null;
		if (bandTemplate!= null) {
			band = BandFactory.getBand(bandTemplate, fc);
			band.setQrCode("https://aplicaciones.aragon.es/ccsv_pub/CSV8976450048556");
		}
		
		PDDocument pdDocument = beapdfAssembler.write(pdfDraft.getStoreContentList(), band);
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

			PDMetadata metadata = new PDMetadata(pdDocument);
			metadata.importXMPMetadata(baos.toByteArray());
			pdDocument.getDocumentCatalog().setMetadata(metadata);

		} catch (BadFieldValueException | TransformerException e) {
			logger.error("Petada nueva", e);
		}

		// sRGB output intent
		InputStream colorProfile = DocumentBandGenerator.class.getClassLoader().getResourceAsStream("pdfa/sRGB.icc");
		PDOutputIntent intent = new PDOutputIntent(pdDocument, colorProfile);
		intent.setInfo("sRGB IEC61966-2.1");
		intent.setOutputCondition("sRGB IEC61966-2.1");
		intent.setOutputConditionIdentifier("sRGB IEC61966-2.1");
		intent.setRegistryName("http://www.color.org");
		pdDocument.getDocumentCatalog().addOutputIntent(intent);
		
		pdDocument.save(orquestationFile);
	}

	public static void addBand(File noband, File withBand, BandTemplate bandTemplate, FieldContainer fc)
			throws IOException {
		BeaPDFBandAssembler beaPDFAssembler = new BeaPDFBandAssembler();
		Band band = BandFactory.getBand(bandTemplate, fc);
		PDDocument document = PDDocument.load(noband);
		PDDocument returningFile = beaPDFAssembler.insertBand(document, band);

		returningFile.save(withBand);

	}

}
