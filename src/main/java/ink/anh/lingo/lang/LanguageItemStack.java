package ink.anh.lingo.lang;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import ink.anh.lingo.AnhyLingo;
import ink.anh.lingo.messages.Logger;
import ink.anh.lingo.utils.StringUtils;
import ink.anh.lingo.api.lang.AbstractLanguage;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LanguageItemStack extends AbstractLanguage<ItemLang> {

    private static LanguageItemStack instance = null;
    private static final Object LOCK = new Object();
    private static final String DIRECTORY = "items";
    
    public LanguageItemStack(AnhyLingo plugin) {
        super(plugin, DIRECTORY);
    }

    public static LanguageItemStack getInstance(AnhyLingo lingoPlugin) {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = new LanguageItemStack(lingoPlugin);
                }
            }
        }
        return instance;
    }

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

	@Override
	public Map<String, ItemLang> extractData(FileConfiguration langConfig) {
		return null;
	}
}
