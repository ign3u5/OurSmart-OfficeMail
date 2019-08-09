import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import java.awt.Rectangle;
import java.awt.Image;
import java.awt.Graphics2D;
import java.util.List;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;  
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripperByArea;  
import java.awt.geom.Rectangle2D;


public class PDFCommands {
public static List<BufferedImage> ReadPdfAsImage(String sPdfFile) {
	try {
		File pdfFile = new File(sPdfFile);
		RandomAccessFile raf = new RandomAccessFile(pdfFile, "r");
		FileChannel channel = raf.getChannel();
		ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
		PDFFile pdf = new PDFFile(buf);
		
		List<BufferedImage> images = new ArrayList<BufferedImage>();
		for (int pageNumber = 1; pageNumber <= pdf.getNumPages(); ++pageNumber) {
			images.add(readPage(pdf, pageNumber));
		}
		
		raf.close();
		return images;
	}
	catch (IOException e) {
		throw new RuntimeException(e);
	}
}
public static String PdfAddressReader(String sPdfFile, double x, double y, double dWidth, double dHeight) throws IOException {
	
	Rectangle2D rAddressArea = new Rectangle2D.Double(x, y, dWidth, dHeight);
	return ExtractText(sPdfFile, 0, rAddressArea, "Address");
}
private static BufferedImage readPage(PDFFile pdf, int pageNumber) {
	double scale = 2.5; // because otherwise the image comes out really tiny
	PDFPage page = pdf.getPage(pageNumber);
	Rectangle rect = new Rectangle(0, 0, (int) page.getBBox().getWidth(), (int) page.getBBox().getHeight());
	BufferedImage bufferedImage = new BufferedImage((int)(rect.width * scale), (int)(rect.height * scale), BufferedImage.TYPE_INT_RGB);
	Image image = page.getImage((int)(rect.width * scale), (int)(rect.height * scale), rect, null, true, true);
	Graphics2D bufImageGraphics = bufferedImage.createGraphics();
	bufImageGraphics.drawImage(image, 0, 0, null);
	return bufferedImage;
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
