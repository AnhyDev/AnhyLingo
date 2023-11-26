package ink.anh.lingo;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.bukkit.configuration.file.YamlConfiguration;

import ink.anh.lingo.api.Translator;
import ink.anh.lingo.messages.Logger;
import net.md_5.bungee.api.ChatColor;

public class ConfigurationManager {

    private AnhyLingo lingoPlugin;
    private File configFile;
    
    private String defaultLang = "en";
    private String pluginName = "ItemLingo";
    private boolean debug;
    private boolean debugPacketShat;
    private List<String> allowedDirectories;
    private List<String> allowedDirectoriesForDeletion;

    ConfigurationManager(AnhyLingo plugin) {
        this.lingoPlugin = plugin;
        this.configFile = new File(lingoPlugin.getDataFolder(), "config.yml");
        saveDefaultConfiguration();
        setDataConfig();
    }

    void saveDefaultConfiguration() {
        if (!configFile.exists()) {
            YamlConfiguration defaultConfig = new YamlConfiguration();
            defaultConfig.set("language", "en");
            defaultConfig.set("plugin_name", "ItemLingo");
            defaultConfig.set("debug", false);
            defaultConfig.set("debug_packet_chat", false);
            defaultConfig.set("allowed_directories", List.of("Denizen/scripts", "ItemLingo/items/tmpfile"));
            defaultConfig.set("allowed_del_directories", List.of("Denizen/scripts", "ItemLingo/items/tmpfile"));
            try {
                defaultConfig.save(configFile);
                Logger.warn(lingoPlugin, "Default configuration created. ");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean reload() {
        try {
            this.lingoPlugin.reloadConfig();
            setDataConfig();
            lingoPlugin.getLanguageSystemChat().reloadLanguages();
            lingoPlugin.getLanguageItemStack().reloadLanguages();
            lingoPlugin.getLanguageChat().reloadLanguages();
            Logger.info(lingoPlugin, Translator.translateKyeWorld("lingo_configuration_reloaded" , new String[] {defaultLang}, lingoPlugin.getLanguageSystemChat()));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Logger.error(lingoPlugin, Translator.translateKyeWorld("lingo_err_reloading_configuration ", new String[] {defaultLang}, lingoPlugin.getLanguageSystemChat()));
            return false;
        }
    }
    
    private void setDataConfig() {
        defaultLang = lingoPlugin.getConfig().getString("language", "en");
        pluginName = ChatColor.translateAlternateColorCodes('&',lingoPlugin.getConfig().getString("plugin_name", "ItemLingo"));
        debug = lingoPlugin.getConfig().getBoolean("debug", false);
        debugPacketShat = lingoPlugin.getConfig().getBoolean("debug_packet_chat", false);
        allowedDirectories = lingoPlugin.getConfig().getStringList("allowed_directories");
        allowedDirectoriesForDeletion = lingoPlugin.getConfig().getStringList("allowed_del_directories");
    }

    public String getDefaultLang() {
        return defaultLang;
    }

    public boolean isDebug() {
        return debug;
    }

    public boolean isDebugPacketShat() {
        return debugPacketShat;
    }

    public List<String> getAllowedDirectories() {
        return allowedDirectories;
    }

    public boolean isPathAllowed(String path) {
        for (String allowedPath : allowedDirectories) {
            if (path.contains(allowedPath)) {
                return true;
            }
        }
        return false;
    }

    public boolean isPathDeleteAllowed(String path) {
        for (String allowedPath : allowedDirectoriesForDeletion) {
            if (path.contains(allowedPath)) {
                return true;
            }
        }
        return false;
    }

	public String getPluginName() {
		return pluginName;
	}

	public List<String> getAllowedDirectoriesForDeletion() {
		return allowedDirectoriesForDeletion;
	}
}
