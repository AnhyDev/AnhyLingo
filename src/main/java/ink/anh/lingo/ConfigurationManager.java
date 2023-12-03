package ink.anh.lingo;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
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
    private boolean itemLingo;
    private boolean packetLingo;
    private boolean allowBrowsing;
    private boolean allowUpload;
    private List<String> allowedDirectories;
    private boolean allowRemoval;
    private List<String> allowedDirectoriesForDeletion;

    ConfigurationManager(AnhyLingo plugin) {
        this.lingoPlugin = plugin;
        this.configFile = new File(lingoPlugin.getDataFolder(), "config.yml");
        saveDefaultConfiguration();
        setDataConfig();
    }

    void saveDefaultConfiguration() {
        if (!configFile.exists()) {
        	lingoPlugin.getConfig().options().copyDefaults(true);
        	lingoPlugin.saveDefaultConfig();
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
        pluginName = ChatColor.translateAlternateColorCodes('&',lingoPlugin.getConfig().getString("plugin_name", "AnhyLingo"));
        debug = lingoPlugin.getConfig().getBoolean("debug", false);
        allowBrowsing = lingoPlugin.getConfig().getBoolean("allow_browsing", false);
        itemLingo = lingoPlugin.getConfig().getBoolean("item_lingo", false);
        packetLingo = lingoPlugin.getConfig().getBoolean("packet_lingo", false);
        allowUpload = lingoPlugin.getConfig().getBoolean("allow_upload", false);
        debugPacketShat = lingoPlugin.getConfig().getBoolean("debug_packet_chat", false);
        allowedDirectories = lingoPlugin.getConfig().getStringList("allowed_directories");
        allowRemoval = lingoPlugin.getConfig().getBoolean("allow_removal", false);
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

	public boolean isItemLingo() {
		return itemLingo;
	}

	public boolean isPacketLingo() {
		return packetLingo;
	}

	public boolean isAllowBrowsing() {
		return allowBrowsing;
	}

	public boolean isAllowUpload() {
		return allowUpload;
	}

	public boolean isAllowRemoval() {
		return allowRemoval;
	}
	
    public static List<String> logo() {
        List<String> asciiArt = new ArrayList<>();

        asciiArt.add("");
        asciiArt.add(" ░█████╗░███╗░░██╗██╗░░██╗██╗░░░██╗██╗░░░░░██╗███╗░░██╗░██████╗░░█████╗░");
        asciiArt.add(" ██╔══██╗████╗░██║██║░░██║╚██╗░██╔╝██║░░░░░██║████╗░██║██╔════╝░██╔══██╗");
        asciiArt.add(" ███████║██╔██╗██║███████║░╚████╔╝░██║░░░░░██║██╔██╗██║██║░░██╗░██║░░██║");
        asciiArt.add(" ██╔══██║██║╚████║██╔══██║░░╚██╔╝░░██║░░░░░██║██║╚████║██║░░╚██╗██║░░██║");
        asciiArt.add(" ██║░░██║██║░╚███║██║░░██║░░░██║░░░███████╗██║██║░╚███║╚██████╔╝╚█████╔╝");
        asciiArt.add(" ╚═╝░░╚═╝╚═╝░░╚══╝╚═╝░░╚═╝░░░╚═╝░░░╚══════╝╚═╝╚═╝░░╚══╝░╚═════╝░░╚════╝░");
        asciiArt.add("");

        return asciiArt;
    }
}
