package ink.anh.lingo.command;

import ink.anh.lingo.ItemLingo;
import ink.anh.lingo.nbt.NBTExplorer;

import java.util.Arrays;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtList;

public class NBTSubCommand {
	
	private ItemLingo itemLingoPlugin;

	public NBTSubCommand(ItemLingo plugin) {
		this.itemLingoPlugin = plugin;
	}
	
    boolean execNBT(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("Command: /lingo nbt <params...>");
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
        ItemStack itemInHand = validatePlayerWithPermissionAndGetItemInHand(sender);
        if (itemInHand == null) {
            return false;
        }

        // Перевіряємо, чи передано достатньо аргументів
        if (args.length < 4) {
            sender.sendMessage("Command: /lingo nbt set <nbt_key> <params...>");
            return false;
        }

        String nbtKey = args[2];
        String nbtValueString = String.join(" ", java.util.Arrays.copyOfRange(args, 3, args.length));

        // Встановлюємо значення NBT
        NBTExplorer.setNBTValueFromString(itemInHand, nbtKey, nbtValueString);

        sender.sendMessage("NBT-значення було встановлено.");
        return true;
    }

    private boolean list(CommandSender sender) {
        ItemStack itemInHand = validatePlayerWithPermissionAndGetItemInHand(sender);
        if (itemInHand == null) {
            return false;
        }

        // Отримуємо NBT-теги для предмета
        NbtCompound compound = NBTExplorer.getNBT(itemInHand);

        if (compound == null || compound.getKeys().isEmpty()) {
            sender.sendMessage("У вашого предмета немає NBT-тегів.");
            return true;
        }

        // Виводимо список ключів
        sender.sendMessage("NBT-ключі для вашого предмета:");
        for (String key : compound.getKeys()) {
            sender.sendMessage("- " + key);
        }
        for (String key : compound.getKeys()) {
        	itemLingoPlugin.getLogger().info("- " + key);
        }

        return true;
    }

    private boolean info(CommandSender sender, String[] args) {
        ItemStack itemInHand = validatePlayerWithPermissionAndGetItemInHand(sender);
        if (itemInHand == null) {
            return false;
        }

        // Перевіряємо, чи передано ключ NBT-тегу
        if (args.length < 3) {
            sender.sendMessage("Command: /lingo nbt info <nbt_key>");
            return false;
        }

        String nbtKey = args[2];

        // Отримуємо NBT-теги для предмета
        NbtCompound compound = NBTExplorer.getNBT(itemInHand);

        if (compound == null || !compound.containsKey(nbtKey)) {
            sender.sendMessage("NBT-тег з таким ключем не існує.");
            return true;
        }

        // Виводимо значення NBT-тегу
        String value = nbtValueToString(compound, nbtKey);
        sender.sendMessage("NBT-" + nbtKey + ": " + value);

        // Відправляємо інформацію в логи серверу
        itemLingoPlugin.getLogger().info("NBT-" + nbtKey + " for player " + sender.getName() + ": " + value);

        return true;
    }

    private ItemStack validatePlayerWithPermissionAndGetItemInHand(CommandSender sender) {
        // Перевіряємо, чи є викликач команди гравцем
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command is only available to players.");
            return null;
        }

        Player player = (Player) sender;

        // Перевіряємо наявність дозволу
        if (!player.hasPermission("itemlingo.manager")) {
            sender.sendMessage("You do not have permission to use this command.");
            return null;
        }

        // Перевіряємо, чи має гравець предмет в руці
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand == null || itemInHand.getType().isAir()) {
            sender.sendMessage("You have no item in your hand.");
            return null;
        }

        return itemInHand;
    }
    
    public String nbtValueToString(NbtCompound compound, String key) {
        if (compound == null || !compound.containsKey(key)) {
            return null;
        }

        Object value = compound.getObject(key);
        if (value instanceof Byte) {
            return value.toString();
        } else if (value instanceof Double) {
            return value.toString();
        } else if (value instanceof Float) {
            return value.toString();
        } else if (value instanceof Integer) {
            return value.toString();
        } else if (value instanceof Long) {
            return value.toString();
        } else if (value instanceof Short) {
            return value.toString();
        } else if (value instanceof byte[]) {
            return Arrays.toString((byte[]) value);
        } else if (value instanceof NbtCompound) {
            return value.toString(); // Або скоректоване представлення для NbtCompound
        } else if (value instanceof int[]) {
            return Arrays.toString((int[]) value);
        } else if (value instanceof long[]) {
            return Arrays.toString((long[]) value);
        } else if (value instanceof float[]) {
            return Arrays.toString((float[]) value);
        } else if (value instanceof double[]) {
            return Arrays.toString((double[]) value);
        } else if (value instanceof String) {
            return (String) value;
        } else if (value instanceof NbtList<?>) {
            return value.toString(); // Або скоректоване представлення для NbtList
        } else {
            return value != null ? value.toString() : "null";
        }
    }

}
