package com.moa.gallerypick.inter;

import android.app.Activity;
import android.content.Context;

import com.moa.gallerypick.widget.GalleryImageView;

import java.io.Serializable;


public interface ImageLoader extends Serializable {
    GalleryImageView displayImage(Activity activity, Context context, String path, GalleryImageView galleryImageView, int width, int height);

    void clearMemoryCache();
}
