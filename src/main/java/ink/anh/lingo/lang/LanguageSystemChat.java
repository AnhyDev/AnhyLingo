package ink.anh.lingo.lang;

import ink.anh.api.lingo.lang.LanguageManager;
import ink.anh.lingo.GlobalManager;

/**
 * Manages the system chat language for the AnhyLingo plugin.
 * This class is responsible for handling language settings specifically for system-related messages.
 * It extends LanguageManager and provides functionality tailored to system chat language management.
 */
public class LanguageSystemChat extends LanguageManager {

    private static LanguageSystemChat instance = null;
    private static final Object LOCK = new Object();
    private static final String DIRECTORY = "system";

    /**
     * Private constructor for LanguageSystemChat.
     * Ensures that LanguageSystemChat can only be instantiated internally.
     *
     * @param libraryManager The LibraryManager instance, typically the main plugin instance.
     */
    private LanguageSystemChat(GlobalManager manager) {
        super(manager, DIRECTORY);
    }

    /**
     * Provides a singleton instance of LanguageSystemChat.
     * Ensures that only one instance of LanguageSystemChat is created and used throughout the plugin.
     *
     * @param manager The GlobalManager instance.
     * @return The singleton instance of LanguageSystemChat.
     */
    public static LanguageSystemChat getInstance(GlobalManager manager) {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = new LanguageSystemChat(manager);
                }
            }
        }
        return instance;
    }
}
