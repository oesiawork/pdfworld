/*
 * 
 */
package es.sinjava.model;

import javax.xml.bind.annotation.XmlRootElement;

// TODO: Auto-generated Javadoc
/**
 * The Class BandTemplate.
 */
@XmlRootElement
public class BandTemplate {

	/** The name. */
	private String name;
	
	/** The line one. */
	private String lineOne;
	
	/** The line two. */
	private String lineTwo;
	
	/** The line tree. */
	private String lineTree;
	
	/** The qr code. */
	private String qrCode;
	
	/** The position. */
	private Band.Position position;

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the line one.
	 *
	 * @return the line one
	 */
	public String getLineOne() {
		return lineOne;
	}

	/**
	 * Sets the line one.
	 *
	 * @param lineOne the new line one
	 */
	public void setLineOne(String lineOne) {
		this.lineOne = lineOne;
	}

	/**
	 * Gets the line two.
	 *
	 * @return the line two
	 */
	public String getLineTwo() {
		return lineTwo;
	}

	/**
	 * Sets the line two.
	 *
	 * @param lineTwo the new line two
	 */
	public void setLineTwo(String lineTwo) {
		this.lineTwo = lineTwo;
	}

	/**
	 * Gets the line tree.
	 *
	 * @return the line tree
	 */
	public String getLineTree() {
		return lineTree;
	}

	/**
	 * Sets the line tree.
	 *
	 * @param lineTree the new line tree
	 */
	public void setLineTree(String lineTree) {
		this.lineTree = lineTree;
	}

	/**
	 * Gets the position.
	 *
	 * @return the position
	 */
	public Band.Position getPosition() {
		return position;
	}

	/**
	 * Sets the position.
	 *
	 * @param position the new position
	 */
	public void setPosition(Band.Position position) {
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
