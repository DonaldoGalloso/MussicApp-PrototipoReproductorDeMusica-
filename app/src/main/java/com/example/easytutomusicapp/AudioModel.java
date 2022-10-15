package com.example.easytutomusicapp;

import android.graphics.drawable.Drawable;
import android.net.Uri;

import java.io.Serializable;

public class AudioModel implements Serializable {
    String path;
    String title;
    String duration;
    String album;
    int fav;
    String idAlbum;
    String image;


    public AudioModel(String path, String title, String duration, String album, int fav, String image, String idAlbum) {
        this.path = path;
        this.title = title;
        this.duration = duration;
        this.album = album;
        this.fav = fav;
        this.image = image;
        this.idAlbum=idAlbum;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getFav() {
        return fav;
    }

    public void setFav(int fav) {
        this.fav = fav;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIdAlbum() {
        return idAlbum;
    }

    public void setIdAlbum(String idAlbum) {
        this.idAlbum = idAlbum;
    }
}
