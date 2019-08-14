import java.io.File;
import java.awt.*;
import java.awt.event.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;


public class Login implements ActionListener, ItemListener {

	 	JFrame fMain=new JFrame("OurSmart OfficePrinter - Login");

	    JLabel lUsername=new JLabel("Username:");
	    JLabel lPassword=new JLabel("Password:");
	    JLabel lRemCred=new JLabel("Remember Me:");
	    
	    JTextField tUsername=new JTextField();
	    JPasswordField pPassword=new JPasswordField();
	    JCheckBox cRemCred = new JCheckBox();
	    JCheckBox cShowPass = new JCheckBox();
	    
	    JButton bLogin=new JButton("Login");
        File fLoginXmlCheck = new File(APITest.fl + "Login.xml");
        Boolean bForgetCheck = false;
	    
	    Login()
	    {
	    	File fTmpFolder = new File(System.getProperty("java.io.tmpdir") + APITest.sCustomerName);
	    	if (!fTmpFolder.exists())
	    		fTmpFolder.mkdir();
	    	
	    	//Set bounds
	    	lUsername.setBounds(50,20,200,25);
	    	lPassword.setBounds(50,70,200,25);
	    	lRemCred.setBounds(50, 120, 200, 25);
	    	tUsername.setBounds(50,40,200,25);
	    	pPassword.setBounds(50,90,200,25);
	    	cShowPass.setBounds(250, 90, 25, 25);
	    	cRemCred.setBounds(150, 120, 25, 25);
	    	bLogin.setBounds(50,150,200,20);
	    	
	        if (fLoginXmlCheck.exists()) {
	        	tUsername.setText(XMLCommands.XmlAttributeRecall(APITest.fl + "Login.xml", "Username", ""));
	        	pPassword.setText(XMLCommands.XmlAttributeRecall(APITest.fl + "Login.xml", "Password", ""));
	        	tUsername.setEnabled(false);
	        	pPassword.setEnabled(false);
	        	cShowPass.setEnabled(false);
	        	cRemCred.setSelected(true);
	        }
	        
	        pPassword.setEchoChar('*');
	        cShowPass.setSelected(true);
	       
	        fMain.add(lUsername);
	        fMain.add(lPassword);
	        
	        fMain.add(tUsername);
	        fMain.add(pPassword);
	        
	        fMain.add(lRemCred);
	        fMain.add(cRemCred);
	        fMain.add(cShowPass);
	        
	        fMain.add(bLogin);
	        
	        
	        bLogin.addActionListener(this);
	        cRemCred.addItemListener(this);
	        cShowPass.addItemListener(this);
	        fMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        fMain.setLayout(null);
	        fMain.setVisible(true);
	        fMain.setSize(300,210);

	   }
	    
	    public void itemStateChanged(ItemEvent e) {
	    	if(e.getSource() == cRemCred) {
	    	if(e.getStateChange() == ItemEvent.DESELECTED) {
	    		pPassword.setEnabled(true);
	    		tUsername.setEnabled(true);
	    		pPassword.setText("");
	    		cShowPass.setEnabled(true);
	    		bForgetCheck = true;
	    	}
	    	}
	    	if (e.getSource() == cShowPass) {
	    		if(e.getStateChange() == ItemEvent.SELECTED) {
	    			pPassword.setEchoChar('*');
	    		}
	    		if (e.getStateChange() == ItemEvent.DESELECTED) {
	    			pPassword.setEchoChar((char)0);
	    		}
	    	}

	    }
	    
	    public void actionPerformed(ActionEvent e){
	        if(e.getSource() == bLogin ){
	        	String sCookieResult = "Error";
	        	if ((tUsername.getText().isEmpty() || String.copyValueOf(pPassword.getPassword()).isEmpty()) && !fLoginXmlCheck.exists())
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
		        			XMLCommands.XmlLoginParser(APITest.smp, tUsername.getText(), String.copyValueOf(pPassword.getPassword()));
		        		if (cRemCred.isSelected())
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
		            	fMain.setVisible(false);
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