package ink.anh.api.player;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import ink.anh.lingo.AnhyLingo;

public class PlayerData {

    private AnhyLingo lingoPlugin;

    public PlayerData() {
        this.lingoPlugin = AnhyLingo.getInstance();
    }

 // Запис масиву рядків
    public void setCustomData(Player player, String dataKey, String[] values) {
        NamespacedKey key = new NamespacedKey(lingoPlugin, dataKey);
        String combined = String.join(",", values);  // Серіалізація масиву в один рядок
        player.getPersistentDataContainer().set(key, PersistentDataType.STRING, combined);
    }

    // Отримання масиву рядків
    public String[] getCustomData(Player player, String dataKey) {
        NamespacedKey key = new NamespacedKey(lingoPlugin, dataKey);
        String value = player.getPersistentDataContainer().get(key, PersistentDataType.STRING);
        return value != null ? value.split(",") : new String[0];  // Десеріалізація рядка назад у масив
    }

    // Отримання даних
    public String getStringData(Player player, String dataKey) {
        NamespacedKey key = new NamespacedKey(lingoPlugin, dataKey);
        return player.getPersistentDataContainer().get(key, PersistentDataType.STRING);
    }

    // Перевірка чи є дані
    public boolean hasCustomData(Player player, String dataKey) {
        NamespacedKey key = new NamespacedKey(lingoPlugin, dataKey);
        return player.getPersistentDataContainer().has(key, PersistentDataType.STRING);
    }

    // Видалення даних
    public void removeCustomData(Player player, String dataKey) {
        NamespacedKey key = new NamespacedKey(lingoPlugin, dataKey);
        player.getPersistentDataContainer().remove(key);
    }
}
