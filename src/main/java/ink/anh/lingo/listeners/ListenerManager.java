package ink.anh.lingo.listeners;

import ink.anh.lingo.AnhyLingo;

public class ListenerManager {

	
	public ListenerManager(AnhyLingo itemLingoPlugin) {
		this.ativateAllListeners(itemLingoPlugin);
	}

	private void ativateAllListeners(AnhyLingo itemLingoPlugin) {
		
		itemLingoPlugin.getServer().getPluginManager().registerEvents(new InventoryLocalizationListener(itemLingoPlugin), itemLingoPlugin);
		
	}
}
