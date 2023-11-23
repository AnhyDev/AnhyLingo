package ink.anh.lingo.lang;

import java.util.Arrays;
import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;

import ink.anh.lingo.AnhyLingo2;
import ink.anh.lingo.nbt.NBTExplorer;

public class TranslateItemStack {

    private AnhyLingo2 itemLingoPlugin;
    private String lang_NBT;
    private String key_NBT;
    
    
	public TranslateItemStack(AnhyLingo2 itemLingoPlugin) {
		this.itemLingoPlugin = itemLingoPlugin;
		this.lang_NBT = "Lingo";
		this.key_NBT = "ItemLingo";
	}


    public void modifyItem(String[] langs, ItemStack item) {

        if (langs == null) {
            return;
        }
        
        // Отримання NBT компаунда предмета
        NbtCompound compound = NbtFactory.asCompound(NbtFactory.fromItemTag(item));
        
        
        // Якщо у компаунда є ключ "ItemLingo", працюємо з ним
        if (compound.containsKey(key_NBT)) {
            String customID = String.valueOf(compound.getValue(key_NBT).getValue());

            // Якщо customID існує в нашому словнику, ми змінюємо ім'я та лор предмета
            if (itemLingoPlugin.getLanguageItemStack().dataContainsKey(customID, langs)) {
            	ItemLang itemLang = null;
            	boolean processed = false;

                // Перевірка на наявність тегу lang_NBT та його відповідність langs
                if (compound.containsKey(lang_NBT)) {
                    String langID = String.valueOf(compound.getValue(lang_NBT).getValue());
                    
                    for (String currentLang : langs) {
                    	
                        itemLang = itemLingoPlugin.getLanguageItemStack().getTranslate(customID, currentLang);

                        if (langID.equals(currentLang)) {
                        	processed = true;
                            return;
                            
                        } else if (itemLang != null) {
                        	translateItemStack(item, itemLang);
                        	processed = true;
                        }
                    }
                }
                if (!processed) {
                	itemLang = itemLingoPlugin.getLanguageItemStack().getData(customID, langs);
                	translateItemStack(item, itemLang);
                }
            }
        }
    }
    
    private void translateItemStack(ItemStack item, ItemLang itemLang) {

        // Встановлюємо значення NBT
        NBTExplorer.setNBTValueFromString(item, lang_NBT, "string:" + itemLang.getLang());
        
        ItemMeta meta = item.getItemMeta();
        
        String displayName = itemLang.getName();
        if (displayName != null) {
            meta.setDisplayName(displayName);
        }
        
        List<String> lore = itemLang.getLore() != null ? Arrays.asList(itemLang.getLore()) : null;
        if (lore != null) {
            meta.setLore(lore);
        }
        
        item.setItemMeta(meta);
    }
	
}
