package ink.anh.lingo.lang;

import ink.anh.lingo.ItemLingo;

public class LanguageSystemChat extends LanguageManager {

    private static LanguageSystemChat instance = null;
    private static final Object LOCK = new Object();
    private static final String CHAT_TYPE = "system";

    private LanguageSystemChat(ItemLingo itemLingoPlugin) {
        super(itemLingoPlugin, CHAT_TYPE);
    }

    public static LanguageSystemChat getInstance(ItemLingo itemLingoPlugin) {
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
