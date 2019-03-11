package wtf.tks.bots.Handlers;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.apache.log4j.Logger;
import wtf.tks.bots.Commands.BottlePost;
import wtf.tks.bots.Commands.Gossip;
import wtf.tks.bots.Config;
import wtf.tks.bots.Launcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Reactions extends ListenerAdapter {
	
	private static Logger log = Logger.getRootLogger();
	
	private JDA jda;
	private Config config;
	
	
	public Reactions() {
		jda = Launcher.getJdaInstance();
		config = Launcher.getConfigInstance();
	}
	
	
	@Override
	public void onGenericMessageReaction(GenericMessageReactionEvent event) {
		if (event.getUser().isBot()) {
			return;
		}
		
		if (event.getReaction().getTextChannel().getId()
				.equals(jda.getTextChannelById(config.getProp("chAdminPost"))
						.getId())) {
			
			String rawMessage = jda.getTextChannelById(config.getProp("chAdminPost"))
					.getMessageById(event.getMessageId()).complete().getContentDisplay();
			String messageId = event.getMessageId();
			String authorId = splitMessage(rawMessage, "(?<=###)(.*)(?=###)");
			String reaction = event.getReaction().getReactionEmote().getName();
			
			if (splitMessage(rawMessage, "(?<=Type:\\*\\*).*").trim().
					equals(Gossip.COMMAND)) {
				
				if (reaction.equals(config.getProp("votePositiveEmoji"))) {
					reactionHandler(reaction, messageId, authorId, config.getProp("chGossip"),
							config.getProp("gossipAccept"));
				} else if (reaction.equals(config.getProp("voteNegativeEmoji"))) {
					reactionHandler(reaction, messageId, authorId, config.getProp("chGossip"),
							config.getProp("gossipDecline"));
				}
			} else if (splitMessage(rawMessage, "(?<=Type:\\*\\*).*").trim()
					.equals(BottlePost.COMMAND)) {
				
				if (reaction.equals(config.getProp("votePositiveEmoji"))) {
					reactionHandler(reaction, messageId, authorId, config.getProp("chBottlePost"),
							config.getProp("bottlePostAccept"));
				} else if (reaction.equals(config.getProp("voteNegativeEmoji"))) {
					reactionHandler(reaction, messageId, authorId, config.getProp("chBottlePost"),
							config.getProp("bottlePostDecline"));
				}
			}
		}
	}
	
	
	private void reactionHandler(
			String reaction, String messageId,
			String authorId, String targetChannel, String confirmation) {
		
		if (reaction.equals(config.getProp("votePositiveEmoji"))) {
			postMsg(messageId, targetChannel);
			sendConfirmation(authorId, confirmation);
		} else if (reaction.equals(config.getProp("voteNegativeEmoji"))) {
			sendConfirmation(authorId, confirmation);
		}
		delMsg(messageId);
	}
	
	
	private void delMsg(String Id) {
		jda.getTextChannelById(config.getProp("chAdminPost"))
				.deleteMessageById(Id)
				.queue();
	}
	
	
	private void postMsg(String Id, String chBottlePost) {
		String strTmp = splitMessage(
				jda.getTextChannelById(config.getProp("chAdminPost"))
						.getMessageById(Id).complete().getContentDisplay(), "().*$");
		jda.getTextChannelById(chBottlePost)
				.sendMessage(strTmp).queue();
	}
	
	
	private String splitMessage(String msg, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(msg);
		matcher.find();
		
		return matcher.group(0);
	}
	
	
	private void sendConfirmation(String user, String message) {
		jda.getUserById(user).openPrivateChannel().complete().sendMessage(message).queue();
	}
}
