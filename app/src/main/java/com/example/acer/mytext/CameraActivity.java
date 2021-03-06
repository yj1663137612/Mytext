package com.example.acer.mytext;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ACER on 2016/11/30.
 */

public class CameraActivity extends AppCompatActivity {
    @BindView(R.id.img_camera)
    ImageView mImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_activity);
        ButterKnife.bind(this);
        Intent intent = this.getIntent();
        File file = intent.getParcelableExtra("file");
        Log.e("tu==", "" + file);
        Picasso.with(this).load(file).into(mImg);
    }
}
