package ink.anh.lingo.api.lang;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import ink.anh.lingo.AnhyLingo;
import ink.anh.lingo.messages.Logger;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public abstract class AbstractLanguage<T> {

    protected Plugin plugin;

    protected Map<String, Map<String, T>> data = new HashMap<>();
    private String directory;

    public AbstractLanguage(Plugin plugin, String directory) {
        this.plugin = plugin;
        this.directory = directory;
        saveDefaultLang();
        loadLanguages();
    }
    
    public abstract Map<String, T> extractData(FileConfiguration langConfig, String lang);
    public abstract Map<String, T> extractData(FileConfiguration langConfig);

    public String getDirectory() {
        return directory;
    }

    private void saveDefaultLang() {
        File dir = new File(plugin.getDataFolder() + File.separator + getDirectory());
        if (!dir.exists()) dir.mkdirs();
        String[] files;
        try {
            files = getResourceFiles(getDirectory());
            for (String filename : files) {
                // Видаляємо початковий слеш, якщо він є
                String resourcePath = filename.startsWith("/") ? filename.substring(1) : filename;
                plugin.saveResource(resourcePath, false);
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void loadLanguages() {
        File dir = new File(plugin.getDataFolder() + File.separator + getDirectory());

        String prefixPattern = ".*_";
        String regex = prefixPattern + "[a-zA-Z]{2}\\.yml";
        FilenameFilter filter = (dir1, name) -> name.matches(regex);

        File[] files = dir.listFiles(filter);

        if (files == null || files.length == 0) {
        	Logger.info(plugin, "No language files found in directory: " + dir);
            return;
        }

        for (File file : files) {
            String fileName = file.getName();
            String lang = fileName.substring(fileName.lastIndexOf('_') + 1, fileName.lastIndexOf('.'));

            FileConfiguration langConfig = YamlConfiguration.loadConfiguration(file);
            Map<String, T> extractedData = extractData(langConfig, lang); // Припустимо, що extractData повертає мапу, де ключ - це ключ об'єкта

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
    
    public T getData(String key, String[] langs) {
        Map<String, T> dataMap = data.get(key);
        if (dataMap == null) {
            return null;
        }

        String defaultLanguage = AnhyLingo.getInstance().getConfigurationManager().getDefaultLang();
        boolean defaultLangChecked = false;
        boolean englishLangChecked = false;

        for (String lang : langs) {
            if (lang.equals(defaultLanguage)) {
                defaultLangChecked = true;
            }
            if (lang.equals("en")) {
                englishLangChecked = true;
            }

            T dataValue = dataMap.get(lang);
            if (dataValue != null) {
                return dataValue;
            }
        }

        if (!defaultLangChecked) {
            T dataValue = dataMap.get(defaultLanguage);
            if (dataValue != null) {
                return dataValue;
            }
        }

        if (!englishLangChecked && !defaultLanguage.equals("en")) {
            T dataValue = dataMap.get("en");
            if (dataValue != null) {
                return dataValue;
            }
        }

        for (String availableLang : dataMap.keySet()) {
            if (!Arrays.asList(langs).contains(availableLang) && !availableLang.equals(defaultLanguage) && !availableLang.equals("en")) {
                return dataMap.get(availableLang);
            }
        }

        return null;
    }

    public T getTranslate(String key, String lang) {
        Map<String, T> dataMap = data.get(key);
        return dataMap.get(lang);
    }

    public boolean dataContainsKey(String key, String[] langs) {
        return getData(key, langs) != null;
    }

    public String[] getResourceFiles(String folderPath) throws IOException, URISyntaxException {
        URL jarUrl = getClass().getProtectionDomain().getCodeSource().getLocation();
        List<String> filenames = new ArrayList<>();

        // Додаємо "jar:" до початку URI і перевіряємо, що шлях починається з '/'
        URI jarUri = new URI("jar:" + jarUrl.toURI().toString());

        try (FileSystem fileSystem = FileSystems.newFileSystem(jarUri, Collections.emptyMap())) {
            Path path = fileSystem.getPath(folderPath.startsWith("/") ? folderPath : "/" + folderPath);

            Files.walk(path)
                 .filter(Files::isRegularFile)
                 .forEach(filePath -> filenames.add(filePath.toString()));  // змінено з getFileName() на toString() для повного шляху
        }
        
        return filenames.toArray(new String[0]);
    }
}
