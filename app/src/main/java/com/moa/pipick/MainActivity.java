package com.moa.pipick;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.moa.gallerypick.config.GalleryConfig;
import com.moa.gallerypick.config.GalleryPick;
import com.moa.gallerypick.inter.IHandlerCallBack;
import com.moa.pipick.loader.FrescoImageLoader;
import com.moa.pipick.loader.GlideImageLoader;
import com.moa.pipick.loader.PicassoImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import bolts.Task;

public class MainActivity extends AppCompatActivity {

    private String TAG = "---PiPick---";
    private Context mContext;
    private Activity mActivity;

    private Button btn;
    private Button btnOpenCamera;
    private Button btnpindah;
    private Switch swMulSelect;
    private Switch swShowCamera;
    private Switch swIsCrop;
    private EditText etMulMaxSize;
    private RecyclerView rvResultPhoto;
    private RadioGroup rgImageLoader;

    //retreive


    private Uri filePath;


    private PhotoAdapter photoAdapter;

    //tadi diapus
    private List<String> path = new ArrayList<>();

    private GalleryConfig galleryConfig;
    private IHandlerCallBack iHandlerCallBack;

    private final int PERMISSIONS_REQUEST_READ_CONTACTS = 8;
    private final int PICK_IMAGE_REQUEST = 71;

    //firebase
    /*FirebaseStorage storage;
    StorageReference storageReference;*/

    //filePath = data.getData(filepaths);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnpindah=(Button) findViewById(R.id.btnpindah);
        btnpindah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,DisplayImageActivity.class);
                startActivity(intent);
            }
        });

        ///FirebaseApp.initializeApp(this);
        //firebase init
        /*storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();*/

       /*btnupload.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               uploadImage();

           }
       });*/


        mContext = this;
        mActivity = this;

        initView();
        initGallery();
        init();

    }




   /*private void uploadImage() {
        if(filePath != null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploadingg . . .");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener(){
                        public void onFailure(Exception e){
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Fail"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>(){
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot){
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }

                    });
        }else{

            Toast.makeText(MainActivity.this,"null",Toast.LENGTH_SHORT).show();
        }
    }*/

    private void initView() {
        btn = (Button) super.findViewById(R.id.btn);
        btnpindah = (Button)super.findViewById(R.id.btnpindah);
        btnOpenCamera = (Button) super.findViewById(R.id.btnOpenCamera);
        swMulSelect = (Switch) super.findViewById(R.id.swMulSelect);
        swShowCamera = (Switch) super.findViewById(R.id.swShowCamera);
        swIsCrop = (Switch) super.findViewById(R.id.swIsCrop);
        etMulMaxSize = (EditText) super.findViewById(R.id.etMulMaxSize);
        rvResultPhoto = (RecyclerView) super.findViewById(R.id.rvResultPhoto);
        rgImageLoader = (RadioGroup) super.findViewById(R.id.rgImageLoader);
    }



    private void init() {

        btnOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                galleryConfig = new GalleryConfig.Builder()
//                        .iHandlerCallBack(iHandlerCallBack)
//                        .filePath("/Gallery/Pictures")
//                        .isOpenCamera(true)
//                        .build();
//                GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(mActivity);

//                galleryConfig.getBuilder().isOpenCamera(true).build();

                //   galleryConfig
                GalleryPick.getInstance().setGalleryConfig(galleryConfig).openCamera(mActivity);

            }
        });

        galleryConfig = new GalleryConfig.Builder()
                .imageLoader(new GlideImageLoader())    // ImageLoader
                .iHandlerCallBack(iHandlerCallBack)     // Antarmuka monitor (wajib)
                .provider("com.moa.pipick.fileprovider")   // provider
                .pathList(path)                         // Rekam gambar yang dipilih
                .multiSelect(false)                      // pilih gambar banyakan default：false
                .multiSelect(false, 9)       // confg banyak pilihan：false ， 9
                .maxSize(9)                             // Konfigurasikan banyak pilihan untuk beberapa pilihan. Default：9
                .crop(false)                             // quick crop, 1 pilihan
                .crop(false, 1, 1, 500, 500)// parameter fungsi crop
                .isShowCamera(true)                     // tombol cam realistis default：false
                .filePath("/Gallery/Pictures")          // file path (Jalur penyimpanan gambar)
                .build();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(etMulMaxSize.getText().toString()) && Integer.valueOf(etMulMaxSize.getText().toString()) > 0) {
                    galleryConfig.getBuilder().maxSize(Integer.valueOf(etMulMaxSize.getText().toString())).build();
                }
                galleryConfig.getBuilder().isOpenCamera(false).build();
                initPermissions();
            }
        });

        // pilih banyakan
        swMulSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                galleryConfig.getBuilder().multiSelect(isChecked).build();
            }
        });

        //tampil kamera
        swShowCamera.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                galleryConfig.getBuilder().isShowCamera(isChecked).build();
            }
        });

        // crop?
        swIsCrop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                galleryConfig.getBuilder().crop(isChecked).build();
            }
        });


        // Pilih Frame
        rgImageLoader.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbGlide) {
                    galleryConfig.getBuilder().imageLoader(new GlideImageLoader()).build();
                } else if (checkedId == R.id.rbPicasso) {
                    galleryConfig.getBuilder().imageLoader(new PicassoImageLoader()).build();
                } else if (checkedId == R.id.rbFresco) {
                    galleryConfig.getBuilder().imageLoader(new FrescoImageLoader(mContext)).build();
                }
            }
        });


        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvResultPhoto.setLayoutManager(gridLayoutManager);

        photoAdapter = new PhotoAdapter(this, path);
        rvResultPhoto.setAdapter(photoAdapter);

    }

    // Manajemen otorisasi
    private void initPermissions() {
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission privilage ");
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Log.i(TAG, "Deny");
                Toast.makeText(mContext, "Harap aktifkan otorisasi penyimpanan untuk aplikasi ini di Pengaturan - Manajemen Aplikasi.", Toast.LENGTH_SHORT).show();
            } else {
                Log.i(TAG, "Privasi/Otorasi");
                ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {
            Log.i(TAG, "No privasi/otorasi ");
            GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(MainActivity.this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "Allow");
                GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(MainActivity.this);
            } else {
                Log.i(TAG, "Deny");
            }
        }
    }


    private void initGallery() {
        iHandlerCallBack = new IHandlerCallBack() {
            @Override
            public void onStart() {
                Log.i(TAG, "onStart: Buka");
            }

            @Override
            public void onSuccess(List<String> photoList) {
                Log.i(TAG, "onSuccess: Back up");
                path.clear();
                for (String s : photoList) {
                    Log.i(TAG, s);
                    path.add(s);
                }
                photoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancel() {
                Log.i(TAG, "onCancel: Batalkan");
            }

            @Override
            public void onFinish() {
                Log.i(TAG, "onFinish: Akhiri");
            }

            @Override
            public void onError() {
                Log.i(TAG, "onError: Kesalahan");
            }
        };

    }
}
