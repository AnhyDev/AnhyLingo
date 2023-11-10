package ink.anh.lingo.file;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import org.bukkit.command.CommandSender;

import ink.anh.lingo.ItemLingo;

public class DirectoryContents {

    public static void listDirectoryContents(CommandSender sender, String directoryPath) {
        if (!isPathAllowed(directoryPath)) {
            sender.sendMessage("Не дозволено працювати з цією папкою: " + directoryPath);
            return;
        }

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
            	sender.sendMessage("Папка порожня або сталася помилка при її читанні.");
            }
        } else {
        	sender.sendMessage("Папка не існує або це не папка.");
        }
    }

    private static boolean isPathAllowed(String path) {
    	return ItemLingo.getInstance().getConfigurationManager().isPathAllowed(path);
    }
}
