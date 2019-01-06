/*
 * 
 */
package es.sinjava.pdf.model;

// TODO: Auto-generated Javadoc
/**
 * The Class StoreContent.
 */
public class StoreContent {

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

	/** The content type. */
	private ContentType contentType;
	
	/** The text content. */
	private String textContent;

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

	/**
	 * The Enum ContentType.
	 */
	public enum ContentType {
		
		/** The title. */
		TITLE, 
 /** The body. */
 BODY, 
 /** The list. */
 LIST, 
 /** The leftcontent. */
 LEFTCONTENT, 
 /** The banner. */
 BANNER, 
 /** The npage. */
 NPAGE, 
 /** The nline. */
 NLINE, 
 /** The nbannerpage. */
 NBANNERPAGE
	}

}
