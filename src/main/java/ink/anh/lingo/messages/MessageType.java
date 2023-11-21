package ink.anh.lingo.messages;

public enum MessageType {
    NORMAL("#0bdebb", "\u001B[94m"), // Звичайне
    ESPECIALLY("#06d44e", "\u001B[32m"), // Особливе 
    IMPORTANT("#FFFF00", "\u001B[33m"), // Важливе
    WARNING("#f54900", "\u001B[33m"), // Попередження
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
