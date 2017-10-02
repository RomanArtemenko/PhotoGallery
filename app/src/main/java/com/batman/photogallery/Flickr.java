package com.batman.photogallery;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Roman on 02.10.2017.
 */

public class Flickr {
    @SerializedName("photos") private Photos mPhotos;

    public Photos getPhotos() {
        return mPhotos;
    }

    public void setPhotos(Photos photos) {
        mPhotos = photos;
    }
}
