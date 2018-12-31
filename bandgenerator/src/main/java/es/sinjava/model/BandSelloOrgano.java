package es.sinjava.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.sinjava.util.TemplateProvider;

public class BandSelloOrgano extends Band {

	private String csv;
	private String owner;
	private String urlVerification;
	private String date;
	private String organismName;

	private final static Logger logger = LoggerFactory.getLogger(BandSelloOrgano.class);
	private final static Template templateDGA = TemplateProvider.getLoadedTemplates().get("templateDGA");

	public BandSelloOrgano(String csv, String owner, String urlVerification, String date, String organismName) {
		super();
		logger.info("Begin BandaSelloOrgano");
		this.csv = csv;
		this.owner = owner;
		this.urlVerification = urlVerification;
		this.date = date;
		this.organismName = organismName;
		// Aqui relleno el resto de lo que es propio de esta Banda
		this.setTemplate(templateDGA);

		// Aqui viene la parte delicada, debería saber que tiene el template

		FieldContainer fcInput = new FieldContainer();
		Map<String, String> valoresInput = new HashMap<>();
		{
			valoresInput.put("csv", csv);
			valoresInput.put("owner", owner);
			valoresInput.put("urlVerification", urlVerification);
			valoresInput.put("organismName", organismName);
			valoresInput.put("date", date);
		}
		fcInput.setContainer(valoresInput);

		templateDGA.setFieldContainer(fcInput);

		this.setTemplate(templateDGA);

		logger.info("Begin BandaSelloOrgano");

	}

	public String getCsv() {
		return csv;
	}

	public void setCsv(String csv) {
		this.csv = csv;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getUrlVerification() {
		return urlVerification;
	}

	public void setUrlVerification(String urlVerification) {
		this.urlVerification = urlVerification;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getOrganismName() {
		return organismName;
	}

	public void setOrganismName(String organismName) {
		this.organismName = organismName;
	}

}
