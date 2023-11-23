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

import ink.anh.lingo.AnhyLingo;
import ink.anh.lingo.utils.LangUtils;
import ink.anh.lingo.utils.StringUtils;
import ink.anh.lingo.utils.TypeText;

import org.bukkit.entity.Player;

public abstract class AbstractPacketListener {
    protected final PacketType packetType;
    protected PacketAdapter packetAdapter;
	public AnhyLingo itemLingoPlugin;

    protected AbstractPacketListener(PacketType packetType) {
        this.packetType = packetType;
        this.itemLingoPlugin = AnhyLingo.getInstance();
    }

    protected abstract void handlePacket(PacketEvent event);

    public abstract TypeText getTypeText();

    protected void register() {
        ProtocolLibrary.getProtocolManager().addPacketListener(packetAdapter);
    }

    public String[] getPlayerLanguage(Player player) {
        return LangUtils.getPlayerLanguage(player);
    }

    public String modifyChat(String originalJson, String[] langs, ModificationState modState, String textBlock) {
        Gson gson = new Gson();
        JsonElement chatElement = gson.fromJson(originalJson, JsonElement.class);
        JsonArray extraArray = chatElement.getAsJsonObject().getAsJsonArray("extra");

        if (extraArray != null) {
            for (int i = 0; i < extraArray.size(); i++) {
                JsonObject element = extraArray.get(i).getAsJsonObject();
                if (element.has(textBlock)) {
                    String text = element.get(textBlock).getAsString();
                    modState = StringUtils.translateKyeWorldModificationState(text, langs, getTypeText(), modState);
                    String modifiedText = modState.getTranslatedText();
                    if (modState.isModified()) {
                        element.addProperty(textBlock, modifiedText);
                    }
                }
            }
        }
        return gson.toJson(chatElement);
    }
    
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
