/*
 * 
 */
package es.sinjava.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

/**
 * The Class PDFAssembler. addBand
 */
public class PDFAssembler {
	
	/** The Constant WIDTH. */
	protected static final float WIDTH = PDRectangle.A4.getWidth();
	
	/** The Constant HEIGHT. */
	protected static final float HEIGHT = PDRectangle.A4.getHeight();
	
	/** The document. */
	protected PDDocument document;

	/**
	 * Instantiates a new PDF assembler.
	 */
	public PDFAssembler() {
		document = new PDDocument();
	}

}
