public class APITest {
	public static String fl = "";
	public static String smp = "";
	public static String tmp = "";
	public static String sPdf = "";
	public static String sCustomerName = "MailADoc";
    public static void main(String[] args) throws Exception {
    	//String sCheck = "s";
    	//sPdf = args[0];  //For Live 
    	if (System.getProperty("os.name").contains("Mac OS X"))
    	{
    		fl = System.getProperty("java.io.tmpdir") + sCustomerName + "/";
    		tmp = "other files/GetTemplates.xml";
    		smp = "other files/Sample.xml";
    		sPdf = "/Users/jonhorler/Desktop/Test.pdf";
    	}
    	else
    	{
    		fl = System.getProperty("java.io.tmpdir") + sCustomerName + "\\";
    		smp = "other files\\Sample.xml";
    		tmp = "other files\\GetTemplates.xml";
    		sPdf = "C:\\Users\\Administrator\\Desktop\\Test.pdf";
    	}
    	//new PostOptions(sCheck);
    	new Login();
    }
}
