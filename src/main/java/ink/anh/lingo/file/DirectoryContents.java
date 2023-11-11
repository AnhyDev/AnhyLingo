package ink.anh.lingo.file;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ink.anh.lingo.ItemLingo;
import ink.anh.lingo.utils.LangUtils;
import ink.anh.lingo.utils.StringUtils;

public class DirectoryContents {

    public static void listDirectoryContents(CommandSender sender, String directoryPath) {
    	if (directoryPath.equals("0")) directoryPath = "";
    	String lang = (sender instanceof Player) ? LangUtils.getPlayerLanguage((Player) sender) : null;
    	String pluginName = ItemLingo.getInstance().getConfigurationManager().getPluginName() + ": ";

        File directory = new File(ItemLingo.getInstance().getServer().getWorldContainer(), "plugins" + File.separator + directoryPath);

        if (directory.exists() && directory.isDirectory()) {
            File[] fileList = directory.listFiles();

            if (fileList != null) {
                // Сортування файлів та папок
                Arrays.sort(fileList, Comparator.comparing(File::isFile)
                                                .thenComparing(File::getName, String.CASE_INSENSITIVE_ORDER));

                for (File file : fileList) {
                    if (file.isDirectory()) {
                    	sender.sendMessage("📁 " + file.getName());
                    } else {
                    	sender.sendMessage("📄 " + file.getName());
                    }
                }
            } else {
            	sender.sendMessage(pluginName + StringUtils.translateKyeWorld("lingo_err_folder_is_empty", lang, true));
            }
        } else {
        	sender.sendMessage(pluginName + StringUtils.translateKyeWorld("lingo_err_folder_is_notexist", lang, true));
        }
    }
}
