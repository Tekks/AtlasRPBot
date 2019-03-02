package wtf.tks.bots;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.simple.parser.ParseException;
import wtf.tks.bots.Handlers.Messages;
import wtf.tks.bots.Handlers.Reactions;

import javax.security.auth.login.LoginException;
import java.io.IOException;

public class Launcher {
	
	private static Logger log = Logger.getRootLogger();
	
	private static JDA jda;
	
	public static void main(String[] args)
			throws LoginException, InterruptedException, IOException, ParseException {
		
		String log4jConfPath = "log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);
		
		JSONObjects jsonObj = new JSONObjects("config/discordConfig.json");
		
		jda = new JDABuilder((String) jsonObj.getObj("token"))
				.build();
		jda.awaitReady();
		jda.addEventListener(new Messages(jda));
		jda.addEventListener(new Reactions(jda));
	}
	
	
}

/*
 ToDo: Implementations
 	1. Flaschenpost mit RNG
 		- Command: (Direct Message) !Flaschenpost
 		- Doings:	Codec is MD
 		- Admin muss verifizieren
 			- Eigener Channel dafür mit UpDown vote System
 	2. Bewerbungsverfahren
 		- Eigener Channel
 		- Reaction bei ablehnung und annahme
*/