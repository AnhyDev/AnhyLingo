package ink.anh.lingo.file;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ink.anh.api.lingo.Translator;
import ink.anh.api.messages.MessageComponents;
import ink.anh.api.messages.MessageType;
import ink.anh.api.messages.Messenger;
import ink.anh.api.utils.LangUtils;
import ink.anh.lingo.AnhyLingo;
import ink.anh.lingo.GlobalManager;
import ink.anh.lingo.command.AnswerToCommand;

/**
 * Utility class for managing and displaying directory contents in the AnhyLingo plugin.
 */
public class DirectoryContents {

	private static GlobalManager globalManager;
	
    // Static initializer to obtain the GlobalManager instance from the AnhyLingo plugin.
    static {
        globalManager = AnhyLingo.getInstance().getGlobalManager();
    }

    /**
     * Lists the contents of a specified directory and sends the list to the CommandSender.
     * This method handles both files and directories, sorting them and presenting them in a user-friendly format.
     *
     * @param sender The CommandSender to whom the directory contents will be sent.
     * @param directoryPath The path to the directory whose contents are to be listed.
     */
    public static void listDirectoryContents(CommandSender sender, String directoryPath) {
    	if (directoryPath.equals("0")) directoryPath = "";
    	String[] langs = (sender instanceof Player) ? LangUtils.getPlayerLanguage((Player) sender) : null;
    	String pluginName = AnhyLingo.getInstance().getGlobalManager().getPluginName() + ": ";

        File directory = new File(AnhyLingo.getInstance().getServer().getWorldContainer(), "plugins" + File.separator + directoryPath);

        if (directory.exists() && directory.isDirectory()) {
            File[] fileList = directory.listFiles();

            if (fileList != null) {
            	String iconFolder = "📁 ";
            	String iconFile = "📄 ";

                Arrays.sort(fileList, Comparator.comparing(File::isFile)
                                                .thenComparing(File::getName, String.CASE_INSENSITIVE_ORDER));

                
            	AnswerToCommand.sendMessage(globalManager, sender, "lingo_file_folder_contents " + iconFolder + directoryPath, MessageType.IMPORTANT, true);
                for (File file : fileList) {
                    if (file.isDirectory()) {
                    	sendMessage(sender, directoryPath, file.getName(), iconFolder, MessageType.IMPORTANT, langs);
                    } else {
                    	sendMessage(sender, null, file.getName(), iconFile, MessageType.ESPECIALLY, null);
                    }
                }
            } else {
            	sender.sendMessage(pluginName + Translator.translateKyeWorld(globalManager, "lingo_err_folder_is_empty", langs));
            }
        } else {
        	sender.sendMessage(pluginName + Translator.translateKyeWorld(globalManager, "lingo_err_folder_is_notexist ", langs));
        }
    }

    private static void sendMessage(CommandSender sender, String patch, String folder, String icon, MessageType type, String[] langs) {
        MessageComponents folderComponent;
        
        if (patch != null) {
            String separator = patch.endsWith("/") ? "" : "/";
            String command = "/lingo dir " + patch + separator + folder;
            String showFolder = Translator.translateKyeWorld(globalManager, "lingo_file_show_folder_contents ", langs);

            MessageComponents hoverTextComponent = MessageComponents.builder()
                .content("\n " + showFolder + folder + " \n")
                .hexColor("#FFFF00")
                .build();

            folderComponent = MessageComponents.builder()
                .content(icon + folder)
                .hexColor(type.getColor(true))
                .hoverComponent(hoverTextComponent)
                .clickActionRunCommand(command)
                .build();
        } else {

            folderComponent = MessageComponents.builder()
                .content(icon + folder)
                .hexColor(type.getColor(true))
                .build();
        }
        
        Messenger.sendMessage(globalManager.getPlugin(), sender, folderComponent, icon + type.getColor(false) + folder);
    }
 
}
