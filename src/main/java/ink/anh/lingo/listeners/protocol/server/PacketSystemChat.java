package ink.anh.lingo.listeners.protocol.server;

import java.util.Optional;

import org.bukkit.Bukkit;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

import net.kyori.adventure.text.Component;
import ink.anh.api.lingo.ModificationState;
import ink.anh.api.lingo.lang.LanguageManager;
import ink.anh.api.messages.Logger;
import ink.anh.api.utils.PaperUtils;
import ink.anh.lingo.AnhyLingo;
import ink.anh.lingo.listeners.protocol.AbstractPacketListener;

/**
 * Listens to system chat packets and modifies them for language localization.
 * Extends AbstractPacketListener to implement specific functionality for system chat packet modification.
 */
public class PacketSystemChat extends AbstractPacketListener {
    /**
     * Constructor for PacketSystemChat.
     * Sets up the listener for Server.SYSTEM_CHAT packet type with normal priority.
     */
    public PacketSystemChat() {
        super(getPacketType());

        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        

        protocolManager.addPacketListener(this.packetAdapter = new PacketAdapter(lingoPlugin, ListenerPriority.NORMAL, packetType) {
            
        	@Override
        	public void onPacketSending(PacketEvent event) {
            	if (lingoPlugin.getGlobalManager().isDebugPacketShat()) {
            		Logger.warn(lingoPlugin, "PacketType: " + event.getPacketType().name());
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

        		
        		if (!lingoPlugin.getGlobalManager().isPacketLingo()) {
        			return;
        		}
        	    
                handlePacket(event);
        	}
        });
    }
	
	private static PacketType getPacketType() {
		double ver = getCurrentServerVersion();

		if (AnhyLingo.getInstance().getGlobalManager().isDebug()) {
			Logger.info(AnhyLingo.getInstance(), "CurrentServerVersion: " + ver);
		}
		
        if (ver < 1.19) {
        	return PacketType.Play.Server.CHAT;
        }
    	return PacketType.Play.Server.SYSTEM_CHAT;
        
	}

    /**
     * Handles the modification of system chat packets.
     * This method is called when a system chat packet is being sent to a player, allowing modification of its content.
     *
     * @param event The PacketEvent to be handled.
     */
    @Override
	protected void handlePacket(PacketEvent event) {
	    PacketContainer packet = event.getPacket();
        StructureModifier<Object> fields = packet.getModifier();
        boolean processed = false;
        for(int i = 0; i < fields.size(); i++) {
            if (fields.read(i) != null) {
                Class<?> fieldType = fields.read(i).getClass();
                String className = fieldType.getName();
                if (className.equals("net.minecraft.network.chat.IChatMutableComponent") || className.equals("net.minecraft.network.chat.MutableComponent")) {
                	handleWrappedChatComponent(event);
                	processed = true;
                } else if (className.equals("io.papermc.paper.adventure.AdventureComponent")) {
                	handleAdventureComponent(event);
                    processed = true;
    	        }
            }
        }
	    if (processed) {
	        return;
	    } else {
	        // Інакше, продовжуємо зі стандартною обробкою
	        double currentVersion = getCurrentServerVersion();
            if (currentVersion < 1.19) {
                if(!handleWrappedChatComponent(event)) {
                	handleChatPacketForOldVersions(event);
                }
            } else {
	            handleStructureModifier(event);
	        }
	    }
	}

    private void handleAdventureComponent(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        StructureModifier<Component> components = packet.getModifier().withType(Component.class);
        if (components.size() > 0 && components.read(0) != null) {
            Component originalComponent = components.read(0);
            modifyAndWriteComponent(event, originalComponent);
        } else {
            if (lingoPlugin.getGlobalManager().isDebugPacketShat()) {
                Logger.warn(lingoPlugin, "No AdventureComponent found in the packet.");
            }
        }
    }

    private void handleChatPacketForOldVersions(PacketEvent event) {
        ModificationState modState = new ModificationState();
        String[] langs = getPlayerLanguage(event.getPlayer());

        PacketContainer packet = event.getPacket();
        StructureModifier<Component> components = packet.getModifier().withType(Component.class);

        if (components.size() > 0 && components.read(0) != null) {
            Component component = components.read(0);
            String jsonSystemChat = PaperUtils.serializeComponent(component);
            String modifiedJson = modifyChat(jsonSystemChat, langs, modState, "text");

            if (modState.isModified() && modifiedJson != null) {
                Component modifiedComponent = PaperUtils.getPaperGsonComponentSerializer().deserialize(modifiedJson);
                components.write(0, modifiedComponent);
            }
        }
    }

    private void modifyAndWriteComponent(PacketEvent event, Component originalComponent) {
        ModificationState modState = new ModificationState();
        String[] langs = getPlayerLanguage(event.getPlayer());

        String jsonSystemChat = PaperUtils.serializeComponent(originalComponent);
        String modifiedJson = modifyChat(jsonSystemChat, langs, modState, "text");

        if (modState.isModified() && modifiedJson != null) {
            Component modifiedComponent = PaperUtils.getPaperGsonComponentSerializer().deserialize(modifiedJson);
            event.getPacket().getModifier().write(0, modifiedComponent);

            if (lingoPlugin.getGlobalManager().isDebugPacketShat()) {
                Logger.info(lingoPlugin, "Component modified and written back to the packet: " + modifiedJson);
            }
        } else {
            if (lingoPlugin.getGlobalManager().isDebugPacketShat()) {
                Logger.info(lingoPlugin, "No modifications were made to the Component.");
            }
        }
    }

    private boolean handleWrappedChatComponent(PacketEvent event) {
        ModificationState modState = new ModificationState();
        String[] langs = getPlayerLanguage(event.getPlayer());

        PacketContainer packet = event.getPacket();

        try {
            StructureModifier<WrappedChatComponent> chatComponents = packet.getChatComponents();
            if (chatComponents.size() > 0 && chatComponents.read(0) != null) {
                WrappedChatComponent wrappedChat = chatComponents.read(0);
                String jsonChat = wrappedChat.getJson();
                String modifiedJson = modifyChat(jsonChat, langs, modState, "text");

                if (modState.isModified() && modifiedJson != null) {
                    WrappedChatComponent modifiedComponent = WrappedChatComponent.fromJson(modifiedJson);
                    chatComponents.write(0, modifiedComponent);
                    return true;
                }
            }
        } catch (Exception e) {
            // Якщо виникла помилка, повертаємо false
            return false;
        }

        return false;
    }

    private void handleStructureModifier(PacketEvent event) {
        ModificationState modState = new ModificationState();
        String[] langs = getPlayerLanguage(event.getPlayer());

        PacketContainer packet = event.getPacket();
        Optional<Boolean> isFiltered = packet.getMeta("psr_filtered_packet");
        if (!(isFiltered.isPresent() && isFiltered.get())) {
            StructureModifier<Boolean> booleans = packet.getBooleans();
            if (booleans.size() == 1 && booleans.read(0)) {
                reSetActionBar(event, langs, modState);
                return;
            }

            StructureModifier<String> stringModifier = packet.getStrings();
            if (lingoPlugin.getGlobalManager().isDebugPacketShat()) Logger.info(lingoPlugin, "Attempting to read String field at index 0.");
            if (lingoPlugin.getGlobalManager().isDebugPacketShat()) Logger.info(lingoPlugin, "String fields count: " + stringModifier.size());

            if (stringModifier.size() > 0) {
                try {
                    String contentField = stringModifier.read(0);
                    if (lingoPlugin.getGlobalManager().isDebugPacketShat()) Logger.info(lingoPlugin, "Successfully read String field: " + contentField);
                } catch (Exception e) {
                    if (lingoPlugin.getGlobalManager().isDebugPacketShat()) Logger.error(lingoPlugin, "Error reading String field at index 0: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                if (lingoPlugin.getGlobalManager().isDebugPacketShat()) Logger.warn(lingoPlugin, "No String fields available in the packet.");
            }

            StructureModifier<Component> componentModifier = packet.getModifier().withType(Component.class);
            String contentField = stringModifier.read(0);

            String jsonSystemChat;
            String modifiedJson;
            boolean isPaper = false;

            if (contentField != null) {
                jsonSystemChat = contentField.toString();
            } else {
                Component read = componentModifier.read(0);
                if (read == null) {
                    return;
                }
                jsonSystemChat = PaperUtils.serializeComponent(read);
                isPaper = true;
            }

            modifiedJson = modifyChat(jsonSystemChat, langs, modState, "text");

            if (modState.isModified() && modifiedJson != null) {
                if (isPaper) {
                    Component modifiedComponent = PaperUtils.getPaperGsonComponentSerializer().deserialize(modifiedJson);
                    componentModifier.write(0, modifiedComponent);
                } else {
                    stringModifier.write(0, modifiedJson);
                }
            }
        }
    }

    /**
     * Gets the LanguageManager instance from the plugin.
     *
     * @return The LanguageManager instance.
     */
	@Override
	public LanguageManager getLangMan() {
		return lingoPlugin.getGlobalManager().getLanguageManager();
	}
	
	public static double getCurrentServerVersion() {
	    String versionString = Bukkit.getBukkitVersion().split("-")[0];
	    String[] splitVersion = versionString.split("\\.");

	    try {
	        int major = Integer.parseInt(splitVersion[0]);
	        int minor = splitVersion.length > 1 ? Integer.parseInt(splitVersion[1]) : 0;
	        double version = major + minor / (minor >= 10 ? 100.0 : 10.0);
	        return version;
	    } catch (NumberFormatException e) {
	        e.printStackTrace();
	        return 0;
	    }
	}
}
