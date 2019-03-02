package wtf.tks.bots.Handlers;

import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.apache.log4j.Logger;
import wtf.tks.bots.Tools.BottlePost;

public class Messages extends ListenerAdapter {
	
	private static Logger log = Logger.getRootLogger();
	
	Guild guild;
	TextChannel textChannel;
	Member member;
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		
		
		var author = event.getAuthor();
		Message message = event.getMessage();
		MessageChannel channel = event.getChannel();

		String msg = message.getContentDisplay();

		if (author.isBot()) {
			return;
		}
		
		log.info(channel.getId() + " " + channel.getName());
		
		if (event.isFromType(ChannelType.PRIVATE)) {

			BottlePost bottlePost = new BottlePost(msg);
			String strTemp = "**EncryptionLevel:** " + bottlePost.getEncryption() + "%"
							 + "\n**Message:** " + bottlePost.getEncryptText();
			channel.sendMessage(strTemp).queue();
		}
	}
	
}
