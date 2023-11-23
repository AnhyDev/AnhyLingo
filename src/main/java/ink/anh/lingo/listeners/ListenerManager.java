package ink.anh.lingo.listeners;

import ink.anh.lingo.AnhyLingo;

public class ListenerManager {

	
	public ListenerManager(AnhyLingo lingoPlugin) {
		this.ativateAllListeners(lingoPlugin);
	}

	private void ativateAllListeners(AnhyLingo lingoPlugin) {
		
		lingoPlugin.getServer().getPluginManager().registerEvents(new InventoryLocalizationListener(lingoPlugin), lingoPlugin);
		
	}
}
