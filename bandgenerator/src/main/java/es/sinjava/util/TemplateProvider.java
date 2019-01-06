/*
 * 
 */
package es.sinjava.util;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.sinjava.model.BandTemplate;
import es.sinjava.pdf.model.PdfTemplate;

// TODO: Auto-generated Javadoc
/**
 * The Class TemplateProvider.
 */
public class TemplateProvider {

	/** The Constant logger. */
	private final static Logger logger = LoggerFactory.getLogger(TemplateProvider.class);

	/**
	 * Retrieve pdf template.
	 *
	 * @param fileTemplate the file template
	 * @return the pdf template
	 * @throws JAXBException the JAXB exception
	 */
	public static PdfTemplate retrievePdfTemplate(File fileTemplate) throws JAXBException {
		logger.trace("Begin retrievePdfTemplate");
		JAXBContext context = JAXBContext.newInstance(PdfTemplate.class);
		Unmarshaller unMarshaller = context.createUnmarshaller();
		PdfTemplate pdfTemplate = (PdfTemplate) unMarshaller.unmarshal(fileTemplate);
		return pdfTemplate;
	}

	/**
	 * Retrieve band template.
	 *
	 * @param fileTemplate the file template
	 * @return the band template
	 * @throws JAXBException the JAXB exception
	 */
	public static BandTemplate retrieveBandTemplate(File fileTemplate) throws JAXBException {
		logger.trace("Begin retrieveBandTemplate");
		JAXBContext context = JAXBContext.newInstance(BandTemplate.class);
		Unmarshaller unMarshaller = context.createUnmarshaller();
		BandTemplate bandTemplate = (BandTemplate) unMarshaller.unmarshal(fileTemplate);
		return bandTemplate;
	}

}
