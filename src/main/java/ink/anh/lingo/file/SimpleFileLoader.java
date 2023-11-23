package ink.anh.lingo.file;

import java.io.IOException;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import ink.anh.lingo.AnhyLingo2;
import ink.anh.lingo.messages.MessageType;

public class SimpleFileLoader extends AbstractFileManager {

    public SimpleFileLoader(AnhyLingo2 plugin) {
        super(plugin);
    }

    @Override
    public void processingFile(CommandSender sender, String urlString, String directoryPath, boolean overwriteExisting) {
        Bukkit.getScheduler().runTaskAsynchronously(itemLingoPlugin, () -> {
            try {
                if (!isPathAllowed(directoryPath, false)) {
                    sendMessage(sender, "lingo_err_uploading_not_allowed " + directoryPath, MessageType.ERROR);
                    return;
                }

                URL fileUrl = new URL(urlString);
                // Вказуємо шлях до папки plugins
                File dir = new File(itemLingoPlugin.getServer().getWorldContainer(), "plugins" + File.separator + directoryPath);
                if (!dir.exists() && !dir.mkdirs()) {
                    sendMessage(sender, "lingo_err_failed_create_folder " + dir.getPath(), MessageType.ERROR);
                    return;
                }

                String fileName = extractFileName(fileUrl);
                File destinationFile = new File(dir, fileName);

                if (!destinationFile.exists()) {
                    saveFileFromUrl(fileUrl, destinationFile);
                    sendMessage(sender, "lingo_file_uploaded_successfully " + destinationFile.getPath(), MessageType.NORMAL);
                } else if (overwriteExisting) {
                    saveFileFromUrl(fileUrl, destinationFile);
                    sendMessage(sender, "lingo_file_updated_successfully " + destinationFile.getPath(), MessageType.NORMAL);
                } else {
                    sendMessage(sender, "lingo_err_file_already_exists " + destinationFile.getPath(), MessageType.NORMAL);
                }
            } catch (MalformedURLException e) {
                sendMessage(sender, "lingo_err_error_in_URL " + e.getMessage(), MessageType.CRITICAL_ERROR);
            } catch (IOException e) {
            	sendMessage(sender, "lingo_err_error_loading_file " + e.getMessage(), MessageType.CRITICAL_ERROR);
            }
        });
    }

}
