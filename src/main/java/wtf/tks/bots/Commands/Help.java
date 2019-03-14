package wtf.tks.bots.Commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import wtf.tks.bots.Config;
import wtf.tks.bots.Launcher;

import java.awt.*;

public class Help {
	
	public final static String COMMAND = "hilfe";
	
	private JDA jda;
	private Config config;
	
	
	public Help(MessageReceivedEvent event) {
		jda = Launcher.getJdaInstance();
		config = Launcher.getConfigInstance();
		
		MessageBuilder mb = new MessageBuilder();
		mb.setEmbed(new EmbedBuilder()
				.setTitle("Hilfe")
				.setDescription("Beschreibung der einzelnen Befehle des Bots")
				.setColor(new Color(0x09DF0A))
				.setFooter("made by " + jda.getUserById("192405032614887425").getAsTag(),
						"https://cdn.discordapp.com/avatars/192405032614887425/a_0dea5864f9ccb766e88c54b83bd6b9e3.webp")
				.addField(config.getProp("prefix") + BottlePost.COMMAND + " DeinText",
						"Der Flaschenpost Befehl sendet eine Nachricht in den Flaschenpost-Channel des Servers. Der Inhalt wird jedoch zuvor von einem Admin oder Mod geprüft.",
						false)
				.addField(config.getProp("prefix") + Gossip.COMMAND + " DeinText",
						"Der Gerüchte Befehl sendet eine anonymisierte Nachticht in den Gerüchte Channel. Der Inhalt wird jedoch zuvor von einem Admin oder Mod geprüft.",
						false)
				.addField(config.getProp("prefix") + Config.GETCOMMAND,
						"Listet alle Bot Parameter auf",
						false)
				.addField(
						config.getProp("prefix") + Config.SETCOMMAND +
						" Parameter---neuerKonfigurationsWert",
						"Setzt einen neuen Konfigurationswert",
						false)
				.build());
		event.getChannel().sendMessage(mb.build()).queue();
	}
}
