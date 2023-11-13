package ink.anh.lingo.utils;

import ink.anh.lingo.ItemLingo;

public class StringUtils {


	public static String colorize(String text) {
        return text.replace("&", "§");
    }
	
	public static String translateKyeWorld(String word, String lang, boolean isSystemChat) {
	    String newWorld = null;
	    ItemLingo itemLingoPlugin = ItemLingo.getInstance();
	    
	    if (itemLingoPlugin == null) {
	        return word;
	    }

	    if (lang == null) {
	        lang = itemLingoPlugin.getConfigurationManager().getDefaultLang();
	    }

	    if (lang == null) {
	        lang = "en";
	    }
	    
	    if (isSystemChat) {
	        newWorld = itemLingoPlugin.getLanguageSystemChat().getData(lang, word);
	    } else {
	        newWorld = itemLingoPlugin.getLanguageChat().getData(lang, word);
	    }
	    
	    return newWorld != null ? newWorld : word;
	}
}
