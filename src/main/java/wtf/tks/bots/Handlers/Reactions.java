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
	
	public Reactions() {
		this.jda = Launcher.getInstance();
	}
	
	@Override
	public void onGenericMessageReaction(GenericMessageReactionEvent event) {
		if (event.getUser().isBot()) {
			return;
		}
		
		
		if (event.getReaction().getTextChannel().getId()
				.equals(jda.getTextChannelById(Config.chAdminPost)
						.getId())) {
			
			String rawMessage = jda.getTextChannelById(Config.chAdminPost)
					.getMessageById(event.getMessageId()).complete().getContentDisplay();
			String messageId = event.getMessageId();
			String authorId = splitMessage(rawMessage, "(?<=###)(.*)(?=###)");
			String reaction = event.getReaction().getReactionEmote().getName();
			
			if (splitMessage(rawMessage, "(?<=Type:\\*\\*).*").trim().
					equals(Gossip.COMMAND)) {
				
				if (reaction.equals(Config.votePositiveEmoji)) {
					reactionHandler(reaction, messageId, authorId, Config.chGossip,
							Config.gossipAccept);
				} else if (reaction.equals(Config.voteNegativeEmoji)) {
					reactionHandler(reaction, messageId, authorId, Config.chGossip,
							Config.gossipDecline);
				}
			} else if (splitMessage(rawMessage, "(?<=Type:\\*\\*).*").trim()
					.equals(BottlePost.COMMAND)) {
				
				if (reaction.equals(Config.votePositiveEmoji)) {
					reactionHandler(reaction, messageId, authorId, Config.chBottlePost,
							Config.bottlePostAccept);
				} else if (reaction.equals(Config.voteNegativeEmoji)) {
					reactionHandler(reaction, messageId, authorId, Config.chBottlePost,
							Config.bottlePostDecline);
				}
			}
		}
	}
	
	private void reactionHandler(
			String reaction, String messageId,
			String authorId, String targetChannel, String confirmation) {
		
		if (reaction.equals(Config.votePositiveEmoji)) {
			postMsg(messageId, targetChannel);
			sendConfirmation(authorId, confirmation);
		} else if (reaction.equals(Config.voteNegativeEmoji)) {
			sendConfirmation(authorId, confirmation);
		}
		delMsg(messageId);
	}
	
	private void delMsg(String Id) {
		jda.getTextChannelById(Config.chAdminPost)
				.deleteMessageById(Id)
				.queue();
	}
	
	private void postMsg(String Id, String chBottlePost) {
		String strTmp = splitMessage(
				jda.getTextChannelById(Config.chAdminPost)
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
