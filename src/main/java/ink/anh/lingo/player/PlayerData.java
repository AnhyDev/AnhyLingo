package ink.anh.lingo.player;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import ink.anh.lingo.ItemLingo; // Це ваш основний клас плагіна

public class PlayerData {

    private ItemLingo itemLingoPlugin;

    public PlayerData() {
        this.itemLingoPlugin = ItemLingo.getInstance();
    }

    // Запис даних
    public void setCustomData(Player player, String dataKey, String value) {
        NamespacedKey key = new NamespacedKey(itemLingoPlugin, dataKey);
        player.getPersistentDataContainer().set(key, PersistentDataType.STRING, value);
    }

    // Отримання даних
    public String getCustomData(Player player, String dataKey) {
        NamespacedKey key = new NamespacedKey(itemLingoPlugin, dataKey);
        return player.getPersistentDataContainer().get(key, PersistentDataType.STRING);
    }

    // Перевірка чи є дані
    public boolean hasCustomData(Player player, String dataKey) {
        NamespacedKey key = new NamespacedKey(itemLingoPlugin, dataKey);
        return player.getPersistentDataContainer().has(key, PersistentDataType.STRING);
    }

    // Видалення даних
    public void removeCustomData(Player player, String dataKey) {
        NamespacedKey key = new NamespacedKey(itemLingoPlugin, dataKey);
        player.getPersistentDataContainer().remove(key);
    }
}
