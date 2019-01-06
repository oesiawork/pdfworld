/*
 * 
 */
package es.sinjava.pdf.model;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class ListContent.
 */
public class ListContent extends StoreContent {

	/**
	 * Instantiates a new list content.
	 *
	 * @param items the items
	 */
	public ListContent(List<String> items) {
		super(ContentType.LIST);
		setTextContent(StringUtils.join(items, "$"));
	}

}
