package com.samyotech.petstand.utils;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.samyotech.petstand.R;


public class ImageZoomActivity extends AppCompatActivity {

    Context mContext;
    TouchImageView IVzoom;
    String imagepath = "";
    String name = "";
    ImageView ivback;
    CustomTextViewBold CTVBname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_image_zoom);
        mContext = ImageZoomActivity.this;

        imagepath = getIntent().getStringExtra(Consts.IMAGE);
        name = getIntent().getStringExtra(Consts.NAME);

        IVzoom = findViewById(R.id.IVzoom);
        ivback = findViewById(R.id.ivback);
        CTVBname = findViewById(R.id.CTVBname);


        CTVBname.setText(name);
        Glide.with(mContext)
                .load(imagepath)
                .placeholder(R.drawable.default_error)
                .dontAnimate() // will load image
                .into(IVzoom);

        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
