package ink.anh.lingo.listeners.protocol;

public class ModificationState {
    private boolean orModified;
    private String translatedText;

    public boolean isModified() {
        return orModified;
    }

    public void setModified(boolean modified) {
        this.orModified = modified;
    }

	public String getTranslatedText() {
		return translatedText;
	}

	public void setTranslatedText(String translatedText) {
		this.translatedText = translatedText;
	}
}
