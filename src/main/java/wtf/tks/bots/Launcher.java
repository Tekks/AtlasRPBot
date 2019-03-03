package wtf.tks.bots;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.simple.parser.ParseException;
import wtf.tks.bots.Handlers.Membership;
import wtf.tks.bots.Handlers.Messages;
import wtf.tks.bots.Handlers.Reactions;

import javax.security.auth.login.LoginException;
import java.io.IOException;

public class Launcher {
	
	private static Logger log = Logger.getRootLogger();
	
	private static JDA jda;
	private static JSONObjects jsonObj;
	
	public static void main(String[] args)
			throws LoginException, InterruptedException, IOException, ParseException {
		
		String log4jConfPath = "log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);
		
		jsonObj = new JSONObjects("config/botConfig.json");
		
		jda = new JDABuilder(jsonObj.read("token", String.class))
				.setGame(Game.listening("<help"))
				.build();
		jda.awaitReady();
		
		jda.addEventListener(new Messages());
		jda.addEventListener(new Reactions());
		jda.addEventListener(new Membership());
	}
	
	public static JDA getInstance() {
		return jda;
	}
	
	public static JSONObjects getJsonInstance() {
		return jsonObj;
	}
}