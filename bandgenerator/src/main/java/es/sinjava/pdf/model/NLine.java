/*
 * 
 */
package es.sinjava.pdf.model;

/**
 * The Class NLine.
 */
public class NLine extends StoreContent {

	/**
	 * Instantiates a new n line.
	 */
	public NLine() {
		super(ContentType.NLINE);
	}

	@Override
	public void setTextContent(String textContent) {
		super.setTextContent(" ");
	}
}
