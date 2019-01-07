/*
 * 
 */
package es.sinjava.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.sinjava.model.FieldContainer;
import es.sinjava.pdf.model.PdfTemplate;
import es.sinjava.pdf.model.StoreContent;
import es.sinjava.pdf.model.StoreContent.ContentType;

/**
 * A factory for creating Draft objects.
 */
public class DraftFactory {

	private DraftFactory() {
	}

	/** The Constant LIST. */
	private static final ContentType LIST = StoreContent.ContentType.LIST;

	/** The Constant logger. */
	private static final  Logger logger = LoggerFactory.getLogger(DraftFactory.class);

	/**
	 * Gets the draft.
	 *
	 * @param pdfTemplate the pdf template
	 * @param fc          the fc
	 * @return the draft
	 */
	public static PdfTemplate getDraft(PdfTemplate pdfTemplate, FieldContainer fc) {
		logger.info("Begin getDraft");
		Map<String, String> fields = fc.getContainer();

		for (StoreContent contentStore : pdfTemplate.getStoreContentList()) {
			// Eliminamos carácteres no imprimibles
			String seed = contentStore.getTextContent().replaceAll("\\p{C}", "");

			if (seed.contains("${") && !contentStore.getContentType().equals(LIST)) {
				logger.debug(" Encontrada semilla");
				for (Entry<String, String> entry : fields.entrySet()) {
					contentStore.setTextContent(seed.replace("${" + entry.getKey() + "}", entry.getValue()));
				}
			} else if (seed.contains("${") && contentStore.getContentType().equals(LIST)) {
				// es un caso de interación sobre los objetos
				List<String> inputList = new ArrayList<>();
				String fieldName = seed.substring(seed.indexOf("${") + 2, seed.indexOf('}'));
				logger.info("Capturando las semilla {}" , fieldName);

				for (String input : fields.get(fieldName).split("\\$")) {
					inputList.add(seed.replace("${" + fieldName + "}", input));
				}

				contentStore.setTextContent(StringUtils.join(inputList, "$"));
			} else {
				contentStore.setTextContent(seed);
			}
			logger.trace(contentStore.getTextContent());
		}

		return pdfTemplate;
	}
}