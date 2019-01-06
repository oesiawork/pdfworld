package es.sinjava.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BandTemplate {

	private String name;
	private String lineOne;
	private String lineTwo;
	private String lineTree;
	private String logo;
	private Band.Position position;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLineOne() {
		return lineOne;
	}

	public void setLineOne(String lineOne) {
		this.lineOne = lineOne;
	}

	public String getLineTwo() {
		return lineTwo;
	}

	public void setLineTwo(String lineTwo) {
		this.lineTwo = lineTwo;
	}

	public String getLineTree() {
		return lineTree;
	}

	public void setLineTree(String lineTree) {
		this.lineTree = lineTree;
	}

	public Band.Position getPosition() {
		return position;
	}

	public void setPosition(Band.Position position) {
		this.position = position;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

}
