package ink.anh.api.messages;

import org.bukkit.plugin.Plugin;

public class Logger {

    public static void info(Plugin plugin, String message) {
    	plugin.getLogger().info(ANSIColors.CYAN_BRIGHT + message + ANSIColors.RESET);
    }

    public static void warn(Plugin plugin, String message) {
    	plugin.getLogger().warning(ANSIColors.YELLOW + message + ANSIColors.RESET);
    }

    public static void error(Plugin plugin, String message) {
    	plugin.getLogger().severe(ANSIColors.RED + message  + ANSIColors.RESET);
    }
}
