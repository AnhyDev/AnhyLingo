package ink.anh.lingo.file;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import ink.anh.lingo.AnhyLingo;
import ink.anh.lingo.messages.MessageType;

import java.io.File;

public class SimpleFileDeleter extends AbstractFileManager {

    public SimpleFileDeleter(AnhyLingo plugin) {
        super(plugin);
    }

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
