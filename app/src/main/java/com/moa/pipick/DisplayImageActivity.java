package com.moa.pipick;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class DisplayImageActivity extends AppCompatActivity {

    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);

        ImageView imageView = (ImageView) findViewById(R.id.image);

        Glide.with(this).load("https://console.firebase.google.com/u/0/project/pipickuploadimage/storage/pipickuploadimage.appspot.com/files").into(imageView);
    }
}
