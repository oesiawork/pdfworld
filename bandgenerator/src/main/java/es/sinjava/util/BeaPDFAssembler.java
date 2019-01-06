package es.sinjava.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.util.Matrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import es.sinjava.model.Band;
import es.sinjava.model.Template;
import es.sinjava.pdf.model.StoreContent;

public class BeaPDFAssembler {

	private static final float BAND_CORRECTION = 1.5f;
	private static final int DEFAULT_SIZE_FONT = 12;
	private static final float WIDTH = PDRectangle.A4.getWidth();
	private static final float HEIGHT = PDRectangle.A4.getHeight();
	private static final float FACTOR_REDUCED = 0.1f;
	private static final float FACTOR_MARGIN = 0.08f;
	private static final float MARGIN_BASE = WIDTH * FACTOR_MARGIN;
	private static final float X_SIZE_BANNER = WIDTH - (MARGIN_BASE * 2);
	private static final float Y_SIZE_BANNER = DEFAULT_SIZE_FONT * 5f;

	private final Logger logger = LoggerFactory.getLogger(BeaPDFAssembler.class);
	private PDDocument document = new PDDocument();
	private PDImageXObject pdImageBand = null;
	private PDImageXObject pdImage;
	private PDFont font = null;
	private float marginLeft;
	private boolean containsBanner;
	private float lineStack = HEIGHT - 10f * DEFAULT_SIZE_FONT;

	public BeaPDFAssembler() {
		logger.info(" Constructor BeaPDFAssembler ");
		logger.info("Dimensiones del documento A4  {}  por  {}", WIDTH, HEIGHT);
		try {
			pdImageBand = PDImageXObject.createFromFile(
					BeaPDFAssembler.class.getClassLoader().getResource("bandClara.png").getFile(), document);
			logger.trace("Cargada la imagen de la banda");
			InputStream arial = BeaPDFAssembler.class.getClassLoader().getResourceAsStream("arial.ttf");
			font = PDType0Font.load(document, arial, true);
			logger.trace("Empotrado el tipo de letra de la banda");
		} catch (IOException e) {
			logger.error("No se ha encontrado un recurso necesario", e);
		}
	}

	public PDDocument write(List<StoreContent> storeContentList, Band band) throws IOException {

		// Si el documento tiene banda creamos la página con banda izda y modificamos el
		// margen para empezar a escribir
		PDPage blankPage = new PDPage();
		PDPageContentStream contents = createPage(band, blankPage);

		containsBanner = storeContentList.get(0).getContentType().equals(StoreContent.ContentType.BANNER);

		if (containsBanner) {
			writeBanner(storeContentList.get(0).getTextContent(), contents);
		}
		contents.beginText();
		// Lo posicionamos correctamente
		resetPage(contents);

		for (StoreContent sc : storeContentList) {
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

			} else if (sc.getContentType().equals(StoreContent.ContentType.NBANNERPAGE)) {
				logger.debug("------------Ha llegado un bannerPage");
				contents.close();
				blankPage = new PDPage();
				contents = createPage(band, blankPage);
				writeBanner(sc.getTextContent(), contents);
				contents.beginText();
				resetPage(contents);
			}

		}

		contents.close();

		return document;
	}

	private PDPageContentStream createPage(Band band, PDPage blankPage) throws IOException {
		document.addPage(blankPage);
		PDPageContentStream contents = new PDPageContentStream(document, blankPage);

		if (band != null && band.getPosition().equals(Band.Position.BOTTON)) {
			contents.drawImage(pdImageBand, 0, 0, WIDTH, HEIGHT * FACTOR_REDUCED);
			logger.debug("Banda de tamaño {}  por {}", WIDTH, HEIGHT * FACTOR_REDUCED);
			marginLeft = MARGIN_BASE * (1 - FACTOR_REDUCED);

		} else if (band != null && band.getPosition().equals(Band.Position.LEFT)) {
			contents.drawImage(pdImageBand, 0, 0, WIDTH * FACTOR_REDUCED, HEIGHT);
			logger.debug("Banda de tamaño {}  por {}", WIDTH * FACTOR_REDUCED, HEIGHT);
			marginLeft = WIDTH * FACTOR_REDUCED;
		} else {
			marginLeft = MARGIN_BASE;
			return contents;
		}

		contents.restoreGraphicsState();

		insertQRCode(band, document, contents);

		// Lo inicializamos como matrix horizontal
		Matrix matrixText = Matrix.getTranslateInstance(100f, 30f);

		if (band.getPosition().equals(Band.Position.LEFT)) {
			matrixText = Matrix.getTranslateInstance(30f, 100f);
			matrixText.rotate(Math.PI / 2);
		}

		pushContentText(band, contents, font, matrixText);
		return contents;
	}

	private void pushContentText(Band band, PDPageContentStream contents, PDFont font, Matrix matrixText)
			throws IOException {
		logger.debug("Begin pushContent");
		contents.beginText();
		contents.setTextMatrix(matrixText);
		contents.setFont(font, 9);

		contents.showText(band.getTemplate().get(Template.HEADER));

		contents.newLineAtOffset(0f, -12f);
		contents.showText(band.getTemplate().get(Template.BODY));

		contents.newLineAtOffset(0f, -12f);
		contents.showText(band.getTemplate().get(Template.FOOTER));

		contents.endText();
		contents.restoreGraphicsState();
		logger.debug("End pushContent");
	}

	private void insertQRCode(Band band, PDDocument documentOut, PDPageContentStream contents) throws IOException {

		logger.debug("Begin insertQRCode");

		if (StringUtils.isNotBlank(band.getQrCode())) {
			// Insertamos el código qr
			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			try {
				BitMatrix bitMatrix = qrCodeWriter.encode(band.getQrCode(), BarcodeFormat.QR_CODE, 90, 90);

				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				MatrixToImageWriter.writeToStream(bitMatrix, "PNG", stream);
				PDImageXObject qr = PDImageXObject.createFromByteArray(documentOut, stream.toByteArray(), "QR");
				contents.drawImage(qr, 0, 0);
				contents.restoreGraphicsState();

			} catch (WriterException e) {
				logger.error("No se ha podido generar el código QR", e);
			}

		}
	}

	private void writeBanner(String textContent, PDPageContentStream contents) throws IOException {

		if (pdImage == null) {
			pdImage = PDImageXObject.createFromFile(
					BeaPDFAssembler.class.getClassLoader().getResource(textContent).getFile(), document);
		}
		contents.drawImage(pdImage, marginLeft * BAND_CORRECTION, HEIGHT - (2 * MARGIN_BASE + Y_SIZE_BANNER),
				X_SIZE_BANNER, Y_SIZE_BANNER);
		contents.restoreGraphicsState();
	}

	private void writeTitle(String textContent, PDPageContentStream contents) throws IOException {

		logger.trace("Begin writeTitle");

		int fontSizeTitle = DEFAULT_SIZE_FONT + 2;

		// Posicionamos en el centro de la página

		float widthcalculate = font.getStringWidth(textContent) / 1000 * fontSizeTitle;
		float horizontalTranslation = ((WIDTH - marginLeft) - widthcalculate) / 2;

		if (horizontalTranslation < 0) {
			logger.debug("Se necesita bajar el tamaño de la letra");
			fontSizeTitle -= 2;
			widthcalculate = font.getStringWidth(textContent) / 1000 * fontSizeTitle;
			horizontalTranslation = ((WIDTH - widthcalculate) / 2) + marginLeft;
		}

		float verticalTranslation = getLineStackAndIncrement(fontSizeTitle);
		Matrix amtrix = Matrix.getTranslateInstance(horizontalTranslation + marginLeft, verticalTranslation);
		contents.setTextMatrix(amtrix);
		contents.setFont(font, fontSizeTitle);
		contents.showText(textContent);
		contents.newLineAtOffset(-horizontalTranslation, -12f);
	}

	private void resetPage(PDPageContentStream contents) throws IOException {
		logger.trace("Begin reset");
		lineStack = HEIGHT - (MARGIN_BASE + Y_SIZE_BANNER);
		float position = (containsBanner) ? lineStack - (Y_SIZE_BANNER) : lineStack;
		contents.newLineAtOffset(marginLeft, position);
	}

	private void writeList(String textContent, PDPageContentStream contents) throws IOException {
		logger.trace("Begin writeList");

		String[] items = textContent.split("\\$");

		int fontSize = DEFAULT_SIZE_FONT;
		contents.setFont(font, fontSize);

		contents.newLineAtOffset(MARGIN_BASE + marginLeft, -6f);

		for (String item : items) {
			contents.showText("-" + item);
			contents.newLineAtOffset(0f, -12f);
		}
		contents.newLineAtOffset(-(MARGIN_BASE + marginLeft), -6f);

		logger.trace("End writeList");
	}

	private void writeLeftContent(String textContent, PDPageContentStream contents) throws IOException {
		// TODO Auto-generated method stub
		writeTitle(textContent, contents);

	}

	private void writeBody(String textContent, PDPageContentStream contents) throws IOException {

		logger.trace(" Se escribe el body");

		String[] words = textContent.split(" ");
		StringWriter stringWritter = new StringWriter();

		int fontSize = DEFAULT_SIZE_FONT;
		contents.setFont(font, fontSize);

		// recuperamos la posición
		contents.newLineAtOffset(marginLeft, -6f);

		for (String word : words) {

			String previewText = stringWritter.toString();

			stringWritter.append(word).append(" ");

			float widthcalculate = font.getStringWidth(stringWritter.toString()) / 1000 * fontSize;

			if (widthcalculate > WIDTH - (marginLeft * BAND_CORRECTION + MARGIN_BASE)) {
				contents.showText(previewText);
				contents.newLineAtOffset(0f, -12f);
				stringWritter.flush();
				stringWritter = new StringWriter();
				stringWritter.append(word).append(" ");
			}
		}
		contents.showText(stringWritter.toString());
		contents.newLineAtOffset(-marginLeft, -12f);
	}

	private float getLineStackAndIncrement(int sizeFont) {
		float lineStackCurrent = (containsBanner) ? lineStack - (Y_SIZE_BANNER) : lineStack;
		logger.trace("posición " + lineStackCurrent);
		lineStack = lineStack - sizeFont;
		return lineStackCurrent;
	}

}
