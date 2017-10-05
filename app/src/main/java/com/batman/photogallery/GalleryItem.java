package com.batman.photogallery;

/**
 * Created by Roman on 26.09.2017.
 */

public class GalleryItem {
    private String mCaption;
    private String mId;
    private String mUrl;

    public String getmId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getCaption() {
        return mCaption;
    }

    public void setCaption(String mCaption) {
        this.mCaption = mCaption;
    }

    @Override
    public String toString() {
        return mCaption;
    }
}
