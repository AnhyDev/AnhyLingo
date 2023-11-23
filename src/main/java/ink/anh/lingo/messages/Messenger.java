package ink.anh.lingo.messages;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import ink.anh.lingo.AnhyLingo;
import ink.anh.lingo.utils.StringUtils;

public class Messenger {

	private static BukkitAudiences bukkitAudiences;

	static {
		bukkitAudiences = AnhyLingo.getBukkitAudiences();
	}
	
    public static void sendMessage(CommandSender sender, String message, MessageType type) {
        String pluginName = "[" + AnhyLingo.getInstance().getConfigurationManager().getPluginName() + "] ";
        String coloredMessage = StringUtils.translateKyeWorld(message, null, !(sender instanceof Player));

        if (sender instanceof Player) {
            Player player = (Player) sender;
            TextColor color = TextColor.fromCSSHexString(type.getColor(true));
            Component pluginNameComponent = Component.text(pluginName)
                                                     .color(TextColor.fromCSSHexString("#1D87E4")) // Блакитний колір для назви плагіна
                                                     .decorate(TextDecoration.BOLD);
            Component messageComponent = Component.text(coloredMessage)
                                                 .color(color);
            bukkitAudiences.sender(player).sendMessage(pluginNameComponent.append(messageComponent));
        } else {
            switch (type) {
                case WARNING:
                    AnhyLingo.warn(coloredMessage);
                    break;
                case ERROR:
                case CRITICAL_ERROR:
                    AnhyLingo.error(coloredMessage);
                    break;
                default:
                    AnhyLingo.info(coloredMessage);
                    break;
            }
        }
    }
    
    public static void sendMessageSimple(CommandSender sender, String message, String icon, MessageType type) {
        String coloredMessage = StringUtils.translateKyeWorld(icon + message, null, !(sender instanceof Player));

        if (sender instanceof Player) {
            Player player = (Player) sender;
            TextColor color = TextColor.fromCSSHexString(type.getColor(true));
            Component messageComponent = Component.text(coloredMessage)
                                                 .color(color);
            bukkitAudiences.sender(player).sendMessage(messageComponent);
        } else {
            switch (type) {
                case WARNING:
                    AnhyLingo.warn(coloredMessage);
                    break;
                case ERROR:
                case CRITICAL_ERROR:
                    AnhyLingo.error(coloredMessage);
                    break;
                default:
                    AnhyLingo.info(coloredMessage);
                    break;
            }
        }
    }
    
    public static void sendShowFolder(CommandSender sender, String patch, String folder, String icon, MessageType type, String[] langs) {
        // Перевіряємо, чи patch закінчується на слеш, і додаємо слеш, якщо потрібно
        String separator = (patch.endsWith("/")) ? "" : "/";
        String command = "/lingo dir " + patch + separator + folder;
        String showFolder = StringUtils.translateKyeWorld("lingo_file_show_folder_contents ", langs, true);
        
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
            String coloredFolder = StringUtils.translateKyeWorld(icon + folder, null, true);
            switch (type) {
                case WARNING:
                    AnhyLingo.warn(coloredFolder);
                    break;
                case ERROR:
                case CRITICAL_ERROR:
                    AnhyLingo.error(coloredFolder);
                    break;
                default:
                    AnhyLingo.info(coloredFolder);
                    break;
            }
        }
    }
}
