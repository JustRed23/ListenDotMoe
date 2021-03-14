package dev.JustRed23.ListenDotMoe.Music;

import com.google.gson.*;
import dev.JustRed23.ListenDotMoe.ListenDotMoe;

public class Song {

    private Info songInfo = new Info();

    public Song(JsonObject json) {
        JsonObject d = json.get("d").getAsJsonObject();
        JsonObject song = d.get("song").getAsJsonObject();

        songInfo.songID = parseInt(song, "id");
        songInfo.title = parseString(song, "title");

        JsonArray artists = song.getAsJsonArray("artists");
        JsonObject artist = parseJsonObject(artists, 0);

        songInfo.artistID = parseInt(artist, "id");
        songInfo.artistNameEnglish = parseString(artist, "name");
        songInfo.artistNameRomaji = parseString(artist, "nameRomaji");
        songInfo.artistImage += parseString(artist, "image");

        JsonArray albums = song.getAsJsonArray("albums");
        JsonObject album = parseJsonObject(albums, 0);

        songInfo.albumID = parseInt(album, "id");
        songInfo.albumNameEnglish = parseString(album, "name");
        songInfo.albumNameRomaji = parseString(album, "nameRomaji");
        songInfo.albumImage += parseString(album, "image");

        int duration = parseInt(song, "duration");
        songInfo.durationMinutes = duration / 60;
        songInfo.durationSeconds = duration % 60;

        String dateTime = parseString(d, "startTime");
        String date = dateTime.substring(0, dateTime.indexOf("T"));
        String time = dateTime.substring(dateTime.indexOf("T") + 1, dateTime.lastIndexOf("."));

        songInfo.songStartTime = date + "@" + time;
    }

    public Info getSongInfo() {
        return songInfo;
    }

    private String parseString(JsonObject object, String value) {
        JsonElement json = object.get(value);
        return json instanceof JsonNull || json == null ? "" : json.getAsString();
    }

    private int parseInt(JsonObject object, String value) {
        JsonElement json = object.get(value);
        return json instanceof JsonNull || json == null ? -1 : json.getAsInt();
    }

    private JsonObject parseJsonObject(JsonArray object, int index) {
        JsonElement json = object.get(index);
        return json instanceof JsonNull || json == null ? new JsonObject() : json.getAsJsonObject();
    }
}