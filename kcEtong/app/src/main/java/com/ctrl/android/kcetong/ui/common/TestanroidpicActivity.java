package com.ctrl.android.kcetong.ui.common;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ctrl.android.kcetong.CustomApplication;
import com.ctrl.android.kcetong.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
/*
*
* 图片放大activity
*
* */

public class TestanroidpicActivity extends Activity {
    private String mData;
    private int    mLocationX;
    private int    mLocationY;
    private int    mWidth;
    private int    mHeight;
    SmoothImageView imageView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mData = getIntent().getStringExtra("imageUrl");
        mLocationX = getIntent().getIntExtra("locationX", 0);
        mLocationY = getIntent().getIntExtra("locationY", 0);
        mWidth = getIntent().getIntExtra("width", 0);
        mHeight = getIntent().getIntExtra("height", 0);

        imageView = new SmoothImageView(this);
        imageView.setOriginalInfo(mWidth, mHeight, mLocationX, mLocationY);
        imageView.transformIn();
        imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        setContentView(imageView);
//        Arad.imageLoader.load(mData==null||mData.equals("")?"aa":mData).placeholder(R.drawable.default_image).resize(500,500).into(imageView);
        CustomApplication.setImageWithDiffDisplayImageOptions(mData==null||mData.equals("")?"aa":mData,imageView,null,mBannerOptions);

        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                imageView.setOnTransformListener(new SmoothImageView.TransformListener() {
                    @Override
                    public void onTransformComplete(int mode) {
                        if (mode == 2) {
                            finish();
                        }
                    }
                });
                imageView.transformOut();
            }
        });
    }
    public static DisplayImageOptions mBannerOptions = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.default_image)
            // 图片加载的时候显示的图片
            .showImageForEmptyUri(R.drawable.default_image)
            // 图片加载地址为空的时候显示的图片
            .showImageOnFail(R.drawable.default_image)
            // 图片加载失败的时候显示的图片
            .cacheInMemory(true).cacheOnDisk(true)
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();
    @Override
    public void onBackPressed() {
        imageView.setOnTransformListener(new SmoothImageView.TransformListener() {
            @Override
            public void onTransformComplete(int mode) {
                if (mode == 2) {
                    finish();
                }
            }
        });
        imageView.transformOut();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            overridePendingTransition(0, 0);
        }
    }

}
