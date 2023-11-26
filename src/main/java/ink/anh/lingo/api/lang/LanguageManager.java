package ink.anh.lingo.api.lang;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public abstract class LanguageManager extends AbstractLanguage<String> {
	

	public LanguageManager(Plugin plugin, String directory) {
        super(plugin, directory);
    }

	@Override
	public Map<String, String> extractData(FileConfiguration langConfig) {
        Map<String, String> langMap = new HashMap<>();
        for (String key : langConfig.getKeys(false)) {
            String value = langConfig.getString(key);
            
            langMap.put(key, value);
        }
        return langMap;
    }

	@Override
	public Map<String, String> extractData(FileConfiguration langConfig, String lang) {
		return extractData(langConfig);
	}
}
