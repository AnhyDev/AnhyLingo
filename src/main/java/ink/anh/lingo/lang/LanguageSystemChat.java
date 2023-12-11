package ink.anh.lingo.lang;

import ink.anh.api.LibraryManager;
import ink.anh.api.lingo.lang.LanguageManager;

public class LanguageSystemChat extends LanguageManager {

    private static LanguageSystemChat instance = null;
    private static final Object LOCK = new Object();
    private static final String DIRECTORY = "system";

    private LanguageSystemChat(LibraryManager libraryManager) {
        super(libraryManager, DIRECTORY);
    }

    public static LanguageSystemChat getInstance(LibraryManager libraryManager) {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = new LanguageSystemChat(libraryManager);
                }
            }
        }
        return instance;
    }
}
