package wtf.tks.bots.Handlers;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;
import wtf.tks.bots.JSONObjects;
import wtf.tks.bots.Launcher;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Reactions extends ListenerAdapter {
	
	private static Logger log = Logger.getRootLogger();
	
	private JSONObjects jsonObj;
	private JDA jda;
	
	public Reactions() throws IOException, ParseException {
		jsonObj = Launcher.getJsonInstance();
		this.jda = Launcher.getInstance();
	}
	
	@Override
	public void onGenericMessageReaction(GenericMessageReactionEvent event) {
		if (event.getUser().isBot()) {
			return;
		}
		
		if (event.getReaction().getTextChannel().getId()
				.equals(jda.getTextChannelById(jsonObj.read("chAdminBottlePost", String.class))
						.getId())) {
			
			if (event.getReaction().getReactionEmote().getName()
					.equals(jsonObj.read("votePositiveEmoji", String.class))) {
				postMsg(event.getMessageId());
				sendConfirmation(splitMessage(
						jda.getTextChannelById(jsonObj.read("chAdminBottlePost", String.class))
								.getMessageById(event.getMessageId()).complete()
								.getContentDisplay(),
						"(?<=###)(.*)(?=###)"), jsonObj.read("bottlePostAccept", String.class));
				delMsg(event.getMessageId());
				
			} else if (event.getReaction().getReactionEmote().getName().equals(
					jsonObj.read("voteNegativeEmoji", String.class))) {
				sendConfirmation(splitMessage(
						jda.getTextChannelById(jsonObj.read("chAdminBottlePost", String.class))
								.getMessageById(event.getMessageId()).complete()
								.getContentDisplay(),
						"(?<=###)(.*)(?=###)"), jsonObj.read("bottlePostDecline", String.class));
				delMsg(event.getMessageId());
				
			}
		}
	}
	
	private void delMsg(String Id) {
		jda.getTextChannelById(jsonObj.read("chAdminBottlePost", String.class))
				.deleteMessageById(Id)
				.queue();
	}
	
	private void postMsg(String Id) {
		String strTmp = splitMessage(
				jda.getTextChannelById(jsonObj.read("chAdminBottlePost", String.class))
						.getMessageById(Id).complete().getContentDisplay(), "().*$");
		jda.getTextChannelById(jsonObj.read("chBottlePost", String.class))
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
