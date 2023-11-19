package ink.anh.lingo.listeners;

import java.util.Arrays;
import java.util.List;

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
import org.bukkit.inventory.meta.ItemMeta;

import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;

import ink.anh.lingo.ItemLingo;
import ink.anh.lingo.lang.ItemLang;
import ink.anh.lingo.nbt.NBTExplorer;
import ink.anh.lingo.utils.LangUtils;

public class InventoryLocalizationListener implements Listener {

    private ItemLingo itemLingoPlugin;
    private String lang_NBT;
    private String key_NBT;

    
    public InventoryLocalizationListener(ItemLingo itemLingoPlugin) {
		this.itemLingoPlugin = itemLingoPlugin;
		this.lang_NBT = "Lingo";
		this.key_NBT = "ItemLingo";
	}

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryOpen(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();
        String[] langs = getPlayerLanguage(player);

        if(event.getInventory().getViewers().size() == 1) {
            for (ItemStack item : event.getInventory().getContents()) {
            	if (checkItem(item)) modifyItem(langs, item);
            }
        }
    }

	@EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getType() == InventoryType.CREATIVE) return;
        
        Player player = (Player) event.getWhoClicked();
        String[] langs = getPlayerLanguage(player);

        if(event.getInventory().getViewers().size() == 1) {
            ItemStack currentItem = event.getCurrentItem();
            
            if (checkItem(currentItem)) {
        	    modifyItem(langs, currentItem);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPickupItem(EntityPickupItemEvent event) {
    	if (!(event.getEntity() instanceof Player)) return;
    	
        Player player = (Player) event.getEntity();
        String[] langs = getPlayerLanguage(player);

        ItemStack pickedItem = event.getItem().getItemStack();
        if (checkItem(pickedItem)) modifyItem(langs, pickedItem);
    }


    private void modifyItem(String[] langs, ItemStack item) {
        
        // Отримання NBT компаунда предмета
        NbtCompound compound = NbtFactory.asCompound(NbtFactory.fromItemTag(item));
        
        
        // Якщо у компаунда є ключ "ItemLingo", працюємо з ним
        if (compound.containsKey(key_NBT)) {
            String customID = String.valueOf(compound.getValue(key_NBT).getValue());

            // Якщо customID існує в нашому словнику, ми змінюємо ім'я та лор предмета
            if (itemLingoPlugin.getLanguageItemStack().dataContainsKey(customID, langs)) {

                // Перевірка на наявність тегу lang_NBT та його відповідність langs
                if (compound.containsKey(lang_NBT)) {
                    String langID = String.valueOf(compound.getValue(lang_NBT).getValue());
                    if (langID.equals(langs)) {
                        return; // Якщо умова виконується, повертаємо предмет без змін
                    }
                }

                // Встановлюємо значення NBT
                NBTExplorer.setNBTValueFromString(item, lang_NBT, "string:" + langs);
                
                ItemMeta meta = item.getItemMeta();
                
                String displayName = getTranslatedName(customID, langs);
                if (displayName != null) {
                    meta.setDisplayName(displayName);
                }
                
                List<String> lore = getTranslatedLore(customID, langs);
                if (lore != null) {
                    meta.setLore(lore);
                }
                
                item.setItemMeta(meta);
            }
        }
    }

    private String[] getPlayerLanguage(Player player) {
        return LangUtils.getPlayerLanguage(player);
    }

    private String getTranslatedName(String customID, String[] langs) {
        ItemLang itemLang = itemLingoPlugin.getLanguageItemStack().getData(customID, langs);
        return itemLang != null ? itemLang.getName() : null; 
    }

    private List<String> getTranslatedLore(String customID, String[] langs) {
        ItemLang itemLang = itemLingoPlugin.getLanguageItemStack().getData(customID, langs);
        return itemLang != null && itemLang.getLore() != null ? Arrays.asList(itemLang.getLore()) : null;
    }
    
    private boolean checkItem(ItemStack item) {
        return (item != null && item.getType() != Material.AIR && item.hasItemMeta());
    }

}
