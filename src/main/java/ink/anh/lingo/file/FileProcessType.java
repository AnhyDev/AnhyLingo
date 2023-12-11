package ink.anh.lingo.file;

/**
 * Enum representing different types of file processing operations in the AnhyLingo plugin.
 * This enum is used to distinguish between various file operations like loading and deleting.
 */
public enum FileProcessType {
    /**
     * Represents a file operation that loads YAML files.
     */
    YAML_LOADER,

    /**
     * Represents a file operation that loads files in a simple format (not specifically YAML).
     */
    SIMPLE_LOADER,

    /**
     * Represents a file operation for deleting files.
     */
    FILE_DELETER
}
