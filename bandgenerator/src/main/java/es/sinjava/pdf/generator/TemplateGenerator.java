package es.sinjava.pdf.generator;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import es.sinjava.DGAMultiBand;
import es.sinjava.pdf.model.Paragraph;
import es.sinjava.pdf.model.PdfTemplate;
import es.sinjava.pdf.model.StoreContent;
import es.sinjava.pdf.model.Title;
import es.sinjava.util.TemplateProvider;

public class TemplateGenerator {

	public static void main(String[] args) throws IOException, JAXBException {

		// JAXBContext context = JAXBContext.newInstance(PdfTemplate.class);
		// Marshaller marshaller = context.createMarshaller();
		// marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		//
		// PdfTemplate newPDFTemplate = new PdfTemplate();
		// List<StoreContent> storeContentList = new ArrayList<>();
		// StoreContent storeContent = new Title();
		// storeContent.setTextContent("Jovencillo emponzoñado de anisete");
		// storeContentList.add(storeContent);
		//
		// StoreContent storeContent2 = new Paragraph();
		// storeContent2.setTextContent("Jovencillo emponzoñado de anisete");
		// storeContentList.add(storeContent2);
		//
		// newPDFTemplate.setStoreContentList(storeContentList);
		//
		// File tempFile = File.createTempFile("xmlexample", ".xml");
		// Writer writer = new StringWriter();
		// marshaller.marshal(newPDFTemplate, tempFile);


//		File template = new File(DGAMultiBand.class.getClassLoader().getResource("template.xml").getFile());
//		PdfTemplate newPDFTemplate = TemplateProvider.retrievePdfTemplate(template);
//		System.out.println("newPdfTemplate " + newPDFTemplate.getStoreContentList().size());
	}

}
