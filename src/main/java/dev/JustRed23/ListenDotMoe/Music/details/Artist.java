package dev.JustRed23.ListenDotMoe.Music.details;

import dev.JustRed23.ListenDotMoe.ListenDotMoe;

public class Artist {

    private int artistID;
    private String artistNameEnglish;
    private String artistNameRomaji;
    private String artistImage = ListenDotMoe.LDM_ARTISTS_ENDPOINT;

    public Artist(int artistID, String artistNameEnglish, String artistNameRomaji, String artistImage) {
        this.artistID = artistID;
        this.artistNameEnglish = artistNameEnglish;
        this.artistNameRomaji = artistNameRomaji;
        this.artistImage += artistImage;
    }

    public int getArtistID() {
        return artistID;
    }

    public String getArtistNameEnglish() {
        return artistNameEnglish;
    }

    public String getArtistNameRomaji() {
        return artistNameRomaji;
    }

    public String getArtistImage() {
        return artistImage;
    }
}
