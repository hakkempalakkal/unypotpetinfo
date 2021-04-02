package com.samyotech.petstand.utils.imagepager;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ToxicBakery.viewpager.transforms.ZoomOutSlideTransformer;
import com.samyotech.petstand.R;
import com.samyotech.petstand.models.PetMarketDTO;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.ProjectUtils;


import java.util.ArrayList;

public class ImageviewpagerActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    Context mContext;
    private Dialog dialog;
    private ViewPager viewpagerBar;
    public int dotsCount;
    private ImageView[] dots;
    private LinearLayout viewPagerCountDots;
    private ArrayList<PetMarketDTO.PetImage> newsImageLIST = new ArrayList<>();

    ImageViewPagerAdapter listadpt;
    int flag = 0;
    public int imageposition = 0;
    ImageView IVback;

    String functionid;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.Fullscreen(ImageviewpagerActivity.this);
        setContentView(R.layout.image_layout);
        mContext = ImageviewpagerActivity.this;


        viewpagerBar = (ViewPager) findViewById(R.id.viewpagerBar);
        IVback = findViewById(R.id.IVback);

        viewPagerCountDots = (LinearLayout) findViewById(R.id.viewPagerCountDots);


        newsImageLIST = (ArrayList<PetMarketDTO.PetImage>) getIntent().getSerializableExtra(Consts.IMAGE);
        listadpt = new ImageViewPagerAdapter(ImageviewpagerActivity.this, ImageviewpagerActivity.this, newsImageLIST);

        viewpagerBar.setAdapter(listadpt);
        viewpagerBar.setPageTransformer(true, new ZoomOutSlideTransformer());
        viewpagerBar.setCurrentItem(0);
        viewpagerBar.setOnPageChangeListener(this);
        IVback.setOnClickListener(this);


    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void setPageViewIndicator(ImageViewPagerAdapter bpa, final ViewPager viewpagerBar, LinearLayout linearLayout) {

        Log.d("###setPageViewIndicator", " : called");
        dotsCount = bpa.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {


            dots[i] = new ImageView(mContext);
            dots[i].setImageDrawable(ImageviewpagerActivity.this.getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    20,
                    20
            );

            params.setMargins(4, 0, 4, 0);

            final int presentPosition = i;
            dots[presentPosition].setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    viewpagerBar.setCurrentItem(presentPosition);
                    return true;
                }

            });


            linearLayout.addView(dots[i], params);
        }

        dots[0].setImageDrawable(ImageviewpagerActivity.this.getResources().getDrawable(R.drawable.selecteditem_dot));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    public void updateList() {
        listadpt = new ImageViewPagerAdapter(ImageviewpagerActivity.this, ImageviewpagerActivity.this, newsImageLIST);
        viewpagerBar.setAdapter(listadpt);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.IVback:
                finish();
                break;
        }
    }


}

