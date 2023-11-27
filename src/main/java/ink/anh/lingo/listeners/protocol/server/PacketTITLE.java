package ink.anh.lingo.listeners.protocol.server;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

import ink.anh.lingo.utils.SpigotUtils;
import ink.anh.lingo.utils.PaperUtils;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.chat.BaseComponent;
import ink.anh.lingo.api.lang.LanguageManager;
import ink.anh.lingo.listeners.protocol.AbstractPacketListener;
import ink.anh.lingo.listeners.protocol.ModificationState;
import ink.anh.lingo.messages.Logger;

public class PacketTITLE extends AbstractPacketListener {


    @SuppressWarnings("deprecation")
	public PacketTITLE() {
        super(PacketType.Play.Server.TITLE);

        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        

        protocolManager.addPacketListener(this.packetAdapter = new PacketAdapter(lingoPlugin, ListenerPriority.NORMAL,
                PacketType.Play.Server.TITLE) {
            
        	@Override
        	public void onPacketSending(PacketEvent event) {
        		
        		if (!lingoPlugin.getConfigurationManager().isPacketLingo()) {
        			return;
        		}

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
            	}
        	    
                //handlePacket(event);
        	}
        });
    }

    @Override
    protected void handlePacket(PacketEvent event) {
        
        ModificationState modState = new ModificationState();
        String[] langs = getPlayerLanguage(event.getPlayer());
        
        
        PacketContainer packet = event.getPacket();


        String jsonSystemChat = null;
        String modifiedJson;
        
        StructureModifier<WrappedChatComponent> componentModifier = packet.getChatComponents();
        WrappedChatComponent wrappedChatComponent = componentModifier.read(0);
        if (wrappedChatComponent != null) {
        	if (lingoPlugin.getConfigurationManager().isDebugPacketShat())
        	Logger.info(lingoPlugin, "contentField != null");
            jsonSystemChat = wrappedChatComponent.getJson();
        } else {
            StructureModifier<Object> modifier = packet.getModifier();
        	if (lingoPlugin.isSpigot()) {
        		StructureModifier<BaseComponent[]> component = modifier.withType(BaseComponent[].class);
                if (componentModifier.size() == 0) {
                    return;
                }
                BaseComponent[] read = component.read(0);
                if (read == null) {
                    return;
                }
                jsonSystemChat = SpigotUtils.serializeComponents(read);
                
        	}
        	if (jsonSystemChat == null) {
                StructureModifier<Component> component = packet.getModifier().withType(Component.class);
                Component read = component.read(0);
                if (read == null) {
                    return;
                }
                jsonSystemChat = PaperUtils.serializeComponent(read);
        		
        	}
        }

    	if (lingoPlugin.getConfigurationManager().isDebugPacketShat())
    	Logger.info(lingoPlugin, "jsonSystemChat: " + jsonSystemChat);
    	modifiedJson = modifyChat(jsonSystemChat, langs, modState, "text");

        // Запис модифікованого рядка назад у компонент
        if (modState.isModified() && modifiedJson != null) {
        	componentModifier.write(0, WrappedChatComponent.fromJson(modifiedJson));
        	if (lingoPlugin.getConfigurationManager().isDebugPacketShat())
        	Logger.info(lingoPlugin, "modifiedJson: " + modifiedJson);
        }
    }

	@Override
	public LanguageManager getLangMan() {
		return lingoPlugin.getLanguageSystemChat();
	}
}
