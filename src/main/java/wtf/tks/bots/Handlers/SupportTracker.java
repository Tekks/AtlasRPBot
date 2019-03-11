package wtf.tks.bots.Handlers;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import wtf.tks.bots.Config;
import wtf.tks.bots.Launcher;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SupportTracker extends ListenerAdapter {
	
	private JDA jda;
	private Config config;
	
	private String channelJoined;
	private List<User> users;
	
	
	public SupportTracker() {
		jda = Launcher.getJdaInstance();
		config = Launcher.getConfigInstance();
	}
	
	
	@Override
	public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
		users = new ArrayList<>();
		channelJoined = event.getChannelJoined().getId();
		
		if (channelJoined.equals(config.getProp("chVoiceSupport")) ||
			channelJoined.equals(config.getProp("chVoiceWhitelist"))) {
			
			for (Member member :
					event.getGuild()
							.getMembersWithRoles(jda.getRoleById(config.getProp("roleAdminId")))) {
				users.add(member.getUser());
			}
			for (Member member :
					event.getGuild()
							.getMembersWithRoles(jda.getRoleById(config.getProp("roleModId")))) {
				users.add(member.getUser());
			}
			List<User> usersUnified = users.stream()
					.distinct()
					.collect(Collectors.toList());
			
			for (User usr : usersUnified) {
				usr.openPrivateChannel().queue((channel) -> {
					channel.sendMessage(
							event.getMember().getUser().getAsTag() + " --> wartet in Channel --> " +
							event.getChannelJoined().getName()).queue();
				});
			}
		}
	}
	
}
