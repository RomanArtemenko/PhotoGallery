package com.batman.photogallery;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Roman on 02.10.2017.
 */

public class Photo {
    @SerializedName("id") private String mId;
    @SerializedName("title") private String mTitle;
    @SerializedName("url_s") private String mUrl;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    @Override
    public String toString() {
        return  String.format("Id  : %1s ; Title : %2s ; URLs :  %3s" , this.mId, this.mTitle, this.mUrl);
    }
}
