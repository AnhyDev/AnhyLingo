package ink.anh.api.nbt;

public class NBTValueParser {

    /**
     * Parses input with a prefix to determine the type.
     * Examples:
     *   "int:12345"          -> for integers
     *   "double:3.14"        -> for doubles
     *   "bool:true"          -> for booleans
     *   "intarray:[1,2,3]"   -> for integer arrays
     *   "string:Hello world!" -> for strings
     */
    public static Object parseValueByPrefix(String input) {
        if (input.contains(":")) {
            String[] parts = input.split(":", 2);
            String prefix = parts[0].toLowerCase();
            String value = parts[1];

            try {
                switch (prefix) {
                    case "int":
                        return Integer.parseInt(value);
                    case "double":
                        return Double.parseDouble(value);
                    case "intarray":
                        return parseIntArray(value);
                    case "string":
                        return value; 
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return parse(input);
    }

    /**
     * Automatically tries to determine the type from the input.
     * Examples:
     *   "Hello world!" 	-> for strings
     *   "12345"            -> for integers
     *   "3.14"             -> for doubles
     *   "true" or "false"  -> for booleans
     *   "[1,2,3]"          -> for integer arrays
     */
    private static Object parse(String valueString) {
        try {
            if (valueString.startsWith("\"") && valueString.endsWith("\"")) {
                return parseString(valueString);
            } else if (valueString.matches("-?\\d+")) {
                return parseInt(valueString);
            } else if (valueString.matches("-?\\d+\\.\\d+")) {
                return parseDouble(valueString);
            } else if (valueString.startsWith("[") && valueString.endsWith("]")) {
                return parseIntArray(valueString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; 
    }

    private static String parseString(String value) {
        return value.substring(1, value.length() - 1);
    }

    private static int parseInt(String value) {
        return Integer.parseInt(value);
    }

    private static double parseDouble(String value) {
        return Double.parseDouble(value);
    }

    private static int[] parseIntArray(String value) {
        String[] parts = value.substring(1, value.length() - 1).split(",");
        int[] array = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            array[i] = Integer.parseInt(parts[i].trim());
        }
        return array;
    }
}
