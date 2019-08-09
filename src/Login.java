import java.io.File;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Login implements ActionListener, ItemListener {

    /**
     *
     * Usage:
     *          java PostSOAP http://mywebserver:80/ SOAPAction c:\foo.xml
     *
     *  @param args command line arguments
     *                 Argument 0 is a URL to a web server
     *                 Argument 1 is the SOAP Action
     *                 Argument 2 is a local filename
     *
     */
	 	JFrame f=new JFrame("MailADoc Hybrid Mail Service Printer - Login");

	 	JLabel lXmlSoapRequest=new JLabel("Stock XML Path:");
	    JLabel l1=new JLabel("Username:");
	    JLabel l2=new JLabel("Password:");
	    JLabel l3=new JLabel("Remember Me:");
	    
	    JTextField tXmlSoapRequest=new JTextField(APITest.fl + "Sample.xml");
	    JTextField t1=new JTextField();
	    JTextField t2=new JTextField();
	    JCheckBox c1 = new JCheckBox();
	    
	    JButton b1=new JButton("Go");
	    JButton b2=new JButton("Test");
        File fLoginXmlCheck = new File(APITest.fl + "Login.xml");
        Boolean bForgetCheck = false;
	    
	    Login()
	    {
	    	File fTmpFolder = new File(System.getProperty("java.io.tmpdir") + APITest.sCustomerName);
	    	if (!fTmpFolder.exists())
	    		fTmpFolder.mkdir();
	    	lXmlSoapRequest.setBounds(50, 50, 300, 20);
	    	tXmlSoapRequest.setBounds(50, 70, 300, 20);
	    	
	        l1.setBounds(50,100,300,20);
	        t1.setBounds(50,120,300,20);
	        if(fLoginXmlCheck.exists())
	        	t1.setEnabled(false);
	        l2.setBounds(50,150,300,20);        
	        t2.setBounds(50,170,300,20);
	        if(fLoginXmlCheck.exists())
	        	t2.setEnabled(false);
	        //t2.setEchoChar('*');
	        l3.setBounds(50, 200, 300, 20);
	        c1.setBounds(150, 200, 20, 20);

	        if (fLoginXmlCheck.exists())
	        	c1.setSelected(true);
	        
	        b1.setBounds(50,300,300,20);
	        b2.setBounds(50, 320, 300, 20);
	        
	        f.add(lXmlSoapRequest);
	        f.add(tXmlSoapRequest);
	       
	        f.add(l1);
	        f.add(l2);
	        
	        f.add(t1);
	        f.add(t2);
	        
	        f.add(l3);
	        f.add(c1);
	        
	        f.add(b1);
	        f.add(b2);
	        
	        
	        b1.addActionListener(this);
	        b2.addActionListener(this);
	        c1.addItemListener(this);
	        
	        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        f.setLayout(null);
	        f.setVisible(true);
	        f.setSize(400,400);
	    }
	    
	    public void itemStateChanged(ItemEvent e) {
	    	if(e.getStateChange() == ItemEvent.DESELECTED) {
	    		t1.setEnabled(true);
	    		t2.setEnabled(true);
	    		bForgetCheck = true;
	    	}
	    }
	    
	    public void actionPerformed(ActionEvent e){
	    	if (e.getSource() == b2) {
	    		//ErrorBox(System.getProperty("java.io.tmpdir"), "Test");
	    		//ErrorBox(System.getProperty("os.name"), "OS");
	    		/*try {
	    		l1.setText(PDFCommands.PdfAddressReader("C:\\Users\\Administrator\\Desktop\\Test.pdf", 19 * 2.835, 47 * 2.835, 85 * 2.835, 39 * 2.835));
	    		}
	    		catch (Exception ex) {
	    			
	    		}*/
	    	}
	        if(e.getSource() == b1 ){
	        	String sCookieResult = "Error";
	        	if ((t1.getText().isEmpty() || t2.getText().isEmpty()) && !fLoginXmlCheck.exists())
	        	{
	        		ErrorBox("Blank inputs remaining", "Blank Inputs");
	        	}
        	
	        	else
	        	{
		        	try {
			        	if (bForgetCheck == true && fLoginXmlCheck.exists())
			        	{
			        		fLoginXmlCheck.delete();
			        	}
		        		if (!fLoginXmlCheck.exists())
		        			XMLCommands.XmlLoginParser(tXmlSoapRequest.getText(), t1.getText(), t2.getText());
		        		if (c1.isSelected())
		        		{
		        			sCookieResult = SOAPCommands.LoginSoapRequest("http://xeroxdemo.minkzmail.co.uk/webservice/api.asmx", "http://www.minkz.net/Login", APITest.fl+ "Login.xml", false);
		        		}
		        		else
		        		{
		        			sCookieResult = SOAPCommands.LoginSoapRequest("http://xeroxdemo.minkzmail.co.uk/webservice/api.asmx", "http://www.minkz.net/Login", APITest.fl + "Login.xml", true);
		        		}
		        	}
		        	catch  (Exception ex)
		        	{
		        		System.out.println(ex);
		        	}
		        	
		        	if (sCookieResult.indexOf("ASP.NET") < 0) {
		            	ErrorBox(sCookieResult, "Error");
		            	System.out.println(sCookieResult);
		            }
		            else
		            {
		            	try {
		            	SOAPCommands.APIRequest("http://xeroxdemo.minkzmail.co.uk/webservice/api.asmx", "http://www.minkz.net/GetTemplates", APITest.fl + "GetTemplates.xml", sCookieResult);
		            	f.setVisible(false);
		                new PostOptions(sCookieResult);
		            	}
		            	catch (Exception ex) {
		            		ErrorBox(ex.toString(), "Soap Request");
		            	}
		            }
		        }
	        }

	    }

	    private static void ErrorBox(String sMessage, String sTitle)
	    {
	    	JOptionPane.showMessageDialog(null, sMessage, "Error: " + sTitle, JOptionPane.ERROR_MESSAGE);
	    }
	   
}