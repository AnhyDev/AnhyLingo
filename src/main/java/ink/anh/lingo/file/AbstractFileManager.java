package ink.anh.lingo.file;

import java.net.URL;
import org.bukkit.command.CommandSender;
import ink.anh.lingo.ItemLingo;

public abstract class AbstractFileManager {

    protected ItemLingo itemLingoPlugin;

    public AbstractFileManager(ItemLingo plugin) {
        this.itemLingoPlugin = plugin;
    }

    public abstract void processingFile(CommandSender sender, String urlString, String directoryPath, boolean overwriteExisting);

    protected String extractFileName(URL url) {
        String path = url.getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }

    public boolean isPathAllowed(String path, boolean del) {
        if (path.contains(".") || path.contains(":") || path.contains("%2e") 
                || path.contains("%3a") || path.startsWith("/")) {
                return false;
            }

        if (del) {
            return itemLingoPlugin.getConfigurationManager().isPathDeleteAllowed(path);
        } else {
            return itemLingoPlugin.getConfigurationManager().isPathAllowed(path);
        }
    }

}
