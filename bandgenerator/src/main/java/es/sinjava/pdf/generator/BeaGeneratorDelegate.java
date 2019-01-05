package es.sinjava.pdf.generator;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.util.Matrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.sinjava.pdf.model.StoreContent;

public class BeaGeneratorDelegate {

	private static final float MARGIN = 50f;
	private static final int DEFAULT_SIZE_FONT = 12;
	private static final float WIDTH = PDRectangle.A4.getWidth();
	private static final float HEIGHT = PDRectangle.A4.getHeight();
	private static final float X_SIZE_BANNER = WIDTH - (MARGIN * 2);
	private static final float Y_SIZE_BANNER = DEFAULT_SIZE_FONT * 5f;

	private static final Logger logger = LoggerFactory.getLogger(BeaGeneratorDelegate.class);

	PDFont fontBold;
	PDFont font;
	boolean containsBanner;

	protected PDDocument documentOut;

	private float lineStack = HEIGHT - 10f * DEFAULT_SIZE_FONT;

	public BeaGeneratorDelegate() {
		super();
	}

	protected void pushContent(List<StoreContent> scList, boolean isPDFA) throws IOException {
		logger.info("Begin pushContent");
		PDPage currentPage = documentOut.getPage(0);
		PDPageContentStream contents = new PDPageContentStream(documentOut, currentPage);

		loadFont(isPDFA);

		// comprobamos si llega un banner
		containsBanner = scList.get(0).getContentType().equals(StoreContent.ContentType.BANNER);

		if (containsBanner) {
			writeBanner(scList.get(0).getTextContent(), contents);
		}

		contents.beginText();
		for (StoreContent sc : scList) {
			if (sc.getContentType().equals(StoreContent.ContentType.BODY)) {
				writeBody(sc.getTextContent(), contents);
			} else if (sc.getContentType().equals(StoreContent.ContentType.LEFTCONTENT)) {
				writeLeftContent(sc.getTextContent(), contents);
			} else if (sc.getContentType().equals(StoreContent.ContentType.LIST)) {
				writeList(sc.getTextContent(), contents);
			} else if (sc.getContentType().equals(StoreContent.ContentType.TITLE)) {
				writeTitle(sc.getTextContent(), contents);
			} else if (sc.getContentType().equals(StoreContent.ContentType.BANNER)) {
				logger.debug("Ha llegado un banner");
			} else if (sc.getContentType().equals(StoreContent.ContentType.NLINE)) {
				// Escribimos un párrafo vacio
				writeBody("", contents);
			} else if (sc.getContentType().equals(StoreContent.ContentType.NPAGE)) {
				// nos posicionamos sobre una nueva página
				contents = resetToNewPage(contents);
			}

		}
		contents.close();
		logger.info("End pushContent");

	}

	private PDPageContentStream resetToNewPage(PDPageContentStream contents) throws IOException {
		PDPage currentPage;
		contents.endText();
		contents.close();
		currentPage = new PDPage();
		documentOut.addPage(currentPage);
		logger.debug("Tengo estas páginas " + documentOut.getNumberOfPages());
		contents = new PDPageContentStream(documentOut, currentPage, AppendMode.APPEND, true, true);
		contents.beginText();
		contents.newLineAtOffset(0, HEIGHT - (2 * MARGIN));
		return contents;
	}

	private void loadFont(boolean isPDFA) {
		logger.trace("Begin loadFont");
		if (isPDFA) {
			InputStream arial = BeaGeneratorDelegate.class.getClassLoader().getResourceAsStream("arial.ttf");
			InputStream arialbold = BeaGeneratorDelegate.class.getClassLoader().getResourceAsStream("arialbd.ttf");
			try {
				fontBold = PDType0Font.load(documentOut, arialbold, true);
				font = PDType0Font.load(documentOut, arial, true);
			} catch (IOException e) {
				logger.error("No deberían faltar estas ttf en el archivo", e);
			}
		} else {
			fontBold = PDType1Font.HELVETICA_BOLD;
			font = PDType1Font.HELVETICA;
		}
		logger.trace("End loadFont");

	}

	private void writeBanner(String textContent, PDPageContentStream contents) throws IOException {

		PDImageXObject pdImage = PDImageXObject.createFromFile(
				BeaGeneratorDelegate.class.getClassLoader().getResource(textContent).getFile(), documentOut);

		contents.drawImage(pdImage, MARGIN, HEIGHT - (2 * MARGIN + Y_SIZE_BANNER), X_SIZE_BANNER, Y_SIZE_BANNER);
		contents.restoreGraphicsState();

	}

	private void writeTitle(String textContent, PDPageContentStream contents) throws IOException {

		logger.info("Begin writeTitle");

		int fontSizeTitle = DEFAULT_SIZE_FONT + 2;

		// Posicionamos en el centro de la página

		float widthcalculate = fontBold.getStringWidth(textContent) / 1000 * fontSizeTitle;
		float horizontalTranslation = (WIDTH - widthcalculate) / 2;

		if (horizontalTranslation < 0) {
			logger.debug("Se necesita bajar el tamaño de la letra");
			fontSizeTitle -= 2;
			widthcalculate = fontBold.getStringWidth(textContent) / 1000 * fontSizeTitle;
			horizontalTranslation = (WIDTH - widthcalculate) / 2;
		}

		float verticalTranslation = getLineStackAndIncrement(fontSizeTitle);
		Matrix amtrix = Matrix.getTranslateInstance(horizontalTranslation, verticalTranslation);
		contents.setTextMatrix(amtrix);
		contents.setFont(fontBold, fontSizeTitle);
		contents.showText(textContent);
		contents.newLineAtOffset(-horizontalTranslation, -12f);
	}

	private void writeList(String textContent, PDPageContentStream contents) throws IOException {
		logger.info("Begin writeList");

		String[] items = textContent.split("\\$");

		int fontSize = DEFAULT_SIZE_FONT;
		contents.setFont(font, fontSize);

		contents.newLineAtOffset(MARGIN * 2, -12f);

		for (String item : items) {
			contents.showText("-" + item);
			contents.newLineAtOffset(0f, -12f);
		}
		contents.newLineAtOffset(-MARGIN * 2, 12f);
		logger.info("End writeList");
	}

	private void writeLeftContent(String textContent, PDPageContentStream contents) throws IOException {
		// TODO Auto-generated method stub
		writeTitle(textContent, contents);

	}

	private void writeBody(String textContent, PDPageContentStream contents) throws IOException {

		logger.info(" Se escribe el body");

		String[] words = textContent.split(" ");
		StringWriter stringWritter = new StringWriter();

		int fontSize = DEFAULT_SIZE_FONT;
		contents.setFont(font, fontSize);

		// recuperamos la posición
		contents.newLineAtOffset(MARGIN, -12f);

		for (String word : words) {

			String previewText = stringWritter.toString();

			stringWritter.append(word).append(" ");

			float widthcalculate = font.getStringWidth(stringWritter.toString()) / 1000 * fontSize;

			if (widthcalculate > WIDTH - (MARGIN * 2)) {
				contents.showText(previewText);
				contents.newLineAtOffset(0f, -12f);
				stringWritter.flush();
				stringWritter = new StringWriter();
				stringWritter.append(word).append(" ");
			}
		}
		contents.showText(stringWritter.toString());
		contents.newLineAtOffset(-MARGIN, -1f);
	}

	private float getLineStackAndIncrement(int sizeFont) {

		float lineStackCurrent = (containsBanner) ? lineStack - (Y_SIZE_BANNER) : lineStack;
		logger.info("posición " + lineStackCurrent);
		lineStack = lineStack - sizeFont;
		return lineStackCurrent;
	}

}