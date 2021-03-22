package dev.JustRed23.ListenDotMoe.Music.details;

import dev.JustRed23.ListenDotMoe.ListenDotMoe;

public class Album {

    private int albumID;
    private String albumNameEnglish;
    private String albumNameRomaji;
    private String albumImage = "";

    public Album(int albumID, String albumNameEnglish, String albumNameRomaji, String albumImage) {
        if (albumID == -1) return;
        this.albumID = albumID;
        this.albumNameEnglish = albumNameEnglish;
        this.albumNameRomaji = albumNameRomaji;
        if (!albumImage.isEmpty())
            this.albumImage = ListenDotMoe.LDM_ALBUM_ENDPOINT + albumImage;
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

    public String toString() {
        return "Album{" +
                "albumID=" + albumID +
                ", albumNameEnglish='" + albumNameEnglish + '\'' +
                ", albumNameRomaji='" + albumNameRomaji + '\'' +
                ", albumImage='" + albumImage + '\'' +
                '}';
    }
}
