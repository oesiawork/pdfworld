/*
 * 
 */
package es.sinjava.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class Band.
 */
public class Band {

	/** The Constant logger. */
	private final static Logger logger = LoggerFactory.getLogger(Band.class);
	
	/** The position. */
	private Position position;
	
	/** The template. */
	private Template template;
	
	/** The qr code. */
	private String qrCode;

	/**
	 * The Enum Position.
	 */
	public enum Position {
		
		/** The left. */
		LEFT, 
 /** The botton. */
 BOTTON
	}

	/**
	 * Gets the template.
	 *
	 * @return the template
	 */
	public Template getTemplate() {
		return template;
	}

	/**
	 * Sets the template.
	 *
	 * @param template the new template
	 */
	public void setTemplate(Template template) {
		this.template = template;
	}

	/**
	 * Gets the position.
	 *
	 * @return the position
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * Sets the position.
	 *
	 * @param position the new position
	 */
	public void setPosition(Position position) {
		this.position = position;
	}

	/**
	 * Gets the qr code.
	 *
	 * @return the qr code
	 */
	public String getQrCode() {
		return qrCode;
	}

	/**
	 * Sets the qr code.
	 *
	 * @param qrCode the new qr code
	 */
	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}

}
