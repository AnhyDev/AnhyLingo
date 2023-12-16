package ink.anh.lingo.command;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import ink.anh.api.lingo.Translator;
import ink.anh.api.messages.MessageType;
import ink.anh.api.messages.Messenger;
import ink.anh.api.utils.LangUtils;
import ink.anh.lingo.AnhyLingo;
import ink.anh.lingo.GlobalManager;
import ink.anh.lingo.Permissions;
import ink.anh.lingo.file.DirectoryContents;
import ink.anh.lingo.file.FileProcessType;
import ink.anh.lingo.item.ItemLang;
import net.md_5.bungee.api.ChatColor;
import ink.anh.lingo.file.FileCommandProcessor;

/**
 * Command executor for the 'lingo' command in the AnhyLingo plugin.
 * This class processes and executes various subcommands related to language settings and file management.
 */
public class LingoCommand implements CommandExecutor {
	
	private AnhyLingo lingoPlugin;
    private GlobalManager globalManager;

    /**
     * Constructor for the LingoCommand class.
     *
     * @param lingoPlugin The instance of AnhyLingo plugin.
     */
	public LingoCommand(AnhyLingo lingoPlugin) {
		this.lingoPlugin = lingoPlugin;
		this.globalManager = lingoPlugin.getGlobalManager();
	}

	/**
     * Executes the given command, returning its success.
     *
     * @param sender Source of the command.
     * @param cmd The command which was executed.
     * @param label Alias of the command which was used.
     * @param args Passed command arguments.
     * @return true if a valid command, otherwise false.
     */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length > 0) {

            switch (args[0].toLowerCase()) {
            case "nbt":
                return new NBTSubCommand(lingoPlugin).execNBT(sender, args);
            case "items":
                return itemLang(sender, args);
            case "reload":
                return reload(sender);
            case "set":
                return setLang(sender, args);
            case "get":
                return getLang(sender);
            case "reset":
                return resetLang(sender);
            case "dir":
                return directory(sender, args);
            case "flingo":
            case "fl":
                return new FileCommandProcessor(lingoPlugin).processFile(sender, args, FileProcessType.YAML_LOADER);
            case "fother":
            case "fo":
                return new FileCommandProcessor(lingoPlugin).processFile(sender, args, FileProcessType.SIMPLE_LOADER);
            case "fdel":
            case "fd":
                return new FileCommandProcessor(lingoPlugin).processFile(sender, args, FileProcessType.FILE_DELETER);
            default:
                return false;
            }
        }
		return false;
	}

    private boolean directory(CommandSender sender, String[] args) {
    	if (!lingoPlugin.getGlobalManager().isAllowBrowsing()) {
            sendMessage(sender, "lingo_err_not_alloved_config ", MessageType.WARNING);
    		return true;
    	}
    	
    	int perm = checkPlayerPermissions(sender, Permissions.DIR_VIEW);
	    if (perm != 0 && perm != 1) {
            return true;
	    }

        // Перевірка, чи достатньо аргументів
        if (args.length != 2) {
            sendMessage(sender, "lingo_err_command_format /lingo dir <path>", MessageType.WARNING);
            return true;
        }
        
        String path = args[1];
        DirectoryContents.listDirectoryContents(sender, path);
        return true;
    }

    private boolean reload(CommandSender sender) {
    	int perm = checkPlayerPermissions(sender, Permissions.RELOAD);
	    if (perm != 0 && perm != 1) {
            return true;
	    }
	    
        if (lingoPlugin.getGlobalManager().reload()) {
            sendMessage(sender, "lingo_language_reloaded ", MessageType.NORMAL);
            return true;
        }
        return false;
    }

    /**
     * Handles the 'set' command to set player's language preferences.
     * 
     * @param sender The sender of the command.
     * @param args Arguments provided with the command.
     * @return true if the operation was successful, false otherwise.
     */
    private boolean setLang(CommandSender sender, String[] args) {
        // Check if the command is executed by a player
        if (sender instanceof Player) {
            Player player = (Player) sender;

            // Check if enough arguments are provided (minimum 2)
            if (args.length < 2) {
                sendMessage(sender, "lingo_err_command_format /example set <lang1> <lang2> ...", MessageType.WARNING);
                return true;
            }

            // Create an array to store language codes
            String[] newlangs = Arrays.copyOfRange(args, 1, args.length);
            
            // Attempt to set the player's language preferences
            int result = LangUtils.setLangs(player, newlangs);
            
            // Handling the result of the language setting operation
            if (result == 1) {
                // Successful operation
                String[] langs = LangUtils.getPlayerLanguage(player);
                sendMessage(sender, "lingo_language_is_selected " + String.join(", ", langs), MessageType.NORMAL);
            } else if (result == 0) {
                // Invalid language code length
                sendMessage(sender, "lingo_err_language_code_2letters ", MessageType.WARNING);
            } else {
                // Other errors
                sendMessage(sender, "lingo_err_invalid_language_code ", MessageType.WARNING);
            }
        } else {
            // Command can only be executed by a player
            sendMessage(sender, "lingo_err_command_only_player", MessageType.WARNING);
        }
        return true;
    }

    /**
     * Handles the 'get' command to retrieve the player's current language settings.
     * 
     * @param sender The sender of the command.
     * @return true if the operation was successful, false otherwise.
     */
    private boolean getLang(CommandSender sender) {
        // Check if the command is executed by a player
        if (sender instanceof Player) {
            Player player = (Player) sender;
            
            // Retrieve the player's current language settings
            String[] langs = LangUtils.getLangs(player);
            if (langs != null) {
                // Display the current language settings to the player
                sendMessage(sender, "lingo_you_language " + String.join(", ", langs), MessageType.NORMAL);
            } else {
                // No language settings found
                sendMessage(sender, "lingo_you_have_not_set_language ", MessageType.WARNING);
            }
        } else {
            // Command can only be executed by a player
            sendMessage(sender, "lingo_err_command_only_player", MessageType.WARNING);
        }
        return true;
    }

    /**
     * Handles the 'reset' command to reset the player's language settings.
     * 
     * @param sender The sender of the command.
     * @return true if the operation was successful, false otherwise.
     */
    private boolean resetLang(CommandSender sender) {
        // Check if the command is executed by a player
        if (sender instanceof Player) {
            Player player = (Player) sender;
            
            // Attempt to reset the player's language settings
            int result = LangUtils.resetLangs(player);
            
            // Handling the result of the reset operation
            if (result == 1) {
                // Successful reset
                sendMessage(sender, "lingo_cleared_the_language ", MessageType.NORMAL);
            } else {
                // No language settings to reset
                sendMessage(sender, "lingo_you_have_not set_language ", MessageType.WARNING);
            }
        } else {
            // Command can only be executed by a player
            sendMessage(sender, "lingo_err_command_only_player", MessageType.WARNING);
        }
        return true;
    }

    private boolean itemLang(CommandSender sender, String[] args) {
    	
    	int checkPlayer = checkPlayerPermissions(sender, Permissions.RELOAD);
    	if (checkPlayer != 0) {
    	    if (checkPlayer == 2) {
                return true;

    	    }
    	}
    	
        if (args.length != 3) {
            sendMessage(sender, "lingo_err_command_format /lingo items list or /lingo items <lang> <key>", MessageType.WARNING);
            return true;
        }

        String lang = args[1];

        if (lang.equalsIgnoreCase("list")) {
    		return listKeysForLang(sender, args, checkPlayer != 0);
    	}
        

        String[] langs = new String[]{lang};
        
        String key = args[2];

        // Отримання об'єкта ItemLang
        ItemLang itemLang = globalManager.getLanguageItemStack().getData(key, langs);

        if (itemLang == null) {
            sendMessage(sender, "lingo_err_no_item_data_found ", MessageType.WARNING);
            return true;
        }

        // Виведення toString() об'єкта ItemLang
        sendMessage(sender, "\n" + ChatColor.RESET + itemLang.toString(), MessageType.ESPECIALLY);
        if (checkPlayer != 0) lingoPlugin.getLogger().info(itemLang.toString());

        return true;
    }

    private boolean listKeysForLang(CommandSender sender, String[] args, boolean isPlayer) {

        String lang = args[2];
        // Отримання мапи елементів з LanguageItemStack
        Map<String, Map<String, ItemLang>> data = globalManager.getLanguageItemStack().getDataMap();

        // Перебір усіх елементів, шукаючи ті, що відповідають вказаній мові
        List<String> keysForLang = new ArrayList<>();
        for (Map.Entry<String, Map<String, ItemLang>> entry : data.entrySet()) {
            if (entry.getValue().containsKey(lang)) {
                keysForLang.add(entry.getKey());
            }
        }

        if (keysForLang.isEmpty()) {
            sendMessage(sender, "lingo_err_no_item_data_found ", MessageType.WARNING);
            return true;
        }

        // Виведення всіх ключів
        sendMessage(sender, "lingo_keys_for_language " + lang + ":", MessageType.NORMAL);
        for (String key : keysForLang) {
            sendMessage(sender, key, MessageType.ESPECIALLY);
            if (isPlayer) {
                lingoPlugin.getLogger().info(key);
            }
        }

        return true;
    }
	
    private int checkPlayerPermissions(CommandSender sender, String permission) {
        // Перевірка, чи команду виконує консоль
        if (sender instanceof ConsoleCommandSender) {
            return 0;
        }

        if (sender instanceof Player) {

            // Перевіряємо наявність дозволу у гравця
            if (!sender.hasPermission(permission)) {
                sendMessage(sender, "lingo_err_not_have_permission ", MessageType.ERROR);
                return 2;
            }
        }

        return 1;
    }

	private void sendMessage(CommandSender sender, String message, MessageType type) {
		String[] langs = new String[] {globalManager.getDefaultLang()};
        if (sender instanceof Player) {
            Player player = (Player) sender;
            langs = LangUtils.getPlayerLanguage(player);
        }

    	Messenger.sendMessage(globalManager, sender, Translator.translateKyeWorld(globalManager, message, langs), type);
    }
}
