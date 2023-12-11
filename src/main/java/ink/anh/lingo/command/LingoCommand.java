package ink.anh.lingo.command;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import ink.anh.api.lingo.Translator;
import ink.anh.api.messages.MessageType;
import ink.anh.api.messages.Messenger;
import ink.anh.api.player.PlayerData;
import ink.anh.api.utils.LangUtils;
import ink.anh.lingo.AnhyLingo;
import ink.anh.lingo.GlobalManager;
import ink.anh.lingo.Permissions;
import ink.anh.lingo.file.DirectoryContents;
import ink.anh.lingo.file.FileProcessType;
import ink.anh.lingo.item.ItemLang;
import ink.anh.lingo.file.FileCommandProcessor;

public class LingoCommand implements CommandExecutor {
	
	private AnhyLingo lingoPlugin;
    private GlobalManager globalManager;

	public LingoCommand(AnhyLingo lingoPlugin) {
		this.lingoPlugin = lingoPlugin;
		this.globalManager = lingoPlugin.getGlobalManager();
	}

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
            sendMessage(sender, "lingo_err_not_alloved ", MessageType.WARNING);
    		return true;
    	}
    	
    	String[] langs = checkPlayerPermissions(sender, Permissions.DIR_VIEW);
	    if (langs != null && langs[0] == null) {
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
    	String[] langs = checkPlayerPermissions(sender, Permissions.RELOAD);
	    if (langs != null && langs[0] == null) {
            return true;
	    }
	    
        if (lingoPlugin.getGlobalManager().reload()) {
            sendMessage(sender, Translator.translateKyeWorld(globalManager, "lingo_language_reloaded ", langs), MessageType.NORMAL);
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
            // Перевірка, чи є достатньо аргументів (мінімум 2)
            if (args.length < 2) {
                sendMessage(sender, "lingo_err_command_format /lingo set <lang1> <lang2> ...", MessageType.WARNING);
                return true;
            }

            // Створюємо масив для зберігання мовних кодів
            String[] langs = Arrays.copyOfRange(args, 1, args.length);

            // Отримуємо масив доступних мовних кодів ISO 639-1
            String[] isoLanguages = Locale.getISOLanguages();
            
            for (int i = 0; i < langs.length; i++) {
                String lang = langs[i].toLowerCase(); // Переведення до нижнього регістру

                // Перевірка на довжину мовного коду
                if (lang.length() != 2) {
                    sendMessage(sender, "lingo_err_language_code_2letters ", MessageType.WARNING);
                    return true;
                }

                // Перевірка, чи є код мови дійсним ISO мовним кодом або спеціальним випадком "ua"
                if (!Arrays.asList(isoLanguages).contains(lang) && !lang.equals(ua)) {
                    sendMessage(sender, "lingo_err_invalid_language_code " + lang, MessageType.WARNING);
                    return true;
                }

                if (lang.equals(uk)) {
                    langs[i] = ua;
                }
            }

            // Якщо все добре, здійснюємо налаштування мови для гравця
            new PlayerData(lingoPlugin).setCustomData(player, langData, langs);
            sendMessage(sender, "lingo_language_is_selected " + String.join(" ", langs), MessageType.NORMAL);
        }
        return true;
    }

    private boolean getLang(CommandSender sender) {
        // Перевіряємо, чи є викликач команди гравцем
        if (sender instanceof Player) {
            Player player = (Player) sender;
        	String langs;
        	String langData = "Lingo";
        	
        	PlayerData data = new PlayerData(lingoPlugin);
        	if (data.hasCustomData(player, langData)) {
        		langs = data.getStringData(player, langData).replace(',', ' ');
                sendMessage(sender, "lingo_you_language " + langs, MessageType.NORMAL);
                return true;
        	}
            sendMessage(sender, "lingo_you_have_not set_language ", MessageType.WARNING);
            return true;
        }
        return false;
    }

    private boolean resetLang(CommandSender sender) {
        // Перевіряємо, чи є викликач команди гравцем
        if (sender instanceof Player) {
            Player player = (Player) sender;
        	String langData = "Lingo";
        	
        	PlayerData data = new PlayerData(lingoPlugin);
        	if (data.hasCustomData(player, langData)) {
        		data.removeCustomData(player, langData);
                sendMessage(sender, "lingo_cleared_the_language ", MessageType.NORMAL);
                return true;
        	}
            sendMessage(sender, "lingo_you_have_not set_language ", MessageType.WARNING);
            return true;
        }
        return false;
    }

    private boolean itemLang(CommandSender sender, String[] args) {
    	
    	String[] checkPlayer = checkPlayerPermissions(sender, Permissions.RELOAD);
    	if (checkPlayer != null) {
    	    if (checkPlayer[1] == null) {
                sendMessage(sender, "lingo_err_not_have_permission ", MessageType.WARNING);
                return true;

    	    }
    	}
    	
        if (args.length != 3) {
            sendMessage(sender, "lingo_err_command_format /lingo items list or /lingo items <lang> <key>", MessageType.WARNING);
            return true;
        }

        String lang = args[1];

        if (lang.equalsIgnoreCase("list")) {
    		return listKeysForLang(sender, args, checkPlayer != null);
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
        sendMessage(sender, itemLang.toString(), MessageType.ESPECIALLY);
        if (checkPlayer != null) lingoPlugin.getLogger().info(itemLang.toString());

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
	
    private String[] checkPlayerPermissions(CommandSender sender, String permission) {
        // Перевірка, чи команду виконує консоль
        if (sender instanceof ConsoleCommandSender) {
            return null;
        }

        // Ініціалізація масиву з одним елементом null
        String[] langs = new String[] {null};

        if (sender instanceof Player) {
            Player player = (Player) sender;

            // Отримуємо мови для гравця
            langs = LangUtils.getPlayerLanguage(player, lingoPlugin);

            // Перевіряємо наявність дозволу у гравця
            if (!player.hasPermission(permission)) {
                sendMessage(sender, Translator.translateKyeWorld(globalManager, "lingo_err_not_have_permission ", langs), MessageType.ERROR);
                return langs;
            }
        }

        return langs;
    }

	private void sendMessage(CommandSender sender, String message, MessageType type) {
    	Messenger.sendMessage(globalManager, sender, message, type);
    }
}
