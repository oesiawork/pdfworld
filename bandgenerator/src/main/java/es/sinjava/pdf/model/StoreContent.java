package es.sinjava.pdf.model;

public class StoreContent {

	private StoreContent() {
	}

	public StoreContent(ContentType contentType) {
		this();
		this.contentType = contentType;
	}

	private ContentType contentType;
	private String textContent;

	public ContentType getContentType() {
		return contentType;
	}

	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}

	public String getTextContent() {
		return textContent;
	}

	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}

	public enum ContentType {
		TITLE, BODY, LIST, LEFTCONTENT, BANNER
	}

}
