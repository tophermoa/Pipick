package com.moa.gallerypick.config;

import com.moa.gallerypick.inter.IHandlerCallBack;
import com.moa.gallerypick.inter.ImageLoader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class GalleryConfig {

    private ImageLoader imageLoader;    // muat gambar
    private IHandlerCallBack iHandlerCallBack;

    private boolean multiSelect;        // select banyakan ： false
    private int maxSize;                // max foto ：9
    private boolean isShowCamera;       // Camera on：true
    private String provider;            // kompatibel
    private String filePath;            // path：/Gallery/Pictures
    private ArrayList<String> pathList;      // list path
    private boolean isOpenCamera;             // nyalain langsung：false

    private boolean isCrop;                 // matiin crop
    private float aspectRatioX;
    private float aspectRatioY;
    private int maxWidth;
    private int maxHeight;

    private Builder builder;


    private GalleryConfig(Builder builder) {
        setBuilder(builder);
    }


    private void setBuilder(Builder builder) {
        this.imageLoader = builder.imageLoader;
        this.iHandlerCallBack = builder.iHandlerCallBack;
        this.multiSelect = builder.multiSelect;
        this.maxSize = builder.maxSize;
        this.isShowCamera = builder.isShowCamera;
        this.pathList = builder.pathList;
        this.filePath = builder.filePath;
        this.isOpenCamera = builder.isOpenCamera;
        this.isCrop = builder.isCrop;
        this.aspectRatioX = builder.aspectRatioX;
        this.aspectRatioY = builder.aspectRatioY;
        this.maxWidth = builder.maxWidth;
        this.maxHeight = builder.maxHeight;
        this.provider = builder.provider;
        this.builder = builder;
    }

    public static class Builder implements Serializable {

        private static GalleryConfig galleryConfig;

        private ImageLoader imageLoader;
        private IHandlerCallBack iHandlerCallBack;

        private boolean multiSelect = false;
        private int maxSize = 9;
        private boolean isShowCamera = true;
        private String filePath = "/Gallery/Pictures";

        private boolean isCrop = false;
        private float aspectRatioX = 1;
        private float aspectRatioY = 1;
        private int maxWidth = 500;
        private int maxHeight = 500;

        private String provider;

        private ArrayList<String> pathList = new ArrayList<>();

        private boolean isOpenCamera = false;

        public Builder provider(String provider) {
            this.provider = provider;
            return this;
        }

        public Builder crop(boolean isCrop) {
            this.isCrop = isCrop;
            return this;
        }

        public Builder crop(boolean isCrop, float aspectRatioX, float aspectRatioY, int maxWidth, int maxHeight) {
            this.isCrop = isCrop;
            this.aspectRatioX = aspectRatioX;
            this.aspectRatioY = aspectRatioY;
            this.maxWidth = maxWidth;
            this.maxHeight = maxHeight;
            return this;
        }


        public Builder imageLoader(ImageLoader imageLoader) {
            this.imageLoader = imageLoader;
            return this;
        }

        public Builder iHandlerCallBack(IHandlerCallBack iHandlerCallBack) {
            this.iHandlerCallBack = iHandlerCallBack;
            return this;
        }


        public Builder multiSelect(boolean multiSelect) {
            this.multiSelect = multiSelect;
            return this;
        }

        public Builder multiSelect(boolean multiSelect, int maxSize) {
            this.multiSelect = multiSelect;
            this.maxSize = maxSize;
            return this;
        }

        public Builder maxSize(int maxSize) {
            this.maxSize = maxSize;
            return this;
        }

        public Builder isShowCamera(boolean isShowCamera) {
            this.isShowCamera = isShowCamera;
            return this;
        }

        public Builder filePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public Builder isOpenCamera(boolean isOpenCamera) {
            this.isOpenCamera = isOpenCamera;
            return this;
        }


        public Builder pathList(List<String> pathList) {
            this.pathList.clear();
            this.pathList.addAll(pathList);
            return this;
        }

        public GalleryConfig build() {
            if (galleryConfig == null) {
                galleryConfig = new GalleryConfig(this);
            } else {
                galleryConfig.setBuilder(this);
            }
            return galleryConfig;
        }

    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public boolean isMultiSelect() {
        return multiSelect;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public boolean isShowCamera() {
        return isShowCamera;
    }

    public ArrayList<String> getPathList() {
        return pathList;
    }

    public String getFilePath() {
        return filePath;
    }

    public IHandlerCallBack getIHandlerCallBack() {
        return iHandlerCallBack;
    }

    public Builder getBuilder() {
        return builder;
    }

    public boolean isOpenCamera() {
        return isOpenCamera;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public boolean isCrop() {
        return isCrop;
    }

    public float getAspectRatioX() {
        return aspectRatioX;
    }

    public float getAspectRatioY() {
        return aspectRatioY;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public String getProvider() {
        return provider;
    }
}
