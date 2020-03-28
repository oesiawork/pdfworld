/*
 * 
 */
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

/**
 * The Class BeaPDFAssembler.
 */
public class BeaPDFAssembler extends PDFAssembler {

	/** The Constant BAND_CORRECTION. */
	private static final float BAND_CORRECTION = 1.5f;

	/** The Constant DEFAULT_SIZE_FONT. */
	private static final int DEFAULT_SIZE_FONT = 12;

	/** The Constant FACTOR_REDUCED. */
	private static final float FACTOR_REDUCED = 0.1f;

	/** The Constant FACTOR_MARGIN. */
	private static final float FACTOR_MARGIN = 0.08f;

	/** The Constant MARGIN_BASE. */
	private static final float MARGIN_BASE = WIDTH * FACTOR_MARGIN;

	/** The Constant X_SIZE_BANNER. */
	private static final float X_SIZE_BANNER = WIDTH - (MARGIN_BASE * 2);

	/** The Constant Y_SIZE_BANNER. */
	private static final float Y_SIZE_BANNER = DEFAULT_SIZE_FONT * 5f;

	/** The Constant IMAGEBANDFILE. */
	private static final String IMAGEBANDFILE = BeaPDFAssembler.class.getClassLoader().getResource("bandClara.png")
			.getFile();

	/** The logger. */
	private final Logger logger = LoggerFactory.getLogger(BeaPDFAssembler.class);

	/** The pd image band. */
	private PDImageXObject pdImageBand = null;

	/** The pd image. */
	private PDImageXObject pdImage;

	/** The margin left. */
	private float marginLeft;

	/** The line stack. */
	private float lineStack = HEIGHT - 10f * DEFAULT_SIZE_FONT;

	/** The font. */
	private PDType0Font font;

	/**
	 * Instantiates a new bea PDF assembler.
	 */
	public BeaPDFAssembler() {
		logger.info(" Constructor BeaPDFAssembler ");
		logger.trace("Dimensiones del documento A4  {}  por  {}", WIDTH, HEIGHT);

		try {

			pdImageBand = PDImageXObject.createFromFile(IMAGEBANDFILE, document);
			logger.trace("Cargada la imagen de la banda {}", pdImageBand.getBitsPerComponent());

			InputStream arial = BeaPDFAssembler.class.getClassLoader().getResourceAsStream("arial.ttf");
			font = PDType0Font.load(document, arial, true);
			logger.trace("Empotrado el tipo de letra de la banda");
		} catch (IOException e) {
			logger.error("No se ha encontrado un recurso necesario", e);
		}

	}

	/**
	 * Write.
	 *
	 * @param storeContentList the store content list
	 * @param band             the band
	 * @return the PD document
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public PDDocument write(List<StoreContent> storeContentList, Band band) throws IOException {

		// Si el documento tiene banda creamos la página con banda izda y modificamos el
		// margen para empezar a escribir
		PDPage blankPage = new PDPage();
		PDPageContentStream contents = createPage(band, blankPage);

		boolean containsBanner = storeContentList.get(0).getContentType().equals(StoreContent.ContentType.BANNER);

		contents.beginText();
		float currentPosition = resetPageAt(contents, containsBanner);
		logger.debug("Escibiendo en position {}", currentPosition);
		for (StoreContent sc : storeContentList) {
			if (sc.getContentType().equals(StoreContent.ContentType.BODY)) {
				writeBody(sc.getTextContent(), contents);
			} else if (sc.getContentType().equals(StoreContent.ContentType.LIST)) {
				writeList(sc.getTextContent(), contents);
			} else if (sc.getContentType().equals(StoreContent.ContentType.TITLE)) {
				writeTitle(sc.getTextContent(), contents, containsBanner, currentPosition );
			} else if (sc.getContentType().equals(StoreContent.ContentType.NLINE)) {
				// Escribimos un párrafo vacio
				writeBody("", contents);

			} else if (sc.getContentType().equals(StoreContent.ContentType.NBANNERPAGE)) {
				logger.debug("Ha llegado un bannerPage");
				contents.close();
				blankPage = new PDPage();
				contents = createPage(band, blankPage);
				writeBanner(sc.getTextContent(), contents);
				contents.beginText();
				resetPageAt(contents, containsBanner);
			}
		}
		contents.endText();

		if (containsBanner) {
			writeBanner(storeContentList.get(0).getTextContent(), contents);
		}

		contents.close();

		return document;
	}

	/**
	 * Creates the page.
	 *
	 * @param band      the band
	 * @param blankPage the blank page
	 * @return the PD page content stream
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private PDPageContentStream createPage(Band band, PDPage blankPage) throws IOException {
		document.addPage(blankPage);
		PDPageContentStream contents = new PDPageContentStream(document, blankPage);

		if (band != null && band.getPosition().equals(Band.Position.BOTTON)) {
			contents.drawImage(pdImageBand, 0, 0, WIDTH, HEIGHT * FACTOR_REDUCED);
			logger.trace("Banda de tamaño {}  por {}", WIDTH, HEIGHT * FACTOR_REDUCED);
			marginLeft = MARGIN_BASE * (1 - FACTOR_REDUCED);

		} else if (band != null && band.getPosition().equals(Band.Position.LEFT)) {
			contents.drawImage(pdImageBand, 0, 0, WIDTH * FACTOR_REDUCED, HEIGHT);
			logger.trace("Banda de tamaño {}  por {}", WIDTH * FACTOR_REDUCED, HEIGHT);
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

	/**
	 * Push content text.
	 *
	 * @param band       the band
	 * @param contents   the contents
	 * @param font       the font
	 * @param matrixText the matrix text
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
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

	/**
	 * Insert QR code.
	 *
	 * @param band        the band
	 * @param documentOut the document out
	 * @param contents    the contents
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
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

	/**
	 * Write banner.
	 *
	 * @param textContent the text content
	 * @param contents    the contents
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void writeBanner(String textContent, PDPageContentStream contents) throws IOException {

		if (pdImage == null) {
			pdImage = PDImageXObject.createFromFile(
					BeaPDFAssembler.class.getClassLoader().getResource(textContent).getFile(), document);
		}
		contents.drawImage(pdImage, marginLeft * BAND_CORRECTION, HEIGHT - (2 * MARGIN_BASE + Y_SIZE_BANNER),
				X_SIZE_BANNER, Y_SIZE_BANNER);
		contents.restoreGraphicsState();
	}

	/**
	 * Write title.
	 *
	 * @param textContent the text content
	 * @param contents    the contents
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void writeTitle(String textContent, PDPageContentStream contents, boolean containsBanner, float currentPosition)
			throws IOException {

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

		float verticalTranslation = getLineStackAndIncrement(fontSizeTitle, currentPosition);
		Matrix amtrix = Matrix.getTranslateInstance(horizontalTranslation + marginLeft, verticalTranslation);
		contents.setTextMatrix(amtrix);
		contents.setFont(font, fontSizeTitle);
		contents.showText(textContent);
		contents.newLineAtOffset(-horizontalTranslation, -12f);
	}

	/**
	 * Reset page.
	 *
	 * @param contents the contents
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private float resetPageAt(PDPageContentStream contents, boolean containsBanner) throws IOException {
		logger.trace("Begin reset");
		lineStack = HEIGHT - (MARGIN_BASE + Y_SIZE_BANNER);
		float position = (containsBanner) ? lineStack - (Y_SIZE_BANNER * 1.1f) : lineStack;
		logger.debug("Altura para empezar a escribir :" + position);
		contents.newLineAtOffset(marginLeft, position);
		return position;
	}

	/**
	 * Write list.
	 *
	 * @param textContent the text content
	 * @param contents    the contents
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
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

	/**
	 * Write body.
	 *
	 * @param textContent the text content
	 * @param contents    the contents
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
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

	/**
	 * Gets the line stack and increment.
	 *
	 * @param sizeFont the size font
	 * @return the line stack and increment
	 */
	private float getLineStackAndIncrement(int sizeFont, float currentPosition) {
		float lineStackCurrent = currentPosition ;
		logger.debug("posición  {}", lineStackCurrent);
		lineStack = lineStack - sizeFont;
		return lineStackCurrent;
	}

}
