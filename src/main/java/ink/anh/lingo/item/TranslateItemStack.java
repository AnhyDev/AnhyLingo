package ink.anh.lingo.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import ink.anh.api.lingo.Translator;
import ink.anh.api.nbt.NBTExplorer;
import ink.anh.api.utils.StringUtils;
import ink.anh.lingo.AnhyLingo;
import ink.anh.lingo.GlobalManager;

/**
 * Handles the translation of ItemStacks based on language settings in the AnhyLingo plugin.
 * This class modifies ItemStacks by updating their name and lore according to the specified language.
 */
public class TranslateItemStack {

    private GlobalManager globalManager;
    private String lang_NBT;
    private String key_NBT;
    private Logger logger;
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("%%([^%]+)%%");
    
    /**
     * Constructor for TranslateItemStack.
     *
     * @param lingoPlugin The instance of AnhyLingo plugin.
     */
    public TranslateItemStack(AnhyLingo lingoPlugin) {
        this.globalManager = lingoPlugin.getGlobalManager();
        this.lang_NBT = "Lingo";
        this.key_NBT = "ItemLingo";
        this.logger = lingoPlugin.getLogger();
    }

    /**
     * Modifies the provided ItemStack based on the specified languages.
     * Updates the item's name and lore to match the translation defined for the selected languages.
     *
     * @param langs The languages to use for translation.
     * @param item The ItemStack to be modified.
     */
    public void modifyItem(String[] langs, ItemStack item, boolean forceTranslation) {
    	
        if (langs == null) {
            logger.warning("Languages array is null.");
            return;
        }

        String customID = NBTExplorer.getNBTValue(item, key_NBT);
        if (customID == null) {
            return;
        }

        if (globalManager.getLanguageItemStack().dataContainsKey(customID, langs)) {
            ItemLang itemLang = null;
            boolean processed = false;

            String langID = NBTExplorer.getNBTValue(item, lang_NBT);
            if (langID != null) {

                for (String currentLang : langs) {
                    itemLang = globalManager.getLanguageItemStack().getTranslate(customID, currentLang);

                    if (langID.equals(currentLang) && !forceTranslation) {
                        processed = true;
                        return;
                    } else if (itemLang != null) {
                        translateItemStack(langs, item, itemLang);
                        processed = true;
                    }
                }
            }
            if (!processed) {
                itemLang = globalManager.getLanguageItemStack().getData(customID, langs);
                translateItemStack(langs, item, itemLang);
            }
        }

    }

    /**
     * Translates the given ItemStack using the specified ItemLang.
     * Updates the item's name and lore according to the ItemLang's properties.
     *
     * @param item The ItemStack to be translated.
     * @param itemLang The ItemLang containing the translation details.
     */
    private void translateItemStack(String[] langs, ItemStack item, ItemLang itemLang) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        // Обробка назви з плейсхолдерами
        String displayName = itemLang.getName();
        if (displayName != null) {
            displayName = replacePlaceholders(langs, displayName, item);
            meta.setDisplayName(displayName);
        }

        // Обробка лору з плейсхолдерами
        List<String> lore = itemLang.getLore() != null ? Arrays.asList(itemLang.getLore()) : null;
        if (lore != null) {
        	
            boolean hasPlaceholders = (displayName != null && displayName.contains("%%")) || anyLoreHasPlaceholders(lore);
            if (hasPlaceholders) {
                List<String> translatedLore = new ArrayList<>(lore.size());
                for (String line : lore) {
                    translatedLore.add(replacePlaceholders(langs, line, item));
                }
                meta.setLore(translatedLore);
            } else {
                meta.setLore(lore);
            }
        }

        item.setItemMeta(meta);
        NBTExplorer.setNBTValueFromString(item, lang_NBT, "string:" + itemLang.getLang());
    }
    
    private boolean anyLoreHasPlaceholders(List<String> lore) {
        for (String line : lore) {
            if (line.contains("%%")) return true;
        }
        return false;
    }
    
    private String replacePlaceholders(String[] langs, String text, ItemStack item) {
        if (!text.contains("%%")) return text; // Швидка перевірка

        Matcher matcher = PLACEHOLDER_PATTERN.matcher(text);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String key = matcher.group(1); // Отримуємо ключ між %%
            String value = StringUtils.colorize(Translator.translateKyeWorld(globalManager,
            		NBTExplorer.getNBTValue(item, key) != null ? NBTExplorer.getNBTValue(item, key) : "null", langs));
            matcher.appendReplacement(result, value != null ? value : matcher.group(0)); // Якщо значення немає, лишаємо плейсхолдер
        }
        matcher.appendTail(result);

        return result.toString();
    }
}
