package ink.anh.lingo.utils;

import ink.anh.lingo.ItemLingo;
import ink.anh.lingo.lang.LanguageManager;

public class StringUtils {


	public static String colorize(String text) {
        return text.replace("&", "§");
    }
	
	public static String translateKyeWorld(String text, String lang, boolean isSystemChat) {
	    String newText = null;
	    ItemLingo itemLingoPlugin = ItemLingo.getInstance();
	    
	    if (itemLingoPlugin == null) {
	        return text;
	    }

	    if (lang == null && itemLingoPlugin.getConfigurationManager() != null) {
	        lang = itemLingoPlugin.getConfigurationManager().getDefaultLang();
	    }

	    if (lang == null) {
	        lang = "en";
	    }
	    
	    LanguageManager langMan;
	    if (isSystemChat) {
	    	langMan = itemLingoPlugin.getLanguageSystemChat();
	    } else {
	    	langMan = itemLingoPlugin.getLanguageChat();
	    }
	    
	    newText = processText(text, langMan, lang);
	    return newText != null ? newText : text;
	}

	public static String processText(String text, LanguageManager langMan, String lang) {
        boolean prependSpace = text.startsWith(" ");
        boolean appendSpace = text.endsWith(" ");

        String[] words = text.trim().split(" ");
        StringBuilder newText = new StringBuilder();

        if (prependSpace) {
            newText.append(" ");
        }

        boolean textModified = false;
        for (String word : words) {
            String replacement = langMan.getData(word, lang);
            if (replacement != null) {
                newText.append(replacement);
                textModified = true;
            } else {
                newText.append(word);
            }
            newText.append(" ");
        }

        if (!appendSpace && newText.length() > 0) {
            newText.setLength(newText.length() - 1);
        }

        return textModified ? newText.toString() : null;
    }
}
