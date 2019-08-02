package com.moa.gallerypick.utils;

import android.app.Activity;
import android.net.Uri;

import com.yalantis.ucrop.UCrop;
import com.moa.gallerypick.R;

import java.io.File;


public class UCropUtils {

    public static void start(Activity mActivity, File sourceFile, File destinationFile, float aspectRatioX, float aspectRatioY, int maxWidth, int maxHeight) {
        UCrop uCrop = UCrop.of(Uri.fromFile(sourceFile), Uri.fromFile(destinationFile))
                .withAspectRatio(aspectRatioX, aspectRatioY)
                .withMaxResultSize(maxWidth, maxHeight);

        UCrop.Options options = new UCrop.Options();
        options.setToolbarColor(mActivity.getResources().getColor(R.color.gallerypickgallery_blue));
        options.setStatusBarColor(mActivity.getResources().getColor(R.color.gallerypickgallery_blue));
        uCrop.withOptions(options);


        uCrop.start(mActivity);
    }


}
