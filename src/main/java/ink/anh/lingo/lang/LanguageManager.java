package ink.anh.lingo.lang;

import org.bukkit.configuration.file.FileConfiguration;
import ink.anh.lingo.ItemLingo;
import java.util.HashMap;
import java.util.Map;

public abstract class LanguageManager extends AbstractLanguage<String> {
	

    public LanguageManager(ItemLingo itemLingoPlugin, String directory) {
        super(itemLingoPlugin, directory);
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
}
