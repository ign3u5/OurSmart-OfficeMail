public class APITest {
	public static String fl = "";
	public static String smp = "";
	public static String tmp = "";
	public static String sPdf = "";
	public static String sCustomerName = "MailADoc";
	public static String sApiAddress = "http://xeroxdemo.minkzmail.co.uk/webservice/api.asmx";
	public static String sSoapRequest = "http://www.minkz.net/";
    public static void main(String[] args) throws Exception {
    	//String sCheck = "s";
    	//sPdf = args[0];  //For Live
    	sApiAddress = "https://officemail.oursmart.co.uk/webservice/api.asmx";
    	if (System.getProperty("os.name").contains("Mac OS X"))
    	{
    		fl = System.getProperty("java.io.tmpdir") + sCustomerName + "/";
    		tmp = System.getProperty("user.home") + "/.MailADoc/GetTemplates.xml";
    		smp = System.getProperty("user.home") + "/.MailADoc/Sample.xml";
    		sPdf = "/Users/jonhorler/Desktop/Test.pdf";
    	}
    	else
    	{
    		fl = System.getProperty("java.io.tmpdir") + sCustomerName + "\\";
    		smp = "other files\\Sample.xml";
    		tmp = "other files\\GetTemplates.xml";
    		//sPdf = "C:\\Users\\Administrator\\Desktop\\Test.pdf";
    	}
    	//new PostOptions(sCheck);
    	new Login();
    }
}
