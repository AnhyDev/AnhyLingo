package ink.anh.lingo;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.bukkit.configuration.file.YamlConfiguration;

import ink.anh.lingo.utils.StringUtils;
import net.md_5.bungee.api.ChatColor;

public class ConfigurationManager {

    private ItemLingo itemLingoPlugin;
    private File configFile;
    
    private String defaultLang = "en";
    private String pluginName = "ItemLingo";
    private boolean debug;
    private boolean debugPacketShat;
    private List<String> allowedDirectories;
    private List<String> allowedDirectoriesForDeletion;

    ConfigurationManager(ItemLingo plugin) {
        this.itemLingoPlugin = plugin;
        this.configFile = new File(itemLingoPlugin.getDataFolder(), "config.yml");
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
                ItemLingo.warn("Default configuration created.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean reload() {
        try {
            this.itemLingoPlugin.reloadConfig();
            setDataConfig();
            itemLingoPlugin.getLanguageSystemChat().reloadLanguages();
            itemLingoPlugin.getLanguageItemStack().reloadLanguages();
            itemLingoPlugin.getLanguageChat().reloadLanguages();
            ItemLingo.info(StringUtils.translateKyeWorld("lingo_configuration_reloaded", defaultLang, true));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            ItemLingo.error(StringUtils.translateKyeWorld("lingo_err_reloading_configuration", defaultLang, true));
            return false;
        }
    }
    
    private void setDataConfig() {
        defaultLang = itemLingoPlugin.getConfig().getString("language", "en");
        pluginName = ChatColor.translateAlternateColorCodes('&',itemLingoPlugin.getConfig().getString("plugin_name", "ItemLingo"));
        debug = itemLingoPlugin.getConfig().getBoolean("debug", false);
        debugPacketShat = itemLingoPlugin.getConfig().getBoolean("debug_packet_chat", false);
        allowedDirectories = itemLingoPlugin.getConfig().getStringList("allowed_directories");
        allowedDirectoriesForDeletion = itemLingoPlugin.getConfig().getStringList("allowed_del_directories");
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
