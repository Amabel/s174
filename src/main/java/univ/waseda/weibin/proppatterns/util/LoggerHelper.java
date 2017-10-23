package univ.waseda.weibin.proppatterns.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerHelper {

	public static final String logPath = "log/";
//	public static String loggerName;
//	
//	public LoggerHelper(String loggerName) {
//		this.loggerName = loggerName;
//	}
	
	public static Logger getLogger(String loggerName) {
		Logger logger = Logger.getLogger(loggerName);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String logFilePath = logPath  + loggerName + "." + df.format(new Date()) + ".log";
		System.out.println(logFilePath);
		logger.setLevel(Level.FINE);
      	logger.setUseParentHandlers(false);
		try {
			File logFile = new File(logFilePath);
			System.out.println(logFile.getAbsolutePath());
			if (!logFile.exists()) {
				logFile.createNewFile();
			}
			FileHandler fileHandler = new FileHandler(logFilePath, false);
			fileHandler.setFormatter(new SimpleFormatter());
			logger.addHandler(fileHandler);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ConsoleHandler consoleHandler = new ConsoleHandler();
      	consoleHandler.setLevel(Level.CONFIG);      
      	logger.addHandler(consoleHandler);
		
		return logger;
	}

}
