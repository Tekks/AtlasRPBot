package wtf.tks.bots;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import org.apache.log4j.*;
import wtf.tks.bots.Handlers.Membership;
import wtf.tks.bots.Handlers.Messages;
import wtf.tks.bots.Handlers.Reactions;
import wtf.tks.bots.Handlers.SupportTracker;

import javax.security.auth.login.LoginException;
import java.io.IOException;

public class Launcher {
	
	private static Logger log = LogManager.getLogger(Launcher.class);
	private static JDA jda;
	private static Config config;
	
	public static void main(String[] args)
			throws LoginException, InterruptedException, IOException {
		
		config = new Config();
		initLogger();
		
		jda = new JDABuilder(config.getProp("token"))
				.setGame(Game.listening("Privat " + config.getProp("prefix") + "hilfe"))
				.build();
		jda.awaitReady();
		
		jda.addEventListener(new Messages());
		jda.addEventListener(new Reactions());
		jda.addEventListener(new Membership());
		jda.addEventListener(new SupportTracker());
		
	}
	
	public static JDA getJdaInstance() {
		return jda;
	}
	
	public static Config getConfigInstance() {
		return config;
	}
	
	
	private static void initLogger() {
		ConsoleAppender console = new ConsoleAppender();
		String PATTERN = "%d [%p|%c|%C{1}] %m%n";
		console.setLayout(new PatternLayout(PATTERN));
		console.setThreshold(Level.INFO);
		console.activateOptions();
		log.getRootLogger().addAppender(console);
	}
}