import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

public class Main {
    static int cursor = 0;

    public static void main(String[] args) {

    }

    public static boolean isValid(String stale, String latest, ArrayList<String> operations) {
        String temp = stale;

        for (String ops : operations) {
            // Parse the JSON operation
            JsonObject operation = JsonParser.parseString(ops).getAsJsonObject();
            String opType = operation.get("op").getAsString();

            switch (opType) {
                case "insert" -> {
                    String charsToInsert = operation.get("chars").getAsString();
                    temp = temp.substring(0, cursor) + charsToInsert + temp.substring(cursor);
                    cursor = cursor + charsToInsert.length();
                }
                case "delete" -> {
                    int count = operation.get("count").getAsInt();
                    if (temp.length() <= cursor + count) {
                        return false;
                    }
                    temp = temp.substring(0, cursor) + temp.substring(cursor + count);
                }
                case "skip" -> {
                    int count = operation.get("count").getAsInt();
                    if (temp.length() <= cursor + count) {
                        cursor = temp.length();
                        return false;
                    }
                    cursor += count;
                }
            }
        }
        return temp.equals(latest);
    }
}
