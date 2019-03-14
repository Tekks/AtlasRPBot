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

import java.util.List;

public class Messages extends ListenerAdapter {
	
	private static Logger log = Logger.getRootLogger();
	
	private JDA jda;
	private Config config;
	
	private String PREFIX;
	private Guild guild;
	private User author;
	private List<Role> userRoles;
	private Message message;
	private MessageChannel channel;
	private String dpMessage;
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
		
		guild = jda.getGuildById(config.getProp("serverId"));
		author = event.getAuthor();
		userRoles = guild.getMember(author).getRoles();
		message = event.getMessage();
		channel = event.getChannel();
		dpMessage = message.getContentDisplay();
		
		if (author.isBot()) {
			return;
		}
		
		if (event.isFromType(ChannelType.PRIVATE)) {
			log.info("Message Received: " + event.getAuthor().getAsTag() + " Command: " +
					 event.getMessage().getContentDisplay());
			
			
			if (dpMessage.startsWith(PREFIX + BottlePost.COMMAND)) {
				if (!userAuth(config.getProp("rolePlayerId"))) {
					channel.sendMessage("❌ Keine Berechtigung!").queue();
					return;
				}
				bottlePost = new BottlePost(event);
			} else if (dpMessage.startsWith(PREFIX + Gossip.COMMAND)) {
				if (!userAuth(config.getProp("rolePlayerId"))) {
					channel.sendMessage("❌ Keine Berechtigung!").queue();
					return;
				}
				gossip = new Gossip(event);
			} else if (dpMessage.startsWith(PREFIX + Help.COMMAND)) {
				help = new Help(event);
			} else if (dpMessage.startsWith(PREFIX + Config.GETCOMMAND)) {
				if (!userAuth(config.getProp("roleModId")) &&
					(!userAuth(config.getProp("roleAdminId")))) {
					channel.sendMessage("❌ Keine Berechtigung!").queue();
					return;
				}
				channel.sendMessage(config.getaAllKeys()).queue();
			} else if (dpMessage.startsWith(PREFIX + Config.SETCOMMAND)) {
				if (!userAuth(config.getProp("roleModId")) &&
					(!userAuth(config.getProp("roleAdminId")))) {
					channel.sendMessage("❌ Keine Berechtigung!").queue();
					return;
				}
				dpMessage = message.getContentDisplay();
				String rawMessage = dpMessage.replaceAll("(.*)" + Config.SETCOMMAND, "");
				if (rawMessage.trim().length() <= 0) {
					event.getChannel()
							.sendMessage("❌ Bitte eine Nachricht nach dem Befehl angeben!").queue();
					return;
				}
				if (split(rawMessage).length <= 1) {
					event.getChannel().sendMessage("❌ Bitte Parameter Wert angeben!").queue();
					return;
				}
				channel.sendMessage(
						config.setProp(split(rawMessage)[0].replace(" ", ""), split(rawMessage)[1]))
						.queue();
			} else {
				event.getChannel().sendMessage("❌ Kein gültiger Befehl!").queue();
			}
			
		}
	}
	
	private boolean userAuth(String roleNeeded) {
		if (userRoles.contains(guild.getRoleById(roleNeeded))) {
			return true;
		} else {
			return false;
		}
	}
	
	private String[] split(String str) {
		String[] parts = str.split("---");
		return parts;
	}
}