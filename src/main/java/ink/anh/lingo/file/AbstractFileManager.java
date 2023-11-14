package ink.anh.lingo.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

}
