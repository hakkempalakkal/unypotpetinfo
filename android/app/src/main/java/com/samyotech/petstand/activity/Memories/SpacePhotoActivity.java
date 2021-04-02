package com.samyotech.petstand.activity.Memories;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.samyotech.petstand.R;
import com.samyotech.petstand.adapter.MemoryPagerAdapter;
import com.samyotech.petstand.models.GalleryDTO;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.ProjectUtils;

import java.util.ArrayList;

public class SpacePhotoActivity extends AppCompatActivity {

    private ViewPager viewpager;
    private ArrayList<GalleryDTO> galleryDTOList;
    private MemoryPagerAdapter mAdapter;
    private Context mcontext;
    int pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(SpacePhotoActivity.this);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_photo_detail);
        mcontext = SpacePhotoActivity.this;
        viewpager = (ViewPager) findViewById(R.id.viewpager);

        if (getIntent().hasExtra(Consts.EXTRA_SPACE_PHOTO)) {
            Bundle args = getIntent().getBundleExtra(Consts.EXTRA_SPACE_PHOTO);
            galleryDTOList = new ArrayList<>();
            galleryDTOList = (ArrayList<GalleryDTO>) args.getSerializable("ARRAYLIST");
            pos = getIntent().getIntExtra("pos",0);
        }

        mAdapter = new MemoryPagerAdapter(SpacePhotoActivity.this, mcontext, galleryDTOList);
        viewpager.setAdapter(mAdapter);
        //viewpager.setPageTransformer(true, new ZoomOutSlideTransformer());
        viewpager.setCurrentItem(pos);
        //viewpager.setOnPageChangeListener(this);


    }

}
