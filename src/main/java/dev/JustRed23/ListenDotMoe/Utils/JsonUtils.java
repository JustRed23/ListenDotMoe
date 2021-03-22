package dev.JustRed23.ListenDotMoe.Utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

public class JsonUtils {

    public static String parseString(JsonObject object, String value) {
        JsonElement json = object.get(value);
        return json instanceof JsonNull || json == null ? "" : json.getAsString();
    }

    public static int parseInt(JsonObject object, String value) {
        JsonElement json = object.get(value);
        return json instanceof JsonNull || json == null ? -1 : json.getAsInt();
    }

    public static JsonObject parseJsonObject(JsonArray object, int index) {
        JsonElement json;

        try {
            json = object.get(index);
        } catch (IndexOutOfBoundsException e) {
            return new JsonObject();
        }

        return json instanceof JsonNull || json == null ? new JsonObject() : json.getAsJsonObject();
    }
}
