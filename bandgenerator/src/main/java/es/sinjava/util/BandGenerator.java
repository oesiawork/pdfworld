package es.sinjava.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.sinjava.model.Band;

public class BandGenerator {

	private final static Logger logger = LoggerFactory.getLogger(BandGenerator.class);

	PDDocument documentOut;
	PDDocument document;
	Band band;

	private BandGenerator(PDDocument documentIn, Band bandIn) {
		document = documentIn;
		band = bandIn;
	}

	public static BandGenerator getInstance(PDDocument documentIn, Band bandIn) {
		logger.debug("Begin getInstance");
		BandGenerator bandGenerator = new BandGenerator(documentIn, bandIn);
		return bandGenerator;
	}

	public static BandGenerator getDummiInstance(PDDocument documentIn, Band bandIn) {
		// Can optimize this?
		BandGenerator bandGenerator = new BandGenerator(documentIn, bandIn);
		bandGenerator.documentOut = documentIn;
		return bandGenerator;
	}

	public String buildAsTempFile() {
		logger.debug("Begin buildAsTempFile");
		File tempFile = null;
		PDFAssembler pdfAssembler = new PDFAssembler();
		try {
			PDFont font = PDType1Font.HELVETICA;
			PDDocument documentOut = pdfAssembler.build(document, band,font);
			tempFile = File.createTempFile("Bea", ".pdf");
			documentOut.save(tempFile);
		} catch (IOException e) {
			logger.error("KO creating temp file", e);
		}
		return tempFile.getAbsolutePath();
	}

	public void buildAsFile(String file) {

		logger.debug("Begin buildAsFile");

		try {
			File tempFile = new File(file);
			documentOut.save(tempFile);
		} catch (IOException e) {
			logger.error("KO creating file", e);
		}

	}

	public void buildAsOutpuStream(OutputStream oe) {
		logger.debug("Begin buildAsOutpuStream");

		try {
			documentOut.save(oe);
		} catch (IOException e) {
			logger.error("KO creating file", e);
		}
	}

	public byte[] buildAsByteArray() {
		logger.debug("Begin buildAsByteArray");
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		buildAsOutpuStream(stream);
		return stream.toByteArray();
	}

	public OutputStream buildAsOutpuStream() {
		logger.debug("Begin buildAsOutpuStream");
		OutputStream stream = new ByteArrayOutputStream();
		try {
			documentOut.save(stream);
		} catch (IOException e) {
			logger.error("KO OutputStream", e);
		}
		return stream;
	}

}
