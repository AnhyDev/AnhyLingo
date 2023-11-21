package ink.anh.lingo.file;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ink.anh.lingo.ItemLingo;
import ink.anh.lingo.messages.MessageType;
import ink.anh.lingo.messages.Messenger;
import ink.anh.lingo.utils.LangUtils;
import ink.anh.lingo.utils.StringUtils;
import net.md_5.bungee.api.ChatColor;

public class DirectoryContents {

    public static void listDirectoryContents(CommandSender sender, String directoryPath) {
    	if (directoryPath.equals("0")) directoryPath = "";
    	String[] langs = (sender instanceof Player) ? LangUtils.getPlayerLanguage((Player) sender) : null;
    	String pluginName = ItemLingo.getInstance().getConfigurationManager().getPluginName() + ": ";

        File directory = new File(ItemLingo.getInstance().getServer().getWorldContainer(), "plugins" + File.separator + directoryPath);

        if (directory.exists() && directory.isDirectory()) {
            File[] fileList = directory.listFiles();

            if (fileList != null) {
            	String iconFolder = "📁 ";
            	String iconFile = "📄 ";
                // Сортування файлів та папок
                Arrays.sort(fileList, Comparator.comparing(File::isFile)
                                                .thenComparing(File::getName, String.CASE_INSENSITIVE_ORDER));

                Messenger.sendMessage(sender, "lingo_file_folder_contents " + ChatColor.YELLOW + iconFolder + directoryPath, MessageType.NORMAL);
                for (File file : fileList) {
                    if (file.isDirectory()) {
                    	Messenger.sendShowFolder(sender, directoryPath, file.getName(), iconFolder, MessageType.IMPORTANT, langs);
                    } else {
                    	Messenger.sendMessageSimple(sender, file.getName(), iconFile, MessageType.ESPECIALLY);
                    }
                }
            } else {
            	sender.sendMessage(pluginName + StringUtils.translateKyeWorld("lingo_err_folder_is_empty", langs, true));
            }
        } else {
        	sender.sendMessage(pluginName + StringUtils.translateKyeWorld("lingo_err_folder_is_notexist ", langs, true));
        }
    }
}
