package com.samyotech.petstand.activity.MyOrder.vieworder;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.PaymentViewActivity;
import com.samyotech.petstand.adapter.AdapterViewOrderDetails;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.models.MyOrderDetails;
import com.samyotech.petstand.models.MyOrderDto;
import com.samyotech.petstand.models.OrderStatusDTO;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomTextView;
import com.samyotech.petstand.utils.CustomTextViewBold;
import com.samyotech.petstand.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ViewOrderActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    private String TAG = ViewOrderActivity.class.getSimpleName();
    private RecyclerView lsVieworder;
    private ArrayList<MyOrderDetails> myOrderDtoItemList;
    private AdapterViewOrderDetails viewOrderAdapter;
    private LayoutInflater adaptorInflator;
    private LinearLayout back;
    private CustomTextView tvgrandtotel, tvpay;
    CardView cvpay;
    private String order_id = "";
    private String final_price = "";
    private String status = "";
    private String currencytype = "";
    private LoginDTO loginDTO;
    private SharedPrefrence prefrence;
    private HashMap<String, String> paramsOrder = new HashMap<>();
    private ImageView ivOrderImage, ivProducted, IVpacked, IVdispatched, IVdelivered;
    CustomTextView tvOrderID, tvDeliveryDate, tvAddress;
    MyOrderDto myOrderDto;
    CustomTextView ctvConformeddate, ctvPackedddate, ctvDispatcheddate, ctvDeleverddate, ctvPendingdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(ViewOrderActivity.this);
        setContentView(R.layout.activity_view_order);
        mContext = ViewOrderActivity.this;
        adaptorInflator = LayoutInflater.from(mContext);

        prefrence = SharedPrefrence.getInstance(mContext);
        loginDTO = prefrence.getParentUser(Consts.LOGINDTO);
        paramsOrder.put(Consts.USER_ID, loginDTO.getId());

        if (getIntent().hasExtra(Consts.ORDER_ID)) {
            final_price = getIntent().getStringExtra(Consts.FINAL_PRICE);
            myOrderDto = (MyOrderDto) getIntent().getSerializableExtra(Consts.ORDER);
            order_id = getIntent().getStringExtra(Consts.ORDER_ID);
            status = getIntent().getStringExtra(Consts.CONFIRM_STATUS);
            currencytype = getIntent().getStringExtra(Consts.CURRENCYTYPE);
            paramsOrder.put(Consts.ORDER_ID, order_id);

        }
        init();
    }

    private void init() {
        tvgrandtotel = (CustomTextView) findViewById(R.id.tvgrandtotel);
        cvpay = findViewById(R.id.cvpay);
        back = (LinearLayout) findViewById(R.id.back);
        lsVieworder = (RecyclerView) findViewById(R.id.lsVieworder);
        ivProducted = (ImageView) findViewById(R.id.ivProducted);
        IVpacked = (ImageView) findViewById(R.id.IVpacked);
        IVdispatched = (ImageView) findViewById(R.id.IVdispatched);
        IVdelivered = (ImageView) findViewById(R.id.IVdelivered);

        tvOrderID = findViewById(R.id.tvOrderID);
        tvDeliveryDate = findViewById(R.id.tvDeliveryDate);
        tvAddress = findViewById(R.id.tvAddress);
        ctvConformeddate = findViewById(R.id.ctvConformeddate);
        ctvPackedddate = findViewById(R.id.ctvPackedddate);
        ctvDeleverddate = findViewById(R.id.ctvDeleverddate);
        ctvDispatcheddate = findViewById(R.id.ctvDispatcheddate);
        ctvPendingdate = findViewById(R.id.ctvPendingdate);

        tvOrderID.setText(myOrderDto.getOrder_id());
        tvDeliveryDate.setText(myOrderDto.getName());
        tvAddress.setText(myOrderDto.getAddress());

        tvgrandtotel.setText(currencytype + " " + final_price);


        if (status.equals("0")) {
            cvpay.setVisibility(View.VISIBLE);
        } else if (status.equals("1")) {
            cvpay.setVisibility(View.GONE);
        }

        cvpay.setOnClickListener(this);
        back.setOnClickListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        getMyOrder();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cvpay:
                initPayment();
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    public void getMyOrder() {
        ProjectUtils.showProgressDialog(mContext, true, "Please Wait!!");

        new HttpsRequest(Consts.GET_MY_ORDER_DETAILS, paramsOrder, mContext).stringPost(TAG, new Helper() {
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    Type listType = new TypeToken<List<MyOrderDetails>>() {
                    }.getType();
                    try {
                        myOrderDtoItemList = new ArrayList<>();
                        myOrderDtoItemList = (ArrayList<MyOrderDetails>) new Gson().fromJson(response.getJSONArray("data").toString(), listType);
                        showDataList();
                        getMyOrderStatus();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    ProjectUtils.showLong(mContext, msg);
                }
            }
        });

    }

    public void getMyOrderStatus() {
        //  ProjectUtils.showProgressDialog(mContext, true, "Please Wait!!");

        new HttpsRequest(Consts.GET_ORDER_STATUS, paramsOrder, mContext).stringPost(TAG, new Helper() {
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {

                    try {
                        OrderStatusDTO orderStatusDTO = new Gson().fromJson(response.getJSONObject("data").toString(), OrderStatusDTO.class);

                        if (orderStatusDTO.getOd_status().equalsIgnoreCase("6")) {

                        } else if (orderStatusDTO.getOd_status().equalsIgnoreCase("1")) {
                            ivProducted.setImageResource(R.drawable.produced);

                        } else if (orderStatusDTO.getOd_status().equalsIgnoreCase("3")) {
                            ivProducted.setImageResource(R.drawable.produced);
                            IVpacked.setImageResource(R.drawable.packed);


                        } else if (orderStatusDTO.getOd_status().equalsIgnoreCase("4")) {
                            ivProducted.setImageResource(R.drawable.produced);
                            IVpacked.setImageResource(R.drawable.packed);
                            IVdispatched.setImageResource(R.drawable.group);

                        } else if (orderStatusDTO.getOd_status().equalsIgnoreCase("2")) {
                            ivProducted.setImageResource(R.drawable.produced);
                            IVpacked.setImageResource(R.drawable.packed);
                            IVdispatched.setImageResource(R.drawable.group);
                            IVdelivered.setImageResource(R.drawable.delivered_active);

                        }

                        ctvPendingdate.setText(ProjectUtils.parseToddMMMyyyy(orderStatusDTO.getOd_pending_date()));
                        ctvConformeddate.setText(ProjectUtils.parseToddMMMyyyy(orderStatusDTO.getOd_confirm_date()));
                        ctvPackedddate.setText(ProjectUtils.parseToddMMMyyyy(orderStatusDTO.getOd_packed_date()));
                        ctvDispatcheddate.setText(ProjectUtils.parseToddMMMyyyy(orderStatusDTO.getOd_dispatched_date()));
                        ctvDeleverddate.setText(ProjectUtils.parseToddMMMyyyy(orderStatusDTO.getOd_delivered_date()));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    ProjectUtils.showLong(mContext, msg);
                }
            }
        });

    }

    public void showDataList() {

        if (myOrderDtoItemList.size() != 0) {
            LinearLayoutManager  mLayoutManager = new LinearLayoutManager(mContext);
            lsVieworder.setLayoutManager(mLayoutManager);
            viewOrderAdapter = new AdapterViewOrderDetails(ViewOrderActivity.this, myOrderDtoItemList, adaptorInflator);
            lsVieworder.setAdapter(viewOrderAdapter);
        } else {

        }

    }


    public void initPayment() {
        final Dialog dialog = new Dialog(mContext);
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
                String url = "http://phpstack-132936-652468.cloudwaysapps.com/Paypal/paypal?user_id=" + loginDTO.getId() +
                        "&order_id=" + order_id + "&user_name=" + loginDTO.getFirst_name() + "&amount=" + final_price;
                Intent intent = new Intent(mContext, PaymentViewActivity.class);
                intent.putExtra(Consts.PAYAMENT_URL, url);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        llstripe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://phpstack-132936-652468.cloudwaysapps.com/Stripe/BookingPayement/make_payment?user_id=" + loginDTO.getId() +
                        "&order_id=" + order_id + "&user_name=" + loginDTO.getFirst_name() + "&amount=" + final_price;
                Intent intent = new Intent(mContext, PaymentViewActivity.class);
                intent.putExtra(Consts.PAYAMENT_URL, url);
                startActivity(intent);

                dialog.dismiss();
            }
        });
    }


}
