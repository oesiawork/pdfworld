/*
 * 
 */
package es.sinjava.model;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class BandSelloOrgano.
 */
public class BandSelloOrgano {

	/** The Constant logger. */
	private final static Logger logger = LoggerFactory.getLogger(BandSelloOrgano.class);

	/**
	 * Builds the.
	 *
	 * @param csv the csv
	 * @param owner the owner
	 * @param urlVerification the url verification
	 * @param date the date
	 * @param organismName the organism name
	 * @return the field container
	 */
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
