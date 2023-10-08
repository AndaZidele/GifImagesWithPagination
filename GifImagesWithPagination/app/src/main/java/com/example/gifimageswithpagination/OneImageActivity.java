package com.example.gifimageswithpagination;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class OneImageActivity extends AppCompatActivity {

    private ImageView oneImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_image);

        oneImg = (ImageView) findViewById(R.id.oneImgView);
        Intent inte = getIntent();
        String imgUrl = inte.getStringExtra("oneImg");

        Glide.with(this).load(imgUrl).into(oneImg);
    }
}