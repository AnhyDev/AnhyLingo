package ink.anh.lingo.listeners.protocol;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;

import ink.anh.api.messages.Logger;
import ink.anh.lingo.AnhyLingo;
import ink.anh.lingo.listeners.protocol.server.PacketTITLE;
import ink.anh.lingo.listeners.protocol.server.PacketChat;
import ink.anh.lingo.listeners.protocol.server.PacketSystemChat;

import java.util.ArrayList;
import java.util.List;

public class PacketListenerManager {

    private ListenerPriority listenerPriority;

    public ListenerPriority getListenerPriority() {
        return listenerPriority;
    }

    public void initialize() {
        listenerPriority = determineListenerPriority();
        addListeners();
    }

    private ListenerPriority determineListenerPriority() {
        // Тут можна додати логіку визначення пріоритету слухача.
        // Наразі за замовчуванням встановлюється HIGHEST.
        return ListenerPriority.HIGHEST;
    }

    public void addListeners() {
        List<AbstractPacketListener> listeners = new ArrayList<>();

        listeners.add(new PacketChat());
        listeners.add(new PacketSystemChat());
        listeners.add(new PacketTITLE());
        // ... додати інших слухачів

        for (AbstractPacketListener listener : listeners) {
            try {
                listener.register();
            } catch (Throwable throwable) {
            	Logger.warn(AnhyLingo.getInstance(), "Unable to register listener " + listener.getClass().getSimpleName() + ":");
                throwable.printStackTrace();
            }
        }
    }

    public void removeListeners() {
        ProtocolLibrary.getProtocolManager().removePacketListeners(AnhyLingo.getInstance());
    }
}
