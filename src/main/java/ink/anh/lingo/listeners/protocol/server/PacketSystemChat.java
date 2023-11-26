package ink.anh.lingo.listeners.protocol.server;

import java.util.Optional;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.EnumWrappers;
import ink.anh.lingo.utils.PaperUtils;
import net.kyori.adventure.text.Component;
import ink.anh.lingo.api.lang.LanguageManager;
import ink.anh.lingo.listeners.protocol.AbstractPacketListener;
import ink.anh.lingo.listeners.protocol.ModificationState;
import ink.anh.lingo.messages.Logger;

public class PacketSystemChat extends AbstractPacketListener {


    public PacketSystemChat() {
        super(PacketType.Play.Server.SYSTEM_CHAT);

        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        

        protocolManager.addPacketListener(this.packetAdapter = new PacketAdapter(lingoPlugin, ListenerPriority.NORMAL,
                PacketType.Play.Server.SYSTEM_CHAT) {
            
        	@Override
        	public void onPacketSending(PacketEvent event) {

            	if (lingoPlugin.getConfigurationManager().isDebugPacketShat()) {
            		Logger.warn(lingoPlugin, "NBT event.getPacketType(): " + event.getPacketType().name());
                    PacketContainer packet = event.getPacket();
                    StructureModifier<Object> fields = packet.getModifier();
                    for(int i = 0; i < fields.size(); i++) {
                        if (fields.read(i) != null) {
                            Class<?> fieldType = fields.read(i).getClass();
                            Logger.warn(lingoPlugin, "Field " + i + " is of type: " + fieldType.getName());
                        }
                        Logger.info(lingoPlugin, "Field " + i + ": " + fields.read(i));
                    }
            	}
        	    
                handlePacket(event);
        	}
        });
    }

    @Override
    protected void handlePacket(PacketEvent event) {
        
        ModificationState modState = new ModificationState();
        String[] langs = getPlayerLanguage(event.getPlayer());
        
        
        PacketContainer packet = event.getPacket();
        Optional<Boolean> isFiltered = packet.getMeta("psr_filtered_packet");
        if (!(isFiltered.isPresent() && isFiltered.get())) {

            StructureModifier<Boolean> booleans = packet.getBooleans();
            if (booleans.size() == 1) {
            	
            	if (lingoPlugin.getConfigurationManager().isDebugPacketShat())
                	Logger.warn(lingoPlugin, "booleans.read(0): " + booleans.read(0));
            	
                if (booleans.read(0)) {
                	reSetActionBar(event, langs, modState);
                    return;
                }
            } else if (packet.getIntegers().read(0) == EnumWrappers.ChatType.GAME_INFO.getId()) {
            	
            	if (lingoPlugin.getConfigurationManager().isDebugPacketShat())
            	Logger.warn(lingoPlugin, "packet.getIntegers().read(0) == EnumWrappers.ChatType.GAME_INFO.getId()");
            	
                return;
            }

            StructureModifier<String> stringModifier = packet.getStrings();
            StructureModifier<Component> componentModifier = packet.getModifier().withType(Component.class);
            String contentField = stringModifier.read(0);

            String jsonSystemChat;
            String modifiedJson;
            boolean isPaper = false;
            
            if (contentField != null) {
            	
            	if (lingoPlugin.getConfigurationManager().isDebugPacketShat())
            		Logger.info(lingoPlugin, "contentField != null");
            	
            	jsonSystemChat = contentField.toString();

            } else {
            	
            	if (lingoPlugin.getConfigurationManager().isDebugPacketShat())
            		Logger.info(lingoPlugin, "contentField == null");
            	
                Component read = componentModifier.read(0);
                if (read == null) {
                    return;
                }
                jsonSystemChat = PaperUtils.serializeComponent(read);
                isPaper = true;
            }

        	modifiedJson = modifyChat(jsonSystemChat, langs, modState, "text");

            // Запис модифікованого рядка назад у компонент
            if (modState.isModified() && modifiedJson != null) {
            	if (isPaper) {
                    // 1. Серіалізація зміненого JSON-рядка назад у об'єкт Component
                    Component modifiedComponent = PaperUtils.getPaperGsonComponentSerializer().deserialize(modifiedJson);

                    // 3. Запис зміненого об'єкту Component назад у пакет
                    componentModifier.write(0, modifiedComponent);
            	} else {
                	// Запис зміненого JSON-рядка назад у пакет
                	stringModifier.write(0, modifiedJson);
            	}
            }
        }
    }

	@Override
	public LanguageManager getLangMan() {
		return lingoPlugin.getLanguageSystemChat();
	}
}
