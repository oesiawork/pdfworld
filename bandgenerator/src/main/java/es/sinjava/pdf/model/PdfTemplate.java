package es.sinjava.pdf.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PdfTemplate {

	public PdfTemplate() {
		this.storeContentList = new ArrayList<>();
	}
	private List<StoreContent> storeContentList ;

	public List<StoreContent> getStoreContentList() {
		return storeContentList;
	}

	public void setStoreContentList(List<StoreContent> storeContentList) {
		this.storeContentList = storeContentList;
	}

}
