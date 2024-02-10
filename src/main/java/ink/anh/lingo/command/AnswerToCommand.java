package ink.anh.lingo.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ink.anh.api.lingo.Translator;
import ink.anh.api.messages.MessageComponents;
import ink.anh.api.messages.MessageComponents.MessageBuilder;
import ink.anh.api.messages.MessageType;
import ink.anh.api.messages.Messenger;
import ink.anh.api.utils.LangUtils;
import ink.anh.lingo.GlobalManager;

public class AnswerToCommand {


	public static void sendMessage(GlobalManager globalManager, CommandSender sender, String message, MessageType type, boolean addPluginName) {
	    String[] langs = sender instanceof Player ? LangUtils.getPlayerLanguage((Player) sender) : new String[] {globalManager.getDefaultLang()};
	    String translateText = Translator.translateKyeWorld(globalManager, message, langs);
	    String consoleText = type.getColor(false) + translateText;
	    
	    MessageBuilder mBuilder = MessageComponents.builder();
	    if (addPluginName) {
	        mBuilder.append(MessageComponents.builder()
	            .content("[" + globalManager.getPluginName() + "] ")
	            .hexColor("#1D87E4")
	            .decoration("BOLD", true)
	            .build());
	    }

	    mBuilder.append(MessageComponents.builder()
	        .content(translateText)
	        .hexColor(type.getColor(true))
	        .build());

	    Messenger.sendMessage(globalManager.getPlugin(), sender, mBuilder.build(), consoleText);
	}

}
