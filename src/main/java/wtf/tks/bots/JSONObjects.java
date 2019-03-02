package wtf.tks.bots;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class JSONObjects {
	
	private static Logger log = Logger.getRootLogger();
	
	private JSONObject jsonObj;
	private JSONParser parser;
	
	public JSONObjects(String filePath) throws IOException, ParseException {
		parser = new JSONParser();
		jsonObj = new JSONObject();
		jsonObj = (JSONObject) parser
				.parse(new FileReader(getClass().getClassLoader().getResource(filePath).getFile()));
	}
	
	public Object getObj(String key) {
		return jsonObj.get(key);
	}
	
}
