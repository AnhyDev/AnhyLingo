package ink.anh.lingo.lang;

import ink.anh.lingo.ItemLingo;

public class LanguageChat extends LanguageManager {

    private static LanguageChat instance = null;
    private static final Object LOCK = new Object();
    private static final String DIRECTORY = "chat";

    private LanguageChat(ItemLingo itemLingoPlugin) {
        super(itemLingoPlugin, DIRECTORY);
    }

    public static LanguageChat getInstance(ItemLingo itemLingoPlugin) {
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