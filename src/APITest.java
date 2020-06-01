public class APITest {
	public static String fl;
	public static String smp;
	public static String tmp;
	public static String sPdf;
	public static String sCustomerName;
	public static String sApiAddress;
	public static String sSoapRequest = "http://www.minkz.net/";
    public static void main(String[] args) throws Exception {
    	//Set this to true if testing
    	boolean testing = false;
    	
    	
    	InitializingProperties(args, testing);
    	new Login();
    }
    
    private static void InitializingProperties(String[] args, boolean testing)
    {
    	sCustomerName = "MailADoc";
    	if (testing)
    	{
    		sApiAddress = "https://xeroxdemo.minkzmail.co.uk/webservice/api.asmx";
    	}
    	else
    	{
    		sPdf = args[0];
    		sApiAddress = "https://officemail.oursmart.co.uk/webservice/api.asmx";
    	}
    	if (System.getProperty("os.name").contains("Mac OS X"))
    	{
    		fl = System.getProperty("java.io.tmpdir") + sCustomerName + "/";
    		tmp = System.getProperty("user.home") + "/.MailADoc/GetTemplates.xml";
    		smp = System.getProperty("user.home") + "/.MailADoc/Sample.xml";
    		if (testing)
    			sPdf = "/Users/jonhorler/Desktop/Test.pdf";
    	}
    	else
    	{
    		fl = System.getProperty("java.io.tmpdir") + sCustomerName + "\\";
    		smp = "other files\\Sample.xml";
    		tmp = "other files\\GetTemplates.xml";
    		
    		if (testing)
    			sPdf = "C:\\Users\\user\\Desktop\\Google.pdf";
    	}
    }
}
