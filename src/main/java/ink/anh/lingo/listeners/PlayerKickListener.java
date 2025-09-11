package ink.anh.lingo.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;

import ink.anh.api.LibraryManager;
import ink.anh.api.lingo.Translator;
import ink.anh.api.utils.LangUtils;
import ink.anh.lingo.AnhyLingo;

public class PlayerKickListener implements Listener {
	
	private LibraryManager manager;
	
	public PlayerKickListener(AnhyLingo lingo) {
		manager = lingo.getGlobalManager();
	}
	
	@EventHandler
	public void PlayerKickedEvent(PlayerKickEvent event) {
		Player player = event.getPlayer();
		String[] langs = LangUtils.getLangs(player);
		
		String reason = event.getReason();
		String kickMessage = event.getLeaveMessage();
		
		reason = Translator.translateKyeWorld(
				manager, reason, langs);
		kickMessage = Translator.translateKyeWorld(
				manager, kickMessage, langs);
		
		event.setReason(reason);
		event.setLeaveMessage(kickMessage);
	}

}
