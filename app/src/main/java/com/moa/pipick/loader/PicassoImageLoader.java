package com.moa.pipick.loader;

import android.app.Activity;
import android.content.Context;

import com.squareup.picasso.Picasso;
import com.moa.gallerypick.inter.ImageLoader;
import com.moa.gallerypick.widget.GalleryImageView;
import com.moa.pipick.R;


public class PicassoImageLoader implements ImageLoader {

    private final static String TAG = "PicassoImageLoader";

    @Override
    public GalleryImageView displayImage(Activity activity, Context context, String path, GalleryImageView galleryImageView, int width, int height) {

        Picasso.with(context)
                .load("file://" + path)
                .resize(width, height)
                .placeholder(R.mipmap.gallery_pick_photo)
                .error(R.mipmap.ic_launcher)
                .into(galleryImageView);
        return galleryImageView;
    }

    @Override
    public void clearMemoryCache() {

    }
}

