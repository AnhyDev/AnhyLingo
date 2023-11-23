package ink.anh.lingo.lang;

import ink.anh.lingo.AnhyLingo;

public class LanguageChat extends LanguageManager {

    private static LanguageChat instance = null;
    private static final Object LOCK = new Object();
    private static final String DIRECTORY = "chat";

    private LanguageChat(AnhyLingo itemLingoPlugin) {
        super(itemLingoPlugin, DIRECTORY);
    }

    public static LanguageChat getInstance(AnhyLingo itemLingoPlugin) {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = new LanguageChat(itemLingoPlugin);
                }
            }
        }
        return instance;
    }
}