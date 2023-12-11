package ink.anh.lingo.item;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents the language-specific details of an item, including its name and lore (description).
 */
public class ItemLang {

	private String lang;
	private String name;
	private String[] lore;

    /**
     * Constructs an ItemLang with the specified name.
     *
     * @param name The name of the item.
     */
	public ItemLang(String name) {
		this.name = name;
	}

    /**
     * Constructs an ItemLang with the specified name and lore.
     *
     * @param name The name of the item.
     * @param lore The lore (description) of the item.
     */
	public ItemLang(String name, String[] lore) {
		this.name = name;
		this.lore = lore;
	}

    // Getters and setters for name, lore, and language

    /**
     * Gets the name of the item.
     *
     * @return The name of the item.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the lore (description) of the item.
     *
     * @return An array of strings representing the lore of the item. Each string is a line of lore.
     */
    public String[] getLore() {
        return lore;
    }

    /**
     * Sets the name of the item.
     *
     * @param name The new name to be set for the item.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the lore (description) of the item.
     *
     * @param lore An array of strings representing the new lore to be set for the item. Each string is a line of lore.
     */
    public void setLore(String[] lore) {
        this.lore = lore;
    }

    /**
     * Gets the language code associated with this item.
     *
     * @return The language code. For example, 'en' for English.
     */
    public String getLang() {
        return lang;
    }

    /**
     * Sets the language code for this item.
     *
     * @param lang The language code to be set. For example, 'en' for English.
     */
    public void setLang(String lang) {
        this.lang = lang;
    }

    /**
     * Generates a hash code for the ItemLang instance.
     *
     * @return The hash code.
     */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(lore);
		result = prime * result + Objects.hash(name);
		return result;
	}

    /**
     * Compares this ItemLang instance with another object for equality.
     *
     * @param obj The object to compare with.
     * @return true if the specified object is equal to this ItemLang instance.
     */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemLang other = (ItemLang) obj;
		return Arrays.equals(lore, other.lore) && Objects.equals(name, other.name);
	}

    /**
     * Returns a string representation of this ItemLang instance.
     * The string includes the item name and lore (if any), each on a new line.
     *
     * @return A string representation of the ItemLang instance.
     */
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();

	    // Додаємо ім'я
	    sb.append(name).append("\n");

	    // Додаємо кожен елемент лору з нового рядка і з відступом
	    if (lore != null) {
	        for (String line : lore) {
	            sb.append("  ").append(line).append("\n"); // два пробіли в якості відступу
	        }
	    }

	    return sb.toString().trim(); // використовуємо trim(), щоб видалити зайвий рядок на кінці, якщо він є
	}
}
