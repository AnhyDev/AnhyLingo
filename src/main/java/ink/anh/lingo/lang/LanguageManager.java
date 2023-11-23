package ink.anh.lingo.lang;

import org.bukkit.configuration.file.FileConfiguration;
import ink.anh.lingo.AnhyLingo;
import java.util.HashMap;
import java.util.Map;

public abstract class LanguageManager extends AbstractLanguage<String> {
	

    public LanguageManager(AnhyLingo itemLingoPlugin, String directory) {
        super(itemLingoPlugin, directory);
    }

	@Override
    protected Map<String, String> extractData(FileConfiguration langConfig) {
        Map<String, String> langMap = new HashMap<>();
        for (String key : langConfig.getKeys(false)) {
            String value = langConfig.getString(key);
            
            if (itemLingoPlugin.getConfigurationManager().isDebug())
            	AnhyLingo.info("LanguageManager: " + key + ": " + value);
            
            langMap.put(key, value);
        }
        return langMap;
    }

	@Override
	protected Map<String, String> extractData(FileConfiguration langConfig, String lang) {
		return extractData(langConfig);
	}
}
