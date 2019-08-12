import java.io.File;
import java.io.IOException;
import java.nio.charset.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.dom.*;
import javax.xml.parsers.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class XMLCommands {

	enum TemplateReturnType {
		TITLE, 
		FLAGS, 
		ADDRESSBOX,
		ENVELOPE,
		MAXSHEETS,
		BACKGROUND,
		ID
	}
	
public static void XmlResponseParser(String sXmlPath, String sXmlResponse, String sResponseType) throws java.io.FileNotFoundException
{
		PrintWriter out = new PrintWriter(sXmlPath + sResponseType + ".xml");
		out.println(sXmlResponse);
		out.close();
}

public static ArrayList<String> XmlTemplateParser(String sXmlPath, TemplateReturnType trtReturnType)
{
	ArrayList<String> alReturnValues = new ArrayList<String>();
	switch(trtReturnType) {
		case TITLE:
			alReturnValues = XmlTemplateType(sXmlPath, "title");
			break;
		case ADDRESSBOX:
			alReturnValues = XmlTemplateType(sXmlPath, "addressbox");
			break;
		case FLAGS:
			alReturnValues = XmlTemplateType(sXmlPath, "flags");
			break;
	}
	return alReturnValues;
}

private static ArrayList<String> XmlTemplateType(String sXmlPath, String sReturnType)
{
	ArrayList<String> alReturnValues = new ArrayList<String>();
	try {

	DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	Document doc = docBuilder.parse(sXmlPath);
	Element eNode;
	NodeList Templates = doc.getElementsByTagName("template");
	for (int i = 0; i < Templates.getLength(); ++i) {
		eNode = (Element)Templates.item(i);
		alReturnValues.add(eNode.getAttribute(sReturnType));
	}
	

	   } catch (ParserConfigurationException pce) {
		pce.printStackTrace();
	   } catch (java.io.IOException ioe) {
		ioe.printStackTrace();
	   } catch (SAXException sae) {
		   sae.printStackTrace();
	   }
	return alReturnValues;
}

public static String XmlAttributeRecall(String sXmlPath, String sTagName, String sAttributeName)
{
	String sReturn = "";
	try {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(sXmlPath);
		Element eNode;
		NodeList Templates = doc.getElementsByTagName(sTagName);
		eNode = (Element)Templates.item(0);
		if (sAttributeName.equals(""))
			return eNode.getTextContent();
		else
			return eNode.getAttribute(sAttributeName);
				

	} catch (ParserConfigurationException pce) {
		pce.printStackTrace();
	} catch (java.io.IOException ioe) {
		ioe.printStackTrace();
	} catch (SAXException sae) {
		   sae.printStackTrace();
	}
	return sReturn;
}
	
public static void XmlLoginParser(String sXmlPath, String sUsername, String sPassword)
{
	   try {
			String filepath = sXmlPath;
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filepath);

			// Get the root element
			Node Envelope = doc.getFirstChild();
			
			// Get the staff element , it may not working if tag has spaces, or
			// whatever weird characters in front...it's better to use
			// getElementsByTagName() to get it directly.
			// Node staff = company.getFirstChild();

			// Get the staff element by tag name directly
			Node Body = doc.getElementsByTagName("soap:Body").item(0);
			Element eLogin = doc.createElement("Login");
			Attr aLogin = doc.createAttribute("xmlns");
			aLogin.setValue("http://www.minkz.net/");
			eLogin.setAttributeNode(aLogin);
			Body.appendChild(eLogin);
			Node nLogin = doc.getElementsByTagName("Login").item(0); 		
			
			//LetterData
			Element eUsername = doc.createElement("Username");
			//Appending the inner text
			eUsername.appendChild(doc.createTextNode(sUsername));
			nLogin.appendChild(eUsername);
			
			//sLetterName
			Element ePassword = doc.createElement("Password");
			//Appending the inner text
			ePassword.appendChild(doc.createTextNode(sPassword));
			nLogin.appendChild(ePassword);
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(APITest.fl + "Login.xml"));
			transformer.transform(source, result);

			System.out.println("Done Login XML parse");

		   } catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		   } catch (TransformerException tfe) {
			tfe.printStackTrace();
		   } catch (java.io.IOException ioe) {
			ioe.printStackTrace();
		   } catch (SAXException sae) {
			   sae.printStackTrace();
		   }
}


public static void XmlPdfParser(String sXmlPath, String sPdfFile, String sLetterName, String sTemplateName, String sPostFlags) throws Exception
    {
 	   try {
 			String filepath = sXmlPath;
 			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
 			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
 			Document doc = docBuilder.parse(filepath);

 			// Get the root element
 			Node Envelope = doc.getFirstChild();

 			// Get the staff element , it may not working if tag has spaces, or
 			// whatever weird characters in front...it's better to use
 			// getElementsByTagName() to get it directly.
 			// Node staff = company.getFirstChild();

 			// Get the staff element by tag name directly
 			Node Body = doc.getElementsByTagName("soap:Body").item(0);
 			Element postLetter = doc.createElement("PostLetter");
 			Attr aPostLetter = doc.createAttribute("xmlns");
 			aPostLetter.setValue("http://www.minkz.net/");
 			postLetter.setAttributeNode(aPostLetter);
 			Body.appendChild(postLetter);
 			Node nPostLetter = doc.getElementsByTagName("PostLetter").item(0); 		
 			
 			//LetterData
 			Element eLetterData = doc.createElement("_LetterData");
 			//Appending the inner text
 			eLetterData.appendChild(doc.createTextNode(encodeFileToBase64Binary(sPdfFile)));
 			nPostLetter.appendChild(eLetterData);
 			
 			//sLetterName
 			Element eLetterName = doc.createElement("_sLetterName");
 			//Appending the inner text
 			eLetterName.appendChild(doc.createTextNode(sLetterName));
 			nPostLetter.appendChild(eLetterName);
 			
 			//sTemplateName
 			Element eTemplateName = doc.createElement("_sTemplateName");
 			//Appending the inner text
 			eTemplateName.appendChild(doc.createTextNode(sTemplateName));
 			nPostLetter.appendChild(eTemplateName);
 			
 			//sPostFlags
 			Element ePostFlags = doc.createElement("_PostFlags");
 			//Appending the inner text
 			ePostFlags.appendChild(doc.createTextNode(sPostFlags));
 			nPostLetter.appendChild(ePostFlags);
 			
 			/*
 			// update staff attribute
 			NamedNodeMap attr = Body.getAttributes();
 			Node nodeAttr = attr.getNamedItem("id");
 			nodeAttr.setTextContent("2");

 			// append a new node to staff
 			Element age = doc.createElement("age");
 			age.appendChild(doc.createTextNode("28"));
 			staff.appendChild(age);

 			// loop the staff child node
 			NodeList list = staff.getChildNodes();

 			for (int i = 0; i < list.getLength(); i++) {
 				
 	                   Node node = list.item(i);

 			   // get the salary element, and update the value
 			   if ("salary".equals(node.getNodeName())) {
 				node.setTextContent("2000000");
 			   }

 	                   //remove firstname
 			   if ("firstname".equals(node.getNodeName())) {
 				staff.removeChild(node);
 			   }

 			}
*/
 			// write the content into xml file
 			TransformerFactory transformerFactory = TransformerFactory.newInstance();
 			Transformer transformer = transformerFactory.newTransformer();
 			DOMSource source = new DOMSource(doc);
 			StreamResult result = new StreamResult(new File(APITest.fl + "PdfSubmission.xml"));
 			transformer.transform(source, result);

 			System.out.println("Done PDF XML parse");

 		   } catch (ParserConfigurationException pce) {
 			pce.printStackTrace();
 		   } catch (TransformerException tfe) {
 			tfe.printStackTrace();
 		   } catch (java.io.IOException ioe) {
 			ioe.printStackTrace();
 		}
    }
    
private static String encodeFileToBase64Binary(String fileName) throws java.io.IOException {
        File file = new File(fileName);
        byte[] encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(file));
        return new String(encoded, StandardCharsets.US_ASCII);
    }
}
