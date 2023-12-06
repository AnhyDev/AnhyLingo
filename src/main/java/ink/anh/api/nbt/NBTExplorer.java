package ink.anh.api.nbt;

import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;
import org.bukkit.inventory.ItemStack;

public class NBTExplorer {

    public static NbtCompound getNBT(ItemStack item) {
        return (NbtCompound) NbtFactory.fromItemTag(item);
    }

    public static void setNBTValueFromString(ItemStack item, String key, String valueString) {
        NbtCompound compound = getNBT(item);
        Object parsedValue = NBTValueParser.parseValueByPrefix(valueString);
        
        if (parsedValue != null) {
            if (parsedValue instanceof String) {
                compound.put(key, (String) parsedValue);
            } else if (parsedValue instanceof Integer) {
                compound.put(key, (Integer) parsedValue);
            } else if (parsedValue instanceof Double) {
                compound.put(key, (Double) parsedValue);
            } else if (parsedValue instanceof int[]) {
                compound.put(key, (int[]) parsedValue);
            }
        }
        setNBT(item, compound);
    }

    public static void setNBT(ItemStack item, NbtCompound compound) {
        NbtFactory.setItemTag(item, compound);
    }

    public static String getNBTValue(ItemStack item, String key) {
        NbtCompound compound = getNBT(item);
        if (compound.containsKey(key)) {
            return compound.getString(key);
        }
        return null;
    }

    public static void setNBTValue(ItemStack item, String key, String value) {
        NbtCompound compound = getNBT(item);
        compound.put(key, value);
        setNBT(item, compound);
    }

    public static void removeNBTValue(ItemStack item, String key) {
        NbtCompound compound = getNBT(item);
        compound.remove(key);
        setNBT(item, compound);
    }

    public static void clearNBT(ItemStack item) {
        setNBT(item, NbtFactory.ofCompound(""));
    }
    
    public static void setDefaultNBTValue(ItemStack item, String key) {
        NbtCompound compound = getNBT(item);
        
        if (!compound.containsKey(key)) {
            return; // Ключ відсутній у NBT, тому немає чого робити
        }
        
        Object currentValue = compound.getValue(key);
        
        if (currentValue instanceof String) {
            compound.put(key, "");
        } else if (currentValue instanceof Integer) {
            compound.put(key, 0);
        } else if (currentValue instanceof Double) {
            compound.put(key, 0.0);
        } else if (currentValue instanceof Float) {
            compound.put(key, 0.0f);
        } else if (currentValue instanceof Long) {
            compound.put(key, 0L);
        } else if (currentValue instanceof Short) {
            compound.put(key, (short) 0);
        } else if (currentValue instanceof Byte) {
            compound.put(key, (byte) 0);
        } else if (currentValue instanceof int[]) {
            compound.put(key, new int[0]);
        } else {
            compound.putObject(key, null);
        }

        setNBT(item, compound);
    }

}

