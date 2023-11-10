package ink.anh.lingo.listeners;

import ink.anh.lingo.ItemLingo;

public class ListenerManager {

	
	public ListenerManager(ItemLingo itemLingoPlugin) {
		this.ativateAllListeners(itemLingoPlugin);
	}

	private void ativateAllListeners(ItemLingo itemLingoPlugin) {
		
		itemLingoPlugin.getServer().getPluginManager().registerEvents(new InventoryLocalizationListener(itemLingoPlugin), itemLingoPlugin);
		
	}
}
