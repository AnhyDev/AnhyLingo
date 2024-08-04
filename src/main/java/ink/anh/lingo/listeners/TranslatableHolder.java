package ink.anh.lingo.listeners;

import org.bukkit.inventory.InventoryHolder;

public interface TranslatableHolder extends InventoryHolder {
    boolean shouldPreventTranslation();
}
