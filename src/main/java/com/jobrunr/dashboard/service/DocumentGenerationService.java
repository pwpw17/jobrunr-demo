package com.jobrunr.dashboard.service;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import org.docx4j.Docx4J;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.stereotype.Service;
import org.wickedsource.docxstamper.DocxStamper;
import org.wickedsource.docxstamper.DocxStamperConfiguration;

@Service
public class DocumentGenerationService {

	public void generateDocument(Path wordTemplatePath, Path pdfOutputPath, Object context)
			throws IOException, Docx4JException {
		Files.createDirectories(pdfOutputPath.getParent().toAbsolutePath());

		try (InputStream template = Files.newInputStream(wordTemplatePath);
				OutputStream out = Files.newOutputStream(pdfOutputPath)) {
			final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			final DocxStamper stamper = new DocxStamperConfiguration().setFailOnUnresolvedExpression(true).build();
			stamper.stamp(template, context, byteArrayOutputStream);

			Docx4J.toPDF(WordprocessingMLPackage.load(new ByteArrayInputStream(byteArrayOutputStream.toByteArray())),
					out);
			System.out.println(String.format("Generated salary slip %s", pdfOutputPath)); // for demo purposes only
		}

	}
	
	public void resizeImage(String path,String outputImagePath,int scaledWidth, int scaledHeight) throws IOException {
		File inputFile = new File(path);
		BufferedImage inputImage = ImageIO.read(inputFile);
		BufferedImage outputImage = new BufferedImage(scaledWidth,
                scaledHeight, inputImage.getType());
		
		Graphics2D g2d = outputImage.createGraphics();
		
		g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();
        String formatName = outputImagePath.substring(outputImagePath
                .lastIndexOf(".") + 1);
        ImageIO.write(outputImage, formatName, new File(outputImagePath));
	}
}
