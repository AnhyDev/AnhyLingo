package ink.anh.lingo.file;


import java.io.InputStreamReader;
import java.io.IOException;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import ink.anh.lingo.ItemLingo;
import ink.anh.lingo.lang.ItemLang;

public class YamlFileLoader extends AbstractFileManager {
    
    public YamlFileLoader(ItemLingo plugin) {
    	super(plugin);
	}

    @Override
    public void processingFile(CommandSender sender, String urlString, String directory, boolean overwriteExisting) {
        Bukkit.getScheduler().runTaskAsynchronously(itemLingoPlugin, () -> {
            try {
                URL fileUrl = new URL(urlString);
                File dir = new File(itemLingoPlugin.getDataFolder(), directory);
                if (!dir.exists()) {
                    sender.sendMessage("lingo_err_folder_does_not_exist " + dir.getPath());
                    return; // Вихід з методу, якщо папка не існує
                }

                String fileName = extractFileName(fileUrl); // Отримайте назву файлу з URL
                File destinationFile = new File(dir, fileName);

                // Перевіряємо, чи потрібно оновлювати або створювати файл
                if (!destinationFile.exists()) {
                    saveFileFromUrl(fileUrl, destinationFile);
                    sender.sendMessage("lingo_file_uploaded_successfully " + destinationFile.getPath());
                } else if (overwriteExisting && needsUpdate(sender, destinationFile, fileUrl, directory)) {
                    saveFileFromUrl(fileUrl, destinationFile);
                    sender.sendMessage("lingo_file_updated_successfully " + destinationFile.getPath());
                } else {
                    sender.sendMessage("lingo_err_file_already_exists " + destinationFile.getPath());
                }
            } catch (MalformedURLException e) {
                sender.sendMessage("lingo_err_error_in_URL " + e.getMessage());
            } catch (IOException e) {
            	sender.sendMessage("lingo_err_error_loading_file " + e.getMessage());
            }
        });
    }

    private boolean needsUpdate(CommandSender sender, File file, URL url, String directory) {
        String langFileRegex = "*._[a-zA-Z]{2}\\.yml";
        
        // Перевірте, чи назва файлу відповідає шаблону
        if (!file.getName().matches(langFileRegex)) {
            sender.sendMessage("Назва файлу не відповідає шаблону: " + file.getName());
            return false;
        }

        try (InputStreamReader reader = new InputStreamReader(url.openStream())) {
            YamlConfiguration yamlConfig = YamlConfiguration.loadConfiguration(reader);
            
            // Перевіряємо вміст файлу
            if (!parseAndCheckYaml(sender, yamlConfig, directory)) {
                // Якщо YAML не пройшов перевірку, файл не валідний
                return false;
            }

            // Якщо YAML розпаршено без винятків, і назва файлу відповідає шаблону, файл є валідним
            return true;
        } catch (Exception e) {
            // Якщо під час розбору виникли помилки, файл не валідний
            sender.sendMessage("Помилка при розборі YAML: " + e.getMessage());
            return false;
        }
    }

    private boolean parseAndCheckYaml(CommandSender sender, YamlConfiguration yamlConfig, String directory) {
        if ("system".equals(directory) || "chat".equals(directory)) {
            // Перевіряємо, чи всі ключі у файлі конфігурації є рядками
            for (String key : yamlConfig.getKeys(true)) {
                if (!(yamlConfig.get(key) instanceof String)) {
                    sender.sendMessage("Ключ '" + key + "' у директорії '" + directory + "' не є рядком.");
                    return false;
                }
            }
        } else if ("items".equals(directory)) {
            // Перевіряємо, чи можна дані конвертувати в об'єкти ItemLang
            ConfigurationSection itemsSection = yamlConfig.getConfigurationSection("items");
            if (itemsSection != null) {
                for (String key : itemsSection.getKeys(false)) {
                    if (!itemsSection.isConfigurationSection(key)) {
                        continue; // Якщо не секція, пропускаємо
                    }

                    ConfigurationSection section = itemsSection.getConfigurationSection(key);
                    if (section.contains("copy")) {
                        continue; // Пропускаємо ключі з під-ключем "copy"
                    }

                    try {
                        String name = section.getString("name");
                        List<String> lore = section.getStringList("lore");

                        // Спроба створення ItemLang для перевірки
                        new ItemLang(name, lore.toArray(new String[0]));
                    } catch (Exception e) {
                        sender.sendMessage("Не вдалося створити ItemLang для ключа '" + key + "': " + e.getMessage());
                        return false;
                    }
                }
            } else {
                sender.sendMessage("Розділ 'items' відсутній у файлі.");
                return false;
            }
        } else {
            sender.sendMessage("Невідомий тип директорії: " + directory);
            return false;
        }
        return true;
    }

}
