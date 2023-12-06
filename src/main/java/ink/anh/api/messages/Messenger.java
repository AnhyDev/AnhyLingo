package ink.anh.api.messages;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import ink.anh.api.lingo.Translator;
import ink.anh.api.lingo.lang.LanguageManager;
import ink.anh.api.utils.LangUtils;
import ink.anh.lingo.AnhyLingo;

public class Messenger {

	private static BukkitAudiences bukkitAudiences;
	private static LanguageManager languageManager;

	static {
		bukkitAudiences = AnhyLingo.getBukkitAudiences();
		languageManager = AnhyLingo.getInstance().getLanguageSystemChat();
	}
	
	public static void sendMessage(Plugin plugin, CommandSender sender, String message, MessageType type) {
	    String pluginName = "[" + AnhyLingo.getInstance().getConfigurationManager().getPluginName() + "] ";
	    String coloredMessage = Translator.translateKyeWorld(message, getPlayerLanguage(sender), languageManager);

	    if (sender instanceof Player) {
	        Player player = (Player) sender;
	        TextColor color = TextColor.fromCSSHexString(type.getColor(true));
	        Component pluginNameComponent = Component.text(pluginName)
	                                                 .color(TextColor.fromCSSHexString("#1D87E4")) // Блакитний колір для назви плагіна
	                                                 .decorate(TextDecoration.BOLD); // Жирний шрифт тільки для назви плагіна

	        Component messageComponent = Component.text(coloredMessage)
	                                             .color(color)
	                                             .decoration(TextDecoration.BOLD, false); // Вимкнути жирний шрифт для тексту повідомлення

	        bukkitAudiences.sender(player).sendMessage(pluginNameComponent.append(messageComponent));
	    } else {
	        sendConsole(plugin, coloredMessage, type);
	    }
	}

    public static void sendMessageSimple(Plugin plugin, CommandSender sender, String message, String icon, MessageType type) {
        String coloredMessage = Translator.translateKyeWorld(icon + message, getPlayerLanguage(sender), languageManager);

        if (sender instanceof Player) {
            Player player = (Player) sender;
            TextColor color = TextColor.fromCSSHexString(type.getColor(true));
            Component messageComponent = Component.text(coloredMessage)
                                                 .color(color);
            bukkitAudiences.sender(player).sendMessage(messageComponent);
        } else {
        	sendConsole(plugin, coloredMessage, type);
        }
    }
    
    public static void sendShowFolder(Plugin plugin, CommandSender sender, String patch, String folder, String icon, MessageType type, String[] langs) {
        // Перевіряємо, чи patch закінчується на слеш, і додаємо слеш, якщо потрібно
        String separator = (patch.endsWith("/")) ? "" : "/";
        String command = "/lingo dir " + patch + separator + folder;
        String showFolder = Translator.translateKyeWorld("lingo_file_show_folder_contents ", langs, languageManager);
        
        // Створюємо компонент для спливаючого тексту з жовтим кольором
        TextColor hoverTextColor = TextColor.fromCSSHexString("#FFFF00");
        Component hoverTextComponent = Component.text("\n " + showFolder + folder + " \n")
                                                .color(hoverTextColor);

        TextColor color = TextColor.fromCSSHexString(type.getColor(true));
        Component folderComponent = Component.text(icon + folder)
                                                .color(color)
                                                .hoverEvent(HoverEvent.showText(hoverTextComponent))
                                                .clickEvent(ClickEvent.runCommand(command));

        if (sender instanceof Player) {
            Player player = (Player) sender;
            
            bukkitAudiences.sender(player).sendMessage(folderComponent);

        } else {
            // Для консолі виводимо просте повідомлення без інтерактивності
            String coloredFolder = Translator.translateKyeWorld(icon + folder, null, languageManager);
            sendConsole(plugin, coloredFolder, type);
        }
    }
    
    private static void sendConsole(Plugin plugin, String message, MessageType type) {
    	String formatedMessage = type.formatConsoleColor(message);
        switch (type) {
            case CRITICAL_ERROR:
            	Logger.error(plugin, formatedMessage);
                break;
            case ERROR:
            	Logger.error(plugin, formatedMessage);
                break;
            case WARNING:
            	Logger.warn(plugin, formatedMessage);
                break;
            default:
            	Logger.info(plugin, formatedMessage);
                break;
        }
    }
    

    
    private static String[] getPlayerLanguage(CommandSender sender) {
    	return ((sender instanceof Player)) ? LangUtils.getPlayerLanguage((Player) sender) : null;
    }
}
