package ink.anh.lingo.file;

import org.bukkit.command.CommandSender;

import ink.anh.api.messages.MessageType;
import ink.anh.lingo.AnhyLingo;
import ink.anh.lingo.Permissions;
import ink.anh.lingo.command.AnswerToCommand;

/**
 * Processes file-related commands for the AnhyLingo plugin.
 * This class serves as a handler for commands that involve file operations like loading and deleting.
 */
public class FileCommandProcessor {
    
    private AnhyLingo lingoPlugin;

    /**
     * Constructor for FileCommandProcessor.
     *
     * @param plugin The instance of AnhyLingo plugin.
     */
    public FileCommandProcessor(AnhyLingo plugin) {
        this.lingoPlugin = plugin;
    }

    /**
     * Processes file-related commands based on the specified FileProcessType.
     *
     * @param sender The command sender.
     * @param args Arguments of the command.
     * @param fileProcessType The type of file processing to perform.
     * @return true if the command was processed successfully, otherwise false.
     */
    public boolean processFile(CommandSender sender, String[] args, FileProcessType fileProcessType) {

    	if (!isCommandAlloved(args[0])) {
            sendMessage(sender, "lingo_err_not_alloved_config ", MessageType.WARNING);
    		return true;
    	}
    	
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
    	AnswerToCommand.sendMessage(lingoPlugin.getGlobalManager(), sender, message, type, true);
    }

    private AbstractFileManager getFileManagerForType(FileProcessType fileProcessType) {
        switch (fileProcessType) {
            case YAML_LOADER:
                return new YamlFileLoader(lingoPlugin);
            case SIMPLE_LOADER:
                return new SimpleFileLoader(lingoPlugin);
            case FILE_DELETER:
                return new SimpleFileDeleter(lingoPlugin);
            default:
                return null;
        }
    }

    private boolean isCommandAlloved(String arg0) {
    	switch (arg0.toLowerCase()) {
        case "fl":
            return lingoPlugin.getGlobalManager().isAllowUpload();
        case "fo":
            return lingoPlugin.getGlobalManager().isAllowUpload();
        case "fd":
            return lingoPlugin.getGlobalManager().isAllowRemoval();
        }
		return false;
    }
}
