package ink.anh.lingo.lang;

import ink.anh.lingo.AnhyLingo;

public class LanguageSystemChat extends LanguageManager {

    private static LanguageSystemChat instance = null;
    private static final Object LOCK = new Object();
    private static final String DIRECTORY = "system";

    private LanguageSystemChat(AnhyLingo itemLingoPlugin) {
        super(itemLingoPlugin, DIRECTORY);
    }

    public static LanguageSystemChat getInstance(AnhyLingo itemLingoPlugin) {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = new LanguageSystemChat(itemLingoPlugin);
                }
            }
        }
        return instance;
    }
}
