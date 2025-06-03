package weddingKart_GenericUtility;

import java.io.FileInputStream;
import java.util.Properties;

public class PropertyFileUtility {
	public String getDataFromPropertyFile(String key) throws Throwable {
		FileInputStream fis=new FileInputStream("./Config_Data/config.properties");
		Properties pro=new Properties();
		pro.load(fis);
		String data=pro.getProperty(key);		
		return data;
		
	}


}
