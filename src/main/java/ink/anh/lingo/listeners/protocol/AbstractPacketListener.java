package ink.anh.lingo.listeners.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import ink.anh.api.lingo.ModificationState;
import ink.anh.api.lingo.Translator;
import ink.anh.api.lingo.lang.LanguageManager;
import ink.anh.api.utils.LangUtils;
import ink.anh.lingo.AnhyLingo;

import org.bukkit.entity.Player;

/**
 * Abstract base class for packet listeners in the AnhyLingo plugin.
 * This class provides common functionalities for handling packet modifications based on language settings.
 */
public abstract class AbstractPacketListener {
	public final PacketType packetType;
    protected PacketAdapter packetAdapter;
	public AnhyLingo lingoPlugin;

    /**
     * Constructor for AbstractPacketListener.
     *
     * @param packetType The PacketType that this listener will handle.
     */
    protected AbstractPacketListener(PacketType packetType) {
        this.packetType = packetType;
        this.lingoPlugin = AnhyLingo.getInstance();
    }

    /**
     * Abstract method to handle the packet event.
     *
     * @param event The PacketEvent to be handled.
     */
    protected abstract void handlePacket(PacketEvent event);

    /**
     * Gets the LanguageManager instance.
     *
     * @return The LanguageManager instance.
     */
    public abstract LanguageManager getLangMan();

    /**
     * Registers the packet listener with the ProtocolManager.
     */
    protected void register() {
        ProtocolLibrary.getProtocolManager().addPacketListener(packetAdapter);
    }

    /**
     * Retrieves the language settings for a player.
     *
     * @param player The player whose language settings are to be retrieved.
     * @return An array of language codes for the player.
     */
    public String[] getPlayerLanguage(Player player) {
        return LangUtils.getPlayerLanguage(player);
    }

    /**
     * Modifies chat components in a packet's JSON structure based on language translations.
     *
     * @param originalJson The original JSON string from the packet.
     * @param langs The language codes for translation.
     * @param modState The current state of modification.
     * @param textBlock The JSON key where the text resides.
     * @return The modified JSON string.
     */
    public String modifyChat(String originalJson, String[] langs, ModificationState modState, String textBlock) {
        Gson gson = new Gson();
        JsonElement chatElement = gson.fromJson(originalJson, JsonElement.class);
        JsonArray extraArray = chatElement.getAsJsonObject().getAsJsonArray("extra");

        if (extraArray != null) {
            for (int i = 0; i < extraArray.size(); i++) {
                JsonObject element = extraArray.get(i).getAsJsonObject();
                if (element.has(textBlock)) {
                    String text = element.get(textBlock).getAsString();
                    modState = Translator.translateKyeWorldModificationState(lingoPlugin.getGlobalManager(), text, langs, modState);
                    String modifiedText = modState.getTranslatedText();
                    if (modState.isModified()) {
                        element.addProperty(textBlock, modifiedText);
                    }
                }
            }
        }
        return gson.toJson(chatElement);
    }

    /**
     * Resets and translates the action bar text in a packet event.
     *
     * @param event The PacketEvent to be modified.
     * @param langs The language codes for translation.
     * @param modState The current state of modification.
     */
    public void reSetActionBar(PacketEvent event, String[] langs, ModificationState modState) {
    	PacketContainer packet = event.getPacket();
        StructureModifier<Object> fields = packet.getModifier();
        if (fields.read(1) != null && fields.read(1) instanceof String) {
        	String jsonActionBar = fields.read(1).toString();
        	String modifiedJson = modifyChat(jsonActionBar, langs, modState, "text");
        	packet.getModifier().write(1, modifiedJson);
        }
    }


}
