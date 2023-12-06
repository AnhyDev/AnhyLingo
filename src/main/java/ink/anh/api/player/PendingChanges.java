package ink.anh.api.player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.persistence.PersistentDataType;

import ink.anh.lingo.AnhyLingo;

// Клас для зберігання запланованих змін
// Клас поки не використовується, заготовка на майбутнє
public class PendingChanges {

    private AnhyLingo lingoPlugin;

    public PendingChanges() {
        this.lingoPlugin = AnhyLingo.getInstance();
    }
    
    private static final Map<UUID, Map<String, String>> pendingChanges = new HashMap<>();

    // Метод для додавання запланованих змін
    private void addChange(UUID playerUUID, String dataKey, String[] values) {
        String combined = String.join(",", values);
        pendingChanges.computeIfAbsent(playerUUID, k -> new HashMap<>()).put(dataKey, combined);
    }

    // Метод для застосування змін
    public void applyChanges(Player player) {
        UUID playerUUID = player.getUniqueId();
        if (pendingChanges.containsKey(playerUUID)) {
            Map<String, String> changes = pendingChanges.get(playerUUID);
            for (Map.Entry<String, String> entry : changes.entrySet()) {
                NamespacedKey key = new NamespacedKey(lingoPlugin, entry.getKey());
                player.getPersistentDataContainer().set(key, PersistentDataType.STRING, entry.getValue());
            }
            pendingChanges.remove(playerUUID); // Видалення застосованих змін
        }
    }
    public void setOfflinePlayerCustomData(UUID playerUUID, String dataKey, String[] values) {
        Bukkit.getScheduler().runTaskAsynchronously(lingoPlugin, () -> {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);
            if (offlinePlayer.hasPlayedBefore()) {
                Bukkit.getScheduler().runTask(lingoPlugin, () -> {
                    Player player = offlinePlayer.getPlayer();
                    if (player != null) {
                        // Гравець в мережі, можна змінити дані
                        new PlayerData().setCustomData(player, dataKey, values);
                    } else {
                        // Гравець офлайн, додаємо зміни до списку запланованих змін
                        addChange(playerUUID, dataKey, values);
                    }
                });
            }
        });
    }
}
