package ink.anh.lingo.listeners.protocol.server;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.events.ListenerPriority;

import ink.anh.lingo.api.lang.LanguageManager;
import ink.anh.lingo.listeners.protocol.AbstractPacketListener;
import ink.anh.lingo.listeners.protocol.ModificationState;
import ink.anh.lingo.messages.Logger;

public class PacketChat extends AbstractPacketListener {


    public PacketChat() {
        super(PacketType.Play.Server.CHAT);

        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        

        protocolManager.addPacketListener(this.packetAdapter = new PacketAdapter(lingoPlugin, ListenerPriority.NORMAL, 
                PacketType.Play.Server.CHAT) {
            
        	@Override
        	public void onPacketSending(PacketEvent event) {

            	if (lingoPlugin.getConfigurationManager().isDebugPacketShat()) {
            		Logger.warn(lingoPlugin, "NBT event.getPacketType(): " + event.getPacketType().name());
                    PacketContainer packet = event.getPacket();
                    StructureModifier<Object> fields = packet.getModifier();
                    for(int i = 0; i < fields.size(); i++) {
                        if (fields.read(i) != null) {
                            Class<?> fieldType = fields.read(i).getClass();
                            Logger.info(lingoPlugin, "Field " + i + " is of type: " + fieldType.getName());
                        }
                        Logger.warn(lingoPlugin, "Field " + i + ": " + fields.read(i));
                    }
                    Logger.info(lingoPlugin, "event.getPacket().getUUIDLists(): " + event.getPacket().getUUIDLists());
            	}
                handlePacket(event);
        	}
        });

        register(); 
    }

    @Override
	protected void handlePacket(PacketEvent event) {
        
        ModificationState modState = new ModificationState();
        String[] langs = getPlayerLanguage(event.getPlayer());
        
        try {
            Object chatComponents = event.getPacket().getChatComponents().read(0);
            if (chatComponents != null) {
                
                WrappedChatComponent wrappedChat = (WrappedChatComponent) chatComponents;
                String jsonChat = wrappedChat.getJson();
                String modifiedJson = modifyChat(jsonChat, langs, modState, "text");
                
                if (modState.isModified() && modifiedJson != null) {
                    WrappedChatComponent modifiedComponent = WrappedChatComponent.fromJson(modifiedJson);
                    event.getPacket().getChatComponents().write(0, modifiedComponent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	@Override
	public LanguageManager getLangMan() {
		return lingoPlugin.getLanguageChat();
	}
}
