package ink.anh.lingo.lang;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import ink.anh.lingo.ItemLingo;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractLanguage<T> {

    protected ItemLingo itemLingoPlugin;

    public static final String[] DEFAULT_LANGS = {"en", "ua", "ru"}; // всі мови за замовчуванням
    protected Map<String, Map<String, T>> data = new HashMap<>();
    private String directory;

    public AbstractLanguage(ItemLingo itemLingoPlugin, String directory) {
        this.itemLingoPlugin = itemLingoPlugin;
        this.directory = directory;
        saveDefaultLang();
        loadLanguages();
    }
    
    protected abstract Map<String, T> extractData(FileConfiguration langConfig);

    protected String getDirectory() {
        return directory;
    }

    private void saveDefaultLang() {
        File dir = new File(itemLingoPlugin.getDataFolder() + File.separator + getDirectory());
        if (!dir.exists()) dir.mkdirs();

        String prefix = "lingo_";
        for (String lang : DEFAULT_LANGS) {
            String filename = prefix + lang + ".yml";
            File file = new File(dir, filename);

            // Перевіряємо, чи існує файл у JAR-пакеті
            if (!file.exists() && itemLingoPlugin.getResource(getDirectory() + "/" + filename) != null) {
                itemLingoPlugin.saveResource(getDirectory() + "/" + filename, false);
            } else if (!file.exists()) {
                // Логування чи інше повідомлення про те, що файл у пакеті не знайдено
            	ItemLingo.error("The resource could not be found: " + filename);
            }
        }
    }

    private void loadLanguages() {
        File dir = new File(itemLingoPlugin.getDataFolder() + File.separator + getDirectory());

        if (dir.listFiles() != null && dir.listFiles().length > 0 && itemLingoPlugin.getConfigurationManager().isDebug()) {
            ItemLingo.info("List of files:");

            for (File file : dir.listFiles()) {
                ItemLingo.info(file.getName());
            }
        }

        String prefixPattern = ".*_";
        String regex = prefixPattern + "[a-zA-Z]{2}\\.yml";
        FilenameFilter filter = (dir1, name) -> name.matches(regex);

        File[] files = dir.listFiles(filter);

        if (files == null || files.length == 0) {
            ItemLingo.error("No language files found in directory: " + dir);
            return;
        }

        for (File file : files) {
            String fileName = file.getName();
            String lang = fileName.substring(fileName.lastIndexOf('_') + 1, fileName.lastIndexOf('.'));

            FileConfiguration langConfig = YamlConfiguration.loadConfiguration(file);
            Map<String, T> extractedData = extractData(langConfig); // Припустимо, що extractData повертає мапу, де ключ - це ключ об'єкта

            for (Map.Entry<String, T> entry : extractedData.entrySet()) {
                String key = entry.getKey();
                T value = entry.getValue();
                
                data.computeIfAbsent(key, k -> new HashMap<>()).put(lang, value);
            }
        }
    }
    
    public void reloadLanguages() {
        data.clear();
        loadLanguages();
    }
    
    public Map<String, Map<String, T>> getDataMap() {
        return data;
    }

    public T getData(String key, String lang) {
        Map<String, T> dataMap = data.get(key);
        if (dataMap == null) {
            return null;
        }

        T dataValue = dataMap.get(lang);
        if (dataValue != null) {
            return dataValue;
        }

        String defaultLanguage = itemLingoPlugin.getConfigurationManager().getDefaultLang();
        if (!lang.equals(defaultLanguage)) {
            dataValue = dataMap.get(defaultLanguage);
            if (dataValue != null) {
                return dataValue;
            }
        }

        if (!defaultLanguage.equals("en") && !lang.equals("en")) {
            dataValue = dataMap.get("en");
            if (dataValue != null) {
                return dataValue;
            }
        }

        for (String availableLang : dataMap.keySet()) {
            if (!availableLang.equals(lang) && !availableLang.equals(defaultLanguage)) {
                return dataMap.get(availableLang);
            }
        }

        return null;
    }

    public boolean dataContainsKey(String key, String lang) {
        return getData(key, lang) != null;
    }
}
