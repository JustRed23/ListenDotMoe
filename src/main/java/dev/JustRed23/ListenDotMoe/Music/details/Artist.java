package dev.JustRed23.ListenDotMoe.Music.details;

import dev.JustRed23.ListenDotMoe.ListenDotMoe;

public class Artist {

    private int artistID;
    private String artistNameEnglish;
    private String artistNameRomaji;
    private String artistImage = "";

    public Artist(int artistID, String artistNameEnglish, String artistNameRomaji, String artistImage) {
        if (artistID == -1) return;
        this.artistID = artistID;
        this.artistNameEnglish = artistNameEnglish;
        this.artistNameRomaji = artistNameRomaji;
        if (!artistImage.isEmpty())
            this.artistImage = ListenDotMoe.LDM_ARTISTS_ENDPOINT + artistImage;
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

    public String toString() {
        return "Artist{" +
                "artistID=" + artistID +
                ", artistNameEnglish='" + artistNameEnglish + '\'' +
                ", artistNameRomaji='" + artistNameRomaji + '\'' +
                ", artistImage='" + artistImage + '\'' +
                '}';
    }
}
