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

/**
 * Listener for inventory-related events, handling localization of item stacks in player inventories.
 * This class listens to events related to opening, clicking, and picking up items in inventories
 * and translates item names and lore based on player language settings.
 */
public class InventoryLocalizationListener implements Listener {

    private AnhyLingo lingoPlugin;
    private boolean itemLingo;


    /**
     * Constructor for InventoryLocalizationListener.
     *
     * @param lingoPlugin The instance of AnhyLingo plugin.
     */
    public InventoryLocalizationListener(AnhyLingo lingoPlugin) {
		this.lingoPlugin = lingoPlugin;
		this.itemLingo = lingoPlugin.getGlobalManager().isItemLingo();
	}

    /**
     * Handles inventory opening events to localize item stacks.
     *
     * @param event The InventoryOpenEvent.
     */
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

    /**
     * Handles inventory click events to localize item stacks.
     *
     * @param event The InventoryClickEvent.
     */
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

    /**
     * Handles item pickup events to localize item stacks.
     *
     * @param event The EntityPickupItemEvent.
     */
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

    /**
     * Retrieves the language settings for a player.
     *
     * @param player The player whose language settings are to be retrieved.
     * @return An array of language codes for the player.
     */
    private String[] getPlayerLanguage(Player player) {
        return LangUtils.getPlayerLanguage(player);
    }

    /**
     * Checks if an item stack is valid for localization.
     *
     * @param item The ItemStack to check.
     * @return true if the item stack is valid for localization, false otherwise.
     */
    private boolean checkItem(ItemStack item) {
        return (item != null && item.getType() != Material.AIR && item.hasItemMeta());
    }

}
