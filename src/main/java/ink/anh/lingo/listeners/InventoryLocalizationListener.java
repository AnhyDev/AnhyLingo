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
import ink.anh.lingo.AnhyLingo2;
import ink.anh.lingo.utils.LangUtils;
import ink.anh.lingo.lang.TranslateItemStack;

public class InventoryLocalizationListener implements Listener {

    private AnhyLingo2 itemLingoPlugin;

    
    public InventoryLocalizationListener(AnhyLingo2 itemLingoPlugin) {
		this.itemLingoPlugin = itemLingoPlugin;
	}

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryOpen(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();
        String[] langs = getPlayerLanguage(player);

        if(event.getInventory().getViewers().size() == 1) {
        	TranslateItemStack translater = new TranslateItemStack(itemLingoPlugin);
            for (ItemStack item : event.getInventory().getContents()) {
            	if (checkItem(item)) translater.modifyItem(langs, item);
            }
        }
    }

	@EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getType() == InventoryType.CREATIVE) return;
        
        Player player = (Player) event.getWhoClicked();
        String[] langs = getPlayerLanguage(player);

        if(event.getInventory().getViewers().size() == 1) {
        	TranslateItemStack translater = new TranslateItemStack(itemLingoPlugin);
            ItemStack currentItem = event.getCurrentItem();
            
            if (checkItem(currentItem)) {
            	translater.modifyItem(langs, currentItem);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPickupItem(EntityPickupItemEvent event) {
    	if (!(event.getEntity() instanceof Player)) return;
    	
        Player player = (Player) event.getEntity();
        String[] langs = getPlayerLanguage(player);

        ItemStack pickedItem = event.getItem().getItemStack();
        if (checkItem(pickedItem)) {
        	TranslateItemStack translater = new TranslateItemStack(itemLingoPlugin);
        	translater.modifyItem(langs, pickedItem);
        }
    }

    private String[] getPlayerLanguage(Player player) {
        return LangUtils.getPlayerLanguage(player);
    }
    
    private boolean checkItem(ItemStack item) {
        return (item != null && item.getType() != Material.AIR && item.hasItemMeta());
    }

}
