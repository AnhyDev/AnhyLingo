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

import ink.anh.lingo.ItemLingo;
import ink.anh.lingo.utils.LangUtils;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;

public abstract class AbstractPacketListener {
    protected final PacketType packetType;
    protected PacketAdapter packetAdapter;
	public ItemLingo itemLingoPlugin;

    protected AbstractPacketListener(@Nonnull PacketType packetType) {
        this.packetType = packetType;
        this.itemLingoPlugin = ItemLingo.getInstance();
    }

    protected abstract void handlePacket(PacketEvent event);

    public abstract String getTranslatedText(String key, String lang);

    protected void register() {
        ProtocolLibrary.getProtocolManager().addPacketListener(packetAdapter);
    }

    public String getPlayerLanguage(Player player) {
        return LangUtils.getPlayerLanguage(player);
    }

    public String modifyChat(String originalJson, String playerLang, ModificationState modState, String textBlock) {
        Gson gson = new Gson();
        JsonElement chatElement = gson.fromJson(originalJson, JsonElement.class);
        JsonArray extraArray = chatElement.getAsJsonObject().getAsJsonArray("extra");

        if (extraArray != null) {
            for (int i = 0; i < extraArray.size(); i++) {
                JsonObject element = extraArray.get(i).getAsJsonObject();
                if (element.has(textBlock)) {
                    String text = element.get(textBlock).getAsString();
                    String modifiedText = processText(text, playerLang, modState);
                    if (modifiedText != null) {
                        element.addProperty(textBlock, modifiedText);
                    }
                }
            }
        }
        return gson.toJson(chatElement);
    }

    private String processText(String text, String playerLang, ModificationState modState) {
        boolean prependSpace = text.startsWith(" ");
        boolean appendSpace = text.endsWith(" ");

        String[] words = text.trim().split(" ");
        StringBuilder newText = new StringBuilder();

        if (prependSpace) {
            newText.append(" ");
        }

        boolean textModified = false;
        for (String word : words) {
            String replacement = getTranslatedText(word, playerLang);
            if (replacement != null) {
                newText.append(replacement);
                textModified = true;
                if (!modState.isModified()) {
                    modState.setModified(true);
                }
            } else {
                newText.append(word);
            }
            newText.append(" ");
        }

        if (!appendSpace && newText.length() > 0) {
            newText.setLength(newText.length() - 1);
        }

        return textModified ? newText.toString() : null;
    }
    
    public void reSetActionBar(PacketEvent event, String playerLang, ModificationState modState) {
    	PacketContainer packet = event.getPacket();
        StructureModifier<Object> fields = packet.getModifier();
        if (fields.read(1) != null && fields.read(1) instanceof String) {
        	String jsonActionBar = fields.read(1).toString();
        	String modifiedJson = modifyChat(jsonActionBar, playerLang, modState, "text");
        	packet.getModifier().write(1, modifiedJson);
        }
    }

    public class ModificationState {
        private boolean orModified = false;

        public boolean isModified() {
            return orModified;
        }

        public void setModified(boolean modified) {
            this.orModified = modified;
        }
    }
}
