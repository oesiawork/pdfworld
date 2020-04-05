/*
 * 
 */
package es.sinjava.pdf.model;

/**
 * The Class StoreContent.
 */
public class StoreContent {

	/** The content type. */
	private ContentType contentType;

	/** The text content. */
	private String textContent;
	
	private String imageContent;

	/**
	 * Instantiates a new store content.
	 */
	private StoreContent() {
	}

	/**
	 * Instantiates a new store content.
	 *
	 * @param contentType the content type
	 */
	public StoreContent(ContentType contentType) {
		this();
		this.contentType = contentType;
	}


	/**
	 * Gets the content type.
	 *
	 * @return the content type
	 */
	public ContentType getContentType() {
		return contentType;
	}

	/**
	 * Sets the content type.
	 *
	 * @param contentType the new content type
	 */
	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}

	/**
	 * Gets the text content.
	 *
	 * @return the text content
	 */
	public String getTextContent() {
		return textContent;
	}

	/**
	 * Sets the text content.
	 *
	 * @param textContent the new text content
	 */
	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}

	public String getImageContent() {
		return imageContent;
	}

	public void setImageContent(String imageContent) {
		this.imageContent = imageContent;
	}

	/**
	 * The Enum ContentType.
	 */
	public enum ContentType {
		TITLE, BODY, LIST, LEFTCONTENT, BANNER, NPAGE, NLINE, NBANNERPAGE, COMMON
	}

}
