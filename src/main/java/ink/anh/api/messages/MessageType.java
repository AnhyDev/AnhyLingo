package ink.anh.api.messages;

public enum MessageType {
    NORMAL("#0bdebb", ANSIColors.CYAN_BRIGHT), // Звичайне
    ESPECIALLY("#06d44e", ANSIColors.GREEN_BRIGHT), // Особливе 
    IMPORTANT("#FFFF00", ANSIColors.YELLOW_BRIGHT), // Важливе
    WARNING("#f54900", ANSIColors.YELLOW), // Попередження
    ERROR("#FF0000", ANSIColors.RED_BRIGHT), // Помилка, Червоний
    CRITICAL_ERROR("#8B0000", ANSIColors.RED); // Критична помилка, Темно-червоний

    private final String hexColor;
    private final String consoleColor;

    MessageType(String hexColor, String consoleColor) {
        this.hexColor = hexColor;
        this.consoleColor = consoleColor;
    }

    public String getColor(boolean forPlayer) {
        return forPlayer ? hexColor : consoleColor;
    }

    public String formatConsoleColor(String message) {
        return this.consoleColor + message + ANSIColors.RESET;
    }
}
