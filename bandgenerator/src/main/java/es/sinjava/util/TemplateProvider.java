package es.sinjava.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.sinjava.DGABand;
import es.sinjava.model.BandTemplate;
import es.sinjava.model.Template;
import es.sinjava.pdf.model.PdfTemplate;

public class TemplateProvider {

	private final static Logger logger = LoggerFactory.getLogger(TemplateProvider.class);

//	private static Map<String, Template> loadedTemplates;
//
//	public static Map<String, Template> getLoadedTemplates() {
//		if (loadedTemplates == null) {
//			load();
//		}
//		return loadedTemplates;
//	}
//
//	public static void setLoadedTemplates(Map<String, Template> loadedTemplatesIn) {
//		loadedTemplates = loadedTemplatesIn;
//	}
//
//	public static Map<String, String> getTemplate(String templateName) {
//		logger.info("Se recupera una plantilla " + templateName);
//		if (loadedTemplates == null) {
//			load();
//		}
//		return loadedTemplates.get(templateName);
//	}
//
//	private static void load() {
//		logger.info("Se recupera la base de plantillas ");
//		loadedTemplates = new HashMap<>();
//		Properties properties = new Properties();
//		try {
//			File entrada = new File(DGABand.class.getClassLoader().getResource("templates.properties").getFile());
//			Reader reader = new FileReader(entrada);
//			properties.load(reader);
//		} catch (IOException e) {
//			logger.error("No se han cargado las plantillas", e);
//		}
//
//		for (Entry<Object, Object> entry : properties.entrySet()) {
//			logger.debug("Property " + entry.getKey());
//			String[] aux = entry.getKey().toString().split("\\.");
//			String templName = aux[0];
//			String keyName = aux[1];
//			String value = entry.getValue().toString();
//			if (!loadedTemplates.containsKey(templName)) {
//				// Si no existe lo creo
//				loadedTemplates.put(templName, new Template(templName));
//			}
//			loadedTemplates.get(templName).put(keyName, value);
//		}
//	}

	public static PdfTemplate retrievePdfTemplate (File fileTemplate) throws JAXBException {	
		JAXBContext context = JAXBContext.newInstance(PdfTemplate.class);
		Unmarshaller unMarshaller = context.createUnmarshaller();
		PdfTemplate pdfTemplate =(PdfTemplate) unMarshaller.unmarshal(fileTemplate);
		return pdfTemplate;
	}
	
	
	public static BandTemplate retrieveBandTemplate (File fileTemplate) throws JAXBException {	
		JAXBContext context = JAXBContext.newInstance(BandTemplate.class);
		Unmarshaller unMarshaller = context.createUnmarshaller();
		BandTemplate bandTemplate =(BandTemplate) unMarshaller.unmarshal(fileTemplate);
		return bandTemplate;
	}
	
	
	
	
	

}
