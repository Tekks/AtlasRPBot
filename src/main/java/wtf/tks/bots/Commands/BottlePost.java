package wtf.tks.bots.Commands;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import wtf.tks.bots.JSONObjects;
import wtf.tks.bots.Launcher;

import java.util.Random;

public class BottlePost {
	
	public final static String COMMAND = "bottlepost";
	
	JSONObjects jsonObj;
	private JDA jda;
	private String rawMessage;
	private int encryption;
	private Random rng;
	
	private MessageReceivedEvent event;
	private User author;
	private Message message;
	private MessageChannel channel;
	private String displayMessage;
	
	private MessageChannel chAdminBottlePost;
	
	
	public BottlePost(MessageReceivedEvent event) {
		jda = Launcher.getInstance();
		jsonObj = Launcher.getJsonInstance();
		rng = new Random();
		
		this.event = event;
		this.author = event.getAuthor();
		this.message = event.getMessage();
		this.channel = event.getChannel();
		
		displayMessage = message.getContentDisplay();
		
		this.rawMessage = displayMessage.replaceAll("(.*)" + BottlePost.COMMAND + " ", "");
		;
		this.encryption = setEncryption();
		
		chAdminBottlePost = jda.getTextChannelById(
				jsonObj.read("chAdminBottlePost", String.class));
		
		makeMessage();
	}
	
	
	private int setEncryption() {
		Random rng = new Random();
		int low = 5;
		int high = 30;
		int rngResult = rng.nextInt(high - low) + low;
		return rngResult;
	}
	
	
	private void makeMessage() {
		channel.sendMessage("**Nachricht**\n" + rawMessage +
							"\n\nwurde zur Freigabe an einen Moderator/Admin versendet").queue();
		String strTemp = "**User:** "
						 + event.getAuthor().getAsTag() + " ###" + event.getAuthor().getId() +
						 "###"
						 + "\n**EncryptionLevel:** " + this.getEncryption() + "%"
						 + "\n**Message:**\n"
						 + this.getDecryptedText()
						 + "\n\n**Encrypted Message:**\n"
						 + this.getEncryptText();
		chAdminBottlePost.sendMessage(strTemp).queue(message -> getSetMessage(message));
	}
	
	
	private Message getSetMessage(Message lMessage) {
		lMessage.addReaction(jsonObj.read("votePositiveEmoji", String.class)).queue();
		lMessage.addReaction(jsonObj.read("voteNegativeEmoji", String.class)).queue();
		return lMessage;
	}
	
	
	public String getEncryptText() {
		
		int encryptChars = rawMessage.length() * encryption / 100;
		StringBuilder strBuilder = new StringBuilder(rawMessage);
		
		for (int i = 0; i <= encryptChars; i++) {
			int rngLocation = rng.nextInt(rawMessage.length() - 1) + 1;
			if (strBuilder.charAt(0) == '█') {
				i--;
			} else {
				strBuilder.setCharAt(rngLocation, '█');
			}
		}
		return strBuilder.toString();
	}
	
	
	public String getDecryptedText() {
		return rawMessage;
	}
	
	public int getEncryption() {
		return encryption;
	}
}