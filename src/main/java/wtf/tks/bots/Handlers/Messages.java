package wtf.tks.bots.Handlers;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.apache.log4j.Logger;
import wtf.tks.bots.Commands.BottlePost;
import wtf.tks.bots.Commands.Gossip;
import wtf.tks.bots.Commands.Help;
import wtf.tks.bots.Config;
import wtf.tks.bots.Launcher;

public class Messages extends ListenerAdapter {
	
	private static Logger log = Logger.getRootLogger();
	
	private JDA jda;
	private Config config;
	
	private String PREFIX;
	private Guild guild;
	private User author;
	private Message message;
	private MessageChannel channel;
	private String displayMessage;
	private BottlePost bottlePost;
	private Help help;
	private Gossip gossip;
	
	
	public Messages() {
		jda = Launcher.getJdaInstance();
		config = Launcher.getConfigInstance();
	}
	
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		PREFIX = config.getProp("prefix");
		
		author = event.getAuthor();
		message = event.getMessage();
		channel = event.getChannel();
		displayMessage = message.getContentDisplay();
		
		if (author.isBot()) {
			return;
		}
		
		if (event.isFromType(ChannelType.PRIVATE)) {
			guild = jda.getGuildById(config.getProp("serverId"));
			if (guild.getMember(event.getAuthor()).getRoles()
					.contains(guild.getRoleById(config.getProp("rolePlayerId")))) {
				log.info("Message Received: " + event.getAuthor().getAsTag() + " Command: " +
						 event.getMessage().getContentDisplay());
				if (displayMessage.startsWith(PREFIX + BottlePost.COMMAND)) {
					bottlePost = new BottlePost(event);
				} else if (displayMessage.startsWith(PREFIX + Help.COMMAND)) {
					help = new Help(event);
				} else if (displayMessage.startsWith(PREFIX + Gossip.COMMAND)) {
					gossip = new Gossip(event);
				}
			} else {
				event.getChannel().sendMessage("❌ Keine gültige Berechtigung").queue();
			}
		}
	}
}