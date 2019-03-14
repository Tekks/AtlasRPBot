package wtf.tks.bots.Handlers;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.apache.log4j.Logger;
import wtf.tks.bots.Commands.Help;
import wtf.tks.bots.Config;
import wtf.tks.bots.Launcher;

import java.awt.*;

public class Membership extends ListenerAdapter {
	
	private static Logger log = Logger.getRootLogger();
	
	private JDA jda;
	private Config config;
	
	private Role newMember;
	private Role Whitelisted;
	
	
	public Membership() {
		jda = Launcher.getJdaInstance();
		config = Launcher.getConfigInstance();
	}
	
	
	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		newMember = event.getGuild().getRoleById(config.getProp("roleNewMemberId"));
		event.getGuild().getController().addRolesToMember(event.getMember(), newMember).queue();
		log.info("New Discord Member: " + event.getMember().getUser().getAsTag());
		MessageBuilder mb = new MessageBuilder();
		mb.setEmbed(new EmbedBuilder()
				.setTitle("Willkommen")
				.setDescription(":heart:-lich Willkommen auf dem Horizon-RP Server")
				.setColor(new Color(14640667))
				.setThumbnail(
						"https://cdn.discordapp.com/attachments/550795122892210196/552587276916490260/114958-OP0CLZ-201.png")
				.setImage(
						"https://cdn.discordapp.com/attachments/551411897883164685/552606027103731724/Map.png")
				.addField("#FAQs",
						"Bitte ließ dir die `#FAQs` durch. Damit werden 90% der meisten Fragen beantwortet.",
						false)
				.addField("Keiner Da? 🙄",
						"Falls dennoch Fragen auftreten, stehen das `@Admin` und `@Mod` Team gerne für Dich zur Verfügung.\n" +
						"Joine einfach in den `⏳Warte auf Support` Channel.\n" +
						"Falls keiner anwesend sein sollte, wird automatisch eine Nachricht hinterlassen und wir melden uns bei Dir :upside_down:",
						false)
				.addField("Bot", "Nutze `" + config.getProp("prefix") + Help.COMMAND +
								 "` um mehr über den Bot zu erfahren.", false)
				.build())
				.build();
		jda.getUserById(event.getUser().getId()).openPrivateChannel().complete()
				.sendMessage(mb.build()).queue();
	}
	
	
	@Override
	public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
		newMember = event.getGuild().getRoleById(config.getProp("roleNewMemberId"));
		Whitelisted = event.getGuild().getRoleById(config.getProp("rolePlayerId"));
		if (event.getRoles().get(0).equals(Whitelisted)) {
			event.getGuild().getController().removeRolesFromMember(event.getMember(), newMember)
					.queue();
			log.info("New RolePlayer: " + event.getMember().getUser().getAsTag());
		}
	}
}
