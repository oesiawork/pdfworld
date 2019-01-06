package es.sinjava.model;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BandSelloOrgano {

	private final static Logger logger = LoggerFactory.getLogger(BandSelloOrgano.class);

	public static FieldContainer build(String csv, String owner, String urlVerification, String date,
			String organismName) {

		logger.trace("Begin BandaSelloOrgano");

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

		logger.trace("Begin BandaSelloOrgano");
		return fcInput;
	}

}
