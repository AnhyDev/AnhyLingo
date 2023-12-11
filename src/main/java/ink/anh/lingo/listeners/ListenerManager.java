package ink.anh.lingo.listeners;

import ink.anh.lingo.AnhyLingo;

/**
 * Manages the activation and registration of event listeners for the AnhyLingo plugin.
 * This class is responsible for initializing and setting up various listeners used by the plugin.
 */
public class ListenerManager {


    /**
     * Constructor for ListenerManager.
     * Initializes and activates all necessary listeners for the AnhyLingo plugin.
     *
     * @param lingoPlugin The instance of AnhyLingo plugin.
     */
	public ListenerManager(AnhyLingo lingoPlugin) {
		this.ativateAllListeners(lingoPlugin);
	}

    /**
     * Activates all listeners required by the AnhyLingo plugin.
     * Registers each listener with the server's plugin manager.
     *
     * @param lingoPlugin The instance of AnhyLingo plugin.
     */
	private void ativateAllListeners(AnhyLingo lingoPlugin) {
		
		lingoPlugin.getServer().getPluginManager().registerEvents(new InventoryLocalizationListener(lingoPlugin), lingoPlugin);
		
	}
}
