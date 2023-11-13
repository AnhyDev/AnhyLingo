package ink.anh.lingo.lang;

import org.bukkit.configuration.file.FileConfiguration;
import ink.anh.lingo.ItemLingo;
import java.util.HashMap;
import java.util.Map;

public class LanguageChat extends AbstractLanguage<String> {

    private static LanguageChat instance = null;

    private static final Object LOCK = new Object();

    private LanguageChat(ItemLingo itemLingoPlugin) {
        super(itemLingoPlugin);
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

    @Override
    protected String getDirectory() {
        return "chat";
    }

    @Override
    protected Map<String, String> extractData(FileConfiguration langConfig) {
        Map<String, String> langMap = new HashMap<>();
        for (String key : langConfig.getKeys(false)) {
            String value = langConfig.getString(key);
            langMap.put(key, value);
        }
        return langMap;
    }

    public Map<String, String> getLanguageMap(String lang) {
        Map<String, String> langMap = data.get(lang);
        if (langMap != null) {
            return langMap;
        } else {
            String defaultLanguage = itemLingoPlugin.getConfigurationManager().getDefaultLang();
            langMap = data.get(defaultLanguage);
            if (langMap != null) {
                return langMap;
            } else {
                return data.get("en");
            }
        }
    }

    public String getChatData(String lang, String key) {
        Map<String, String> langMap = getLanguageMap(lang);
        if (langMap == null) {
            return null;
        }
        return langMap.getOrDefault(key, null);
    }

    public boolean chatDataContainsKey(String lang, String key) {
        return getChatData(lang, key) != null;
    }
}
