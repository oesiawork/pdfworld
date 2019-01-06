package es.sinjava.util;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.sinjava.model.BandTemplate;
import es.sinjava.pdf.model.PdfTemplate;

public class TemplateProvider {

	private final static Logger logger = LoggerFactory.getLogger(TemplateProvider.class);

	public static PdfTemplate retrievePdfTemplate(File fileTemplate) throws JAXBException {
		logger.debug("Begin retrievePdfTemplate");
		JAXBContext context = JAXBContext.newInstance(PdfTemplate.class);
		Unmarshaller unMarshaller = context.createUnmarshaller();
		PdfTemplate pdfTemplate = (PdfTemplate) unMarshaller.unmarshal(fileTemplate);
		return pdfTemplate;
	}

	public static BandTemplate retrieveBandTemplate(File fileTemplate) throws JAXBException {
		logger.debug("Begin retrieveBandTemplate");
		JAXBContext context = JAXBContext.newInstance(BandTemplate.class);
		Unmarshaller unMarshaller = context.createUnmarshaller();
		BandTemplate bandTemplate = (BandTemplate) unMarshaller.unmarshal(fileTemplate);
		return bandTemplate;
	}

}
