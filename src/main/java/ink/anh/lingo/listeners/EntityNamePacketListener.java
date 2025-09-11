package ink.anh.lingo.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import ink.anh.api.lingo.Translator;
import ink.anh.api.utils.LangUtils;
import ink.anh.lingo.AnhyLingo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class EntityNamePacketListener {

    public static void entityNameTranslate() {
        ProtocolLibrary.getProtocolManager().addPacketListener(
            new PacketAdapter(AnhyLingo.getInstance(), PacketType.Play.Server.ENTITY_METADATA) {
                @Override
                public void onPacketSending(PacketEvent event) {
                    if (event.isCancelled()) return;

                    PacketContainer packet = event.getPacket();
                    Entity entity = packet.getEntityModifier(event.getPlayer().getWorld()).read(0);
                    if (entity == null) {

                        return;
                    }

                    List<WrappedDataValue> dataValues = packet.getDataValueCollectionModifier().read(0);
                    if (dataValues == null || dataValues.isEmpty()) {

                        return;
                    }

                    boolean customNameFound = false;
                    String originalName = null;
                    String originalJson = null;

                    for (WrappedDataValue value : dataValues) {
                        if (value.getIndex() == 2) {
                            Object rawValue = value.getValue();

                            if (rawValue instanceof Optional) {
                                Optional<?> optional = (Optional<?>) rawValue;
                                if (optional.isPresent()) {
                                    Object component = optional.get();
                                    if (component instanceof WrappedChatComponent) {
                                        WrappedChatComponent chatComponent = (WrappedChatComponent) component;
                                        originalJson = chatComponent.getJson();
                                        originalName = extractTextFromJson(originalJson);
                                    } else {
                                        originalName = component.toString();
                                        originalJson = "\"" + originalName + "\"";
                                    }
                                }
                            }
                            customNameFound = true;
                            break;
                        }
                    }

                    if (customNameFound) {
                        if (originalName != null && originalJson != null) {
                            Player player = event.getPlayer();
                            String[] langs = player != null ? LangUtils.getPlayerLanguage(player) :
                                new String[] {AnhyLingo.getInstance().getGlobalManager().getDefaultLang()};
                            String translatedName = Translator.translateKyeWorld(
                                AnhyLingo.getInstance().getGlobalManager(), originalName, langs
                            );

                            if (translatedName.startsWith("\"") && translatedName.endsWith("\"")) {
                                translatedName = translatedName.substring(1, translatedName.length() - 1);
                            }

                            if (!translatedName.equals(originalName)) {
                                String formattedTranslatedName = applyColorFromOriginal(originalJson, translatedName);

                                List<WrappedDataValue> newDataValues = new ArrayList<>(dataValues);
                                WrappedDataWatcher.Serializer chatSerializer = WrappedDataWatcher.Registry.getChatComponentSerializer(true);
                                newDataValues.removeIf(value -> value.getIndex() == 2);

                                try {
                                    JsonObject jsonObject = JsonParser.parseString(originalJson).getAsJsonObject();
                                    jsonObject.remove("extra");
                                    jsonObject.addProperty("text", translatedName);
                                    newDataValues.add(new WrappedDataValue(
                                        2,
                                        chatSerializer,
                                        Optional.of(WrappedChatComponent.fromJson(jsonObject.toString()).getHandle())
                                    ));
                                } catch (Exception e) {
                                    newDataValues.add(new WrappedDataValue(
                                        2,
                                        chatSerializer,
                                        Optional.of(WrappedChatComponent.fromText(formattedTranslatedName).getHandle())
                                    ));
                                }

                                newDataValues.removeIf(value -> value.getIndex() == 3);
                                newDataValues.add(new WrappedDataValue(
                                    3,
                                    WrappedDataWatcher.Registry.get(Boolean.class),
                                    true
                                ));

                                packet.getDataValueCollectionModifier().write(0, newDataValues);
                                event.setPacket(packet);
                            }
                        }
                    }
                }
            }
        );
    }

    private static String extractTextFromJson(String json) {
        if (json == null) return null;

        // Парсимо JSON, якщо це об’єкт
        try {
            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
            if (jsonObject.has("text")) {
                return stripColorCodes(jsonObject.get("text").getAsString());
            }
        } catch (Exception e) {
            // Якщо не JSON, обробляємо як рядок
        }

        // Складний випадок: {"text":"","extra":["value"]}
        if (json.contains("\"extra\":[")) {
            int extraStart = json.indexOf("\"extra\":[") + 9;
            int extraEnd = json.lastIndexOf("]");
            String extraContent = json.substring(extraStart, extraEnd);
            StringBuilder text = new StringBuilder();
            for (String part : extraContent.split(",")) {
                part = part.trim();
                if (part.startsWith("\"") && part.endsWith("\"")) {
                    text.append(part.substring(1, part.length() - 1));
                } else if (part.contains("\"text\":\"")) {
                    int textStart = part.indexOf("\"text\":\"") + 8;
                    int textEnd = part.indexOf("\"", textStart);
                    if (textEnd != -1) {
                        text.append(part.substring(textStart, textEnd));
                    }
                }
            }
            return stripColorCodes(text.toString());
        }

        // Якщо це просто рядок у лапках (наприклад, "cook_ostin")
        if (json.startsWith("\"") && json.endsWith("\"")) {
            return stripColorCodes(json.substring(1, json.length() - 1));
        }

        // Якщо це рядок із кодами кольорів (наприклад, §6cook_ostin)
        return stripColorCodes(json);
    }

    private static String applyColorFromOriginal(String originalJson, String translatedName) {
        if (originalJson.contains("§") || originalJson.contains("&")) {
            int colorIndex = originalJson.indexOf("§") != -1 ? originalJson.indexOf("§") : originalJson.indexOf("&");
            if (colorIndex >= 0 && colorIndex + 1 < originalJson.length()) {
                char colorCode = originalJson.charAt(colorIndex + 1);
                return "§" + colorCode + translatedName.replaceAll("[§&]" + colorCode, "");
            }
        }
        return translatedName;
    }

    private static String stripColorCodes(String text) {
        return text.replaceAll("[§&][0-9a-fA-F]", "");
    }
}