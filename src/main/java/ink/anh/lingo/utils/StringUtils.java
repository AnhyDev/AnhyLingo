package ink.anh.lingo.utils;

import ink.anh.lingo.ItemLingo;
import ink.anh.lingo.lang.LanguageManager;
import ink.anh.lingo.listeners.protocol.ModificationState;

public class StringUtils {


	public static String colorize(String text) {
        return text.replace("&", "§");
    }
	
	public static ModificationState translateKyeWorldModificationState(String text, String[] langs, TypeText typeText, ModificationState state) {
		boolean isSystemChat = typeText == TypeText.SYSTEM_CHAT ? true : false;
		String newText = translateKyeWorld(text, langs, isSystemChat);
		if (!newText.equals(text)) {
			state.setModified(true);
			state.setTranslatedText(newText);
		}
		return state;
	}
	
	public static String translateKyeWorld(String text, String[] langs, boolean isSystemChat) {
	    String newText = null;
	    ItemLingo itemLingoPlugin = ItemLingo.getInstance();
	    
	    if (itemLingoPlugin == null) {
	        return text;
	    }

	    if ((langs == null || langs.length == 0) && itemLingoPlugin.getConfigurationManager() != null) {
	    	langs = new String[]{itemLingoPlugin.getConfigurationManager().getDefaultLang()};
	    }

	    if ((langs == null || langs.length == 0)) {
	    	langs = new String[]{"en"};
	    }
	    
	    LanguageManager langMan;
	    if (isSystemChat) {
	    	langMan = itemLingoPlugin.getLanguageSystemChat();
	    } else {
	    	langMan = itemLingoPlugin.getLanguageChat();
	    }
	    
	    newText = processText(text, langMan, langs);
	    return newText != null ? newText : text;
	}

	public static String processText(String text, LanguageManager langMan, String[] langs) {
        boolean prependSpace = text.startsWith(" ");
        boolean appendSpace = text.endsWith(" ");

        String[] words = text.trim().split(" ");
        StringBuilder newText = new StringBuilder();

        if (prependSpace) {
            newText.append(" ");
        }

        boolean textModified = false;
        for (String word : words) {
            String replacement = langMan.getData(word, langs);
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
