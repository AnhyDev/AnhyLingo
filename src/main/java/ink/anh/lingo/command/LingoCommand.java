package ink.anh.lingo.command;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ink.anh.lingo.ItemLingo;
import ink.anh.lingo.Permissions;
import ink.anh.lingo.file.DirectoryContents;
import ink.anh.lingo.file.FileProcessType;
import ink.anh.lingo.file.FileCommandProcessor;
import ink.anh.lingo.lang.ItemLang;
import ink.anh.lingo.player.PlayerData;
import ink.anh.lingo.utils.LangUtils;
import ink.anh.lingo.utils.StringUtils;

public class LingoCommand implements CommandExecutor {
	
	private ItemLingo itemLingoPlugin;

	public LingoCommand(ItemLingo plugin) {
		this.itemLingoPlugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length > 0) {

            switch (args[0].toLowerCase()) {
            case "nbt":
                return new NBTSubCommand(itemLingoPlugin).execNBT(sender, args);
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
                return new FileCommandProcessor(itemLingoPlugin).processFile(sender, args, FileProcessType.YAML_LOADER);
            case "fother":
            case "fo":
                return new FileCommandProcessor(itemLingoPlugin).processFile(sender, args, FileProcessType.SIMPLE_LOADER);
            case "fdel":
            case "fd":
                return new FileCommandProcessor(itemLingoPlugin).processFile(sender, args, FileProcessType.FILE_DELETER);
            default:
                return false;
            }
        }
		return false;
	}

    private boolean directory(CommandSender sender, String[] args) {
    	String lang = checkPlayerPermissions(sender, Permissions.DIR_VIEW);
	    if (lang != null && lang.equals("no_permission")) {
            return true;
	    }

        // Перевірка, чи достатньо аргументів
        if (args.length != 2) {
            sender.sendMessage("Usage: /lingo dir <path>");
            return true;
        }
        
        String path = args[1];
        DirectoryContents.listDirectoryContents(sender, path);
        return true;
    }

    private boolean reload(CommandSender sender) {
    	String lang = checkPlayerPermissions(sender, "itemlingo.reload");
	    if (lang != null && lang.equals("no_permission")) {
            return true;
	    }
	    
        if (itemLingoPlugin.getConfigurationManager().reload()) {
            sender.sendMessage(getPluginName() + StringUtils.translateKyeWorld("lingo_language_reloaded ", lang, true));
            return true;
        }
        return false;
    }

    private boolean setLang(CommandSender sender, String[] args) {
        String langData = "Lingo";
        String ua = "ua";
        String uk = "uk";
        // Перевіряємо, чи є викликач команди гравцем
        if (sender instanceof Player) {
            Player player = (Player) sender;
            // Перевірка, чи достатньо аргументів
            if (args.length != 2) {
                sender.sendMessage("Usage: /lingo set <lang>");
                return true;
            }
            String lang = args[1].toLowerCase(); // Переведення введення до нижнього регістру
            // Перевірка на довжину мовного коду
            if (lang.length() != 2) {
                sender.sendMessage("Language code should be 2 letters.");
                return true;
            }
            // Отримуємо масив доступних мовних кодів ISO 639-1
            String[] isoLanguages = Locale.getISOLanguages();
            // Перевірка, чи є код мови дійсним ISO мовним кодом або спеціальним випадком "ua"
            if (!Arrays.asList(isoLanguages).contains(lang) && !lang.equals(ua)) {
                sender.sendMessage("Invalid language code.");
                return true;
            }
            if (lang.equals(uk)) {
            	lang = ua;
            }
            // Якщо все добре, можна здійснити налаштування мови для гравця...
            new PlayerData().setCustomData(player, langData, lang);
        }
        return true;
    }

    private boolean getLang(CommandSender sender) {
        // Перевіряємо, чи є викликач команди гравцем
        if (sender instanceof Player) {
            Player player = (Player) sender;
        	String lang;
        	String langData = "Lingo";
        	
        	PlayerData data = new PlayerData();
        	if (data.hasCustomData(player, langData)) {
        		lang = data.getCustomData(player, langData);
                sender.sendMessage("You language: " + lang);
                return true;
        	}
            sender.sendMessage("You have not set the language");
            return true;
        }
        return false;
    }

    private boolean resetLang(CommandSender sender) {
        // Перевіряємо, чи є викликач команди гравцем
        if (sender instanceof Player) {
            Player player = (Player) sender;
        	String langData = "Lingo";
        	
        	PlayerData data = new PlayerData();
        	if (data.hasCustomData(player, langData)) {
        		data.removeCustomData(player, langData);
                sender.sendMessage("You have cleared the language");
                return true;
        	}
            sender.sendMessage("You have not set the language");
            return true;
        }
        return false;
    }

    private boolean itemLang(CommandSender sender, String[] args) {
    	boolean isPlayer = false;
        if (sender instanceof Player) {
        	isPlayer = true;
            Player player = (Player) sender;
            // Перевіряємо наявність дозволу
            if (!player.hasPermission("itemlingo.items.info")) {
                sender.sendMessage("You do not have permission to use this command.");
                return true;
            }
        }
    	
        if (args.length != 3) {
            sender.sendMessage("Usage: /lingo items list or /lingo items <lang> <key>");
            return true;
        }

        String lang = args[1];

        if (lang.equalsIgnoreCase("list")) {
    		return listKeysForLang(sender, args, isPlayer);
    	}
        
        String key = args[2];

        // Отримання об'єкта ItemLang
        ItemLang itemLang = itemLingoPlugin.getLanguageItemStack().getData(key, lang);

        if (itemLang == null) {
            sender.sendMessage("No item data found for the specified language and key.");
            return true;
        }

        // Виведення toString() об'єкта ItemLang
        sender.sendMessage(itemLang.toString());
        if (isPlayer) itemLingoPlugin.getLogger().info(itemLang.toString());

        return true;
    }

    private boolean listKeysForLang(CommandSender sender, String[] args, boolean isPlayer) {

        String lang = args[2];
        // Отримання мапи елементів з LanguageItemStack
        Map<String, Map<String, ItemLang>> data = itemLingoPlugin.getLanguageItemStack().getDataMap();

        // Перебір усіх елементів, шукаючи ті, що відповідають вказаній мові
        List<String> keysForLang = new ArrayList<>();
        for (Map.Entry<String, Map<String, ItemLang>> entry : data.entrySet()) {
            if (entry.getValue().containsKey(lang)) {
                keysForLang.add(entry.getKey());
            }
        }

        if (keysForLang.isEmpty()) {
            sender.sendMessage("No data found for the specified language.");
            return true;
        }

        // Виведення всіх ключів
        sender.sendMessage("Keys for language " + lang + ":");
        for (String key : keysForLang) {
            sender.sendMessage(key);
            if (isPlayer) {
                itemLingoPlugin.getLogger().info(key);
            }
        }

        return true;
    }
	
	private String checkPlayerPermissions(CommandSender sender, String permission) {
		String lang = null;
	    if (sender instanceof Player) {
	        Player player = (Player) sender;
	        lang = LangUtils.getPlayerLanguage(player);
	        // Перевіряємо наявність дозволу
	        if (!player.hasPermission("permission")) {
	            sender.sendMessage(getPluginName() + StringUtils.translateKyeWorld("lingo_err_not_have_permission ", lang, true));
	            return "no_permission";
	        }
	    }
		return lang;
	}

	private String getPluginName() {
		return "[" + ItemLingo.getInstance().getConfigurationManager().getPluginName() + "] ";
	}
}
