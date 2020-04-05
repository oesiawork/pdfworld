/*
 * 
 */
package es.sinjava.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.multipdf.Overlay;
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

/**
 * The Class BeaPDFBandAssembler.
 */
public class WaterBandAssembler {
	
	/** The Constant WIDTH. */
	protected static final float WIDTH = PDRectangle.A4.getWidth();
	
	/** The Constant HEIGHT. */
	protected static final float HEIGHT = PDRectangle.A4.getHeight();

	private static final float FACTOR_A4_SHAPE = 0.7F;

	/** The Constant FACTOR_REDUCED. */
	private static final float FACTOR_REDUCED = 0.1f;

	/** The logger. */
	private final Logger logger = LoggerFactory.getLogger(WaterBandAssembler.class);

	/** The pd image band. */
	private PDImageXObject pdImageBand;

	/** The font. */
	private PDType0Font font;

	public PDType0Font getFont() {
		return font;
	}

	public void setFont(PDType0Font font) {
		this.font = font;
	}

	/**
	 * Insert band.
	 *
	 * @param documentIn the document in
	 * @param band       the band
	 * @return the PD document
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public PDDocument insertBand(PDDocument documentIn, Band band) throws IOException {

		logger.info("Begin build");

		logger.info("Documento de entrada tiene {} páginas", documentIn.getNumberOfPages());
		
		PDDocument documentBand = generateDocumentBand(band);
		
		Overlay overlay = new Overlay();
		overlay.setAllPagesOverlayPDF(documentBand);
		overlay.setInputPDF(documentIn);
		overlay.setOverlayPosition(Overlay.Position.BACKGROUND);
		overlay.overlay(new HashMap<Integer, String>());
	
		return documentIn;
	}

	private PDDocument generateDocumentBand(Band band) throws IOException {
		PDDocument documentBand = new PDDocument();

		if (pdImageBand == null) {
			pdImageBand = PDImageXObject.createFromFile(
					WaterBandAssembler.class.getClassLoader().getResource("banda.jpg").getFile(), documentBand);
		}
		InputStream arial = BeaPDFAssembler.class.getClassLoader().getResourceAsStream("arial.ttf");
		font = PDType0Font.load(documentBand, arial, true);

		PDPage blankPage = new PDPage();
		documentBand.addPage(blankPage);
		PDPageContentStream contents = new PDPageContentStream(documentBand, blankPage);

		contents.drawImage(pdImageBand, 0, 0, WIDTH * FACTOR_REDUCED, HEIGHT);

		Matrix matrixVertical = Matrix.getTranslateInstance(100f, 30f);

		if (band.getPosition().equals(Band.Position.LEFT)) {
			matrixVertical = Matrix.getTranslateInstance(25f, 100f);
			matrixVertical.rotate(Math.PI / 2);
		}

		pushContent(band, contents, font, matrixVertical);

		insertQRCode(band, documentBand, contents);

		contents.restoreGraphicsState();
		contents.saveGraphicsState();
		contents.close();
		return documentBand;
	}

	/**
	 * Push band page.
	 *
	 * @param document the document
	 * @param band     the band
	 * @param font     the font
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void pushBandPage(PDDocument document, Band band, PDFont font) throws IOException {

		logger.info("Begin build");

		// Metemos la imagen

		if (pdImageBand == null) {
			// si ya lo tenemos lo utilizaremos la misma imagen, optimizando el espacio
			pdImageBand = PDImageXObject.createFromFile(
					WaterBandAssembler.class.getClassLoader().getResource("bandClara.png").getFile(), document);
		}

		PDPage blankPage = new PDPage();
		document.addPage(blankPage);
		PDPageContentStream contents = new PDPageContentStream(document, blankPage);

		if (band.getPosition().equals(Band.Position.BOTTON)) {
			contents.drawImage(pdImageBand, 0, 0, WIDTH, HEIGHT * FACTOR_REDUCED);
		} else {
			contents.drawImage(pdImageBand, 0, 0, WIDTH * FACTOR_REDUCED, HEIGHT);
		}

		contents.restoreGraphicsState();

		// Lo inicializamos como matrix horizontal
		Matrix matrixVertical = Matrix.getTranslateInstance(100f, 30f);

		if (band.getPosition().equals(Band.Position.LEFT)) {
			matrixVertical = Matrix.getTranslateInstance(25f, 100f);
			matrixVertical.rotate(Math.PI / 2);
		}

		insertQRCode(band, document, contents);

		// metemos el texto
		pushContent(band, contents, font, matrixVertical);

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
		if (StringUtils.isNotBlank(band.getQrCode())) {
			// Insertamos el código qr
			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			try {
				BitMatrix bitMatrix = qrCodeWriter.encode(band.getQrCode(), BarcodeFormat.QR_CODE, 80, 80);

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
	 * Push content.
	 *
	 * @param band           the band
	 * @param contents       the contents
	 * @param font           the font
	 * @param matrixVertical the matrix vertical
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void pushContent(Band band, PDPageContentStream contents, PDFont font, Matrix matrixVertical)
			throws IOException {
		logger.debug("Begin pushContent");
		contents.beginText();
		contents.setTextMatrix(matrixVertical);
		contents.setFont(font, 9);

		contents.showText(band.getTemplate().get(Template.HEADER));

		contents.newLineAtOffset(0f, -12f);
		contents.showText(band.getTemplate().get(Template.BODY));

		contents.newLineAtOffset(0f, -12f);
		contents.showText(band.getTemplate().get(Template.FOOTER));

		contents.endText();
		logger.debug("End pushContent");
	}

}
