package com.samyotech.petstand.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.samyotech.petstand.R;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.models.MakeOrderDTO;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.ProjectUtils;

import java.util.HashMap;

public class PaymentViewActivity extends AppCompatActivity {
    private LinearLayout llBackMC;
    private WebView payment_us;
    private TextView tv_add_worm_title;
    private String url;
    Context mContext;
    HashMap<String, String> parmsOrder = new HashMap<>();
    private static String surl = "http://phpstack-225750-688566.cloudwaysapps.com/Paypal/paymentsuccess";
    private static String furl = "http://phpstack-225750-688566.cloudwaysapps.com/Paypal/paymentfailure";
    String orderID = "", address = "", landmark = "", name = "", country_code = "", mobile_no = "", email = "", city = "", country = "", zip = "";
    private SharedPrefrence prefrence;
    private LoginDTO loginDTO;
    private String TAG = PaymentViewActivity.class.getSimpleName();
    private MakeOrderDTO makeOrderDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(PaymentViewActivity.this);
        setContentView(R.layout.activity_payment_view);
        mContext = PaymentViewActivity.this;

        prefrence = SharedPrefrence.getInstance(mContext);
        loginDTO = prefrence.getParentUser(Consts.LOGINDTO);
        payment_us = (WebView) findViewById(R.id.payment_us);
        url = getIntent().getStringExtra(Consts.PAYAMENT_URL);

       /* address = getIntent().getStringExtra(Consts.ADDRESS);
        landmark = getIntent().getStringExtra(Consts.LANDMARK);
        name = getIntent().getStringExtra(Consts.NAME);
        country_code = getIntent().getStringExtra(Consts.COUNTRY_CODE);
        mobile_no = getIntent().getStringExtra(Consts.MOBILE_NO);
        email = getIntent().getStringExtra(Consts.EMAIL);
        city = getIntent().getStringExtra(Consts.CITY);
        country = getIntent().getStringExtra(Consts.COUNTRY);
        zip = getIntent().getStringExtra(Consts.ZIP);
        orderID = getIntent().getStringExtra(Consts.ORDER_ID);*/

        llBackMC = (LinearLayout) findViewById(R.id.llBackMC);

        llBackMC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                // goHome();
            }
        });
      /*  payment_us.setWebViewClient(new MyBrowser());
        payment_us.getSettings().setLoadsImagesAutomatically(true);
        payment_us.getSettings().setJavaScriptEnabled(true);
        payment_us.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        payment_us.loadUrl(url);*/


        WebSettings settings = payment_us.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        payment_us.loadUrl(url);
        payment_us.setWebViewClient(new SSLTolerentWebViewClient());
    }


    private class SSLTolerentWebViewClient extends WebViewClient {

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            // this will ignore the Ssl error and will go forward to your site
            handler.proceed();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            Log.e("PageStarted", url);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            Log.e("OverrideUrlLoading", url);
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            ProjectUtils.pauseProgressDialog();
            //Page load finished
            Log.e("PageFinished", url);
            if (url.contains(surl)) {
                ProjectUtils.showToast(mContext, "Payment successful.");
                super.onPageFinished(view, surl);
                finish();

            } else if (url.equals(furl)) {
                ProjectUtils.showToast(mContext, "Payment Unsuccessful.");
                super.onPageFinished(view, furl);
                finish();

            } else {
                super.onPageFinished(view, url);
            }

        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            // TODO Auto-generated method stub
            super.onReceivedError(view, errorCode, description, failingUrl);
        }
    }

    public void goHome() {

        Intent intent = new Intent(mContext, BaseActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        // goHome();
        finish();

    }


   /* public void makeOrder() {

        parmsOrder.put(Consts.USER_ID, loginDTO.getId());
        parmsOrder.put(Consts.ORDER_ID, orderID);
        parmsOrder.put(Consts.ADDRESS, address);
        parmsOrder.put(Consts.LANDMARK, landmark);
        parmsOrder.put(Consts.NAME, name);
        parmsOrder.put(Consts.COUNTRY_CODE, country_code);
        parmsOrder.put(Consts.MOBILE_NO, mobile_no);
        parmsOrder.put(Consts.EMAIL, email);
        parmsOrder.put(Consts.CITY, city);
        parmsOrder.put(Consts.COUNTRY, country);
        parmsOrder.put(Consts.ZIP, zip);
        // parmsOrder.put(Consts.IMAGE, product_image);

        ProjectUtils.showProgressDialog(mContext, true, "Please Wait!!");
        new HttpsRequest(Consts.ORDER, parmsOrder, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        makeOrderDTO = new Gson().fromJson(response.getJSONObject("data").toString(), MakeOrderDTO.class);

                        Intent intent = new Intent(PaymentViewActivity.this, ViewOrderDetails.class);
                        intent.putExtra(Consts.MAKE_ORDER, makeOrderDTO);
                        startActivity(intent);
                        // finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    ProjectUtils.showLong(mContext, msg);

                }
            }
        });
    }*/
}
