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

public class GlobalManager implements LibraryManager {

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
    
	
	private GlobalManager(AnhyLingo lingoPlugin) {
		this.lingoPlugin = lingoPlugin;
		this.saveDefaultConfig();
		this.loadFields(lingoPlugin);
	}

    public static synchronized GlobalManager getManager(AnhyLingo lingoPlugin) {
        if (instance == null) {
            instance = new GlobalManager(lingoPlugin);
        }
        return instance;
    }
    
	@Override
	public Plugin getPlugin() {
		return lingoPlugin;
	}

	@Override
	public String getPluginName() {
		return pluginName;
	}

	@Override
	public BukkitAudiences getBukkitAudiences() {
		return bukkitAudiences;
	}

	@Override
	public LanguageManager getLanguageManager() {
		return this.langManager;
	}

	@Override
	public String getDefaultLang() {
		return defaultLang;
	}

	@Override
	public boolean isDebug() {
		return debug;
	}

    public LanguageItemStack getLanguageItemStack() {
    	return languageItemStack;
    }

	public boolean isAllowBrowsing() {
		return allowBrowsing;
	}

	public boolean isItemLingo() {
		return itemLingo;
	}

	public boolean isPacketLingo() {
		return packetLingo;
	}

	public boolean isAllowUpload() {
		return allowUpload;
	}

	public boolean isAllowRemoval() {
		return allowRemoval;
	}

    public boolean isPathAllowed(String path) {
        for (String allowedPath : allowedDirectories) {
            if (path.contains(allowedPath)) {
                return true;
            }
        }
        return false;
    }

    public List<String> getAllowedDirectories() {
        return allowedDirectories;
    }

    public boolean isPathDeleteAllowed(String path) {
        for (String allowedPath : allowedDirectoriesForDeletion) {
            if (path.contains(allowedPath)) {
                return true;
            }
        }
        return false;
    }

	public List<String> getAllowedDirectoriesForDeletion() {
		return allowedDirectoriesForDeletion;
	}
    
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

    private void saveDefaultConfig() {
    	File configFile = new File(lingoPlugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
        	lingoPlugin.getConfig().options().copyDefaults(true);
        	lingoPlugin.saveDefaultConfig();
        }
    }

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

    public boolean isDebugPacketShat() {
        return debugPacketShat;
    }
}
