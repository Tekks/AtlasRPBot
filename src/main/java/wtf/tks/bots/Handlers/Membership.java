package wtf.tks.bots.Handlers;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.apache.log4j.Logger;
import wtf.tks.bots.Config;
import wtf.tks.bots.Launcher;

public class Membership extends ListenerAdapter {
	
	private static Logger log = Logger.getRootLogger();
	
	private JDA jda;
	private Config config;
	
	private Role newMember;
	private Role Whitelisted;
	
	
	public Membership(){
		jda = Launcher.getJdaInstance();
		config = Launcher.getConfigInstance();
	}
	
	
	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		newMember = event.getGuild().getRoleById(config.getProp("roleNewMemberId"));
		event.getGuild().getController().addRolesToMember(event.getMember(), newMember).queue();
		//ToDo: Send Welcome Message
		log.info("New Discord Member: " + event.getMember().getUser().getAsTag());
	}
	
	
	@Override
	public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
		newMember = event.getGuild().getRoleById(config.getProp("roleNewMemberId"));
		Whitelisted  = event.getGuild().getRoleById(config.getProp("rolePlayerId"));
		if (event.getRoles().get(0).equals(Whitelisted)){
			event.getGuild().getController().removeRolesFromMember(event.getMember(),newMember).queue();
			log.info("New RolePlayer: " + event.getMember().getUser().getAsTag());
		}
	}
}
