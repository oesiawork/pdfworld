package es.sinjava.pdf.generator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
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
import es.sinjava.pdf.model.StoreContent.ContentType;

public class DocumentGenerator {

	private final Logger logger = LoggerFactory.getLogger(DocumentGenerator.class);

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

	// por defecto
	private float marginLeft = CBLOCK;

	private float cursor = BLOCK;

	private PDPageContentStream contents;

	// Constructor privado
	private DocumentGenerator() {

	}

	public static PDDocument documentFromTemplate(PdfTemplate pdfTemplate, FieldContainer fieldContainer,
			File destFile) {
		DocumentGenerator generator = new DocumentGenerator();
		PDDocument newDocument = generator.build(pdfTemplate, fieldContainer, destFile, false);
		return newDocument;

	}

	public static PDDocument documentBandFromTemplate(PdfTemplate pdfTemplate, FieldContainer fieldContainer,
			File destFile) {
		DocumentGenerator generator = new DocumentGenerator();
		PDDocument newDocument = generator.build(pdfTemplate, fieldContainer, destFile, true);
		return newDocument;

	}

	public PDDocument build(PdfTemplate pdfTemplate, FieldContainer fieldContainer, File destFile,
			boolean addSpaceForBand) {
		// Macheamos el contenido contra la plantilla
		PdfTemplate pdfDraft = DraftFactory.getDraft(pdfTemplate, fieldContainer);

		// escribimos el pdf
		PDDocument newDocument = null;

		try (PDDocument newDocumentin = new PDDocument()) {
			newDocument = writePDF(pdfDraft.getStoreContentList(), newDocumentin, addSpaceForBand);
			newDocument.save(destFile);
		} catch (Exception e) {
			logger.error(" Algo ha fallado", e);
		}
		return newDocument;

	}

	private PDDocument writePDF(List<StoreContent> storeContentList, PDDocument newDocument, boolean addSpaceForBand)
			throws IOException {

		logger.debug("Begin writePDF");

		if (addSpaceForBand) {
			marginLeft = 2 * CBLOCK;
		}

		// Buscar un bloque common para crear las páginas con esto

		PDPage blankPage = new PDPage();
		// Inicialización
		InputStream arial = DocumentGenerator.class.getClassLoader().getResourceAsStream("arial.ttf");
		font = PDType0Font.load(newDocument, arial, true);

		// Busqueda del bloque comun a todas las páginas
		StoreContent common = selectCommon(storeContentList);

		if (common != null) {
			contents = writeCommonPage(newDocument, common, blankPage);
			cursor = BLOCK * 2.0f;
		} else {
			contents = new PDPageContentStream(newDocument, blankPage);
			cursor = BLOCK;
		}

		// EScribimos el resto del documento

		for (Iterator<StoreContent> currentIt = storeContentList.iterator(); currentIt.hasNext();) {
			StoreContent current = currentIt.next();
			if (current.getContentType().equals(StoreContent.ContentType.COMMON)) {
				logger.debug("Se recibe un bloque common");
			} else if (current.getContentType().equals(StoreContent.ContentType.TITLE)) {
				writeTitle(newDocument, current, blankPage);
				cursor = cursor + CBLOCK * 2.0f;
			} else if (current.getContentType().equals(StoreContent.ContentType.BODY)) {
				writeBody(newDocument, current, blankPage);
			} else if (current.getContentType().equals(StoreContent.ContentType.LIST)) {
				writeList(newDocument, current, blankPage);
			}
		}

		newDocument.addPage(blankPage);

		contents.close();

		return newDocument;
	}

	private void writeList(PDDocument newDocument, StoreContent current, PDPage blankPage) throws IOException {
		StoreContent currentItem = new StoreContent(ContentType.BODY);
		// Sangramos
		marginLeft = marginLeft + BLOCK;
		for (String itemList : current.getTextContent().split("\\$")) {
			itemList = "- ".concat(itemList);
			currentItem.setTextContent(itemList);
			writeBody(newDocument, currentItem, blankPage);
		}
		// Quitamos sangria
		cursor += 16.0f;
		marginLeft = marginLeft - BLOCK;
	}

	private StoreContent selectCommon(List<StoreContent> storeContentList) {
		StoreContent common = null;
		for (Iterator<StoreContent> scCommonit = storeContentList.iterator(); scCommonit.hasNext() && common == null;) {
			common = scCommonit.next();
			// si no es del common lo descartamos
			if (!common.getContentType().equals(StoreContent.ContentType.COMMON)) {
				common = null;
			}
		}
		return common;
	}

	private PDPageContentStream writeCommonPage(PDDocument newDocument, StoreContent common, PDPage commonPage)
			throws IOException {

		PDPageContentStream contents = new PDPageContentStream(newDocument, commonPage);
		logger.trace("Begin writeCommonPage");

		float heigthImage = BLOCK * 0.8f;// factor de corrección para que no llene todo el bloque
		// Quitamos el margen izqquierdo y el derecho
		float widhtImage = WIDTH - (marginLeft + CBLOCK);
		float coordinateX = marginLeft;
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

		float horizontalTranslation = ((CBLOCK * 10) - widthcalculate) / 2;

		if (horizontalTranslation < 0) {
			logger.debug("Se necesita bajar el tamaño de la letra");
			fontSizeTitle -= 2;
			widthcalculate = font.getStringWidth(textContent) / 1000 * fontSizeTitle;
			horizontalTranslation = ((WIDTH - widthcalculate) / 2) + marginLeft;
		}

		cursor = HEIGHT - BLOCK * 2.5f;
		contents.beginText();
		Matrix matrix = Matrix.getTranslateInstance(horizontalTranslation + marginLeft, cursor);
		contents.setTextMatrix(matrix);
		contents.setFont(font, fontSizeTitle);
		contents.showText(textContent);
		contents.newLineAtOffset(-horizontalTranslation, -12f);
		contents.endText();
		return contents;
	}

	private PDPageContentStream writeTitle(PDDocument newDocument, StoreContent title, PDPage commonPage)
			throws IOException {

		logger.trace("Begin writeTitle");

		int fontSizeTitle = DEFAULT_SIZE_FONT + 2;
		String textContent = title.getTextContent();

		// Posicionamos en el centro de la página

		float widthcalculate = font.getStringWidth(textContent) / 1000 * fontSizeTitle;

		float horizontalTranslation = ((CBLOCK * 10) - widthcalculate) / 2;

		if (horizontalTranslation < 0) {
			logger.debug("Se necesita bajar el tamaño de la letra");
			fontSizeTitle -= 2;
			widthcalculate = font.getStringWidth(textContent) / 1000 * fontSizeTitle;
			horizontalTranslation = ((WIDTH - widthcalculate) / 2) + marginLeft;
		}

		logger.debug("El cursor va por el bloque {}  que ese el paso {} ", cursor, cursor / BLOCK);
		// En medio del bloque
		float verticalTranslation = HEIGHT - (cursor + BLOCK);

		contents.beginText();
		Matrix matrix = Matrix.getTranslateInstance(horizontalTranslation + marginLeft, verticalTranslation);
		contents.setTextMatrix(matrix);
		contents.setFont(font, fontSizeTitle);
		contents.showText(textContent);
		contents.newLineAtOffset(-horizontalTranslation, -12f);
		contents.endText();
		return contents;
	}

	private void writeBody(PDDocument newDocument, StoreContent current, PDPage blankPage) throws IOException {
		logger.trace(" Se escribe el body");

		String[] words = current.getTextContent().split(" ");
		StringWriter stringWritter = new StringWriter();

		int fontSize = DEFAULT_SIZE_FONT;
		contents.setFont(font, fontSize);
		contents.beginText();
		contents.newLineAtOffset(marginLeft, HEIGHT - cursor);
		for (String word : words) {

			String previewText = stringWritter.toString();

			stringWritter.append(word).append(" ");

			float widthcalculate = font.getStringWidth(stringWritter.toString()) / 1000 * fontSize;

			if (widthcalculate > WIDTH - (marginLeft + BLOCK * 0.5f)) {
				contents.showText(previewText);
				contents.newLineAtOffset(0f, -16.0f);
				stringWritter.flush();
				stringWritter = new StringWriter();
				stringWritter.append(word).append(" ");
				cursor = cursor + 16.0f;
			}
		}

		contents.showText(stringWritter.toString());
		contents.newLineAtOffset(-marginLeft, -16.0f);
		cursor = cursor + 16.0f;
		contents.endText();

	}

}
