package ink.anh.lingo.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.bukkit.command.CommandSender;

import ink.anh.api.messages.MessageType;
import ink.anh.api.messages.Messenger;
import ink.anh.lingo.AnhyLingo;

/**
 * Abstract base class for file management within the AnhyLingo plugin.
 * This class provides common functionalities for handling files, such as saving and validating paths.
 */
public abstract class AbstractFileManager {

    protected AnhyLingo lingoPlugin;

    /**
     * Constructor for AbstractFileManager.
     *
     * @param plugin The instance of AnhyLingo plugin.
     */
    public AbstractFileManager(AnhyLingo plugin) {
        this.lingoPlugin = plugin;
    }

    /**
     * Abstract method to process a file. 
     * Implementations should define how to handle the file based on the provided parameters.
     *
     * @param sender The CommandSender who initiated the file processing.
     * @param urlString The URL of the file to be processed.
     * @param directoryPath The directory path where the file should be processed.
     * @param overwriteExisting Whether to overwrite the existing file.
     */
    public abstract void processingFile(CommandSender sender, String urlString, String directoryPath, boolean overwriteExisting);

    /**
     * Extracts the file name from a given URL.
     *
     * @param url The URL from which the file name is to be extracted.
     * @return The extracted file name.
     */
    protected String extractFileName(URL url) {
        String path = url.getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }

    /**
     * Checks if the given path is allowed, optionally considering if it's for deletion.
     *
     * @param path The path to be validated.
     * @param del Boolean flag indicating if the validation is for deletion.
     * @return true if the path is allowed, false otherwise.
     */
    public boolean isPathAllowed(String path, boolean del) {
        if (path.contains(".") || path.contains(":") || path.contains("%2e") 
                || path.contains("%3a") || path.startsWith("/")) {
                return false;
            }

        if (del) {
            return lingoPlugin.getGlobalManager().isPathDeleteAllowed(path);
        } else {
            return lingoPlugin.getGlobalManager().isPathAllowed(path);
        }
    }

    /**
     * Saves a file from a given URL to the specified file location.
     *
     * @param url The URL from which the file will be downloaded.
     * @param file The file to which the content will be saved.
     * @throws IOException if an I/O error occurs.
     */
    protected void saveFileFromUrl(URL url, File file) throws IOException {
        try (InputStream in = url.openStream();
             FileOutputStream out = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        }
    }

    /**
     * Sends a message to a CommandSender.
     *
     * @param sender The CommandSender to whom the message will be sent.
     * @param message The message to send.
     * @param type The type of message being sent.
     */
    public void sendMessage(CommandSender sender, String message, MessageType type) {
    	Messenger.sendMessage(lingoPlugin.getGlobalManager(), sender, message, type);
    }
}
