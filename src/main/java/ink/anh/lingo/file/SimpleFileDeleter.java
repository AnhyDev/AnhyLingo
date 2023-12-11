package ink.anh.lingo.file;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import ink.anh.api.messages.MessageType;
import ink.anh.lingo.AnhyLingo;

import java.io.File;

/**
 * Handles the deletion of files as part of the AnhyLingo plugin's file management functionality.
 * This class extends AbstractFileManager and provides specific implementation for deleting files.
 */
public class SimpleFileDeleter extends AbstractFileManager {

    /**
     * Constructor for SimpleFileDeleter.
     *
     * @param plugin The instance of AnhyLingo plugin.
     */
    public SimpleFileDeleter(AnhyLingo plugin) {
        super(plugin);
    }

    /**
     * Processes the file deletion command.
     * Deletes a specified file within a given directory if allowed and if the file exists.
     *
     * @param sender The command sender who initiated the file deletion.
     * @param directoryPath The path to the directory containing the file.
     * @param fileName The name of the file to delete.
     * @param overwriteExisting This parameter is not used in file deletion.
     */
    @Override
	public void processingFile(CommandSender sender, String directoryPath, String fileName,
			boolean overwriteExisting) {
        Bukkit.getScheduler().runTaskAsynchronously(lingoPlugin, () -> {
            try {
                if (!isPathAllowed(directoryPath, true)) {
                    sendMessage(sender, "lingo_err_not_allowed_to_delete " + directoryPath, MessageType.ERROR);
                    return;
                }

                // Вказуємо шлях до папки plugins
                File dir = new File(lingoPlugin.getServer().getWorldContainer(), "plugins" + File.separator + directoryPath);

                if (!dir.exists()) {
                    sendMessage(sender, "lingo_err_folder_does_not_exist " + dir.getPath(), MessageType.ERROR);
                    return;
                }

                File fileToDelete = new File(dir, fileName);

                if (fileToDelete.exists()) {
                    if (fileToDelete.delete()) {
                        sendMessage(sender, "lingo_file_deleted_successfully " + fileToDelete.getPath(), MessageType.NORMAL);
                    } else {
                        sendMessage(sender, "lingo_err_not_allowed_delete_from_this_folder " + fileToDelete.getPath(), MessageType.ERROR);
                    }
                } else {
                    sendMessage(sender, "lingo_err_file_not_found " + fileToDelete.getPath(), MessageType.ERROR);
                }
            } catch (Exception e) {
                sendMessage(sender, "lingo_err_error_deleting_file " + e.getMessage(), MessageType.ERROR);
            }
        });
    }
}
