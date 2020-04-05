package es.sinjava.pdf.generator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.util.Matrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.sinjava.factory.DraftFactory;
import es.sinjava.model.FieldContainer;
import es.sinjava.pdf.model.PdfTemplate;
import es.sinjava.pdf.model.StoreContent;

public class DocumentGenerator {

	private static final Logger logger = LoggerFactory.getLogger(DocumentGenerator.class);

	/** The Constant DEFAULT_SIZE_FONT. */
	private static final int DEFAULT_SIZE_FONT = 12;

	/** The Constant WIDTH. */
	protected static final float WIDTH = PDRectangle.A4.getWidth();

	/** The Constant HEIGHT. */
	protected static final float HEIGHT = PDRectangle.A4.getHeight();

	protected static final float BLOCK = HEIGHT / 12;

	protected static final float CBLOCK = WIDTH / 12;

	/** The font. */
	private static PDType0Font font;

	public static PDDocument documentoFromTemplate(PdfTemplate pdfTemplate, FieldContainer fieldContainer,
			File destFile) {
		// Macheamos el contenido contra la plantilla
		PdfTemplate pdfDraft = DraftFactory.getDraft(pdfTemplate, fieldContainer);

		// escribimos el pdf
		PDDocument newDocument = null;

		try (PDDocument newDocumentin = new PDDocument()) {
			newDocument = writePDF(pdfDraft.getStoreContentList(), newDocumentin);
			newDocument.save(destFile);
		} catch (Exception e) {
			logger.error(" Algo ha fallado", e);
		}
		return newDocument;

	}

	public static PDDocument documentoBandFromTemplate(PdfTemplate pdfTemplate, FieldContainer fieldContainer) {

		// Revisamos esto
		return null;

	}

	private static PDDocument writePDF(List<StoreContent> storeContentList, PDDocument newDocument) throws IOException {
		// Buscar un bloque common para crear las páginas con esto
		StoreContent common = null;
		PDPage blankPage = new PDPage();
		// Inicialización
		InputStream arial = DocumentGenerator.class.getClassLoader().getResourceAsStream("arial.ttf");
		font = PDType0Font.load(newDocument, arial, true);

		for (Iterator<StoreContent> scCommonit = storeContentList.iterator(); scCommonit.hasNext() && common == null;) {
			common = scCommonit.next();
			// si no es del common lo descartamos
			if (!common.getContentType().equals(StoreContent.ContentType.COMMON)) {
				common = null;
			}
		}

		PDPageContentStream contents;

		if (common != null) {
			contents = writeCommonPage(newDocument, common, blankPage);
		} else {
			contents = new PDPageContentStream(newDocument, blankPage);
		}

		newDocument.addPage(blankPage);

		contents.close();

		return newDocument;
	}

	private static PDPageContentStream writeCommonPage(PDDocument newDocument, StoreContent common, PDPage commonPage)
			throws IOException {

		PDPageContentStream contents = new PDPageContentStream(newDocument, commonPage);
		logger.trace("Begin writeCommonPage");

		float heigthImage = BLOCK;
		float widhtImage = CBLOCK * 8;
		float coordinateX = CBLOCK;
		float coordinateY = BLOCK * 10;

		if (StringUtils.isNotBlank(common.getImageContent())) {
			logger.debug("Pintamos un banner {}", common.getImageContent());
			PDImageXObject pdImageBand = PDImageXObject.createFromFile(
					DocumentGenerator.class.getClassLoader().getResource(common.getImageContent()).getFile(),
					newDocument);
			contents.drawImage(pdImageBand, coordinateX, coordinateY, widhtImage, heigthImage);
		}

		int fontSizeTitle = DEFAULT_SIZE_FONT + 2;
		String textContent = common.getTextContent();

		// Posicionamos en el centro de la página

		float widthcalculate = font.getStringWidth(textContent) / 1000 * fontSizeTitle;
		float marginLeft = CBLOCK;

		float horizontalTranslation = ((CBLOCK * 10) - widthcalculate) / 2;

		if (horizontalTranslation < 0) {
			logger.debug("Se necesita bajar el tamaño de la letra");
			fontSizeTitle -= 2;
			widthcalculate = font.getStringWidth(textContent) / 1000 * fontSizeTitle;
			horizontalTranslation = ((WIDTH - widthcalculate) / 2) + marginLeft;
		}

		float verticalTranslation = HEIGHT - BLOCK*3;
		contents.beginText();
		Matrix matrix = Matrix.getTranslateInstance(horizontalTranslation + marginLeft, verticalTranslation);
		contents.setTextMatrix(matrix);
		contents.setFont(font, fontSizeTitle);
		contents.showText(textContent);
		contents.newLineAtOffset(-horizontalTranslation, -12f);
		contents.endText();
		return contents;
	}

}
