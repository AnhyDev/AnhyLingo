package ink.anh.lingo.messages;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.audience.Audience;

import ink.anh.lingo.ItemLingo;
import ink.anh.lingo.utils.StringUtils;

public class Messenger {

    public static void sendMessage(CommandSender sender, String message, MessageType type) {
        String pluginName = "[" + ItemLingo.getInstance().getConfigurationManager().getPluginName() + "] ";
        String coloredMessage = StringUtils.translateKyeWorld(message, null, !(sender instanceof Player));

        if (sender instanceof Player) {
            Player player = (Player) sender;
            TextColor color = TextColor.fromCSSHexString(type.getColor(true));
            Component pluginNameComponent = Component.text(pluginName)
                                                     .color(TextColor.fromCSSHexString("#1D87E4")) // Блакитний колір для назви плагіна
                                                     .decorate(TextDecoration.BOLD);
            Component messageComponent = Component.text(coloredMessage)
                                                 .color(color);
            Audience audience = (Audience) player;
            audience.sendMessage(pluginNameComponent.append(messageComponent));
        } else {
            switch (type) {
                case IMPORTANT:
                case WARNING:
                    ItemLingo.warn(coloredMessage);
                    break;
                case ERROR:
                case CRITICAL_ERROR:
                    ItemLingo.error(coloredMessage);
                    break;
                default:
                    ItemLingo.info(coloredMessage);
                    break;
            }
        }
    }
}
