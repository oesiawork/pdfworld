package es.sinjava.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.multipdf.LayerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.util.Matrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import es.sinjava.DGABand;
import es.sinjava.model.Band;
import es.sinjava.model.FieldContainer;
import es.sinjava.model.Template;

public class PDFAssembler {

	private static final float WIDTH = PDRectangle.A4.getWidth();
	private static final float HEIGHT = PDRectangle.A4.getHeight();
	private static final float FACTOR_REDUCED = 0.1f;
	private final Logger logger = LoggerFactory.getLogger(PDFAssembler.class);

	// Precargamos los valores

	public PDDocument build(PDDocument document, Band band, PDFont font) throws IOException {

		logger.info("Begin build");

		PDDocument documentOut = new PDDocument();
		PDPage blankPage = new PDPage();
		documentOut.addPage(blankPage);
		// Metemos la imagen

		PDImageXObject pdImage = PDImageXObject.createFromFile(
				PDFAssembler.class.getClassLoader().getResource("bandClara.png").getFile(), documentOut);

		PDPageContentStream contents = new PDPageContentStream(documentOut, blankPage);

		if (band.getPosition().equals(Band.Position.BOTTON)) {
			contents.drawImage(pdImage, 0, 0, WIDTH, HEIGHT * FACTOR_REDUCED);
		} else {
			contents.drawImage(pdImage, 0, 0, WIDTH * FACTOR_REDUCED, HEIGHT);
		}

		contents.restoreGraphicsState();

		// Lo inicializamos como matrix horizontal
		Matrix matrixVertical = Matrix.getTranslateInstance(100f, 30f);

		if (band.getPosition().equals(Band.Position.LEFT)) {
			matrixVertical = Matrix.getTranslateInstance(25f, 100f);
			matrixVertical.rotate(Math.PI / 2);
		}

		insertQRCode(band, documentOut, contents);

		// metemos el texto
		pushContent(band, contents, font, matrixVertical);

		LayerUtility layerUtility = new LayerUtility(documentOut);
		Matrix matrix = Matrix.getScaleInstance(0.9f, 0.9f);
		matrix.translate(WIDTH * 0.1f, HEIGHT * 0.1f);
		contents.transform(matrix);
		PDFormXObject form = layerUtility.importPageAsForm(document, 0);
		contents.drawForm(form);
		contents.restoreGraphicsState();
		contents.saveGraphicsState();
		contents.close();

		return documentOut;
	}

	private void insertQRCode(Band band, PDDocument documentOut, PDPageContentStream contents) throws IOException {
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
		contents.restoreGraphicsState();
		logger.debug("End pushContent");
	}

}
