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

import java.util.ArrayList;
import java.util.List;


public class PhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private Activity mActivity;
    private LayoutInflater mLayoutInflater;
    private List<PhotoInfo> photoInfoList;                      // Data foto lokal
    private List<String> selectPhoto = new ArrayList<>();                   // Data gambar yang dipilih
    private OnCallBack onCallBack;
    private final static String TAG = "PhotoAdapter";

    private GalleryConfig galleryConfig = GalleryPick.getInstance().getGalleryConfig();

    private final static int HEAD = 0;    // untuk letak tampilan kamera hiduo
    private final static int ITEM = 1;    // letak foto

    public PhotoAdapter(Activity mActivity, Context mContext, List<PhotoInfo> photoInfoList) {
        mLayoutInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.photoInfoList = photoInfoList;
        this.mActivity = mActivity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEAD) {
            return new HeadHolder(mLayoutInflater.inflate(R.layout.gallery_pick_gallery_item_camera, parent, false));
        }
        return new ViewHolder(mLayoutInflater.inflate(R.layout.gallery_pick_gallery_item_photo, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        // masing2 ukuran
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        params.height = ScreenUtils.getScreenWidth(mContext) / 3;
        params.width = ScreenUtils.getScreenWidth(mContext) / 3;
        holder.itemView.setLayoutParams(params);

        if (getItemViewType(position) == HEAD) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (galleryConfig.getMaxSize() <= selectPhoto.size()) {        // stop select saat max
                        return;
                    }
                    onCallBack.OnClickCamera(selectPhoto);
                }
            });
            return;
        }

        final PhotoInfo photoInfo;
        if (galleryConfig.isShowCamera()) {
            photoInfo = photoInfoList.get(position - 1);
        } else {
            photoInfo = photoInfoList.get(position);
        }
        final ViewHolder viewHolder = (ViewHolder) holder;
        galleryConfig.getImageLoader().displayImage(mActivity, mContext, photoInfo.path, viewHolder.ivPhotoImage, ScreenUtils.getScreenWidth(mContext) / 3, ScreenUtils.getScreenWidth(mContext) / 3);


        if (selectPhoto.contains(photoInfo.path)) {
            viewHolder.chkPhotoSelector.setChecked(true);
            viewHolder.chkPhotoSelector.setButtonDrawable(R.mipmap.gallery_pick_select_checked);
            viewHolder.vPhotoMask.setVisibility(View.VISIBLE);
        } else {
            viewHolder.chkPhotoSelector.setChecked(false);
            viewHolder.chkPhotoSelector.setButtonDrawable(R.mipmap.gallery_pick_select_unchecked);
            viewHolder.vPhotoMask.setVisibility(View.GONE);
        }

        if (!galleryConfig.isMultiSelect()) {
            viewHolder.chkPhotoSelector.setVisibility(View.GONE);
            viewHolder.vPhotoMask.setVisibility(View.GONE);
        }


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!galleryConfig.isMultiSelect()) {
                    selectPhoto.clear();
                    selectPhoto.add(photoInfo.path);
                    onCallBack.OnClickPhoto(selectPhoto);
                    return;
                }

                if (selectPhoto.contains(photoInfo.path)) {
                    selectPhoto.remove(photoInfo.path);
                    viewHolder.chkPhotoSelector.setChecked(false);
                    viewHolder.chkPhotoSelector.setButtonDrawable(R.mipmap.gallery_pick_select_unchecked);
                    viewHolder.vPhotoMask.setVisibility(View.GONE);
                } else {
                    if (galleryConfig.getMaxSize() <= selectPhoto.size()) {        // saat pilihan max, gabisa diklik
                        return;
                    }
                    selectPhoto.add(photoInfo.path);
                    viewHolder.chkPhotoSelector.setChecked(true);
                    viewHolder.chkPhotoSelector.setButtonDrawable(R.mipmap.gallery_pick_select_checked);
                    viewHolder.vPhotoMask.setVisibility(View.VISIBLE);
                }
                onCallBack.OnClickPhoto(selectPhoto);
            }
        });


    }



    private class ViewHolder extends RecyclerView.ViewHolder {
        private GalleryImageView ivPhotoImage;
        private View vPhotoMask;
        private CheckBox chkPhotoSelector;

        private ViewHolder(View itemView) {
            super(itemView);
            ivPhotoImage = (GalleryImageView) itemView.findViewById(R.id.ivGalleryPhotoImage);
            vPhotoMask = itemView.findViewById(R.id.vGalleryPhotoMask);
            chkPhotoSelector = (CheckBox) itemView.findViewById(R.id.chkGalleryPhotoSelector);
        }
    }


    private class HeadHolder extends RecyclerView.ViewHolder {
        private HeadHolder(View itemView) {
            super(itemView);
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (galleryConfig.isShowCamera() && position == 0) {
            return HEAD;
        }
        return ITEM;
    }

    @Override
    public int getItemCount() {
        if (galleryConfig.isShowCamera())
            return photoInfoList.size() + 1;
        else
            return photoInfoList.size();
    }

    public interface OnCallBack {
        void OnClickPhoto(List<String> selectPhoto);

        void OnClickCamera(List<String> selectPhoto);
    }

    public void setOnCallBack(OnCallBack onCallBack) {
        this.onCallBack = onCallBack;
    }

    //select foto
    public void setSelectPhoto(List<String> selectPhoto) {
        this.selectPhoto.addAll(selectPhoto);

//        for (String filePath : selectPhoto) {
//            PhotoInfo photoInfo = getPhotoByPath(filePath);
//            if (photoInfo != null) {
//                this.selectPhoto.add(photoInfo);
//            }
//        }
//        if (selectPhoto.size() > 0) {
//            notifyDataSetChanged();
//        }
    }


//    private PhotoInfo getPhotoByPath(String filePath) {
//        if (photoInfoList != null && photoInfoList.size() > 0) {
//            for (PhotoInfo photoInfo : photoInfoList) {
//                if (photoInfo.path.equalsIgnoreCase(filePath)) {
//                    return photoInfo;
//                }
//            }
//        }
//        return null;
//    }


}
