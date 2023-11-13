package ink.anh.lingo.lang;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import ink.anh.lingo.ItemLingo;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractLanguage<T> {

    protected ItemLingo itemLingoPlugin;

    public static final String[] DEFAULT_LANGS = {"en", "ua", "ru"}; // всі мови за замовчуванням
    protected Map<String, Map<String, T>> data = new HashMap<>();
    protected String[] langs;

    public AbstractLanguage(ItemLingo itemLingoPlugin) {
        this.itemLingoPlugin = itemLingoPlugin;
        saveDefaultLang();
        langs = retrieveAvailableLangs();
        loadLanguages();
    }

    protected abstract String getDirectory();
    protected abstract Map<String, T> extractData(FileConfiguration langConfig);

    private String[] retrieveAvailableLangs() {
    	// Визначаємо регулярний вираз заздалегідь
        String langFileRegex = ".*_[a-zA-Z]{2}\\.yml";
        File dir = new File(itemLingoPlugin.getDataFolder() + File.separator + getDirectory());
        FilenameFilter filter = (dir1, name) -> {
        	return name.matches(langFileRegex);
        };
        File[] files = dir.listFiles(filter);

        return files == null ? new String[]{} : Arrays.stream(files)
                .map(file -> file.getName().replaceFirst(".*_(?=[a-zA-Z]{2}\\.yml)", ""))
                .toArray(String[]::new);
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

            // Пройдіться по кожному файлу в масиві і виведіть їх імена
            for (File file : dir.listFiles()) {
            	ItemLingo.info(file.getName());
            }
        }
        String prefixPattern = ".*_";
        String regex = prefixPattern + "[a-zA-Z]{2}\\.yml";
        FilenameFilter filter = (dir1, name) -> name.matches(regex);

        File[] files = dir.listFiles(filter);

        if (files == null || files.length == 0) {
            // Логування, якщо не знайдено файлів
        	ItemLingo.error("No language files found in directory: " + dir);
            return;
        }

        for (File file : files) {
            String fileName = file.getName();
            String lang = fileName.substring(fileName.lastIndexOf('_') + 1, fileName.lastIndexOf('.'));

            FileConfiguration langConfig = YamlConfiguration.loadConfiguration(file);
            Map<String, T> newData = extractData(langConfig);

            Map<String, T> existingData = data.get(lang);
            if (existingData == null) {
                data.put(lang, newData);
            } else {
                existingData.putAll(newData);
            }

            // Додавання запису до логу про завантаження файлу
            ItemLingo.info("Loaded language file: " + dir + "/" + fileName);
        }
    }
    
    public void reloadLanguages() {
        data.clear();
        langs = retrieveAvailableLangs();
        loadLanguages();
    }
}
