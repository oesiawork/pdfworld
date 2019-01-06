/*
 * 
 */
package es.sinjava.pdf.model;

// TODO: Auto-generated Javadoc
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

	/* (non-Javadoc)
	 * @see es.sinjava.pdf.model.StoreContent#setTextContent(java.lang.String)
	 */
	public void setTextContent(String textContent) {
		super.setTextContent(" ");
	}
}
