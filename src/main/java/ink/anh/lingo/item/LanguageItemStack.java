package ink.anh.lingo.item;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import ink.anh.api.lingo.lang.AbstractLanguage;
import ink.anh.api.messages.Logger;
import ink.anh.api.utils.StringUtils;
import ink.anh.lingo.GlobalManager;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents a language-specific item stack, handling the loading and processing of item language data.
 * This class extends AbstractLanguage and is tailored for managing item-specific language information.
 */
public class LanguageItemStack extends AbstractLanguage<ItemLang> {

    private static LanguageItemStack instance = null;
    private static final Object LOCK = new Object();
    private static final String DIRECTORY = "items";

    /**
     * Constructor for LanguageItemStack.
     *
     * @param globalManager The global manager instance from the AnhyLingo plugin.
     */
    public LanguageItemStack(GlobalManager globalManager) {
        super(globalManager, DIRECTORY);
    }

    /**
     * Singleton instance getter for LanguageItemStack.
     * Ensures that only one instance of this class is created.
     *
     * @param globalManager The global manager instance from the AnhyLingo plugin.
     * @return The singleton instance of LanguageItemStack.
     */
    public static LanguageItemStack getInstance(GlobalManager globalManager) {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = new LanguageItemStack(globalManager);
                }
            }
        }
        return instance;
    }

    /**
     * Extracts and processes item language data from the given file configuration.
     * This implementation handles both regular items and items that copy data from other items.
     *
     * @param langConfig The file configuration from which to extract item data.
     * @param lang The language code for which the data is being extracted.
     * @return A map of item keys to their corresponding ItemLang objects.
     */
    @Override
	public Map<String, ItemLang> extractData(FileConfiguration langConfig, String lang) {
        Map<String, ItemLang> langMap = new HashMap<>();

        for (String key : langConfig.getKeys(false)) {
            if (langConfig.isConfigurationSection(key)) {
                ConfigurationSection section = langConfig.getConfigurationSection(key);

                ItemLang itemLang;
                if (section.contains("copy")) {
                    itemLang = processCopiedItem(section, langMap);
                    if (itemLang == null) {
                        // Якщо processCopiedItem повертає null, пропускаємо цей елемент
                        continue;
                    }
                } else {
                    // Звичайне зчитування предмета
                    String name = StringUtils.colorize(section.getString("name"));
                    List<String> loreList = section.getStringList("lore").stream()
                                        .map(StringUtils::colorize)
                                        .collect(Collectors.toList());

                    String[] lore = loreList.toArray(new String[0]);
                    itemLang = new ItemLang(name, lore);
                }
                if (itemLang != null) itemLang.setLang(lang);
                langMap.put(key, itemLang);
            }
        }

        return langMap;
    }

    /**
     * Processes items that are marked to copy data from another item, allowing for shared language properties.
     *
     * @param section The configuration section of the item to process.
     * @param langMap The current map of processed language items.
     * @return An ItemLang instance representing the processed item, or null if base item is not found.
     */
    private ItemLang processCopiedItem(ConfigurationSection section, Map<String, ItemLang> langMap) {
        String baseKey = section.getString("copy");
        ItemLang baseItemLang = langMap.get(baseKey);

        if (baseItemLang == null) {
        	Logger.error(plugin, "Base item for copy not found: " + baseKey);
            return null; // Повертаємо null, щоб уникнути подальшої обробки
        }

        String name = baseItemLang.getName();
        List<String> loreList = new ArrayList<>(Arrays.asList(baseItemLang.getLore()));

        if (section.contains("lines")) {
            ConfigurationSection linesSection = section.getConfigurationSection("lines");
            for (String lineKey : linesSection.getKeys(false)) {
                int lineIndex = Integer.parseInt(lineKey) - 1; // Індексація з 0

                if (lineIndex >= loreList.size()) {
                    // Якщо індекс виходить за межі існуючого списку лору, додаємо лінію в кінець
                    loreList.add(StringUtils.colorize(linesSection.getString(lineKey)));
                } else {
                    // Інакше, замінюємо існуючу лінію
                    loreList.set(lineIndex, StringUtils.colorize(linesSection.getString(lineKey)));
                }
            }
        }

        String[] lore = loreList.toArray(new String[0]);
        return new ItemLang(name, lore);
    }

    /**
     * Override of extractData method from AbstractLanguage class. Not implemented in this subclass.
     *
     * @param langConfig The file configuration from which to extract data.
     * @return Always returns null as this method is not implemented.
     */
	@Override
	public Map<String, ItemLang> extractData(FileConfiguration langConfig) {
		return null;
	}
}
