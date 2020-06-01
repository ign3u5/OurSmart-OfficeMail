import java.util.List;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;  
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.text.PDFTextStripperByArea;  
import java.awt.geom.Rectangle2D;


public class PDFCommands {
public static List<BufferedImage> ReadPdfAsImage(String sPdfFile) {
	try {
		return PDFToBufferedImages(sPdfFile);
	}
	catch (IOException e) {
		throw new RuntimeException(e);
	}
}

private static List<BufferedImage> PDFToBufferedImages(String pdfFilePath) throws IOException
{
	PDDocument document = null;
	try
	{
		List<BufferedImage> pdfImages = new ArrayList<BufferedImage>();
		File pdfFile = new File(pdfFilePath);
		int dpi = 300;
		if (pdfFile.exists())
		{
			document = PDDocument.load(pdfFile);
			PDFRenderer pdfRenderer = new PDFRenderer(document);
			int numberOfPages = document.getNumberOfPages();
			for (int i = 0; i < numberOfPages; ++i)
			{
				pdfImages.add(pdfRenderer.renderImageWithDPI(i, dpi, ImageType.RGB));
			}
		}
		return pdfImages;
	}
	catch (IOException ex)
	{
		throw new IOException(" PDFToBufferedImages - " + ex);
	}
	finally
	{
		if (document != null)
			document.close();
	}
}

public static String PdfAddressReader(String sPdfFile, double x, double y, double dWidth, double dHeight) throws IOException {
	
	Rectangle2D rAddressArea = new Rectangle2D.Double(x, y, dWidth, dHeight);
	return ExtractText(sPdfFile, 0, rAddressArea, "Address");
}

private static String ExtractText(String sPdfFile, int iPageNumber, Rectangle2D rRegion, String sRegionName) throws IOException{
	String sExtractedText = "";
	File fPdf = new File(sPdfFile);
	PDDocument doc = PDDocument.load(fPdf);
	PDPage pPage = doc.getPage(iPageNumber);
	PDFTextStripperByArea ptsbaTextStripper = new PDFTextStripperByArea();
	ptsbaTextStripper.addRegion(sRegionName, rRegion);
	ptsbaTextStripper.extractRegions(pPage);
	sExtractedText = ptsbaTextStripper.getTextForRegion(sRegionName);
	doc.close();
	return sExtractedText;
}
}
