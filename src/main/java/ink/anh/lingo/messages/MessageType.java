package ink.anh.lingo.messages;

public enum MessageType {
    NORMAL("#1D87E4", "\u001B[94m"), // Звичайне, Світло-синій
    ESPECIALLY("#FFFF00", "\u001B[33m"), // Особливе, Жовтий
    IMPORTANT("#00FF00", "\u001B[32m"), // Важливе, Зелений
    WARNING("#FFA500", "\u001B[33m"), // Попередження, Золотий
    ERROR("#FF0000", "\u001B[91m"), // Помилка, Червоний
    CRITICAL_ERROR("#8B0000", "\u001B[31m"); // Критична помилка, Темно-червоний

    private final String minecraftColor;
    private final String consoleColor;

    MessageType(String minecraftColor, String consoleColor) {
        this.minecraftColor = minecraftColor;
        this.consoleColor = consoleColor;
    }

    public String getColor(boolean forPlayer) {
        return forPlayer ? minecraftColor : consoleColor;
    }
}
