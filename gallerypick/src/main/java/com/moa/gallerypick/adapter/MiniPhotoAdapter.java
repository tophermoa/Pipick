package com.moa.gallerypick.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.moa.gallerypick.R;
import com.moa.gallerypick.bean.PhotoInfo;
import com.moa.gallerypick.config.GalleryConfig;
import com.moa.gallerypick.config.GalleryPick;
import com.moa.gallerypick.utils.ScreenUtils;
import com.moa.gallerypick.widget.GalleryImageView;

import java.util.List;


public class MiniPhotoAdapter extends RecyclerView.Adapter<MiniPhotoAdapter.ViewHolder> {

    private Context mContext;
    private Activity mActivity;
    private LayoutInflater mLayoutInflater;
    private List<PhotoInfo> photoInfoList;
    private final static String TAG = "MiniPhotoAdapter";

    private GalleryConfig galleryConfig = GalleryPick.getInstance().getGalleryConfig();

    public MiniPhotoAdapter(Context context, List<PhotoInfo> photoInfoList) {
        mLayoutInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.photoInfoList = photoInfoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.gallery_pick_gallery_mini_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        galleryConfig.getImageLoader().displayImage(mActivity, mContext, photoInfoList.get(position).path, holder.ivPhotoImage, ScreenUtils.getScreenWidth(mContext), ScreenUtils.getScreenWidth(mContext));
    }

    @Override
    public int getItemCount() {
        return photoInfoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private GalleryImageView ivPhotoImage;
        private CheckBox chkPhotoSelector;

        public ViewHolder(View itemView) {
            super(itemView);
            ivPhotoImage = (GalleryImageView) itemView.findViewById(R.id.ivGalleryPhotoImage);
            chkPhotoSelector = (CheckBox) itemView.findViewById(R.id.chkGalleryPhotoSelector);
        }

    }

    public void setmActivity(Activity mActivity) {
        this.mActivity = mActivity;
    }
}
