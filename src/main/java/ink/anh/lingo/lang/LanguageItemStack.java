package ink.anh.lingo.lang;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import ink.anh.lingo.ItemLingo;
import ink.anh.lingo.utils.StringUtils;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LanguageItemStack extends AbstractLanguage<ItemLang> {

    private static LanguageItemStack instance = null;

    private static final Object LOCK = new Object();
    
    public LanguageItemStack(ItemLingo plugin) {
        super(plugin);
    }

    public static LanguageItemStack getInstance(ItemLingo itemLingoPlugin) {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = new LanguageItemStack(itemLingoPlugin);
                }
            }
        }
        return instance;
    }

    @Override
    protected String getPrefix() {
        return null;
    }

    @Override
    protected String getDirectory() {
        return "items";
    }

    @Override
    protected Map<String, ItemLang> extractData(FileConfiguration langConfig) {
        Map<String, ItemLang> langMap = new HashMap<>();

        for (String key : langConfig.getKeys(false)) {
            if (langConfig.isConfigurationSection(key)) {
                ConfigurationSection section = langConfig.getConfigurationSection(key);

                ItemLang itemLang;
                if (section.contains("copy")) {
                    itemLang = processCopiedItem(section, langMap);
                } else {
                    // Звичайне зчитування предмета
                    String name = StringUtils.colorize(section.getString("name"));
                    List<String> loreList = section.getStringList("lore").stream()
                                        .map(StringUtils::colorize)
                                        .collect(Collectors.toList());

                    String[] lore = loreList.toArray(new String[0]);
                    itemLang = new ItemLang(name, lore);
                }
                langMap.put(key, itemLang);
            }
        }

        return langMap;
    }

    private ItemLang processCopiedItem(ConfigurationSection section, Map<String, ItemLang> langMap) {
        String baseKey = section.getString("copy");
        ItemLang baseItemLang = langMap.get(baseKey);

        if (baseItemLang == null) {
            throw new IllegalStateException("Base item for copy not found: " + baseKey);
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

    public Map<String, ItemLang> getLanguageMap(String lang) {
        Map<String, ItemLang> langMap = data.get(lang);
        if (langMap != null) {
            return langMap;
        } else {
            String defaultLanguage = itemLingoPlugin.getConfigurationManager().getDefaultLang();
            langMap = data.get(defaultLanguage);
            if (langMap != null) {
                return langMap;
            } else {
                return data.get("en");
            }
        }
    }

    public ItemLang getItemData(String lang, String key) {
        Map<String, ItemLang> langMap = getLanguageMap(lang);
        if (langMap == null) {
            return null;
        }
        return langMap.getOrDefault(key, null);
    }

    public boolean itemsDataContainsKey(String lang, String key) {
        return getItemData(lang, key) != null;
    }
}
