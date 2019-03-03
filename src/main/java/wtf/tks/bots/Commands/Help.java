package wtf.tks.bots.Commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import wtf.tks.bots.Launcher;

import java.awt.*;

public class Help {
	
	public final static String COMMAND = "hilfe";
	
	private JDA jda;
	
	public Help(MessageReceivedEvent event) {
		jda = Launcher.getInstance();
		MessageBuilder mb = new MessageBuilder();
		mb.setEmbed(new EmbedBuilder()
				.setTitle("Hilfe")
				.setDescription("Beschreibung der einzelnen Befehle des Bots")
				.setColor(new Color(14640667))
				.setFooter("made by " + jda.getUserById("192405032614887425").getAsTag(),
						"https://cdn.discordapp.com/avatars/192405032614887425/a_0dea5864f9ccb766e88c54b83bd6b9e3.webp")
				.addField("!flaschenpost DeinText",
						"Der Flaschenpost Befehl sendet eine Nachricht in den Flaschenpost-Channel des Servers. Der Inhalt wird jedoch zuvor von einem Admin oder Mod geprüft.",
						false)
				.addField("!gerücht DeinText",
						"Der Gerüchte Befehl sendet eine anonymisierte Nachticht in den Gerüchte Channel. Der Inhalt wird jedoch zuvor von einem Admin oder Mod geprüft.",
						false)
				.build())
				.build();
		event.getChannel().sendMessage(mb.build()).queue();
	}
}
