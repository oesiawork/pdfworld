/*
 * 
 */
package es.sinjava.pdf.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

// TODO: Auto-generated Javadoc
/**
 * The Class PdfTemplate.
 */
@XmlRootElement
public class PdfTemplate {

	/**
	 * Instantiates a new pdf template.
	 */
	public PdfTemplate() {
		this.storeContentList = new ArrayList<>();
	}

	/** The store content list. */
	private List<StoreContent> storeContentList;

	/**
	 * Gets the store content list.
	 *
	 * @return the store content list
	 */
	public List<StoreContent> getStoreContentList() {
		return storeContentList;
	}

	/**
	 * Sets the store content list.
	 *
	 * @param storeContentList the new store content list
	 */
	public void setStoreContentList(List<StoreContent> storeContentList) {
		this.storeContentList = storeContentList;
	}

}
