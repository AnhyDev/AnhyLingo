package ink.anh.lingo.utils;

import org.bukkit.entity.Player;

import ink.anh.lingo.player.PlayerData;

public class LangUtils {


    public static String getPlayerLanguage(Player player) {
    	String lang;
    	String langData = "Lingo";
    	
    	PlayerData data = new PlayerData();
    	if (data.hasCustomData(player, langData)) {
    		lang = data.getCustomData(player, langData);
    	} else {
    		lang = processLocale(player);
    	}
    	return lang != null ? lang : "en";
    }

    public static String processLocale(Player player) {
        String locale = player.getLocale();
        String[] parts = locale.split("_");
        if (parts[0].equalsIgnoreCase("uk")) {
            return "ua";
        }
        return parts[0];
    }
}
