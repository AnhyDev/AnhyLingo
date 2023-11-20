package ink.anh.lingo.file;

import org.bukkit.command.CommandSender;
import ink.anh.lingo.ItemLingo;
import ink.anh.lingo.Permissions;
import ink.anh.lingo.messages.MessageType;
import ink.anh.lingo.messages.Messenger;

public class FileCommandProcessor {
    
    private ItemLingo itemLingoPlugin;

    public FileCommandProcessor(ItemLingo plugin) {
        this.itemLingoPlugin = plugin;
    }

    public boolean processFile(CommandSender sender, String[] args, FileProcessType fileProcessType) {
        String permission = getPermissionForFileType(fileProcessType);
        if (!sender.hasPermission(permission)) {
            sendMessage(sender, "lingo_err_not_have_permission: ", MessageType.WARNING);
            return true;
        }

        if (args.length >= 3) {
            String url = args[1];
            String directoryPath = args[2];
            boolean isReplace = args.length >= 4 && Boolean.parseBoolean(args[3]);

            AbstractFileManager fileLoader = getFileManagerForType(fileProcessType);
            fileLoader.processingFile(sender, url, directoryPath, isReplace);
            sendMessage(sender, "lingo_file_operation_initiated " + directoryPath, MessageType.WARNING);
        } else {
        	switch (args[0].toLowerCase()) {
            case "fl":
                sendMessage(sender, "lingo_err_command_format /lingo fl <url> <folder> [is_replaced]", MessageType.WARNING);
                return true;
            case "fo":
                sendMessage(sender, "lingo_err_command_format /lingo fo <url> <path> [is_replaced]", MessageType.WARNING);
                return true;
            case "fd":
                sendMessage(sender, "lingo_err_command_format /lingo fd <path> <file_name>", MessageType.WARNING);
                return true;
            }
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

    private void sendMessage(CommandSender sender, String message, MessageType type) {
    	Messenger.sendMessage(sender, message, type);
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
