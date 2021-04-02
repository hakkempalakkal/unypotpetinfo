package com.samyotech.petstand.activity.food;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.BaseActivity;
import com.samyotech.petstand.activity.PaymentViewActivity;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.models.MakeOrderDTO;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomTextView;
import com.samyotech.petstand.utils.CustomTextViewBold;
import com.samyotech.petstand.utils.ProjectUtils;

public class ViewOrderDetails extends AppCompatActivity {
    private CustomTextView tvOrderID, tvTotalPrice, tvCOD, tvFinalPrice;
    private CardView submitBTN;
    private MakeOrderDTO makeOrderDTO;
    SharedPrefrence sharedPrefrence;
    LoginDTO loginDTO;
    String orderPaymentStatus = "";
    //String orderID = "";
    private static final String TAG = ViewOrderDetails.class.getSimpleName();

    String address = "", landmark = "", name = "", country_code = "", mobile_no = "", email = "", city = "", country = "", zip = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(ViewOrderDetails.this);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        setContentView(R.layout.activity_view_order_details);
        sharedPrefrence = SharedPrefrence.getInstance(ViewOrderDetails.this);
        loginDTO = sharedPrefrence.getParentUser(Consts.LOGINDTO);

        init();
    }

    public void init() {
        if (getIntent().hasExtra(Consts.MAKE_ORDER)) {
            makeOrderDTO = (MakeOrderDTO) getIntent().getSerializableExtra(Consts.MAKE_ORDER);
            orderPaymentStatus = getIntent().getStringExtra(Consts.PAYMENT_STATUS);
        }
        tvOrderID = findViewById(R.id.tvOrderID);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvCOD = findViewById(R.id.tvCOD);
        tvFinalPrice = findViewById(R.id.tvFinalPrice);
        submitBTN = findViewById(R.id.submitBTN);


       /* Random otp1 = new Random();
        StringBuilder builder = new StringBuilder();
        for (int count = 0; count <= 3; count++) {
            builder.append(otp1.nextInt(10));
        }
        orderID = builder.toString();*/

        tvOrderID.setText(makeOrderDTO.getOrder_id());
        tvTotalPrice.setText(makeOrderDTO.getCurrency_type() + " " + makeOrderDTO.getTotal_price());
        tvCOD.setText(makeOrderDTO.getCurrency_type() + " " + makeOrderDTO.getCod_charges());
        tvFinalPrice.setText(makeOrderDTO.getCurrency_type() + " " + makeOrderDTO.getFinal_price());

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                goHome();
            }
        });
    }


    public void goHome() {

        Intent intent = new Intent(ViewOrderDetails.this, BaseActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        goHome();
    }


    public void initPayment() {
        final Dialog dialog = new Dialog(ViewOrderDetails.this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.paymentdailog);
        final CustomTextViewBold ctvPayPal = (CustomTextViewBold) dialog.findViewById(R.id.ctvPayPal);
        String payPal = "Pay by <font color='#303F9F'>Pay</font><font color='#00BCD4'>Pal.</font>";
        ctvPayPal.setText(Html.fromHtml(payPal), CustomTextViewBold.BufferType.NORMAL);

        final CustomTextViewBold ctvstripe = (CustomTextViewBold) dialog.findViewById(R.id.ctvstripe);
        String strip = "Pay by <font color='#00BCD4'>Stripe</font>";
        ctvstripe.setText(Html.fromHtml(strip), CustomTextViewBold.BufferType.NORMAL);

        final LinearLayout llPayPal = (LinearLayout) dialog.findViewById(R.id.llPayPal);
        final LinearLayout llstripe = (LinearLayout) dialog.findViewById(R.id.llstripe);
        dialog.show();
        llPayPal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://phpstack-225750-688566.cloudwaysapps.com/Paypal/paypal?user_id=" + loginDTO.getId() +
                        "&order_id=" + makeOrderDTO.getOrder_id() + "&user_name=" + loginDTO.getFirst_name() + "&amount=" + makeOrderDTO.getFinal_price();
                Intent intent = new Intent(ViewOrderDetails.this, PaymentViewActivity.class);
                intent.putExtra(Consts.PAYAMENT_URL, url);
                startActivity(intent);
                finish();
                dialog.dismiss();
            }
        });
        llstripe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://phpstack-225750-688566.cloudwaysapps.com/Stripe/BookingPayement/make_payment?user_id=" + loginDTO.getId() +
                        "&order_id=" + makeOrderDTO.getOrder_id() + "&user_name=" + loginDTO.getFirst_name() + "&amount=" + makeOrderDTO.getFinal_price();
                Intent intent = new Intent(ViewOrderDetails.this, PaymentViewActivity.class);
                intent.putExtra(Consts.PAYAMENT_URL, url);
                startActivity(intent);
                finish();
                dialog.dismiss();
            }
        });
    }


}
