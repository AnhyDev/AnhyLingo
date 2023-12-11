package ink.anh.lingo.listeners.protocol;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;

import ink.anh.api.messages.Logger;
import ink.anh.lingo.AnhyLingo;
import ink.anh.lingo.listeners.protocol.server.PacketSystemChat;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the registration and removal of packet listeners in the AnhyLingo plugin.
 * This class handles the initialization of packet listeners with their respective priority
 * and the addition or removal of these listeners from the ProtocolLibrary.
 */
public class PacketListenerManager {

    private ListenerPriority listenerPriority;

    /**
     * Gets the current listener priority for packet listeners.
     *
     * @return The listener priority.
     */
    public ListenerPriority getListenerPriority() {
        return listenerPriority;
    }

    /**
     * Initializes the PacketListenerManager, determining the listener priority and adding listeners.
     */
    public void initialize() {
        listenerPriority = determineListenerPriority();
        addListeners();
    }

    /**
     * Determines the listener priority for packet listeners.
     * Custom logic can be implemented here to dynamically set the priority.
     *
     * @return The determined ListenerPriority.
     */
    private ListenerPriority determineListenerPriority() {
        // Тут можна додати логіку визначення пріоритету слухача.
        // Наразі за замовчуванням встановлюється HIGHEST.
        return ListenerPriority.HIGHEST;
    }

    /**
     * Adds all defined packet listeners to the ProtocolLibrary.
     * Registers each listener with the determined priority.
     */
    public void addListeners() {
        List<AbstractPacketListener> listeners = new ArrayList<>();

        //listeners.add(new PacketChat());
        listeners.add(new PacketSystemChat());
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

    /**
     * Removes all packet listeners associated with the AnhyLingo plugin from the ProtocolLibrary.
     */
    public void removeListeners() {
        ProtocolLibrary.getProtocolManager().removePacketListeners(AnhyLingo.getInstance());
    }
}
