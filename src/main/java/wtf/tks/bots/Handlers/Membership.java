package wtf.tks.bots.Handlers;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.apache.log4j.Logger;
import wtf.tks.bots.JSONObjects;
import wtf.tks.bots.Launcher;

public class Membership extends ListenerAdapter {
	
	private static Logger log = Logger.getRootLogger();
	
	private JSONObjects jsonObj;
	private JDA jda;
	
	private Role newMember;
	
	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		jda = Launcher.getInstance();
		newMember = event.getGuild().getRoleById(jsonObj.read("roleNewMemberId", String.class));
		event.getGuild().getController().addRolesToMember(event.getMember(), newMember).queue();
		log.info("New Member: " + event.getMember().getEffectiveName());
	}
}
