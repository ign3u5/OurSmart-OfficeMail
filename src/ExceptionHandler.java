import java.util.logging.Level; 
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.io.IOException;
import java.util.logging.FileHandler;
import javax.swing.JOptionPane;
import java.io.File;
public class ExceptionHandler {
	private static String filePath = System.getProperty("user.home") + "\\.MailADoc\\Running.log";
	private Logger logger;	
	private FileHandler logFileHandler;
	ExceptionHandler(Logger classLogger)
	{
		try
		{
			File mailADocDirectory = new File(System.getProperty("user.home") + "\\.MailADoc");
			mailADocDirectory.mkdir();
			File logFile = new File(filePath);
			logFile.createNewFile();
			System.out.println(filePath);
			logFileHandler = new FileHandler(filePath);
			logger = classLogger;
			logger.addHandler(logFileHandler);
			SimpleFormatter formatter = new SimpleFormatter();
			logFileHandler.setFormatter(formatter);
		}
		catch (SecurityException ex)
		{
			ex.printStackTrace();
			ErrorBox(ex.toString(), "Security Exception - Exception Hanlder Failed");
			System.exit(1);
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			ErrorBox(ex.toString(), "IO Exception - Exception Handler Failed");
			System.exit(1);
		}
	}
	public void Log(Level logLevel, String logMessage)
	{
		logger.log(logLevel, logMessage);
	}
    private void ErrorBox(String sMessage, String sTitle)
    {
    	JOptionPane.showMessageDialog(null, sMessage, "Error: " + sTitle, JOptionPane.ERROR_MESSAGE);
    }
}
