package es.sinjava.pdf.generator;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
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

	public static void buildAsTempFile(File orquestationFile, PdfTemplate pdfTemplate, FieldContainer fieldContainer,
			BandTemplate bandTemplate, FieldContainer fc) throws IOException {

		logger.info("Begin buildAsTempFile");
		PdfTemplate pdfDraft = DraftFactory.getDraft(pdfTemplate, fieldContainer);
		BeaPDFAssembler beapdfAssembler = new BeaPDFAssembler();
		if (bandTemplate == null) {
			beapdfAssembler.write(pdfDraft.getStoreContentList(), null, orquestationFile);
		} else {
			Band band = BandFactory.getBand(bandTemplate, fc);
			band.setQrCode("https://aplicaciones.aragon.es/ccsv_pub/CSV8976450048556");
			beapdfAssembler.write(pdfDraft.getStoreContentList(), band, orquestationFile);
		}
	}

	public static void addBand(File noband, File withBand, BandTemplate bandTemplate, FieldContainer fc) throws IOException {
		BeaPDFBandAssembler beaPDFAssembler = new BeaPDFBandAssembler();
		Band band = BandFactory.getBand(bandTemplate, fc);
		PDDocument document = PDDocument.load(noband);
		PDDocument returningFile = beaPDFAssembler.insertBand(document, band);
		
		returningFile.save(withBand);

	}

}
