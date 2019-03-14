package wtf.tks.bots;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import org.apache.log4j.Logger;
import wtf.tks.bots.Backend.OrderedProperties;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class Config {
	
	public final static String GETCOMMAND = "config";
	public final static String SETCOMMAND = "setconfig";
	private final static String propFileLoc = "./config/config.properties";
	private static Logger log = Logger.getRootLogger();
	private OrderedProperties prop;
	private InputStream input;
	
	
	public Config() {
		prop = new OrderedProperties();
		loadProps();
	}
	
	
	private void loadProps() {
		input = null;
		try {
			input = new FileInputStream(propFileLoc);
			prop.load(input);
		} catch (IOException e) {
			log.error(e);
		}
	}
	
	
	public String getProp(String key) {
		return prop.getProperty(key);
	}
	
	
	public String setProp(String key, String value) {
		
		if (!prop.containsProperty(key)) {
			return "❌ Kein gültiger Parameter";
		}
		prop.setProperty(key, value);
		try {
			prop.store(new FileOutputStream(propFileLoc), null);
		} catch (IOException e) {
			log.error(e);
			return "❌ Kritischer Systemfehler. Bitte Administrator benachrichtigen";
		}
		loadProps();
		return "✔ Parameter aktualisiert";
	}
	
	public Message getaAllKeys() {
		MessageBuilder mb = new MessageBuilder();
		EmbedBuilder eb = new EmbedBuilder();
		
		eb.setTitle("Konfigurations Werte");
		eb.setDescription("Liste aller Konfigurierbaren Werte");
		eb.setColor(new Color(0x0400DF));
		for (Map.Entry key : prop.entrySet()) {
			eb.addField(key.getKey().toString(), key.getValue().toString(), false);
		}
		mb.setEmbed(eb.build());
		return mb.build();
	}
	
}
