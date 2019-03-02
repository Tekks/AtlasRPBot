package wtf.tks.bots;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.simple.parser.ParseException;
import wtf.tks.bots.Handlers.Messages;

import javax.security.auth.login.LoginException;
import java.io.IOException;

public class Launcher extends ListenerAdapter {
	
	private static Logger log = Logger.getRootLogger();
	
	public static void main(String[] args)
			throws LoginException, InterruptedException, IOException, ParseException {
		
		String log4jConfPath = "log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);
		
		JSONObjects jsonObj = new JSONObjects("config/discordConfig.json");
		
		JDA jda = new JDABuilder((String)jsonObj.getObject("token"))
				.addEventListener(new Messages())
				.build();
		jda.awaitReady();
	}
}

/*
 ToDo: Implementations
 	1. Flaschenpost mit RNG
 		- Command: (Direct Message) !Flaschenpost
 		- Doings:	Codec is MD
*/