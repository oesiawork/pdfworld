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

	public PDDocument build(PDDocument document, Band band) throws IOException {

		logger.info("Begin build");

		PDDocument documentOut = new PDDocument();
		PDPage blankPage = new PDPage();
		documentOut.addPage(blankPage);
		// Metemos la imagen

		PDImageXObject pdImage = PDImageXObject
				.createFromFile(DGABand.class.getClassLoader().getResource("bandClara.png").getFile(), documentOut);

		PDPageContentStream contents = new PDPageContentStream(documentOut, blankPage);

		if (band.getPosition().equals(Band.Position.BOTTON)) {
			contents.drawImage(pdImage, 0, 0, WIDTH, HEIGHT * FACTOR_REDUCED);
		} else {
			contents.drawImage(pdImage, 0, 0, WIDTH * FACTOR_REDUCED, HEIGHT);
		}

		contents.restoreGraphicsState();

		PDFont font = PDType1Font.HELVETICA;
		PDFont fontBold = PDType1Font.HELVETICA_BOLD;

		// Lo inicializamos como matrix horizontal
		Matrix matrixVertical = Matrix.getTranslateInstance(100f, 30f);

		if (band.getPosition().equals(Band.Position.LEFT)) {
			matrixVertical = Matrix.getTranslateInstance(25f, 100f);
			matrixVertical.rotate(Math.PI / 2);
		}

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

		// metemos el texto
		pushContent(band, contents, font, fontBold, matrixVertical);

		LayerUtility layerUtility = new LayerUtility(documentOut);
		Matrix matrix = Matrix.getScaleInstance(0.9f, 0.9f);
		matrix.translate(50f, 0f);
		contents.transform(matrix);
		PDFormXObject form = layerUtility.importPageAsForm(document, 0);
		contents.drawForm(form);
		contents.restoreGraphicsState();

		// draw a scaled form
		contents.saveGraphicsState();
		contents.close();

		return documentOut;
	}

	private void pushContent(Band band, PDPageContentStream contents, PDFont font, PDFont fontBold,
			Matrix matrixVertical) throws IOException {
		logger.debug("Begin pushContent");
		contents.beginText();
		contents.setTextMatrix(matrixVertical);
		contents.setFont(font, 9);

		processText(contents, band, Template.HEADER, font, fontBold);

		contents.newLineAtOffset(0f, -12f);
		processText(contents, band, Template.BODY, font, fontBold);

		contents.newLineAtOffset(0f, -12f);
		processText(contents, band, Template.FOOTER, font, fontBold);

		contents.endText();
		contents.restoreGraphicsState();
		logger.debug("End pushContent");
	}

	private void processText(PDPageContentStream contents, Band band, String header, PDFont font, PDFont fontBold)
			throws IOException {

		logger.trace("Procesando texto : " + header);
		if (!band.getTemplate().get(header).contains(">>>")) {
			contents.showText(band.getTemplate().get(header));
		} else {
			String[] fragments = band.getTemplate().get(header).split(">>");

			// Here values to replace
			FieldContainer fc = band.getTemplate().getFieldContainer();

			// every fragment can start by a field
			for (String fragment : fragments) {
				logger.trace("Procesando " + fragment);
				if (fragment.startsWith(">")) {
					// bold font
					String[] fragmentsArray = fragment.split(">");

					contents.setFont(fontBold, 9);
					String replacement = fc.getContainer().get(fragmentsArray[1]) != null
							? fc.getContainer().get(fragmentsArray[1])
							: "";
					contents.showText(replacement);

					// restore default and write
					contents.setFont(font, 9);
					contents.showText(fragmentsArray[2]);
				} else {
					contents.showText(fragment);
				}

			}

		}

	}

}
