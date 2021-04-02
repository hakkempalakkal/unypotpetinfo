package com.samyotech.petstand.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.samyotech.petstand.R;
import com.samyotech.petstand.utils.ProjectUtils;

public class WebViewActivity extends AppCompatActivity {
    private LinearLayout llBackMC;
    private WebView about_us;
    private TextView tv_add_worm_title;
    private String title, url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(WebViewActivity.this);
        setContentView(R.layout.activity_web_view);
        about_us = (WebView) findViewById(R.id.about_us);
        tv_add_worm_title = (TextView) findViewById(R.id.tv_add_worm_title);
        title = getIntent().getStringExtra("title");
        url = getIntent().getStringExtra("url");
        tv_add_worm_title.setText(title);
        llBackMC = (LinearLayout) findViewById(R.id.llBackMC);

        llBackMC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        about_us.setWebViewClient(new MyBrowser());
        about_us.getSettings().setLoadsImagesAutomatically(true);
        about_us.getSettings().setJavaScriptEnabled(true);
        about_us.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        about_us.loadUrl(url);
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
