import java.io.File;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PostOptions extends JFrame implements ActionListener {
	enum TemplateFlags
	{
		Scheduled (0x000100),			// letter will be auto-assigned from input queue to default output queue when that output queue is released as a result of a scheduled release time
		DocMan (0x000200),				// extract the address NACS code and set the LetterFlags.DocMan flag on the letter
		AllowFirstClass (0x000400),     // user can change class
		AllowColour (0x000800),			// user can change colour
		AllowSimplex (0x001000),        // user can change plex
		ForcePending (0x002000),        // letter will only go to user's pending queue when posted
		LargeLetter (0x004000),		// i.e. uses envelope larger than C5
		DefaultSelection (0x008000),    // will this be the default template shown on page load
		Public (0x010000),				// available to guest users
		SimpleTextExtraction (0x020000),    // use simple text extraction strategy to read address text from PDF
		DefaultFirstClass (0x040000),       // start with first class option selected
		DefaultColour (0x080000),       // start with colour option selected
		DefaultSimplex (0x100000),      // start with simplex option selected
		EnforceOrientation (0x200000),	// enforce match of template orientation with first page of letter
		JavaTextExtraction (0x400000),	// use PdfBox to extract text (much slower but works with older systems where itextSharp doesn't)
		Deleted (0x800000),	// not actually deleted, but marked as such
		Confidential (0x1000000),		// only the originator can see the item
		DefaultConfidential (0x2000000);     // start with confidential option selected
		private final int iTemplateValue;
		TemplateFlags(int iTemplateValue) {
			this.iTemplateValue = iTemplateValue;
		}
		public int getTemplateValue() {
			return this.iTemplateValue;
		}
	}
	enum PostFlags
	{
		FirstClass (0x00001),
		Colour (0x00002),
		Simplex (0x00004);
		private final int iFlagValue;
		PostFlags(int iFlagValue) {
			this.iFlagValue = iFlagValue;
		}
		public int getFlagValue() {
			return this.iFlagValue;
		}
	}

    	String sAuthKey;
	
		//Frame initialisation
		JFrame fMain = new JFrame("MailADoc Hybrid Mail Service Printer - Post Options");

		//Cursor initialisation
		Cursor cWait = new Cursor(Cursor.WAIT_CURSOR);
		Cursor cDefault = new Cursor(Cursor.DEFAULT_CURSOR);
		
		//Tabbed Pane initialisation
		JTabbedPane jtpOptions = new JTabbedPane();
		
		//Layered Pane initialisation
		JLayeredPane jlpPdfAndAddress = new JLayeredPane();
		
		//Panel initialisation
		JPanel jpMain = new JPanel(null);
		JPanel jpPdfPreview = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel jpTemplates = new JPanel(null);
		JPanel jpPostOptions = new JPanel(null);
		
		//Label Initialisation 
	 	JLabel lXmlSoapRequest=new JLabel("Stock XML Path:");
	    JLabel lClass=new JLabel("First Class");
	    JLabel lColour=new JLabel("Colour");
	    JLabel lSimplex=new JLabel("Simplex");
	    JLabel lTemplates = new JLabel("Select a Template:");
	    JLabel lAddressRect = new JLabel("");
	    
	    //Textbox Initialisation
	    JTextField tXmlSoapRequest=new JTextField();
	    
	    //Checkbox Initialisation
	    JCheckBox cClass = new JCheckBox();
	    JCheckBox cColour = new JCheckBox();
	    JCheckBox cSimplex = new JCheckBox();
	    
	    //Button Initialisation
	    JButton bNextPage = new JButton("Next Page");
	    JButton bPreviousPage = new JButton("Previous Page");
	    JButton bPost = new JButton("Post");
	    
	    //Combo Box Initialisation
	    JComboBox cbTemplates = new JComboBox();
	    ArrayList<String> alTemplates = XMLCommands.XmlTemplateParser(APITest.fl + "GetTemplatesResponse.xml", XMLCommands.TemplateReturnType.TITLE);
	    ArrayList<String> alAddressBox = XMLCommands.XmlTemplateParser(APITest.fl + "GetTemplatesResponse.xml", XMLCommands.TemplateReturnType.ADDRESSBOX);
	    ArrayList<String> alTemplateOptions = XMLCommands.XmlTemplateParser(APITest.fl + "GetTemplatesResponse.xml", XMLCommands.TemplateReturnType.FLAGS);
	    String[] saTemplates = new String[alTemplates.size()];

	    
	    
	    //Initialising Pdf Preview
	    List<BufferedImage> lbiPdfPreview = PDFCommands.ReadPdfAsImage(APITest.sPdf);
	    int iCurrentPage = 0;
	    int iTotalPages = lbiPdfPreview.size();
	    ImageIcon iiPdfPreview = new ImageIcon(lbiPdfPreview.get(0));
	    Image image = iiPdfPreview.getImage();
	    Image iTempPdfPreview = image.getScaledInstance(420, 594, Image.SCALE_SMOOTH);
	    ImageIcon iiFinalPdfPreview = new ImageIcon(iTempPdfPreview);
	    JLabel jPdfPreview = new JLabel(iiFinalPdfPreview, JLabel.LEFT);
	    
	    
	    
	    PostOptions(String sAuthKey)
	    {
	    	this.sAuthKey = sAuthKey;
	    	
	    	
	    	//Template Label Options
	    	lTemplates.setBounds(10, 10, 300, 20);
	    	
	    	//Check box options
	    	if (!FlagCheck(alTemplateOptions, TemplateFlags.AllowFirstClass, 0))
	    		cClass.setEnabled(false);
	    	if (!FlagCheck(alTemplateOptions, TemplateFlags.AllowColour, 0))
	    		cColour.setEnabled(false);
	    	if (!FlagCheck(alTemplateOptions, TemplateFlags.AllowSimplex, 0))
	    		cSimplex.setEnabled(false);
	    	//Post Options Label options
	        lClass.setBounds(10,10,300,20);
	        cClass.setBounds(110, 10, 25, 25);
	        lColour.setBounds(10, 30,300,20);    
	        cColour.setBounds(110, 30, 25, 25);
	        lSimplex.setBounds(10, 50, 300, 20);
	        cSimplex.setBounds(110, 50, 25, 25);
	        String sAddressBox = alAddressBox.get(0);
	        String[] sRect = sAddressBox.split(",");
    		lAddressRect.setBounds(15 + (Integer.parseInt(sRect[0]) * 2),15 + (Integer.parseInt(sRect[1]) * 2), Integer.parseInt(sRect[2])*2,Integer.parseInt(sRect[3])*2);
	        lAddressRect.setBorder(BorderFactory.createLineBorder(Color.RED));
	        
	        //Combo box options
		    int iCount = 0;
		    for (String sTemplate : alTemplates) {
		    	saTemplates[iCount] = alTemplates.get(iCount);
		    	iCount++;
		    }
		    cbTemplates = new JComboBox(saTemplates);
		    cbTemplates.setBounds(125, 10, 300, 25);
		    cbTemplates.addActionListener(this);

	        
	        //Main Panel Button Options
	        bNextPage.setBounds(270, 610, 150, 20);
    		if (iCurrentPage == iTotalPages - 1) {
    			bNextPage.setEnabled(false);
    		}
	        bPreviousPage.setBounds(10, 610, 150, 20);
	        bPreviousPage.setEnabled(false);
	        bPost.setBounds(600, 610, 150, 20);
	        bNextPage.addActionListener(this);
	        bPreviousPage.addActionListener(this);
	        bPost.addActionListener(this);
	    	
	    	//Panel options
	    	jpMain.setLayout(null);//new BoxLayout(jpMain, BoxLayout.X_AXIS));
	    	jpPdfPreview.add(jPdfPreview);
	    	jpPdfPreview.setBounds(10, 10, 450, 600);
	        jpTemplates.add(lTemplates);
	        jpTemplates.add(cbTemplates);
	        jpPostOptions.add(lClass);
	        jpPostOptions.add(lColour);
	        jpPostOptions.add(lSimplex);
	        jpPostOptions.add(cClass);
	        jpPostOptions.add(cColour);
	        jpPostOptions.add(cSimplex);
	        
	        //Tabbed Pane options
	        jtpOptions.add("Templates", jpTemplates);
	        jtpOptions.add("Post Options", jpPostOptions);
	        jtpOptions.setBounds(470, 10, 450, 600);
	        
	        //Layered Pane Options
	        jlpPdfAndAddress.add(jpPdfPreview, JLayeredPane.DEFAULT_LAYER);
	        jlpPdfAndAddress.add(lAddressRect, JLayeredPane.POPUP_LAYER);
	        jlpPdfAndAddress.setBounds(0, 0, 460, 610);
	        
	        //Main Panel Elements
	        jpMain.add(jlpPdfAndAddress);
	        jpMain.add(jtpOptions);
	        jpMain.add(bNextPage);
	        jpMain.add(bPreviousPage);
	        jpMain.add(bPost);
	        
	        //Main Frame options
	        fMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        fMain.add(jpMain);
	        fMain.setVisible(true);
	        fMain.setSize(930,700);

	    }
	    
	    public void actionPerformed(ActionEvent e){
	    	if (e.getSource() == cbTemplates) {
	    		int iSelection = cbTemplates.getSelectedIndex();
		        String sAddressBox = alAddressBox.get(iSelection);
		        String[] sRect = sAddressBox.split(",");
	    		lAddressRect.setBounds(15 + (Integer.parseInt(sRect[0]) * 2),15 + (Integer.parseInt(sRect[1]) * 2), Integer.parseInt(sRect[2])*2,Integer.parseInt(sRect[3])*2);
		    	//Check box options
		    	if (!FlagCheck(alTemplateOptions, TemplateFlags.AllowFirstClass, iSelection))
		    		cClass.setEnabled(false);
		    	else
		    		cClass.setEnabled(true);
		    	if (!FlagCheck(alTemplateOptions, TemplateFlags.AllowColour, iSelection))
		    		cColour.setEnabled(false);
		    	else
		    		cColour.setEnabled(true);
		    	if (!FlagCheck(alTemplateOptions, TemplateFlags.AllowSimplex, iSelection))
		    		cSimplex.setEnabled(false);
		    	else
		    		cSimplex.setEnabled(true);
	    	}
	    	if (e.getSource() == bNextPage) {
	    		
	    		iiPdfPreview = new ImageIcon(lbiPdfPreview.get(++iCurrentPage));
	    		image = iiPdfPreview.getImage();
	    		iTempPdfPreview = image.getScaledInstance(420, 594, Image.SCALE_SMOOTH);
	    		iiFinalPdfPreview = new ImageIcon(iTempPdfPreview);
	    		jPdfPreview.setIcon(iiFinalPdfPreview);
	    		lAddressRect.setVisible(false);
	    		if (iCurrentPage == iTotalPages - 1) {
	    			bNextPage.setEnabled(false);
	    		}
	    		if (iCurrentPage > 0) {
	    			bPreviousPage.setEnabled(true);
	    		}
	    		}
	        if(e.getSource() == bPreviousPage) {
	    		iiPdfPreview = new ImageIcon(lbiPdfPreview.get(--iCurrentPage));
	    		image = iiPdfPreview.getImage();
	    		iTempPdfPreview = image.getScaledInstance(420, 594, Image.SCALE_SMOOTH);
	    		iiFinalPdfPreview = new ImageIcon(iTempPdfPreview);
	    		jPdfPreview.setIcon(iiFinalPdfPreview);
	    		if (iCurrentPage < iTotalPages - 1) {
	    			bNextPage.setEnabled(true);
	    		}
	    		if (iCurrentPage == 0) {
	    			lAddressRect.setVisible(true);
	    			bPreviousPage.setEnabled(false);
	    		}
		        }
	        if (e.getSource() == bPost) {
	        	try {
	        		fMain.setCursor(cWait);
	        		String sAddress = "";
	        		try {
	    	    		sAddress = PDFCommands.PdfAddressReader(APITest.sPdf, 19 * 2.835, 47 * 2.835, 85 * 2.835, 39 * 2.835);
	    	    		}
	    	    		catch (Exception ex) {
	    	    			ErrorBox(ex.toString(), "Post Error - PostOptions");
	    	    			fMain.setCursor(cDefault);
	    	    		}
	        		
	        		if (PostcodeCheck(sAddress))
	        		{
	        			String sPostFlags = "0";
	        			int iPostFlags = 0;
	        			if (cClass.isSelected())
	        				iPostFlags += PostFlags.FirstClass.getFlagValue();
	        			if (cColour.isSelected())
	        				iPostFlags += PostFlags.Colour.getFlagValue();
	        			if (cSimplex.isSelected())
	        				iPostFlags += PostFlags.Simplex.getFlagValue();
	        			sPostFlags = String.valueOf(iPostFlags);
	        			XMLCommands.XmlPdfParser(APITest.smp, APITest.sPdf, "Test", alTemplates.get(cbTemplates.getSelectedIndex()), sPostFlags);
	        			SOAPCommands.APIRequest("http://xeroxdemo.minkzmail.co.uk/webservice/api.asmx", "http://www.minkz.net/PostLetter", APITest.fl + "PdfSubmission.xml", sAuthKey);
	        			fMain.setCursor(cDefault);
	        			InfoBox("Succesfully Posted", "Posted!");
	        		}
	        		else
	        			ErrorBox("Postcode is invalid (invalid format)", "Address Validity Error");
	        	}
	        	catch (Exception ex)
	        	{
	        		ErrorBox(ex.toString(), "Error");
	        		fMain.setCursor(cDefault);
	        	}
	        	
	        }

	    }

	    private static void ErrorBox(String sMessage, String sTitle)
	    {
	    	JOptionPane.showMessageDialog(null, sMessage, "Error: " + sTitle, JOptionPane.ERROR_MESSAGE);
	    }
	   
	    private static void InfoBox(String sMessage, String sTitle) {
	    	JOptionPane.showMessageDialog(null, sMessage, "Info: " + sTitle, JOptionPane.INFORMATION_MESSAGE);
	    }
	    
	    private static boolean PostcodeCheck(String sAddress)
	    {
	    	String[] saAddress = sAddress.split("\n");
	    	String sPostCode = saAddress[saAddress.length - 1].strip();
	    	String sPostCodePattern = "([Gg][Ii][Rr] 0[Aa]{2})|((([A-Za-z][0-9]{1,2})|(([A-Za-z][A-Ha-hJ-Yj-y][0-9]{1,2})|(([A-Za-z][0-9][A-Za-z])|([A-Za-z][A-Ha-hJ-Yj-y][0-9][A-Za-z]?))))\\s?[0-9][A-Za-z]{2})";
	    	Pattern pPostCodePattern = Pattern.compile(sPostCodePattern);
	    	Matcher mPostCodeMatcher = pPostCodePattern.matcher(sPostCode);
	    	if (mPostCodeMatcher.find())
	    		return true;
	    	return false;
	    }
	    private static boolean FlagCheck(ArrayList<String> alList, TemplateFlags tfFlagType, int iRecordNumber) {
	    	return ((Integer.parseInt(alList.get(iRecordNumber))) & tfFlagType.getTemplateValue()) == tfFlagType.getTemplateValue();
	    }
}