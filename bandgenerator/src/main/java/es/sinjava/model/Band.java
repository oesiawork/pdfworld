package es.sinjava.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Band {

	private final static Logger logger = LoggerFactory.getLogger(Band.class);
	private Position position;
	private Template template;
	private String qrCode;

	public enum Position {
		LEFT, BOTTON
	}

	public static Band getDefaultBand() {
		logger.debug("Begin getDefault");
		Band band = new Band();
		// valores por defecto
		band.setPosition(Position.LEFT);
		return band;
	}

	public static Band getBand(Template template) {
		logger.debug("Begin getBand");
		Band band = getDefaultBand();
		band.setTemplate(template);
		return band;
	}

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public String getQrCode() {
		return qrCode;
	}

	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}

}
