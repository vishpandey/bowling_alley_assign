package misc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class ReadConfig {
 
	public static String GetPropValues(String key) throws IOException {
 		String result = "";
 		FileInputStream inputStream = null;
		try {
			Properties prop = new Properties();
			String propFileName = "/home/vishal/Documents/sem2/SE/BowlingAlley_Unit2/BowlingAlley/src/misc/config.properties";
 
			inputStream = new FileInputStream(propFileName);
 
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file not found in the classpath");
			}
			
			result  = prop.getProperty(key);
		
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			inputStream.close();
		}
		
		return result;
	}
}