package es.sinjava.pdf.model;

public class NLine extends StoreContent {

	public NLine() {
		super(ContentType.NLINE);
	}

	public void setTextContent(String textContent) {
		super.setTextContent(" ");
	}
}
