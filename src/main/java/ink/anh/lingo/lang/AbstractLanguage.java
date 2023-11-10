package ink.anh.lingo.lang;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import ink.anh.lingo.ItemLingo;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

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


    protected abstract String getPrefix();
    protected abstract String getDirectory();
    protected abstract Map<String, T> extractData(FileConfiguration langConfig);

    private String[] retrieveAvailableLangs() {
    	// Визначаємо регулярний вираз заздалегідь
        String langFileRegex = "_[a-zA-Z]{2}\\.yml";
        File dir = new File(itemLingoPlugin.getDataFolder() + File.separator + getDirectory());
        FilenameFilter filter = (dir1, name) -> {
            // Якщо префікс не заданий, фільтруємо файли за шаблоном "_XX.yml", де XX - будь-які дві латинські букви
            if (getPrefix() == null || getPrefix().isEmpty()) {
                return name.matches(".*" + langFileRegex);
            } else {
                // Якщо префікс заданий, фільтруємо файли, що містять цей префікс, розділений з мовою через "_"
                return name.matches(Pattern.quote(getPrefix()) + langFileRegex);
            }
        };
        File[] files = dir.listFiles(filter);

        return files == null ? new String[]{} : Arrays.stream(files)
                .map(file -> file.getName().replaceFirst(".*_(?=[a-zA-Z]{2}\\.yml)", ""))
                .toArray(String[]::new);
    }

    private void saveDefaultLang() {
        File dir = new File(itemLingoPlugin.getDataFolder() + File.separator + getDirectory());
        if (!dir.exists()) dir.mkdirs();

        String prefix = (getPrefix() != null && !getPrefix().isEmpty()) ? getPrefix() : "default_";
        for (String lang : DEFAULT_LANGS) {
            String filename = prefix + lang + ".yml";
            File file = new File(dir, filename);

            // Перевіряємо, чи існує файл у JAR-пакеті
            if (!file.exists() && itemLingoPlugin.getResource(getDirectory() + "/" + filename) != null) {
                itemLingoPlugin.saveResource(getDirectory() + "/" + filename, false);
            } else if (!file.exists()) {
                // Логування чи інше повідомлення про те, що файл у пакеті не знайдено
            	ItemLingo.error("Не вдалося знайти ресурс: " + filename);
            }
        }
    }

    private void loadLanguages() {
        File dir = new File(itemLingoPlugin.getDataFolder() + File.separator + getDirectory());
        File[] files = dir.listFiles((dir1, name) -> name.endsWith(".yml"));

        if (files != null) {
            for (File file : files) {
                String fileName = file.getName();
                // Визначаємо мовний код, видаляючи префікс та ".yml" (наприклад, для "authme_en.yml" мовний код буде "en").
                String lang = fileName.substring(fileName.lastIndexOf('_') + 1, fileName.lastIndexOf('.'));

                FileConfiguration langConfig = YamlConfiguration.loadConfiguration(file);
                
                // Витягуємо дані для конкретної мови.
                Map<String, T> newData = extractData(langConfig);
                
                // Перевіряємо, чи вже існує такий мовний код в мапі.
                Map<String, T> existingData = data.get(lang);
                if (existingData == null) {
                    // Якщо немає, то просто додаємо нові дані.
                    data.put(lang, newData);
                } else {
                    // Якщо так, то об'єднуємо існуючі дані з новими.
                    existingData.putAll(newData);
                }
            }
        }
    }

    public void reloadLanguages() {
        data.clear();
        langs = retrieveAvailableLangs();
        loadLanguages();
    }
}
