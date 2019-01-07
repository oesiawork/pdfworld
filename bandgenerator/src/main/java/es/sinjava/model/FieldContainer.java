/*
 * 
 */
package es.sinjava.model;

import java.io.Serializable;
import java.util.Map;

/**
 * The Class FieldContainer.
 */
public class FieldContainer implements Serializable {


	private static final long serialVersionUID = 1L;
	/** The container. */
	private Map<String, String> container;

	/**
	 * Gets the container.
	 *
	 * @return the container
	 */
	public Map<String, String> getContainer() {
		return container;
	}

	/**
	 * Sets the container.
	 *
	 * @param container the container
	 */
	public void setContainer(Map<String, String> container) {
		this.container = container;
	}

}
