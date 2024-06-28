package ink.anh.lingo.command;

import ink.anh.api.messages.MessageType;
import ink.anh.api.nbt.NBTExplorer;
import ink.anh.lingo.AnhyLingo;
import ink.anh.lingo.Permissions;

import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

/**
 * Class handling the NBT (Named Binary Tag) related subcommands for the AnhyLingo plugin.
 * This class provides functionality to manipulate and retrieve NBT data from items in-game.
 */
public class NBTSubCommand {

    private AnhyLingo lingoPlugin;

    /**
     * Constructor for NBTSubCommand.
     *
     * @param plugin The AnhyLingo plugin instance.
     */
    public NBTSubCommand(AnhyLingo plugin) {
        this.lingoPlugin = plugin;
    }

    /**
     * Executes the appropriate NBT related command based on the arguments.
     *
     * @param sender The sender of the command.
     * @param args Arguments passed with the command.
     * @return true if the command was successfully executed, otherwise false.
     */
    boolean execNBT(CommandSender sender, String[] args) {
        if (args.length < 2) {
            return false;
        }
        switch (args[1].toLowerCase()) {
            case "set":
                return set(sender, args);
            case "list":
                return list(sender);
            case "info":
                return info(sender, args);
        }
        return false;
    }

    private boolean set(CommandSender sender, String[] args) {
        ItemStack itemInHand = validatePlayerWithPermissionAndGetItemInHand(sender, Permissions.NBT_SET);
        if (itemInHand == null) {
            return true;
        }

        // Перевіряємо, чи передано достатньо аргументів
        if (args.length < 4) {
            sendMessage(sender, "lingo_err_command_format /lingo nbt set <nbt_key> <params...>", MessageType.WARNING);
            return true;
        }

        String nbtKey = args[2];
        String nbtValueString = String.join(" ", java.util.Arrays.copyOfRange(args, 3, args.length));

        // Встановлюємо значення NBT
        NBTExplorer.setNBTValueFromString(itemInHand, nbtKey, nbtValueString);

        sendMessage(sender, "lingo_NBT_value_set ", MessageType.NORMAL);
        return true;
    }

    private boolean list(CommandSender sender) {
        ItemStack itemInHand = validatePlayerWithPermissionAndGetItemInHand(sender, Permissions.NBT_LIST);
        if (itemInHand == null) {
            return true;
        }

        // Отримуємо NBT-теги для предмета
        ItemMeta itemMeta = itemInHand.getItemMeta();
        if (itemMeta == null) {
            sendMessage(sender, "lingo_err_item_no_NBT_tags ", MessageType.WARNING);
            return true;
        }

        PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();

        if (dataContainer.getKeys().isEmpty()) {
            sendMessage(sender, "lingo_err_item_no_NBT_tags ", MessageType.WARNING);
            return true;
        }

        // Виводимо список ключів
        sendMessage(sender, "lingo_NBT_keys_for_item ", MessageType.NORMAL);
        for (NamespacedKey key : dataContainer.getKeys()) {
            sendMessage(sender, "- " + key.getKey(), MessageType.ESPECIALLY);
            lingoPlugin.getLogger().info("- " + key.getKey());
        }

        return true;
    }

    private boolean info(CommandSender sender, String[] args) {
        ItemStack itemInHand = validatePlayerWithPermissionAndGetItemInHand(sender, Permissions.NBT_INFO);
        if (itemInHand == null) {
            return true;
        }

        // Перевіряємо, чи передано ключ NBT-тегу
        if (args.length < 3) {
            sendMessage(sender, "lingo_err_command_format /lingo nbt info <nbt_key>", MessageType.WARNING);
            return true;
        }

        String nbtKey = args[2];

        // Отримуємо NBT-теги для предмета
        String value = NBTExplorer.getNBTValue(itemInHand, nbtKey);

        if (value == null) {
            sendMessage(sender, "lingo_err_NBT_tag_not_exist ", MessageType.WARNING);
            return true;
        }

        // Виводимо значення NBT-тегу
        sendMessage(sender, "NBT-" + nbtKey + ": " + value, MessageType.NORMAL);

        // Відправляємо інформацію в логи серверу
        lingoPlugin.getLogger().info("NBT-" + nbtKey + " for player " + sender.getName() + ": " + value);

        return true;
    }

    private ItemStack validatePlayerWithPermissionAndGetItemInHand(CommandSender sender, String permission) {
        // Перевіряємо, чи є викликач команди гравцем
        if (!(sender instanceof Player)) {
            sendMessage(sender, "lingo_err_command_only_player ", MessageType.ERROR);
            return null;
        }

        Player player = (Player) sender;

        // Перевіряємо наявність дозволу
        if (!player.hasPermission(permission)) {
            sendMessage(sender, "lingo_err_not_have_permission ", MessageType.ERROR);
            return null;
        }

        // Перевіряємо, чи має гравець предмет в руці
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand == null || itemInHand.getType().isAir()) {
            sendMessage(sender, "lingo_err_have_nothing_hand ", MessageType.WARNING);
            return null;
        }

        return itemInHand;
    }

    private void sendMessage(CommandSender sender, String message, MessageType type) {
        AnswerToCommand.sendMessage(lingoPlugin.getGlobalManager(), sender, message, type, true);
    }

}
