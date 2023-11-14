package ink.anh.lingo.file;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import ink.anh.lingo.ItemLingo;

import java.io.File;

public class SimpleFileDeleter extends AbstractFileManager {

    public SimpleFileDeleter(ItemLingo plugin) {
        super(plugin);
    }

    @Override
	public void processingFile(CommandSender sender, String directoryPath, String fileName,
			boolean overwriteExisting) {
        Bukkit.getScheduler().runTaskAsynchronously(itemLingoPlugin, () -> {
            try {
                if (!isPathAllowed(directoryPath, true)) {
                    sender.sendMessage("lingo_err_not_allowed_to_delete " + directoryPath);
                    return;
                }

                // Вказуємо шлях до папки plugins
                File dir = new File(itemLingoPlugin.getServer().getWorldContainer(), "plugins" + File.separator + directoryPath);

                if (!dir.exists()) {
                    sender.sendMessage("lingo_err_folder_does_not_exist " + dir.getPath());
                    return;
                }

                File fileToDelete = new File(dir, fileName);

                if (fileToDelete.exists()) {
                    if (fileToDelete.delete()) {
                        sender.sendMessage("lingo_file_deleted_successfully " + fileToDelete.getPath());
                    } else {
                        sender.sendMessage("lingo_err_not_allowed_delete_from_this_folder " + fileToDelete.getPath());
                    }
                } else {
                    sender.sendMessage("lingo_err_file_not_found " + fileToDelete.getPath());
                }
            } catch (Exception e) {
                sender.sendMessage("lingo_err_error_deleting_file " + e.getMessage());
            }
        });
    }
}
