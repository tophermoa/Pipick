package com.moa.pipick.loader;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeHolder;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.moa.gallerypick.inter.ImageLoader;
import com.moa.gallerypick.widget.GalleryImageView;
import com.moa.pipick.R;


public class FrescoImageLoader implements ImageLoader {


    public FrescoImageLoader(Context context) {
        Fresco.initialize(context);
    }


    @Override
    public GalleryImageView displayImage(Activity activity, Context context, String path, final GalleryImageView imageView, int width, int height) {


        GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(context.getResources())
                .setFadeDuration(300)
                .setPlaceholderImage(R.mipmap.gallery_pick_photo)   // bagan placeholder
                .setFailureImage(R.mipmap.gallery_pick_photo)       // memuat grafik kegagalan
                .setProgressBarImage(new ProgressBarDrawable())     // loading
                .build();

        final DraweeHolder<GenericDraweeHierarchy> draweeHolder = DraweeHolder.create(hierarchy, context);
        imageView.setOnImageViewListener(new GalleryImageView.OnImageViewListener() {
            @Override
            public void onDraw(Canvas canvas) {
                Drawable drawable = draweeHolder.getHierarchy().getTopLevelDrawable();
                if (drawable == null) {
                    imageView.setImageResource(R.mipmap.gallery_pick_photo);
                } else {
                    imageView.setImageDrawable(drawable);
                }
            }


            @Override
            public boolean verifyDrawable(Drawable dr) {
                return dr == draweeHolder.getHierarchy().getTopLevelDrawable();
            }

            @Override
            public void onDetach() {
                draweeHolder.onDetach();
            }

            @Override
            public void onAttach() {
                draweeHolder.onAttach();
            }
        });

        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_FILE_SCHEME)
                .path(path)
                .build();
        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(width, height))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(draweeHolder.getController())
                .setImageRequest(imageRequest)
                .build();
        draweeHolder.setController(controller);


        return imageView;
    }

    @Override
    public void clearMemoryCache() {

    }
}
