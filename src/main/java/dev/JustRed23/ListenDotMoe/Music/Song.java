package dev.JustRed23.ListenDotMoe.Music;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.JustRed23.ListenDotMoe.Music.details.Album;
import dev.JustRed23.ListenDotMoe.Music.details.Artist;
import dev.JustRed23.ListenDotMoe.Music.details.Duration;

import static dev.JustRed23.ListenDotMoe.Utils.JsonUtils.*;

public class Song {

    private int songID;
    private String title;

    private Artist artist;
    private Album album;
    private Duration duration;

    public Song(JsonObject json) {
        JsonObject d = json.get("d").getAsJsonObject();
        JsonObject song = d.get("song").getAsJsonObject();

        songID = parseInt(song, "id");
        title = parseString(song, "title");

        JsonArray artists = song.getAsJsonArray("artists");
        JsonObject artist = parseJsonObject(artists, 0);

        int artistID = parseInt(artist, "id");
        String artistNameEnglish = parseString(artist, "name");
        String artistNameRomaji = parseString(artist, "nameRomaji");
        String artistImage = parseString(artist, "image");

        this.artist = new Artist(artistID, artistNameEnglish, artistNameRomaji, artistImage);

        JsonArray albums = song.getAsJsonArray("albums");
        JsonObject album = parseJsonObject(albums, 0);

        int albumID = parseInt(album, "id");
        String albumNameEnglish = parseString(album, "name");
        String albumNameRomaji = parseString(album, "nameRomaji");
        String albumImage = parseString(album, "image");

        this.album = new Album(albumID, albumNameEnglish, albumNameRomaji, albumImage);

        int duration = parseInt(song, "duration");
        String dateTime = parseString(d, "startTime");
        String date = dateTime.substring(0, dateTime.indexOf("T"));
        String time = dateTime.substring(dateTime.indexOf("T") + 1, dateTime.lastIndexOf("."));

        this.duration = new Duration(duration, date, time);
    }

    public int getSongID() {
        return songID;
    }

    public String getTitle() {
        return title;
    }

    public Artist getArtist() {
        return artist;
    }

    public Album getAlbum() {
        return album;
    }
}