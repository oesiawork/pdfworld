package es.sinjava.pdf.generator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.transform.TransformerException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent;
import org.apache.xmpbox.XMPMetadata;
import org.apache.xmpbox.schema.DublinCoreSchema;
import org.apache.xmpbox.schema.PDFAIdentificationSchema;
import org.apache.xmpbox.type.BadFieldValueException;
import org.apache.xmpbox.xml.XmpSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.sinjava.pdf.model.StoreContent;

public class BeaGenerator extends BeaGeneratorDelegate {

	private static final Logger logger = LoggerFactory.getLogger(BeaGenerator.class);

	public static BeaGenerator getInstance() {
		return new BeaGenerator();
	}

	public void writePDFFile(List<StoreContent> contentList, File file, boolean pdfaRequired) throws IOException {

		writePDFFile(contentList, pdfaRequired);

		documentOut.save(file);
		documentOut.close();
	}

	public byte[] writePDFFile(List<StoreContent> contentList, boolean isPdfARequired) throws IOException {
		documentOut = new PDDocument();
		PDPage blankPage = new PDPage();
		documentOut.addPage(blankPage);

		pushContent(contentList, isPdfARequired);

		if (isPdfARequired) {

			XMPMetadata xmp = XMPMetadata.createXMPMetadata();

			try {
				DublinCoreSchema dc = xmp.createAndAddDublinCoreSchema();
				dc.setTitle("Documento PDF");

				PDFAIdentificationSchema id = xmp.createAndAddPFAIdentificationSchema();
				id.setPart(1);
				id.setConformance("B");

				XmpSerializer serializer = new XmpSerializer();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				serializer.serialize(xmp, baos, true);

				PDMetadata metadata = new PDMetadata(documentOut);
				metadata.importXMPMetadata(baos.toByteArray());
				documentOut.getDocumentCatalog().setMetadata(metadata);

			} catch (BadFieldValueException | TransformerException e) {
				logger.error("Petada nueva", e);
			}

			// sRGB output intent
			InputStream colorProfile = BeaGenerator.class.getClassLoader().getResourceAsStream("pdfa/sRGB.icc");
			PDOutputIntent intent = new PDOutputIntent(documentOut, colorProfile);
			intent.setInfo("sRGB IEC61966-2.1");
			intent.setOutputCondition("sRGB IEC61966-2.1");
			intent.setOutputConditionIdentifier("sRGB IEC61966-2.1");
			intent.setRegistryName("http://www.color.org");
			documentOut.getDocumentCatalog().addOutputIntent(intent);
		}

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		documentOut.save(byteArrayOutputStream);

		return byteArrayOutputStream.toByteArray();
	}

	private List<StoreContent> contentList;

	public List<StoreContent> getContentList() {
		return contentList;
	}

	public void setContentList(List<StoreContent> contentList) {
		this.contentList = contentList;
	}

	

}
