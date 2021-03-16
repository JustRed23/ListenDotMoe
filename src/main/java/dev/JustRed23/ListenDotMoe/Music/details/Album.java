package dev.JustRed23.ListenDotMoe.Music.details;

import dev.JustRed23.ListenDotMoe.ListenDotMoe;

public class Album {

    private int albumID;
    private String albumNameEnglish;
    private String albumNameRomaji;
    private String albumImage = ListenDotMoe.LDM_ALBUM_ENDPOINT;

    public Album(int albumID, String albumNameEnglish, String albumNameRomaji, String albumImage) {
        this.albumID = albumID;
        this.albumNameEnglish = albumNameEnglish;
        this.albumNameRomaji = albumNameRomaji;
        this.albumImage += albumImage;
    }

    public int getAlbumID() {
        return albumID;
    }

    public String getAlbumNameEnglish() {
        return albumNameEnglish;
    }

    public String getAlbumNameRomaji() {
        return albumNameRomaji;
    }

    public String getAlbumImage() {
        return albumImage;
    }
}
