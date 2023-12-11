package ink.anh.lingo.file;

import java.io.IOException;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import ink.anh.api.messages.MessageType;
import ink.anh.lingo.AnhyLingo;

/**
 * Handles the loading of files from a URL into the server's file system as part of the AnhyLingo plugin.
 * This class extends AbstractFileManager and provides specific implementation for downloading and saving files.
 */
public class SimpleFileLoader extends AbstractFileManager {

    /**
     * Constructor for SimpleFileLoader.
     *
     * @param plugin The instance of AnhyLingo plugin.
     */
    public SimpleFileLoader(AnhyLingo plugin) {
        super(plugin);
    }

    /**
     * Processes the file loading command.
     * Downloads a file from the specified URL and saves it to the given directory path on the server.
     * If the file already exists, it will be overwritten if 'overwriteExisting' is true.
     *
     * @param sender The command sender who initiated the file loading.
     * @param urlString The URL of the file to download.
     * @param directoryPath The path to the directory where the file will be saved.
     * @param overwriteExisting Determines whether to overwrite an existing file.
     */
    @Override
    public void processingFile(CommandSender sender, String urlString, String directoryPath, boolean overwriteExisting) {
        Bukkit.getScheduler().runTaskAsynchronously(lingoPlugin, () -> {
            try {
                if (!isPathAllowed(directoryPath, false)) {
                    sendMessage(sender, "lingo_err_uploading_not_allowed " + directoryPath, MessageType.ERROR);
                    return;
                }

                URL fileUrl = new URL(urlString);
                // Вказуємо шлях до папки plugins
                File dir = new File(lingoPlugin.getServer().getWorldContainer(), "plugins" + File.separator + directoryPath);
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
