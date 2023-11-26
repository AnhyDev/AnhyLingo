package ink.anh.lingo.lang;

import ink.anh.lingo.AnhyLingo;
import ink.anh.lingo.api.lang.LanguageManager;

public class LanguageChat extends LanguageManager {

    private static LanguageChat instance = null;
    private static final Object LOCK = new Object();
    private static final String DIRECTORY = "chat";

    private LanguageChat(AnhyLingo lingoPlugin) {
        super(lingoPlugin, DIRECTORY);
    }

    public static LanguageChat getInstance(AnhyLingo lingoPlugin) {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = new LanguageChat(lingoPlugin);
                }
            }
        }
        return instance;
    }
}