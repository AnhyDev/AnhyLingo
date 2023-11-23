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

import ink.anh.lingo.AnhyLingo;
import ink.anh.lingo.lang.ItemLang;
import ink.anh.lingo.messages.MessageType;

public class YamlFileLoader extends AbstractFileManager {
    
    public YamlFileLoader(AnhyLingo plugin) {
    	super(plugin);
	}

    @Override
    public void processingFile(CommandSender sender, String urlString, String directory, boolean overwriteExisting) {
        Bukkit.getScheduler().runTaskAsynchronously(itemLingoPlugin, () -> {
            try {
                URL fileUrl = new URL(urlString);
                File dir = new File(itemLingoPlugin.getDataFolder(), directory);
                if (!dir.exists()) {
                    sendMessage(sender, "lingo_err_folder_does_not_exist ./" + dir.getPath(), MessageType.ERROR);
                    return; // Вихід з методу, якщо папка не існує
                }

                String fileName = extractFileName(fileUrl); // Отримайте назву файлу з URL
                File destinationFile = new File(dir, fileName);

                // Перевіряємо, чи потрібно оновлювати або створювати файл
                if (!destinationFile.exists()) {
                    saveFileFromUrl(fileUrl, destinationFile);
                    sendMessage(sender, "lingo_file_uploaded_successfully ./" + destinationFile.getPath(), MessageType.NORMAL);
                } else if (overwriteExisting && needsUpdate(sender, destinationFile, fileUrl, directory)) {
                    saveFileFromUrl(fileUrl, destinationFile);
                    sendMessage(sender, "lingo_file_updated_successfully ./" + destinationFile.getPath(), MessageType.NORMAL);
                } else {
                    sendMessage(sender, "lingo_err_file_already_exists ./" + destinationFile.getPath(), MessageType.ERROR);
                }
            } catch (MalformedURLException e) {
                sendMessage(sender, "lingo_err_error_in_URL " + e.getMessage(), MessageType.CRITICAL_ERROR);
            } catch (IOException e) {
            	sendMessage(sender, "lingo_err_error_loading_file " + e.getMessage(), MessageType.CRITICAL_ERROR);
            }
        });
    }

    private boolean needsUpdate(CommandSender sender, File file, URL url, String directory) {
        String langFileRegex = ".*_[a-zA-Z]{2}\\.yml";
        
        // Перевірте, чи назва файлу відповідає шаблону
        if (!file.getName().matches(langFileRegex)) {
            sendMessage(sender, "lingo_err_filename_not_pattern " + file.getName(), MessageType.ERROR);
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
            sendMessage(sender, "lingo_err_error_parsing_YAML " + e.getMessage(), MessageType.CRITICAL_ERROR);
            return false;
        }
    }

    private boolean parseAndCheckYaml(CommandSender sender, YamlConfiguration yamlConfig, String directory) {
        if ("system".equals(directory) || "chat".equals(directory)) {
            // Перевіряємо, чи всі ключі у файлі конфігурації є рядками
            for (String key : yamlConfig.getKeys(true)) {
                if (!(yamlConfig.get(key) instanceof String)) {
                    sendMessage(sender, "lingo_err_key_not_string " + key + " - " + directory, MessageType.ERROR);
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
                        sendMessage(sender, "lingo_err_failed_create_ItemLang_key '" + key + "': " + e.getMessage(), MessageType.CRITICAL_ERROR);
                        return false;
                    }
                }
            } else {
                sendMessage(sender, "lingo_err_items_section_is_missing_file ", MessageType.ERROR);
                return false;
            }
        } else {
            sendMessage(sender, "lingo_err_unknown_directory_type " + directory, MessageType.ERROR);
            return false;
        }
        return true;
    }

}
