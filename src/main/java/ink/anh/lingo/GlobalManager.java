package ink.anh.lingo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import ink.anh.api.LibraryManager;
import ink.anh.api.lingo.Translator;
import ink.anh.api.lingo.lang.LanguageManager;
import ink.anh.api.messages.Logger;
import ink.anh.lingo.item.LanguageItemStack;
import ink.anh.lingo.lang.LanguageSystemChat;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.md_5.bungee.api.ChatColor;


/**
 * Manages global settings and states for the AnhyLingo plugin.
 * This class handles configurations, language management, and plugin-wide settings.
 */
public class GlobalManager extends LibraryManager {

    private static GlobalManager instance;
	private AnhyLingo lingoPlugin;
    
    private String pluginName;
    private String defaultLang;
    private static BukkitAudiences bukkitAudiences;
	
	private LanguageManager langManager;
    private LanguageItemStack languageItemStack;
    
    private boolean debug;
    private boolean debugPacketShat;

    private boolean allowBrowsing;
    private boolean itemLingo;
    private boolean packetLingo;
    private boolean allowUpload;
    private boolean allowRemoval;

    private List<String> allowedDirectories;
    private List<String> allowedDirectoriesForDeletion;
    
    
  	/**
     * Private constructor for singleton pattern.
     * Initializes the plugin and loads configuration.
     *
     * @param lingoPlugin The instance of AnhyLingo plugin.
     */
	private GlobalManager(AnhyLingo lingoPlugin) {
		super(lingoPlugin);
		this.lingoPlugin = lingoPlugin;
		this.saveDefaultConfig();
		this.loadFields(lingoPlugin);
	}

    /**
     * Provides a synchronized method to get or create an instance of GlobalManager.
     * Ensures that only one instance of this class is created (singleton pattern).
     *
     * @param lingoPlugin The instance of AnhyLingo plugin.
     * @return The singleton instance of GlobalManager.
     */
    public static synchronized GlobalManager getManager(AnhyLingo lingoPlugin) {
        if (instance == null) {
            instance = new GlobalManager(lingoPlugin);
        }
        return instance;
    }
    
    /**
     * Gets the plugin instance associated with this GlobalManager.
     * This method is part of the implementation of the LibraryManager interface.
     *
     * @return The instance of the AnhyLingo plugin.
     */
    @Override
    public Plugin getPlugin() {
        return lingoPlugin;
    }

    /**
     * Gets the name of the plugin.
     * This method is part of the implementation of the LibraryManager interface.
     * The name is typically set in the plugin's configuration file and may include color codes.
     *
     * @return The name of the plugin.
     */
    @Override
    public String getPluginName() {
        return pluginName;
    }

    /**
     * Gets the BukkitAudiences instance for the plugin.
     * BukkitAudiences is part of the Adventure library and is used for sending rich text messages.
     * This method is part of the implementation of the LibraryManager interface.
     *
     * @return The BukkitAudiences instance associated with this plugin.
     */
    @Override
    public BukkitAudiences getBukkitAudiences() {
        return bukkitAudiences;
    }

    /**
     * Gets the LanguageManager instance for this plugin.
     * LanguageManager is responsible for handling language-specific functionalities.
     * This method is part of the implementation of the LibraryManager interface.
     *
     * @return The LanguageManager instance.
     */
    @Override
    public LanguageManager getLanguageManager() {
        return this.langManager;
    }

    /**
     * Gets the default language setting for the plugin.
     * This method is part of the implementation of the LibraryManager interface.
     * The default language is typically set in the plugin's configuration file.
     *
     * @return The default language code, e.g., "en" for English.
     */
    @Override
    public String getDefaultLang() {
        return defaultLang;
    }

    /**
     * Checks if the plugin is in debug mode.
     * When in debug mode, additional information might be logged for troubleshooting purposes.
     * This method is part of the implementation of the LibraryManager interface.
     *
     * @return true if the plugin is in debug mode, false otherwise.
     */
    @Override
    public boolean isDebug() {
        return debug;
    }

    /**
     * Gets the LanguageItemStack instance.
     *
     * @return The instance of LanguageItemStack.
     */
    public LanguageItemStack getLanguageItemStack() {
    	return languageItemStack;
    }

    /**
     * Checks if browsing is allowed within the plugin.
     * This setting determines whether users can browse through certain directories or files.
     *
     * @return true if browsing is allowed, false otherwise.
     */
    public boolean isAllowBrowsing() {
        return allowBrowsing;
    }

    /**
     * Checks if item lingo (language translation for items) is enabled.
     * When enabled, item names and descriptions will be translated based on the player's language setting.
     *
     * @return true if item lingo is enabled, false otherwise.
     */
    public boolean isItemLingo() {
        return itemLingo;
    }

    /**
     * Checks if packet lingo (language translation for packets) is enabled.
     * When enabled, certain packet-based texts (like chat) will be translated based on the player's language setting.
     *
     * @return true if packet lingo is enabled, false otherwise.
     */
    public boolean isPacketLingo() {
        return packetLingo;
    }

    /**
     * Checks if uploading files through the plugin is allowed.
     * This setting allows users to upload certain types of files to the server.
     *
     * @return true if file uploading is allowed, false otherwise.
     */
    public boolean isAllowUpload() {
        return allowUpload;
    }

    /**
     * Checks if removal of files or directories through the plugin is allowed.
     * This setting allows users to delete certain files or directories from the server.
     *
     * @return true if file or directory removal is allowed, false otherwise.
     */
    public boolean isAllowRemoval() {
        return allowRemoval;
    }

    /**
     * Checks if a given path is allowed based on the configured directories.
     *
     * @param path The path to check.
     * @return true if the path is within the allowed directories.
     */
    public boolean isPathAllowed(String path) {
        for (String allowedPath : allowedDirectories) {
            if (path.contains(allowedPath)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the list of allowed directories.
     *
     * @return A list of strings representing allowed directories.
     */
    public List<String> getAllowedDirectories() {
        return allowedDirectories;
    }

    /**
     * Checks if a path is allowed for deletion.
     *
     * @param path The path to check.
     * @return true if the path is within the allowed directories for deletion.
     */
    public boolean isPathDeleteAllowed(String path) {
        for (String allowedPath : allowedDirectoriesForDeletion) {
            if (path.contains(allowedPath)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the list of allowed directories for deletion.
     *
     * @return A list of strings representing allowed directories for deletion.
     */
	public List<String> getAllowedDirectoriesForDeletion() {
		return allowedDirectoriesForDeletion;
	}
    
    /**
     * Loads plugin configuration fields from the config file.
     * Initializes the language and item stack managers.
     *
     * @param lingoPlugin The instance of AnhyLingo plugin.
     */
    private void loadFields(AnhyLingo lingoPlugin) {
        bukkitAudiences = BukkitAudiences.create(lingoPlugin);
        defaultLang = lingoPlugin.getConfig().getString("language", "en");
        pluginName = ChatColor.translateAlternateColorCodes('&',lingoPlugin.getConfig().getString("plugin_name", "AnhyLingo"));
        debug = lingoPlugin.getConfig().getBoolean("debug", false);
        setLanguageManagers();

        allowBrowsing = lingoPlugin.getConfig().getBoolean("allow_browsing", false);
        itemLingo = lingoPlugin.getConfig().getBoolean("item_lingo", false);
        packetLingo = lingoPlugin.getConfig().getBoolean("packet_lingo", false);
        allowUpload = lingoPlugin.getConfig().getBoolean("allow_upload", false);
        debugPacketShat = lingoPlugin.getConfig().getBoolean("debug_packet_chat", false);
        allowedDirectories = lingoPlugin.getConfig().getStringList("allowed_directories");
        allowRemoval = lingoPlugin.getConfig().getBoolean("allow_removal", false);
        allowedDirectoriesForDeletion = lingoPlugin.getConfig().getStringList("allowed_del_directories");
    
    }

    /**
     * Ensures the default configuration file is saved.
     */
    private void saveDefaultConfig() {
    	File configFile = new File(lingoPlugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
        	lingoPlugin.getConfig().options().copyDefaults(true);
        	lingoPlugin.saveDefaultConfig();
        }
    }

    /**
     * Sets or reloads language managers.
     */
    private void setLanguageManagers() {
    	
        if (this.langManager == null) {
            this.langManager = LanguageSystemChat.getInstance(this);;
        } else {
        	this.langManager.reloadLanguages();
        }
        
        if (this.languageItemStack == null) {
            this.languageItemStack = LanguageItemStack.getInstance(this);;
        } else {
        	this.languageItemStack.reloadLanguages();
        }
    }

    /**
     * Reloads the plugin configuration and updates internal fields accordingly.
     *
     * @return true if the reload is successful.
     */
	public boolean reload() {
		Bukkit.getScheduler().runTaskAsynchronously(lingoPlugin, () -> {
	        try {
	        	saveDefaultConfig();
	            lingoPlugin.reloadConfig();
	            loadFields(lingoPlugin);
	            Logger.info(lingoPlugin, Translator.translateKyeWorld(instance, "lingo_configuration_reloaded" , new String[] {defaultLang}));
	        } catch (Exception e) {
	            e.printStackTrace();
	            Logger.error(lingoPlugin, Translator.translateKyeWorld(instance, "lingo_err_reloading_configuration ", new String[] {defaultLang}));
	        }
		});
        return true;
    }

    /**
     * Provides a logo for the plugin in ASCII art form.
     *
     * @return A list of strings representing the ASCII art logo.
     */
    public static List<String> logo() {
        List<String> asciiArt = new ArrayList<>();

        // ASCII art logo creation logic
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

    /**
     * Checks if packet chat debugging is enabled.
     *
     * @return true if debug for packet chat is enabled.
     */
    public boolean isDebugPacketShat() {
        return debugPacketShat;
    }
}
