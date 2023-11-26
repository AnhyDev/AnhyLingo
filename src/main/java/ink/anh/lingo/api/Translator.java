package ink.anh.lingo.api;

import ink.anh.lingo.AnhyLingo;
import ink.anh.lingo.api.lang.LanguageManager;
import ink.anh.lingo.listeners.protocol.ModificationState;

public class Translator {
	
	public static ModificationState translateKyeWorldModificationState(String text, String[] langs, LanguageManager langMan, ModificationState state) {
		String newText = translateKyeWorld(text, langs, langMan);
		if (!newText.equals(text)) {
			state.setModified(true);
			state.setTranslatedText(newText);
		}
		return state;
	}
	
	public static String translateKyeWorld(String text, String[] langs, LanguageManager langMan) {
	    String newText = null;
	    AnhyLingo lingoPlugin = AnhyLingo.getInstance();
	    
	    if (lingoPlugin == null) {
	        return text;
	    }

	    if ((langs == null || langs.length == 0) && lingoPlugin.getConfigurationManager() != null) {
	    	langs = new String[]{lingoPlugin.getConfigurationManager().getDefaultLang()};
	    }

	    if ((langs == null || langs.length == 0)) {
	    	langs = new String[]{"en"};
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
