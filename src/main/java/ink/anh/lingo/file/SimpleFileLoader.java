package ink.anh.lingo.file;

import java.io.IOException;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import ink.anh.lingo.ItemLingo;

public class SimpleFileLoader extends AbstractFileManager {

    public SimpleFileLoader(ItemLingo plugin) {
        super(plugin);
    }

    @Override
    public void processingFile(CommandSender sender, String urlString, String directoryPath, boolean overwriteExisting) {
        Bukkit.getScheduler().runTaskAsynchronously(itemLingoPlugin, () -> {
            try {
                if (!isPathAllowed(directoryPath, false)) {
                    sender.sendMessage("lingo_err_uploading_not_allowed " + directoryPath);
                    return;
                }

                URL fileUrl = new URL(urlString);
                // Вказуємо шлях до папки plugins
                File dir = new File(itemLingoPlugin.getServer().getWorldContainer(), "plugins" + File.separator + directoryPath);
                if (!dir.exists() && !dir.mkdirs()) {
                    sender.sendMessage("lingo_err_failed_create_folder " + dir.getPath());
                    return;
                }

                String fileName = extractFileName(fileUrl);
                File destinationFile = new File(dir, fileName);

                if (!destinationFile.exists()) {
                    saveFileFromUrl(fileUrl, destinationFile);
                    sender.sendMessage("lingo_file_uploaded_successfully " + destinationFile.getPath());
                } else if (overwriteExisting) {
                    saveFileFromUrl(fileUrl, destinationFile);
                    sender.sendMessage("lingo_file_updated_successfully " + destinationFile.getPath());
                } else {
                    sender.sendMessage("lingo_err_file_already_exists " + destinationFile.getPath());
                }
            } catch (MalformedURLException e) {
                sender.sendMessage("lingo_err_error_in_URL " + e.getMessage());
            } catch (IOException e) {
            	sender.sendMessage("lingo_err_error_loading_file " + e.getMessage());
            }
        });
    }

}
