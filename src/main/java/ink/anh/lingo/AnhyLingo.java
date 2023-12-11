package ink.anh.lingo;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import ink.anh.api.messages.Logger;
import ink.anh.lingo.command.LingoCommand;
import ink.anh.lingo.listeners.ListenerManager;
import ink.anh.lingo.listeners.protocol.PacketListenerManager;

/**
 * Main class for the AnhyLingo plugin.
 * This class handles the initialization, enabling, and disabling of the plugin.
 * It also provides utility methods to check the server type and version.
 */
public class AnhyLingo extends JavaPlugin {

    private static AnhyLingo instance;
    
    private boolean isSpigot;
    private boolean isPaper;
    private boolean isFolia;
    private boolean hasPaperComponent;
    
    private GlobalManager globalManager;

    /**
     * Called when the plugin is loaded by the Bukkit system.
     * Sets the static instance for global access.
     */
    @Override
    public void onLoad() {
        instance = this;
    }

    /**
     * Called when the plugin is enabled.
     * Performs initial setup such as checking dependencies, determining server type,
     * initializing managers, and setting up commands and listeners.
     */
    @Override
    public void onEnable() {
        checkDepends("ProtocolLib");
        checkServer();
        
        globalManager = GlobalManager.getManager(this);
        new PacketListenerManager().addListeners();
        new ListenerManager(this);
        this.getCommand("lingo").setExecutor(new LingoCommand(this));
    }

    /**
     * Called when the plugin is disabled.
     * Currently, this method does not perform any specific actions.
     */
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    
    /**
     * Checks if the current server version is equal to or newer than a specified version.
     * 
     * @param minorVersionToCheck The minor version to compare against.
     * @return true if the current server version is equal or newer than the specified version.
     */
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

    /**
     * Checks the server for specific implementations like Spigot, Paper, or Folia.
     * Updates the respective boolean fields based on the server type.
     */
    private void checkServer() {
        try {
            Class.forName("org.bukkit.entity.Player$Spigot");
            isSpigot = true;
        } catch (Throwable tr) {
            isSpigot = false;
            Logger.error(this, "Console-Sender.Messages.Initialize.Require-Spigot");
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

    /**
     * Checks for the presence of specified dependencies.
     * If any dependency is missing, the plugin is disabled.
     * 
     * @param depends An array of plugin names to check for.
     * @return true if any dependency is missing.
     */
    private boolean checkDepends(String... depends) {
        boolean missingDepend = false;
        PluginManager pluginManager = Bukkit.getPluginManager();
        for (String depend : depends) {
            if (pluginManager.getPlugin(depend) == null) {
                Logger.error(this, "Console-Sender.Messages.Initialize.Missing-Dependency " + depend);
                missingDepend = true;
            }
        }
        if (missingDepend) {
            pluginManager.disablePlugin(instance);
        }
        return missingDepend;
    }

    /**
     * Gets the singleton instance of this plugin.
     * 
     * @return The singleton instance of AnhyLingo.
     */
    public static AnhyLingo getInstance() {
        return instance;
    }

    /**
     * Gets the GlobalManager instance associated with this plugin.
     * 
     * @return The GlobalManager instance.
     */
    public GlobalManager getGlobalManager() {
        return globalManager;
    }

    /**
     * Checks if the server is running Spigot.
     * 
     * @return true if the server is Spigot.
     */
    public boolean isSpigot() {
        return isSpigot;
    }

    /**
     * Checks if the server is running Paper.
     * 
     * @return true if the server is Paper.
     */
    public boolean isPaper() {
        return isPaper;
    }

    /**
     * Checks if the server is running Folia.
     * 
     * @return true if the server is Folia.
     */
    public boolean isFolia() {
        return isFolia;
    }

    /**
     * Checks if the server has the PaperComponents feature.
     * 
     * @return true if the server has PaperComponents.
     */
    public boolean hasPaperComponent() {
        return hasPaperComponent;
    }
}
