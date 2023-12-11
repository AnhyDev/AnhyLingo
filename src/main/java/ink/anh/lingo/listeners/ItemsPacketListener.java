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

import ink.anh.api.utils.LangUtils;
import ink.anh.lingo.AnhyLingo;
import ink.anh.lingo.GlobalManager;
import ink.anh.lingo.item.ItemLang;

import java.util.Arrays;
import java.util.List;

/**
 * Listener for packet events related to items, handling the localization of ItemStacks based on player language settings.
 * This class intercepts and modifies packets to translate item names and lore for players.
 */
public class ItemsPacketListener {

    private AnhyLingo lingoPlugin;
    private GlobalManager globalManager;
    private String key_NBT;

    /**
     * Constructor for ItemsPacketListener.
     *
     * @param lingoPlugin The instance of AnhyLingo plugin.
     */
    public ItemsPacketListener(AnhyLingo lingoPlugin) {
        this.lingoPlugin = lingoPlugin;
		this.globalManager = lingoPlugin.getGlobalManager();
        this.key_NBT = "ItemLingo";

        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        

        protocolManager.addPacketListener(new PacketAdapter(lingoPlugin, ListenerPriority.NORMAL, 
                PacketType.Play.Server.WINDOW_ITEMS, 
                PacketType.Play.Server.SET_SLOT) {
            
        	@Override
        	public void onPacketSending(PacketEvent event) {
        	    
        	    lingoPlugin.getLogger().warning("NBT event.getPacketType(): " + event.getPacketType().name());
        	    
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
        lingoPlugin.getLogger().info("Handling WINDOW_ITEMS packet...");

        // Додатковий лог для виводу вмісту пакету
        StructureModifier<Object> packetContents = event.getPacket().getModifier();
        for (int index = 0; index < packetContents.size(); index++) {
            Object field = packetContents.read(index);
            lingoPlugin.getLogger().info("Field at index " + index + ": " + (field == null ? "null" : field.toString()));
        }

        StructureModifier<ItemStack[]> itemArrayModifier = event.getPacket().getItemArrayModifier();
        
        if (itemArrayModifier.size() <= 2 || itemArrayModifier.read(2) == null) {
            lingoPlugin.getLogger().warning("ItemArrayModifier is empty or unavailable!");
            return;
        }

        ItemStack[] itemArray = itemArrayModifier.read(2);
        if (itemArray == null) {
            lingoPlugin.getLogger().warning("Item array from packet is null!");
            return;
        }

        lingoPlugin.getLogger().info("Found " + itemArray.length + " items in the packet.");

        int modifiedItemsCount = 0;

        for (int i = 0; i < itemArray.length; i++) {
            if (itemArray[i] != null) {
                lingoPlugin.getLogger().info("Modifying item at index " + i + "...");
                itemArray[i] = modifyItem(langs, itemArray[i]);
                modifiedItemsCount++;
            }
        }

        itemArrayModifier.write(0, itemArray); // Запис модифікованих предметів назад у пакет

        lingoPlugin.getLogger().info("Modified total of " + modifiedItemsCount + " items in the packet.");
    }

    private void handleSetSlotPacket(PacketEvent event, String[] langs) {
        StructureModifier<ItemStack> itemModifier = event.getPacket().getItemModifier();
        
        if (itemModifier.size() > 0 && itemModifier.read(0) != null) {
            lingoPlugin.getLogger().info("Original item found in packet.");
            ItemStack item = itemModifier.read(0);
            lingoPlugin.getLogger().info("Original item name: " + 
	            (item.hasItemMeta() && item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : "null"));
            
            itemModifier.write(0, modifyItem(langs, item));
        }
    }

    // Methods for handling specific packet events

    /**
     * Modifies the provided ItemStack based on the specified languages.
     * Updates the item's name and lore to match the translation defined for the selected languages.
     *
     * @param langs The languages to use for translation.
     * @param item The ItemStack to be modified.
     * @return The modified ItemStack.
     */
    private ItemStack modifyItem(String[] langs, ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return item; // Якщо предмета немає або немає метаданих, просто повертаємо його.
        }
        
        // Отримання NBT компаунда предмета
        NbtCompound compound = NbtFactory.asCompound(NbtFactory.fromItemTag(item));
        lingoPlugin.getLogger().info("NbtCompound compound: " + compound.toString());

        // Якщо у компаунда є ключ "ItemLingo", працюємо з ним
        if (compound.containsKey(key_NBT)) {
            String customID = String.valueOf(compound.getValue(key_NBT).getValue());
            lingoPlugin.getLogger().info("Found customID in NBT: " + customID);

            // Якщо customID існує в нашому словнику, ми змінюємо ім'я та лор предмета
            if (globalManager.getLanguageItemStack().dataContainsKey(customID, langs)) {
            	
                ItemMeta meta = item.getItemMeta();
                
                String displayName = getTranslatedName(customID, langs);
                if (displayName != null) meta.setDisplayName(displayName);
                
                List<String> lore = getTranslatedLore(customID, langs);
                if (lore != null) meta.setLore(lore);
                
                item.setItemMeta(meta);

                lingoPlugin.getLogger().info("Modified item for player");
            }
        }
		return item;
    }


    private String[] getPlayerLanguage(Player player) {
        return LangUtils.getPlayerLanguage(player, lingoPlugin);
    }

    private String getTranslatedName(String customID, String[] langs) {
        ItemLang itemLang = globalManager.getLanguageItemStack().getData(customID, langs);
        return itemLang != null ? itemLang.getName() : null; 
    }

    private List<String> getTranslatedLore(String customID, String[] langs) {
        ItemLang itemLang = globalManager.getLanguageItemStack().getData(customID, langs);
        return itemLang != null && itemLang.getLore() != null ? Arrays.asList(itemLang.getLore()) : null;
    }
}
