package ink.anh.lingo.lang;

import ink.anh.lingo.AnhyLingo;
import ink.anh.lingo.api.lang.LanguageManager;

public class LanguageSystemChat extends LanguageManager {

    private static LanguageSystemChat instance = null;
    private static final Object LOCK = new Object();
    private static final String DIRECTORY = "system";

    private LanguageSystemChat(AnhyLingo lingoPlugin) {
        super(lingoPlugin, DIRECTORY);
    }

    public static LanguageSystemChat getInstance(AnhyLingo lingoPlugin) {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = new LanguageSystemChat(lingoPlugin);
                }
            }
        }
        return instance;
    }
}
