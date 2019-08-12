import java.io.File;
import java.awt.*;
import java.awt.event.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;


public class Login implements ActionListener, ItemListener {

	 	JFrame f=new JFrame("OurSmart OfficePrinter - Login");

	    JLabel l1=new JLabel("Username:");
	    JLabel l2=new JLabel("Password:");
	    JLabel l3=new JLabel("Remember Me:");
	    
	    JTextField t1=new JTextField();
	    JTextField t2=new JTextField();
	    JCheckBox c1 = new JCheckBox();
	    
	    JButton b1=new JButton("Login");
        File fLoginXmlCheck = new File(APITest.fl + "Login.xml");
        Boolean bForgetCheck = false;
	    
	    Login()
	    {
	    	File fTmpFolder = new File(System.getProperty("java.io.tmpdir") + APITest.sCustomerName);
	    	if (!fTmpFolder.exists())
	    		fTmpFolder.mkdir();
	    	
	        l1.setBounds(50,20,200,25);
	        t1.setBounds(50,40,200,25);
	        l2.setBounds(50,70,200,25);        
	        t2.setBounds(50,90,200,25);
	        //t2.setEchoChar('*');
	        l3.setBounds(50, 120, 200, 25);
	        c1.setBounds(150, 120, 25, 25);

	        if (fLoginXmlCheck.exists()) {
	        	t1.setText(XMLCommands.XmlAttributeRecall(APITest.fl + "Login.xml", "Username", ""));
	        	t2.setText(XMLCommands.XmlAttributeRecall(APITest.fl + "Login.xml", "Password", ""));
	        	t1.setEnabled(false);
	        	t2.setEnabled(false);
	        	c1.setSelected(true);
	        }
	        b1.setBounds(50,150,200,20);
	        
	       
	        f.add(l1);
	        f.add(l2);
	        
	        f.add(t1);
	        f.add(t2);
	        
	        f.add(l3);
	        f.add(c1);
	        
	        f.add(b1);
	        
	        
	        b1.addActionListener(this);
	        c1.addItemListener(this);
	        
	        try
	        {
	        	f.setIconImage(ImageIO.read(getClass().getResourceAsStream("/Logo.png")));
	        }
	        catch (IOException ex)
	        {
	        	ErrorBox(ex.toString(), "Logo not found");
	        }
	        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        f.setLayout(null);
	        f.setVisible(true);
	        f.setSize(300,210);

	   }
	    
	    public void itemStateChanged(ItemEvent e) {
	    	if(e.getStateChange() == ItemEvent.DESELECTED) {
	    		t1.setEnabled(true);
	    		t2.setEnabled(true);
	    		bForgetCheck = true;
	    	}
	    }
	    
	    public void actionPerformed(ActionEvent e){
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
		        			XMLCommands.XmlLoginParser(APITest.smp, t1.getText(), t2.getText());
		        		if (c1.isSelected())
		        		{
		        			sCookieResult = SOAPCommands.LoginSoapRequest(APITest.sApiAddress, APITest.sSoapRequest + "Login", APITest.fl+ "Login.xml", false);
		        		}
		        		else
		        		{
		        			sCookieResult = SOAPCommands.LoginSoapRequest(APITest.sApiAddress, APITest.sSoapRequest + "Login", APITest.fl + "Login.xml", true);
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
		            	SOAPCommands.APIRequest(APITest.sApiAddress, APITest.sSoapRequest + "GetTemplates", APITest.tmp, sCookieResult);
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
	    private static void InfoBox(String sMessage, String sTitle) {
	    	JOptionPane.showMessageDialog(null, sMessage, "Info: " + sTitle, JOptionPane.INFORMATION_MESSAGE);
	    }
	   
}