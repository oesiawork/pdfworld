package es.sinjava.docx;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.docx4j.Docx4J;
import org.docx4j.convert.out.FOSettings;
import org.docx4j.fonts.IdentityPlusMapper;
import org.docx4j.fonts.Mapper;
import org.docx4j.fonts.PhysicalFont;
import org.docx4j.fonts.PhysicalFonts;
import org.docx4j.model.datastorage.migration.VariablePrepare;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import es.sinjava.model.FieldContainer;

public class DocPDFConverter {

	public static PDDocument getPDF(InputStream docInPut) throws Exception {

		WordprocessingMLPackage procesor = WordprocessingMLPackage.load(docInPut);

		Mapper fontMapper = new IdentityPlusMapper();

		procesor.setFontMapper(fontMapper);

//		PhysicalFonts.setRegex(null);
//		PhysicalFont font = PhysicalFonts.get("Arial Unicode MS");

		FOSettings foSettings = Docx4J.createFOSettings();
		foSettings.setWmlPackage(procesor);

		ByteArrayOutputStream osStream = new ByteArrayOutputStream();
		Docx4J.toFO(foSettings, osStream, Docx4J.FLAG_EXPORT_PREFER_XSL);
		return PDDocument.load(osStream.toByteArray());
	}
	
	public static PDDocument getPDF(InputStream docInPut, FieldContainer fc) throws Exception {

		WordprocessingMLPackage procesor = WordprocessingMLPackage.load(docInPut);

		Mapper fontMapper = new IdentityPlusMapper();

		procesor.setFontMapper(fontMapper);

		HashMap <String, String> mappings = new HashMap<>(fc.getContainer());
		VariablePrepare.prepare(procesor);
		procesor.getMainDocumentPart().variableReplace(mappings);

		FOSettings foSettings = Docx4J.createFOSettings();
		foSettings.setWmlPackage(procesor);

		ByteArrayOutputStream osStream = new ByteArrayOutputStream();
		Docx4J.toFO(foSettings, osStream, Docx4J.FLAG_EXPORT_PREFER_XSL);
		return PDDocument.load(osStream.toByteArray());
	}
	

}
