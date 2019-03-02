package wtf.tks.bots.Handlers;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;
import wtf.tks.bots.JSONObjects;
import wtf.tks.bots.Tools.BottlePost;

import java.io.IOException;

public class Messages extends ListenerAdapter {
	
	private static Logger log = Logger.getRootLogger();
	JSONObjects jsonObj;
	
	private Guild guild;
	private TextChannel textChannel;
	private Member member;
	private User author;
	private Message message;
	private MessageChannel channel;
	private String msg;
	private BottlePost bottlePost;
	
	private JDA jda;
	
	public Messages(JDA jda) throws IOException, ParseException {
		jsonObj = new JSONObjects("config/botConfig.json");
		this.jda = jda;
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		
		author = event.getAuthor();
		message = event.getMessage();
		channel = event.getChannel();
		
		msg = message.getContentDisplay();
		
		if (author.isBot()) {
			return;
		}
		
		if (event.isFromType(ChannelType.PRIVATE)) {
			log.info("Message Received: " + event.getAuthor());
			
			channel = jda
					.getTextChannelById(Long.valueOf((String) jsonObj.getObj("chAdminBottlePost")));
			
			bottlePost = new BottlePost(msg);
			
			String strTemp = "**User:** "
							 +  event.getAuthor().getAsTag() + " ###" + event.getAuthor().getId() + "###"
							 + "\n**EncryptionLevel:** " + bottlePost.getEncryption() + "%"
							 + "\n**Message:**\n"
							 + bottlePost.getDecryptedText()
							 + "\n\n**Encrypted Message:**\n"
							 + bottlePost.getEncryptText();
			channel.sendMessage(strTemp).queue(message -> {
				getSetMessage(message);
			});
			
			
		}
	}
	
	private Message getSetMessage(Message lMessage) {
		lMessage.addReaction((String) jsonObj.getObj("votePositiveEmoji")).queue();
		lMessage.addReaction((String) jsonObj.getObj("voteNegativeEmoji")).queue();
		return lMessage;
	}
	
}