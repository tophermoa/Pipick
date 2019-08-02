package com.moa.pipick.loader;

import android.app.Activity;
import android.content.Context;

import com.bumptech.glide.Glide;
import com.moa.gallerypick.inter.ImageLoader;
import com.moa.gallerypick.widget.GalleryImageView;
import com.moa.pipick.R;


public class GlideImageLoader implements ImageLoader {

    private final static String TAG = "GlideImageLoader";

    @Override
    public GalleryImageView displayImage(Activity activity, Context context, String path, GalleryImageView galleryImageView, int width, int height) {
        Glide.with(context)
                .load(path)
                .placeholder(R.mipmap.gallery_pick_photo)
                .centerCrop()
                .into(galleryImageView);
        return galleryImageView;
    }

    @Override
    public void clearMemoryCache() {

    }
}
