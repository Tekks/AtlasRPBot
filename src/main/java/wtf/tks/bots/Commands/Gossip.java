package wtf.tks.bots.Commands;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import wtf.tks.bots.Config;
import wtf.tks.bots.Launcher;

import java.util.Random;

public class Gossip {
	
	public final static String COMMAND = "gerücht";
	
	private JDA jda;
	private String rawMessage;
	private Random rng;
	
	private MessageReceivedEvent event;
	private Message message;
	private MessageChannel channel;
	private String displayMessage;
	
	private MessageChannel chAdminBottlePost;
	
	public Gossip(MessageReceivedEvent event) {
		jda = Launcher.getInstance();
		rng = new Random();
		this.event = event;
		this.message = event.getMessage();
		this.channel = event.getChannel();
		
		displayMessage = message.getContentDisplay();
		rawMessage = displayMessage.replaceAll("(.*)" + Gossip.COMMAND, "");
		if (Character.isWhitespace(rawMessage.charAt(0))) {
			rawMessage = rawMessage.replaceFirst(" ", "");
		}
		
		chAdminBottlePost = jda.getTextChannelById(Config.chAdminPost);
		makeMessage();
	}
	
	
	private void makeMessage() {
		channel.sendMessage("**Nachricht**\n" + rawMessage +
							"\n\nwurde zur Freigabe an einen Moderator / Admin versendet").queue();
		String strTemp = "**User:** "
						 + event.getAuthor().getAsTag() + " ###" + event.getAuthor().getId() +
						 "###"
						 + "\n**Type:** " + Gossip.COMMAND
						 + "\n**Message:**\n"
						 + rawMessage;
		chAdminBottlePost.sendMessage(strTemp).queue(message -> getSetMessage(message));
	}
	
	
	private Message getSetMessage(Message lMessage) {
		lMessage.addReaction(Config.votePositiveEmoji).queue();
		lMessage.addReaction(Config.voteNegativeEmoji).queue();
		return lMessage;
	}
}
