package ink.anh.lingo;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigurationManager {

    private ItemLingo itemLingoPlugin;
    private File configFile;
    
    private String defaultLang;
    private boolean debug;
    private boolean debugPacketShat;
    private List<String> allowedDirectories; // Змінна для зберігання списку дозволених директорій

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
            defaultConfig.set("debug", false);
            defaultConfig.set("debug_packet_chat", false);
            defaultConfig.set("allowed_directories", List.of("Denizen/scripts/", "ItemLingo/items/", "ItemLingo/system/", "ItemLingo/chat/"));
            try {
                defaultConfig.save(configFile);
                itemLingoPlugin.getLogger().info("Created default configuration.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean reload() {
        try {
            this.itemLingoPlugin.reloadConfig();
            setDataConfig();
            this.itemLingoPlugin.getLogger().info("Configuration reloaded.");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            this.itemLingoPlugin.getLogger().info("Error reloading configuration...");
            return false;
        }
    }
    
    private void setDataConfig() {
        defaultLang = itemLingoPlugin.getConfig().getString("language", "en");
        debug = itemLingoPlugin.getConfig().getBoolean("debug", false);
        debugPacketShat = itemLingoPlugin.getConfig().getBoolean("debug_packet_chat", false);
        allowedDirectories = itemLingoPlugin.getConfig().getStringList("allowed_directories");
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
        return allowedDirectories; // Геттер для отримання списку дозволених директорій
    }

    public boolean isPathAllowed(String path) {
        for (String allowedPath : allowedDirectories) {
            if (path.contains(allowedPath)) {
                return true;
            }
        }
        return false;
    }
}
