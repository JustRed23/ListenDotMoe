package dev.JustRed23.ListenDotMoe.Music;

import dev.JustRed23.ListenDotMoe.ListenDotMoe;

public class Info {
    public int songID;
    public String title;

    public int artistID;
    public String artistNameEnglish;
    public String artistNameRomaji;
    public String artistImage = ListenDotMoe.LDM_ARTISTS_ENDPOINT;

    public int albumID;
    public String albumNameEnglish;
    public String albumNameRomaji;
    public String albumImage = ListenDotMoe.LDM_ALBUM_ENDPOINT;

    public int durationMinutes;
    public int durationSeconds;

    public String songStartTime;
}
