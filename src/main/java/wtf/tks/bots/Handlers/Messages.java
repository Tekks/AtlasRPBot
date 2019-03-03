package wtf.tks.bots.Handlers;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;
import wtf.tks.bots.Commands.BottlePost;
import wtf.tks.bots.JSONObjects;
import wtf.tks.bots.Launcher;

import java.io.IOException;

public class Messages extends ListenerAdapter {
	
	private static Logger log = Logger.getRootLogger();
	private JSONObjects jsonObj;
	private JDA jda;
	
	private String PREFIX;
	
	private Guild guild;
	private TextChannel textChannel;
	private User author;
	private Message message;
	private MessageChannel channel;
	private String displayMessage;
	private BottlePost bottlePost;
	private String rawMessage;
	
	
	
	
	
	public Messages() throws IOException, ParseException {
		jsonObj = Launcher.getJsonInstance();
		this.jda = Launcher.getInstance();
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		
		PREFIX = jsonObj.read("prefix", String.class);
		
		author = event.getAuthor();
		message = event.getMessage();
		channel = event.getChannel();
		displayMessage = message.getContentDisplay();
		if (author.isBot()) {
			return;
		}
		
		
		if (event.isFromType(ChannelType.PRIVATE)) {
			guild = jda.getGuildById(jsonObj.read("serverId", String.class));
			if (guild.getMember(event.getAuthor()).getRoles()
					.contains(guild.getRoleById(jsonObj.read("rolePlayerId", String.class)))) {
				log.info("Message Received: " + event.getAuthor());
				if (displayMessage.startsWith(PREFIX + BottlePost.COMMAND)) {
					bottlePost = new BottlePost(event);
				} else if (displayMessage.startsWith(PREFIX + BottlePost.COMMAND)) {
					//ToDo: Expander
				}
			} else {
				event.getChannel().sendMessage("❌ Keine gültige Berechtigung").queue();
			}
		}
	}
}