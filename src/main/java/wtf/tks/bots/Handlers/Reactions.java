package wtf.tks.bots.Handlers;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;
import wtf.tks.bots.JSONObjects;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Reactions extends ListenerAdapter {
	
	private static Logger log = Logger.getRootLogger();
	JSONObjects jsonObj;
	
	private JDA jda;
	
	public Reactions(JDA jda) throws IOException, ParseException {
		jsonObj = new JSONObjects("config/botConfig.json");
		this.jda = jda;
	}
	
	@Override
	public void onGenericMessageReaction(GenericMessageReactionEvent event) {
		if (event.getUser().isBot()) {
			return;
		}
		if (event.getReaction().getReactionEmote().getName()
				.equals(jsonObj.getObj("votePositiveEmoji"))) {
			postMsg(event.getMessageId());
			sendConfirmation(splitMessage(
					jda.getTextChannelById(jsonObj.getObj("chAdminBottlePost").toString())
							.getMessageById(event.getMessageId()).complete().getContentDisplay(),
					"(?<=###)(.*)(?=###)"),jsonObj.getObj("bottlePostAcept").toString());
			delMsg(event.getMessageId());
			
		} else if (event.getReaction().getReactionEmote().getName()
				.equals(jsonObj.getObj("voteNegativeEmoji"))) {
			sendConfirmation(splitMessage(
					jda.getTextChannelById(jsonObj.getObj("chAdminBottlePost").toString())
							.getMessageById(event.getMessageId()).complete().getContentDisplay(),
					"(?<=###)(.*)(?=###)"),jsonObj.getObj("bottlePostDecl").toString());
			delMsg(event.getMessageId());
			
			
		}
	}
	
	private void delMsg(String Id) {
		jda.getTextChannelById(jsonObj.getObj("chAdminBottlePost").toString()).deleteMessageById(Id)
				.queue();
	}
	
	private void postMsg(String Id) {
		String strTmp = splitMessage(
				jda.getTextChannelById(jsonObj.getObj("chAdminBottlePost").toString())
						.getMessageById(Id).complete().getContentDisplay(), "().*$");
		jda.getTextChannelById(jsonObj.getObj("chBottlePost").toString())
				.sendMessage(strTmp).queue();
	}
	
	private String splitMessage(String msg, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(msg);
		matcher.find();
		return matcher.group(0);
	}
	
	private void sendConfirmation(String user,String message) {
		jda.getUserById(user).openPrivateChannel().complete().sendMessage(message).queue();
	}
}
