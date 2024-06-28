package ink.anh.lingo.item;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import ink.anh.api.nbt.NBTExplorer;
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
        logger.info("Starting modifyItem method.");
        if (langs == null) {
            logger.warning("Languages array is null.");
            return;
        }

        String customID = NBTExplorer.getNBTValue(item, key_NBT);
        if (customID == null) {
            logger.warning("PersistentDataContainer does not have key: " + key_NBT);
            return;
        }

        logger.info("Found customID: " + customID);

        if (globalManager.getLanguageItemStack().dataContainsKey(customID, langs)) {
            logger.info("Data contains key for customID: " + customID);
            ItemLang itemLang = null;
            boolean processed = false;

            String langID = NBTExplorer.getNBTValue(item, lang_NBT);
            if (langID != null) {
                logger.info("Found langID: " + langID);

                for (String currentLang : langs) {
                    itemLang = globalManager.getLanguageItemStack().getTranslate(customID, currentLang);
                    logger.info("Checking language: " + currentLang);

                    if (langID.equals(currentLang) && !forceTranslation) {
                        logger.info("Language already set and no force translation: " + currentLang);
                        processed = true;
                        return;
                    } else if (itemLang != null) {
                        logger.info("Translating item to language: " + currentLang);
                        translateItemStack(item, itemLang);
                        processed = true;
                    }
                }
            }
            if (!processed) {
                logger.info("No languages matched or force translation, getting default data.");
                itemLang = globalManager.getLanguageItemStack().getData(customID, langs);
                translateItemStack(item, itemLang);
            }
        } else {
            logger.warning("Data does not contain key for customID: " + customID);
        }
    }

    /**
     * Translates the given ItemStack using the specified ItemLang.
     * Updates the item's name and lore according to the ItemLang's properties.
     *
     * @param item The ItemStack to be translated.
     * @param itemLang The ItemLang containing the translation details.
     */
    private void translateItemStack(ItemStack item, ItemLang itemLang) {
        logger.info("Starting translateItemStack method.");

        NBTExplorer.setNBTValue(item, lang_NBT, itemLang.getLang());
        logger.info("Set lang_NBT to: " + itemLang.getLang());

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            logger.warning("ItemMeta is null during translation.");
            return;
        }

        String displayName = itemLang.getName();
        if (displayName != null) {
            meta.setDisplayName(displayName);
            logger.info("Set display name to: " + displayName);
        }

        List<String> lore = itemLang.getLore() != null ? Arrays.asList(itemLang.getLore()) : null;
        if (lore != null) {
            meta.setLore(lore);
            logger.info("Set lore to: " + String.join(", ", lore));
        }

        item.setItemMeta(meta);
        logger.info("Finished translateItemStack method.");
    }
}
