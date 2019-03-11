package wtf.tks.bots;

import java.io.*;
import java.util.Properties;

public class Config {
	
	private final static String propFileLoc = "./config/config.properties";
	
	private Properties prop;
	private InputStream input;
	
	
	public Config() throws IOException {
		prop = new Properties();
		loadProps();
	}
	
	
	private void loadProps() throws IOException {
		input = null;
		input = new FileInputStream(propFileLoc);
		prop.load(input);
	}
	
	
	public String getProp(String key){
		return prop.getProperty(key);
	}
	
	
	public void setProp(String key, String value) throws IOException {
		prop.setProperty(key,value);
		prop.store(new FileOutputStream(propFileLoc), null);
		loadProps();
	}
	
}
