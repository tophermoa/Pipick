package com.moa.gallerypick.config;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.moa.gallerypick.activity.GalleryPickActivity;
import com.moa.gallerypick.utils.FileUtils;


public class GalleryPick {

    private final static String TAG = "GalleryPick";

    private static GalleryPick galleryPick;

    private GalleryConfig galleryConfig;

    public static GalleryPick getInstance() {
        if (galleryPick == null) {
            galleryPick = new GalleryPick();
        }
        return galleryPick;
    }


    public void open(Activity mActivity) {
        if (galleryPick.galleryConfig == null) {
            Log.e(TAG, "Edit GalleryConfig");
            return;
        }
        if (galleryPick.galleryConfig.getImageLoader() == null) {
            Log.e(TAG, "Edit ImageLoader");
            return;
        }
        if (galleryPick.galleryConfig.getIHandlerCallBack() == null) {
            Log.e(TAG, "Edit IHandlerCallBack");
            return;
        }
        if (TextUtils.isEmpty(galleryPick.galleryConfig.getProvider())) {
            Log.e(TAG, "Edit Provider");
            return;
        }

        FileUtils.createFile(galleryPick.galleryConfig.getFilePath());

        Intent intent = new Intent(mActivity, GalleryPickActivity.class);
        mActivity.startActivity(intent);
    }

    public void openCamera(Activity mActivity) {
        if (galleryPick.galleryConfig == null) {
            Log.e(TAG, "GalleryConfig");
            return;
        }
        if (galleryPick.galleryConfig.getImageLoader() == null) {
            Log.e(TAG, "ImageLoader");
            return;
        }
        if (galleryPick.galleryConfig.getIHandlerCallBack() == null) {
            Log.e(TAG, "IHandlerCallBack");
            return;
        }
        if (TextUtils.isEmpty(galleryPick.galleryConfig.getProvider())) {
            Log.e(TAG, "Provider");
            return;
        }

        FileUtils.createFile(galleryPick.galleryConfig.getFilePath());

        Intent intent = new Intent(mActivity, GalleryPickActivity.class);
        intent.putExtra("isOpenCamera", true);
        mActivity.startActivity(intent);
    }


    public GalleryPick setGalleryConfig(GalleryConfig galleryConfig) {
        this.galleryConfig = galleryConfig;
        return this;
    }

    public GalleryConfig getGalleryConfig() {
        return galleryConfig;
    }

    public void clearHandlerCallBack() {
        galleryConfig.getBuilder().iHandlerCallBack(null).build();
    }

}
