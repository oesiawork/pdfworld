package es.sinjava.pdf.model;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class ListContent extends StoreContent {

	public ListContent(List<String> items) {
		super(ContentType.LIST);
		setTextContent(StringUtils.join(items, "$"));
	}

}
