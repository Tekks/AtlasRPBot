package wtf.tks.bots.Commands;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import wtf.tks.bots.Config;
import wtf.tks.bots.Launcher;

import java.util.Random;

public class BottlePost {
	
	public final static String COMMAND = "flaschenpost";
	
	private JDA jda;
	private Config config;
	
	private String rawMessage;
	private int encryption;
	private Random rng;
	private MessageReceivedEvent event;
	private Message message;
	private MessageChannel channel;
	private String displayMessage;
	private MessageChannel chAdminBottlePost;
	
	
	public BottlePost(MessageReceivedEvent event) {
		jda = Launcher.getJdaInstance();
		config = Launcher.getConfigInstance();
		
		chAdminBottlePost = jda.getTextChannelById(config.getProp("chAdminPost"));
		rng = new Random();
		this.event = event;
		this.message = event.getMessage();
		this.channel = event.getChannel();
		
		displayMessage = message.getContentDisplay();
		rawMessage = displayMessage.replaceAll("(.*)" + BottlePost.COMMAND, "");
		if (rawMessage.trim().length() <= 0) {
			event.getChannel().sendMessage("❌ Bitte eine Nachricht nach dem Befehl angeben!")
					.queue();
			return;
		}
		
		if (Character.isWhitespace(rawMessage.charAt(0))) {
			rawMessage.replaceFirst(" ", "");
		}
		
		encryption = setEncryption();
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
							"\n\nwurde zur Freigabe an einen Moderator / Admin versendet").queue();
		String strTemp = "**User:** "
						 + event.getAuthor().getAsTag() + " ###" + event.getAuthor().getId() +
						 "###"
						 + "\n**EncryptionLevel:** " + this.getEncryption() + "%"
						 + "\n**Type:** " + BottlePost.COMMAND
						 + "\n**Message:**\n"
						 + this.getDecryptedText()
						 + "\n\n**Encrypted Message:**\n"
						 + this.getEncryptText();
		chAdminBottlePost.sendMessage(strTemp).queue(message -> getSetMessage(message));
	}
	
	
	private Message getSetMessage(Message lMessage) {
		lMessage.addReaction(config.getProp("votePositiveEmoji")).queue();
		lMessage.addReaction(config.getProp("voteNegativeEmoji")).queue();
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