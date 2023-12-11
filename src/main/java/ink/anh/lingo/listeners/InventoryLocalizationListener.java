package ink.anh.lingo.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import ink.anh.api.utils.LangUtils;
import ink.anh.lingo.AnhyLingo;
import ink.anh.lingo.item.TranslateItemStack;

public class InventoryLocalizationListener implements Listener {

    private AnhyLingo lingoPlugin;
    private boolean itemLingo;

    
    public InventoryLocalizationListener(AnhyLingo lingoPlugin) {
		this.lingoPlugin = lingoPlugin;
		this.itemLingo = lingoPlugin.getGlobalManager().isItemLingo();
	}

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryOpen(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();
		
		if (itemLingo) {
			String[] langs = getPlayerLanguage(player);

	        if(event.getInventory().getViewers().size() == 1) {
	        	TranslateItemStack translater = new TranslateItemStack(lingoPlugin);
	            for (ItemStack item : event.getInventory().getContents()) {
	            	if (checkItem(item)) translater.modifyItem(langs, item);
	            }
	        }
		}   
    }

	@EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getType() == InventoryType.CREATIVE) return;
        
        Player player = (Player) event.getWhoClicked();
		
		if (itemLingo) {
	        String[] langs = getPlayerLanguage(player);

	        if(event.getInventory().getViewers().size() == 1) {
	        	TranslateItemStack translater = new TranslateItemStack(lingoPlugin);
	            ItemStack currentItem = event.getCurrentItem();
	            
	            if (checkItem(currentItem)) {
	            	translater.modifyItem(langs, currentItem);
	            }
	        }
		}
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPickupItem(EntityPickupItemEvent event) {
    	if (!(event.getEntity() instanceof Player)) return;
    	
        Player player = (Player) event.getEntity();
		
		if (itemLingo) {
	        String[] langs = getPlayerLanguage(player);

	        ItemStack pickedItem = event.getItem().getItemStack();
	        if (checkItem(pickedItem)) {
	        	TranslateItemStack translater = new TranslateItemStack(lingoPlugin);
	        	translater.modifyItem(langs, pickedItem);
	        }
		}
    }

    private String[] getPlayerLanguage(Player player) {
        return LangUtils.getPlayerLanguage(player, lingoPlugin);
    }
    
    private boolean checkItem(ItemStack item) {
        return (item != null && item.getType() != Material.AIR && item.hasItemMeta());
    }

}
