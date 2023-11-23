package ink.anh.lingo.listeners;

import ink.anh.lingo.AnhyLingo2;

public class ListenerManager {

	
	public ListenerManager(AnhyLingo2 itemLingoPlugin) {
		this.ativateAllListeners(itemLingoPlugin);
	}

	private void ativateAllListeners(AnhyLingo2 itemLingoPlugin) {
		
		itemLingoPlugin.getServer().getPluginManager().registerEvents(new InventoryLocalizationListener(itemLingoPlugin), itemLingoPlugin);
		
	}
}
