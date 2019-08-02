package com.moa.gallerypick.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.FileProvider;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yalantis.ucrop.UCrop;
import com.moa.gallerypick.R;
import com.moa.gallerypick.adapter.FolderAdapter;
import com.moa.gallerypick.adapter.PhotoAdapter;
import com.moa.gallerypick.bean.FolderInfo;
import com.moa.gallerypick.bean.PhotoInfo;
import com.moa.gallerypick.config.GalleryConfig;
import com.moa.gallerypick.config.GalleryPick;
import com.moa.gallerypick.inter.IHandlerCallBack;
import com.moa.gallerypick.utils.FileUtils;
import com.moa.gallerypick.utils.UCropUtils;
import com.moa.gallerypick.utils.UIUtils;
import com.moa.gallerypick.widget.FolderListPopupWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class GalleryPickActivity extends BaseActivity {

    private Context mContext = null;
    private Activity mActivity = null;
    private final static String TAG = "GalleryPickActivity";

    private ArrayList<String> resultPhoto;

    private TextView tvFinish;                  // Tombol selesai
    private TextView tvGalleryFolder;           // Tombol folder
    private LinearLayout btnGalleryPickBack;    // Tombol kembali
    private RecyclerView rvGalleryImage;        // Daftar gambar

    private PhotoAdapter photoAdapter;              // Adaptor gambar
    private FolderAdapter folderAdapter;            // Adaptor folder


    private List<FolderInfo> folderInfoList = new ArrayList<>();    // Informasi folder lokalList
    private List<PhotoInfo> photoInfoList = new ArrayList<>();      // Informasi gambar lokalList

    private static final int LOADER_ALL = 0;         // Dapatkan semua gambar
    private static final int LOADER_CATEGORY = 1;    // Dapatkan semua gambar dalam folder

    private boolean hasFolderScan = false;           // Sudahkah Anda memindai

    private GalleryConfig galleryConfig;   // GalleryPick Configurator

    private static final int REQUEST_CAMERA = 100;   // Atur foto yang diambil REQUEST_CODE

    private IHandlerCallBack mHandlerCallBack;   // GalleryPick Antarmuka

    private FolderListPopupWindow folderListPopupWindow;   // Popup pilih folder

    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_pick_gallery_main);

        mContext = this;
        mActivity = this;

        UIUtils.hideTitleBar(mActivity, R.id.ll_gallery_pick_main);


        galleryConfig = GalleryPick.getInstance().getGalleryConfig();
        if (galleryConfig == null) {
            exit();
            return;
        }

        Intent intent = getIntent();
        boolean isOpenCamera = intent.getBooleanExtra("isOpenCamera", false);
        if (isOpenCamera || galleryConfig.isOpenCamera()) {
            galleryConfig.getBuilder().isOpenCamera(true).build();
            showCameraAction();
        }

        initView();
        init();
        initPhoto();


    }


    private void initView() {
        tvFinish = (TextView) super.findViewById(R.id.tvFinish);
        tvGalleryFolder = (TextView) super.findViewById(R.id.tvGalleryFolder);
        btnGalleryPickBack = (LinearLayout) super.findViewById(R.id.btnGalleryPickBack);
        rvGalleryImage = (RecyclerView) super.findViewById(R.id.rvGalleryImage);
    }


    private void init() {
        mHandlerCallBack = galleryConfig.getIHandlerCallBack();
        mHandlerCallBack.onStart();

        resultPhoto = galleryConfig.getPathList();

        tvFinish.setText(getString(R.string.gallerypickgallery_finish, resultPhoto.size(), galleryConfig.getMaxSize()));

        btnGalleryPickBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandlerCallBack.onCancel();
                exit();
            }
        });

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3);
        rvGalleryImage.setLayoutManager(gridLayoutManager);
        photoAdapter = new PhotoAdapter(mActivity, mContext, photoInfoList);
        photoAdapter.setOnCallBack(new PhotoAdapter.OnCallBack() {
            @Override
            public void OnClickCamera(List<String> selectPhotoList) {
                resultPhoto.clear();
                resultPhoto.addAll(selectPhotoList);
                showCameraAction();
            }

            @Override
            public void OnClickPhoto(List<String> selectPhotoList) {
                tvFinish.setText(getString(R.string.gallerypickgallery_finish, selectPhotoList.size(), galleryConfig.getMaxSize()));

                resultPhoto.clear();
                resultPhoto.addAll(selectPhotoList);

                if (!galleryConfig.isMultiSelect() && resultPhoto != null && resultPhoto.size() > 0) {
                    if (galleryConfig.isCrop()) {
                        cameraTempFile = new File(resultPhoto.get(0));
                        cropTempFile = FileUtils.getCorpFile(galleryConfig.getFilePath());
                        UCropUtils.start(mActivity, cameraTempFile, cropTempFile, galleryConfig.getAspectRatioX(), galleryConfig.getAspectRatioY(), galleryConfig.getMaxWidth(), galleryConfig.getMaxHeight());
                        return;
                    }
                    mHandlerCallBack.onSuccess(resultPhoto);
                    exit();
                }

            }

        });
        photoAdapter.setSelectPhoto(resultPhoto);
        rvGalleryImage.setAdapter(photoAdapter);


        if (!galleryConfig.isMultiSelect()) {
            tvFinish.setVisibility(View.GONE);
        }


        tvFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resultPhoto != null && resultPhoto.size() > 0) {
                    mHandlerCallBack.onSuccess(resultPhoto);
                    exit();
                }

            }
        });

        tvGalleryFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (folderListPopupWindow != null && folderListPopupWindow.isShowing()) {
                    folderListPopupWindow.dismiss();
                    return;
                }
                folderListPopupWindow = new FolderListPopupWindow(mActivity, mContext, folderAdapter);
                folderListPopupWindow.showAsDropDown(tvGalleryFolder);
            }
        });

        folderAdapter = new FolderAdapter(mActivity, mContext, folderInfoList);
        folderAdapter.setOnClickListener(new FolderAdapter.OnClickListener() {
            @Override
            public void onClick(FolderInfo folderInfo) {
                if (folderInfo == null) {
                    getSupportLoaderManager().restartLoader(LOADER_ALL, null, mLoaderCallback);
                    tvGalleryFolder.setText(R.string.gallerypickgallery_all_folder);
                } else {
                    photoInfoList.clear();
                    photoInfoList.addAll(folderInfo.photoInfoList);
                    photoAdapter.notifyDataSetChanged();
                    tvGalleryFolder.setText(folderInfo.name);
                }
                folderListPopupWindow.dismiss();
                gridLayoutManager.scrollToPosition(0);
            }
        });


    }



    private void initPhoto() {

        mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

            private final String[] IMAGE_PROJECTION = {
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.DATE_ADDED,
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.SIZE
            };

            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                if (id == LOADER_ALL) {
                    return new CursorLoader(mActivity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, null, null, IMAGE_PROJECTION[2] + " DESC");
                } else if (id == LOADER_CATEGORY) {
                    return new CursorLoader(mActivity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, IMAGE_PROJECTION[0] + " like '%" + args.getString("path") + "%'", null, IMAGE_PROJECTION[2] + " DESC");
                }

                return null;
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                if (data != null) {
                    int count = data.getCount();
                    if (count > 0) {
                        List<PhotoInfo> tempPhotoList = new ArrayList<>();
                        data.moveToFirst();
                        do {
                            String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                            String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                            long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                            int size = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[4]));
                            boolean showFlag = size > 1024 * 5;
                            PhotoInfo photoInfo = new PhotoInfo(path, name, dateTime);
                            if (showFlag) {
                                tempPhotoList.add(photoInfo);
                            }
                            if (!hasFolderScan && showFlag) {
                                File photoFile = new File(path);
                                File folderFile = photoFile.getParentFile();

                                FolderInfo folderInfo = new FolderInfo();
                                folderInfo.name = folderFile.getName();
                                folderInfo.path = folderFile.getAbsolutePath();
                                folderInfo.photoInfo = photoInfo;
                                if (!folderInfoList.contains(folderInfo)) {
                                    List<PhotoInfo> photoInfoList = new ArrayList<>();
                                    photoInfoList.add(photoInfo);
                                    folderInfo.photoInfoList = photoInfoList;
                                    folderInfoList.add(folderInfo);
                                } else {
                                    FolderInfo f = folderInfoList.get(folderInfoList.indexOf(folderInfo));
                                    f.photoInfoList.add(photoInfo);
                                }
                            }

                        } while (data.moveToNext());

                        photoInfoList.clear();
                        photoInfoList.addAll(tempPhotoList);

                        List<String> tempPhotoPathList = new ArrayList<>();
                        for (PhotoInfo photoInfo : photoInfoList) {
                            tempPhotoPathList.add(photoInfo.path);
                        }
                        for (String mPhotoPath : galleryConfig.getPathList()) {
                            if (!tempPhotoPathList.contains(mPhotoPath)) {
                                PhotoInfo photoInfo = new PhotoInfo(mPhotoPath, null, 0L);
                                photoInfoList.add(0, photoInfo);
                            }
                        }


                        photoAdapter.notifyDataSetChanged();

                        hasFolderScan = true;
                    }
                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {

            }
        };
        getSupportLoaderManager().restartLoader(LOADER_ALL, null, mLoaderCallback);
    }


    private File cameraTempFile;
    private File cropTempFile;


    private void showCameraAction() {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(mActivity.getPackageManager()) != null) {

            cameraTempFile = FileUtils.createTmpFile(mActivity, galleryConfig.getFilePath());
            Uri imageUri = FileProvider.getUriForFile(mContext, galleryConfig.getProvider(), cameraTempFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            List<ResolveInfo> resInfoList = mContext.getPackageManager().queryIntentActivities(cameraIntent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                mContext.grantUriPermission(packageName, imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }

            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        } else {
            Toast.makeText(mContext, R.string.gallerypickgallery_msg_no_camera, Toast.LENGTH_SHORT).show();
            galleryConfig.getIHandlerCallBack().onError();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == RESULT_OK) {
                if (cameraTempFile != null) {
                    if (!galleryConfig.isMultiSelect()) {
                        resultPhoto.clear();
                        if (galleryConfig.isCrop()) {
                            cropTempFile = FileUtils.getCorpFile(galleryConfig.getFilePath());
                            UCropUtils.start(mActivity, cameraTempFile, cropTempFile, galleryConfig.getAspectRatioX(), galleryConfig.getAspectRatioY(), galleryConfig.getMaxWidth(), galleryConfig.getMaxHeight());
                            return;
                        }
                    }
                    resultPhoto.add(cameraTempFile.getAbsolutePath());


                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri uri = Uri.fromFile(new File(FileUtils.getFilePath(mContext) + galleryConfig.getFilePath()));
                    intent.setData(uri);
                    sendBroadcast(intent);

                    mHandlerCallBack.onSuccess(resultPhoto);
                    exit();
                }
            } else {
                if (cameraTempFile != null && cameraTempFile.exists()) {
                    cameraTempFile.delete();
                }
                if (galleryConfig.isOpenCamera()) {
                    exit();
                }
            }
        } else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
//            final Uri resultUri = UCrop.getOutput(data);
//            if (cameraTempFile != null && cameraTempFile.exists()) {
//                cameraTempFile.delete();
//            }
            resultPhoto.clear();
            resultPhoto.add(cropTempFile.getAbsolutePath());
            mHandlerCallBack.onSuccess(resultPhoto);
            exit();
        } else if (resultCode == UCrop.RESULT_ERROR) {
            galleryConfig.getIHandlerCallBack().onError();
//            final Throwable cropError = UCrop.getError(data);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    private void exit() {
        if (mHandlerCallBack != null) {
            mHandlerCallBack.onFinish();
        }
        finish();
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (folderListPopupWindow != null && folderListPopupWindow.isShowing()) {
                folderListPopupWindow.dismiss();
                return true;
            }
            mHandlerCallBack.onCancel();
            exit();
        }
        return true;
    }


}
