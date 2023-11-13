package ink.anh.lingo.file;

import org.bukkit.command.CommandSender;
import ink.anh.lingo.ItemLingo;
import ink.anh.lingo.Permissions;

public class FileCommandProcessor {
    
    private ItemLingo itemLingoPlugin;

    public FileCommandProcessor(ItemLingo plugin) {
        this.itemLingoPlugin = plugin;
    }

    public boolean processFile(CommandSender sender, String[] args, FileProcessType fileProcessType) {
        String permission = getPermissionForFileType(fileProcessType);
        if (!sender.hasPermission(permission)) {
            sender.sendMessage("You do not have permission to perform this action.");
            return true;
        }

        if (args.length >= 3) {
            String url = args[1];
            String directoryPath = args[2];
            boolean isReplace = args.length >= 4 && Boolean.parseBoolean(args[3]);

            AbstractFileManager fileLoader = getFileManagerForType(fileProcessType);
            fileLoader.processingFile(sender, url, directoryPath, isReplace);
            sender.sendMessage("File operation initiated for directory " + directoryPath);
        } else {
            sender.sendMessage("Usage: /lingo fl/fo/fd <url> <path> [is_replaced]");
        }
        return true;
    }

    private String getPermissionForFileType(FileProcessType fileProcessType) {
        switch (fileProcessType) {
            case YAML_LOADER:
                return Permissions.FILE_LINGO;
            case SIMPLE_LOADER:
                return Permissions.FILE_OTHER;
            case FILE_DELETER:
                return Permissions.FILE_DELETE;
            default:
                return "";
        }
    }

    private AbstractFileManager getFileManagerForType(FileProcessType fileProcessType) {
        switch (fileProcessType) {
            case YAML_LOADER:
                return new YamlFileLoader(itemLingoPlugin);
            case SIMPLE_LOADER:
                return new SimpleFileLoader(itemLingoPlugin);
            case FILE_DELETER:
                return new SimpleFileDeleter(itemLingoPlugin);
            default:
                return null;
        }
    }

}
