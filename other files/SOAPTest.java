import javax.xml.soap.*;

public class SOAPTest {
    public static void main(String args[]) {
        /*
            The example below requests from the Web Service at:
             http://www.webservicex.net/uszip.asmx?op=GetInfoByCity


            To call other WS, change the parameters below, which are:
             - the SOAP Endpoint URL (that is, where the service is responding from)
             - the SOAP Action

            Also change the contents of the method createSoapEnvelope() in this class. It constructs
             the inner part of the SOAP envelope that is actually sent.
         */
       // String soapEndpointUrl = "http://www.webservicex.net/uszip.asmx";
       //String soapAction = "http://www.webserviceX.NET/GetInfoByCity";
       // callSoapWebService(soapEndpointUrl, soapAction);
	   SOAPMessage soapMessage;
    }
}
