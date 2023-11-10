package ink.anh.lingo.file;

import java.net.URL;
import org.bukkit.command.CommandSender;
import ink.anh.lingo.ItemLingo;

public abstract class AbstractFileLoader {

    protected ItemLingo itemLingoPlugin;

    public AbstractFileLoader(ItemLingo plugin) {
        this.itemLingoPlugin = plugin;
    }

    public abstract void loadFile(CommandSender sender, String urlString, String directoryPath, boolean overwriteExisting);

    protected String extractFileName(URL url) {
        String path = url.getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }

    public boolean isPathAllowed(String path) {
    	return itemLingoPlugin.getConfigurationManager().isPathAllowed(path);
    }
}
