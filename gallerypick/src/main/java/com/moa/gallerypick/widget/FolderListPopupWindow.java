package com.moa.gallerypick.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.moa.gallerypick.R;
import com.moa.gallerypick.adapter.FolderAdapter;


public class FolderListPopupWindow extends PopupWindow {

    private final static String TAG = "FolderListPopupWindow";

    private RecyclerView rvFolderList;

    private Context mContext;
    private Activity mActivity;
    private View popupWindow;

    private FolderAdapter folderAdapter;


    public FolderListPopupWindow(Activity mActivity, Context mContext, FolderAdapter folderAdapter) {
        super(mContext);
        this.mContext = mContext;
        this.mActivity = mActivity;
        this.folderAdapter = folderAdapter;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupWindow = inflater.inflate(R.layout.gallery_pick_gallery_popup_folder, null);

        initView();
        init();

    }

    private void initView() {
        rvFolderList = (RecyclerView) popupWindow.findViewById(R.id.rvFolderList);
    }

    private void init() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvFolderList.setLayoutManager(linearLayoutManager);
        rvFolderList.setAdapter(folderAdapter);

        //view pop up window
        this.setContentView(popupWindow);
        //lebar pop up
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //tinggi pop up
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(false);
        //animasi
        this.setAnimationStyle(R.style.gallerypickpopupAnimation);
        ColorDrawable dw = new ColorDrawable(mContext.getResources().getColor(R.color.gallerypickgallery_folder_bg));
        //background
        this.setBackgroundDrawable(dw);

    }


}
