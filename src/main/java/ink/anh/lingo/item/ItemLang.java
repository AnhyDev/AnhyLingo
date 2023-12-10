package ink.anh.lingo.item;

import java.util.Arrays;
import java.util.Objects;

public class ItemLang {

	private String lang;
	private String name;
	private String[] lore;
	
	public ItemLang(String name) {
		this.name = name;
	}
	
	public ItemLang(String name, String[] lore) {
		this.name = name;
		this.lore = lore;
	}

	public String getName() {
		return name;
	}

	public String[] getLore() {
		return lore;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLore(String[] lore) {
		this.lore = lore;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(lore);
		result = prime * result + Objects.hash(name);
		return result;
	}

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
