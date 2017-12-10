package com.kittu.mediaplayer;

/**
 * Created by user on 02-11-2017.
 */

public class SongModel {
    public String songName,artistName,songUrl;

    public SongModel(String songName, String artistName, String songUrl) {
        this.songName = songName;
        this.artistName = artistName;
        this.songUrl = songUrl;
    }


    public void setSongName(String songName) {
        this.songName = songName;
    }


    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
    }


    public String getSongname() {
        return songName;
    }

    public String getArtistname() {
        return artistName;
    }
}
