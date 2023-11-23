package ink.anh.lingo;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import ink.anh.lingo.command.LingoCommand;
import ink.anh.lingo.lang.LanguageChat;
import ink.anh.lingo.lang.LanguageItemStack;
import ink.anh.lingo.lang.LanguageSystemChat;
import ink.anh.lingo.listeners.ListenerManager;
import ink.anh.lingo.listeners.protocol.PacketListenerManager;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;

public class AnhyLingo extends JavaPlugin {

    private static AnhyLingo instance;
	
    private boolean isSpigot;
    private boolean isPaper;
    private boolean isFolia;
    private boolean hasPaperComponent;
    
    private LanguageSystemChat languageSystemChat;
    private LanguageItemStack languageItemStack;
    private LanguageChat languageChat;
    private ConfigurationManager configurationManager;
    private static BukkitAudiences bukkitAudiences;

    @Override
    public void onLoad() {
    	instance = this;
    }

    @Override
    public void onEnable() {
    	checkDepends("ProtocolLib");
    	checkServer();
        
        configurationManager = new ConfigurationManager(this);
        languageSystemChat = LanguageSystemChat.getInstance(this);
        languageItemStack = LanguageItemStack.getInstance(this);
        languageChat = LanguageChat.getInstance(this);
        new PacketListenerManager().addListeners();
        new ListenerManager(this);
        this.getCommand("lingo").setExecutor(new LingoCommand(this));
        bukkitAudiences = BukkitAudiences.create(this);
    }

    @Override
    public void onDisable() {

    }
    
    public static boolean isVersionOrNewer(int minorVersionToCheck) {
        String version = Bukkit.getBukkitVersion();
        String[] splitVersion = version.split("-")[0].split("\\.");

        int major = Integer.parseInt(splitVersion[0]);
        int minor = 0;

        if (splitVersion.length > 1) {
            minor = Integer.parseInt(splitVersion[1]);
        }

        return major == 1 && minor >= minorVersionToCheck;
    }

    private void checkServer() {
    	try {
    		Class.forName("org.bukkit.entity.Player$Spigot");
            isSpigot = true;
        } catch (Throwable tr) {
            isSpigot = false;
            error("Console-Sender.Messages.Initialize.Require-Spigot");
            return;
        }
    	try {
            Class.forName("com.destroystokyo.paper.VersionHistoryManager$VersionData");
            isPaper = true;
            try {
            	Class.forName("io.papermc.paper.text.PaperComponents");
            	hasPaperComponent = true;
            } catch (Throwable tr) {
            	hasPaperComponent = false;
            }
    	} catch (Throwable tr) {
            isPaper = false;
    	}
    	try {
    		Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
    		isFolia = true;
    	} catch (ClassNotFoundException e) {
    		isFolia = false;
    	}
    }

    private boolean checkDepends(String... depends) {
        boolean missingDepend = false;
        PluginManager pluginManager = Bukkit.getPluginManager();
        for (String depend : depends) {
            if (pluginManager.getPlugin(depend) == null) {
            	error("Console-Sender.Messages.Initialize.Missing-Dependency " + depend);
                missingDepend = true;
            }
        }
        if (missingDepend) {
            pluginManager.disablePlugin(instance);
        }
        return missingDepend;
    }

    public static void info(String message) {
    	instance.getLogger().info("\u001b[0;94m" + message + "\u001b[m");
    }

    public static void warn(String message) {
    	instance.getLogger().warning("\u001b[0;93m" + message + "\u001b[m");
    }

    public static void error(String message) {
    	instance.getLogger().severe("\u001b[0;91m" + message + "\u001b[m");
    }

    public static AnhyLingo getInstance() {
    	return instance;
    }

    public LanguageItemStack getLanguageItemStack() {
    	return languageItemStack;
    }

    public LanguageChat getLanguageChat() {
    	return languageChat;
    }

    public ConfigurationManager getConfigurationManager() {
    	return configurationManager;
    }

    public void setConfigurationManager(ConfigurationManager configurationManager) {
    	this.configurationManager = configurationManager;
    }

    public boolean isSpigot() {
    	return isSpigot;
    }

    public boolean isPaper() {
    	return isPaper;
    }

    public boolean isFolia() {
    	return isFolia;
    }

    public boolean hasPaperComponent() {
    	return hasPaperComponent;
    }

    public LanguageSystemChat getLanguageSystemChat() {
    	return languageSystemChat;
    }

	public static BukkitAudiences getBukkitAudiences() {
		return bukkitAudiences;
	}
}
