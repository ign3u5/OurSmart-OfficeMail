import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.FileRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
//import org.apache.commons.codec.binary.Base64;
//import org.apache.commons.io.*;
import java.io.File;
//import java.nio.charset.StandardCharsets;
public class SOAPCommands {

    public static String LoginSoapRequest(String sUrl, String sSoapAction, String sXmlFilename, Boolean bDeleteXml) throws Exception
    {
        File input = new File(sXmlFilename);
        // Prepare HTTP post
        PostMethod post = new PostMethod(sUrl);
        // Request content will be retrieved directly
        // from the input stream
        RequestEntity entity = new FileRequestEntity(input, "text/xml; charset=ISO-8859-1");
        post.setRequestEntity(entity);
        // consult documentation for your web service
        post.setRequestHeader("SOAPAction", sSoapAction);
        // Get HTTP client
        HttpClient httpclient = new HttpClient();
        // Execute request
        try {
            httpclient.executeMethod(post);
            // Display status code
            //System.out.println("Response status code: " + result);
            // Display response
           // System.out.println("Response body: ");
            String sResponse = post.getResponseBodyAsString();
            //System.out.println(post.getResponseBodyAsString());
            if (post.getResponseBodyAsString().indexOf("success=\"true\"") < 0)
            {
            	input.delete();
            	String sReturn = sResponse.substring(sResponse.indexOf("error_message=\"") + 15, sResponse.indexOf("\"></Response>"));
            	return sReturn;
            }
            System.out.println("Login successful");
            String sCookie = post.getResponseHeader("Set-Cookie").toString();
            //System.out.println(sCookie);
            if (bDeleteXml)
            	input.delete();
            return sCookie.substring(sCookie.indexOf(':') + 2, sCookie.indexOf(';'));
        } finally {
            // Release current connection to the connection pool once you are done
            post.releaseConnection();
        }
    }
    public static void APIRequest(String sUrl, String sSoapAction, String sXmlFilename, String sAuthToken) throws Exception
    {
        File input = new File(sXmlFilename);
        // Prepare HTTP post
        PostMethod post = new PostMethod(sUrl);
        // Request content will be retrieved directly
        // from the input stream
        RequestEntity entity = new FileRequestEntity(input, "text/xml; charset=ISO-8859-1");
        // Get HTTP client
        HttpClient httpclient = new HttpClient();
    	try {
	        post.setRequestEntity(entity);
	        // consult documentation for your web service
	        post.setRequestHeader("SOAPAction", sSoapAction);
	        post.setRequestHeader("Cookie", sAuthToken);
    	}
    	catch (Exception ex)
    	{
    		throw new Exception(" API Request Set up - " + ex);
    	}
        // Execute request
        try {
        	int result = httpclient.executeMethod(post);
            // Display status code
            System.out.println("Response status code: " + result);
            // Display response
            System.out.println("Response body: ");
            System.out.println(post.getResponseBodyAsString());
            String sCurrentSoapAction = APITest.sSoapRequest + "GetTemplates";
            if (sSoapAction.equals(sCurrentSoapAction))
            	XMLCommands.XmlResponseParser(APITest.fl, post.getResponseBodyAsString(), "GetTemplatesResponse");
           // System.out.println(post.getResponseHeader("Set-Cookie"));
        }
        catch (Exception ex)
        {
        	throw new Exception(" APIRequest Executing Request - " + ex);
        }
        finally {
            // Release current connection to the connection pool once you are done
            post.releaseConnection();
        }
    }

}
