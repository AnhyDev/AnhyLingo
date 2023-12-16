package ink.anh.lingo.listeners;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.inventory.ItemStack;

import ink.anh.lingo.AnhyLingo;

public class CommandListener implements Listener {
    private AnhyLingo lingoPlugin;

    public CommandListener(AnhyLingo lingoPlugin) {
        this.lingoPlugin = lingoPlugin;
    }

    /*@EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage().split(" ")[0].substring(1).toLowerCase();
    }*/

    @EventHandler
    public void onServerCommand(ServerCommandEvent event) {
    	String customCommand1 = "AnhyLingo-custom-command-event";
    	String customCommand2 = "AnhyLingo-command-translate-slot";
    	
        String[] parts = event.getCommand().split(" ");
        String command = parts[0].toLowerCase();
        String[] args = Arrays.copyOfRange(parts, 1, parts.length);

        if (args.length < 2) {
            return;
        }

        String playerName = args[0];
        Player player = Bukkit.getServer().getPlayer(playerName);
        
        if (player == null) {
            return;
        }

        if (customCommand1.equalsIgnoreCase(command)) {
            boolean booleanValue;
            try {
                booleanValue = Boolean.parseBoolean(args[1]);
            } catch (Exception e) {
                return;
            }
        } else if (customCommand2.equalsIgnoreCase(command)) {
            int slot = 0;
            try {
                slot = Integer.parseInt(args[1]);
            } catch (Exception e) {
                return;
            }
            ItemStack item = player.getOpenInventory().getItem(slot);
        }
    }
}

