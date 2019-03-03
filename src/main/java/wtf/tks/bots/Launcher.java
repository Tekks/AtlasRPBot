package wtf.tks.bots;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;
import wtf.tks.bots.Handlers.Membership;
import wtf.tks.bots.Handlers.Messages;
import wtf.tks.bots.Handlers.Reactions;

import javax.security.auth.login.LoginException;
import java.io.IOException;

public class Launcher {
	
	private static Logger log = Logger.getRootLogger();
	
	private static JDA jda;
	
	public static void main(String[] args)
			throws LoginException, InterruptedException, IOException, ParseException {
		
		BasicConfigurator.configure();
		Logger.getRootLogger().setLevel(Level.INFO);
		
		jda = new JDABuilder(Config.token)
				.setGame(Game.listening(Config.prefix + "help"))
				.build();
		jda.awaitReady();
		
		jda.addEventListener(new Messages());
		jda.addEventListener(new Reactions());
		jda.addEventListener(new Membership());
	}
	
	public static JDA getInstance() {
		return jda;
	}
}