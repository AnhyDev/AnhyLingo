package ink.anh.lingo.listeners;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;

import ink.anh.lingo.ItemLingo;
import ink.anh.lingo.lang.ItemLang;
import ink.anh.lingo.utils.LangUtils;

import java.util.Arrays;
import java.util.List;

public class ItemsPacketListener {

    private ItemLingo itemLingoPlugin;
    private String key_NBT;

    public ItemsPacketListener(ItemLingo plugin) {
        this.itemLingoPlugin = plugin;
        this.key_NBT = "ItemLingo";

        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        

        protocolManager.addPacketListener(new PacketAdapter(plugin, ListenerPriority.NORMAL, 
                PacketType.Play.Server.WINDOW_ITEMS, 
                PacketType.Play.Server.SET_SLOT) {
            
        	@Override
        	public void onPacketSending(PacketEvent event) {
        	    
        	    itemLingoPlugin.getLogger().warning("NBT event.getPacketType(): " + event.getPacketType().name());
        	    
                String[] langs = getPlayerLanguage(event.getPlayer());
        	    
        	    if (event.getPacketType() == PacketType.Play.Server.WINDOW_ITEMS) {
        	        handleWindowItemsPacket(event, langs);
        	    } else if (event.getPacketType() == PacketType.Play.Server.SET_SLOT) {
        	    	handleSetSlotPacket(event, langs);
        	    }
        	}
        });
    }

    private void handleWindowItemsPacket(PacketEvent event, String[] langs) {
        itemLingoPlugin.getLogger().info("Handling WINDOW_ITEMS packet...");

        // Додатковий лог для виводу вмісту пакету
        StructureModifier<Object> packetContents = event.getPacket().getModifier();
        for (int index = 0; index < packetContents.size(); index++) {
            Object field = packetContents.read(index);
            itemLingoPlugin.getLogger().info("Field at index " + index + ": " + (field == null ? "null" : field.toString()));
        }

        StructureModifier<ItemStack[]> itemArrayModifier = event.getPacket().getItemArrayModifier();
        
        if (itemArrayModifier.size() <= 2 || itemArrayModifier.read(2) == null) {
            itemLingoPlugin.getLogger().warning("ItemArrayModifier is empty or unavailable!");
            return;
        }

        ItemStack[] itemArray = itemArrayModifier.read(2);
        if (itemArray == null) {
            itemLingoPlugin.getLogger().warning("Item array from packet is null!");
            return;
        }

        itemLingoPlugin.getLogger().info("Found " + itemArray.length + " items in the packet.");

        int modifiedItemsCount = 0;

        for (int i = 0; i < itemArray.length; i++) {
            if (itemArray[i] != null) {
                itemLingoPlugin.getLogger().info("Modifying item at index " + i + "...");
                itemArray[i] = modifyItem(langs, itemArray[i]);
                modifiedItemsCount++;
            }
        }

        itemArrayModifier.write(0, itemArray); // Запис модифікованих предметів назад у пакет

        itemLingoPlugin.getLogger().info("Modified total of " + modifiedItemsCount + " items in the packet.");
    }

    private void handleSetSlotPacket(PacketEvent event, String[] langs) {
        StructureModifier<ItemStack> itemModifier = event.getPacket().getItemModifier();
        
        if (itemModifier.size() > 0 && itemModifier.read(0) != null) {
            itemLingoPlugin.getLogger().info("Original item found in packet.");
            ItemStack item = itemModifier.read(0);
            itemLingoPlugin.getLogger().info("Original item name: " + 
	            (item.hasItemMeta() && item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : "null"));
            
            itemModifier.write(0, modifyItem(langs, item));
        }
    }

    private ItemStack modifyItem(String[] langs, ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return item; // Якщо предмета немає або немає метаданих, просто повертаємо його.
        }
        
        // Отримання NBT компаунда предмета
        NbtCompound compound = NbtFactory.asCompound(NbtFactory.fromItemTag(item));
        itemLingoPlugin.getLogger().info("NbtCompound compound: " + compound.toString());

        // Якщо у компаунда є ключ "ItemLingo", працюємо з ним
        if (compound.containsKey(key_NBT)) {
            String customID = String.valueOf(compound.getValue(key_NBT).getValue());
            itemLingoPlugin.getLogger().info("Found customID in NBT: " + customID);

            // Якщо customID існує в нашому словнику, ми змінюємо ім'я та лор предмета
            if (itemLingoPlugin.getLanguageItemStack().dataContainsKey(customID, langs)) {
            	
                ItemMeta meta = item.getItemMeta();
                
                String displayName = getTranslatedName(customID, langs);
                if (displayName != null) meta.setDisplayName(displayName);
                
                List<String> lore = getTranslatedLore(customID, langs);
                if (lore != null) meta.setLore(lore);
                
                item.setItemMeta(meta);

                itemLingoPlugin.getLogger().info("Modified item for player");
            }
        }
		return item;
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
}
