package es.sinjava.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

public class PDFAssembler {
	protected static final float WIDTH = PDRectangle.A4.getWidth();
	protected static final float HEIGHT = PDRectangle.A4.getHeight();
	protected PDDocument document;

	public PDFAssembler() {
		document = new PDDocument();
	}

}
