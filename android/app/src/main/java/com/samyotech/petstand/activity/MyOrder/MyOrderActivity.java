package com.samyotech.petstand.activity.MyOrder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.MyOrder.adapter.OrderAdapter;
import com.samyotech.petstand.activity.food.ViewOrderDetails;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.jsonparser.JSONParser;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.models.MakeOrderDTO;
import com.samyotech.petstand.models.MyOrderDto;
import com.samyotech.petstand.network.NetworkManager;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.ProjectUtils;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class MyOrderActivity extends AppCompatActivity implements PaymentResultListener {
    private ListView lvOrder;
    private LinearLayout back;
    private ArrayList<MyOrderDto> myOrderDtoList;
    private Context mContext;
    private SharedPrefrence prefrence;
    private String TAG = MyOrderActivity.class.getSimpleName();
    private OrderAdapter orderAdapter;
    private LayoutInflater adaptorInflator;
    public LoginDTO loginDTO;
    private SearchView svFood;
    private HashMap<String, String> paramsOrder = new HashMap<>();
    String orderID;
    String checksumString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(MyOrderActivity.this);
        setContentView(R.layout.activity_my_order);
        mContext = MyOrderActivity.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        loginDTO = prefrence.getParentUser(Consts.LOGINDTO);
        paramsOrder.put(Consts.USER_ID, loginDTO.getId());
        adaptorInflator = LayoutInflater.from(mContext);
        init();
    }

    public void init() {
        lvOrder = findViewById(R.id.lvOrder);
        back = findViewById(R.id.back);
        svFood = findViewById(R.id.svFood);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        svFood.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    orderAdapter.filter(newText);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (NetworkManager.isConnectToInternet(mContext)) {
            getMyOrder();
        } else {
            ProjectUtils.showToast(mContext, getString(R.string.internet_concation));
        }
    }

    public void getMyOrder() {

        ProjectUtils.showProgressDialog(mContext, true, "Please wait....");
        new HttpsRequest(Consts.GET_MY_ORDER, paramsOrder, mContext).stringPost(TAG, new Helper() {
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    Type listType = new TypeToken<List<MyOrderDto>>() {
                    }.getType();
                    try {
                        myOrderDtoList = new ArrayList<>();
                        myOrderDtoList = (ArrayList<MyOrderDto>) new Gson().fromJson(response.getJSONArray("data").toString(), listType);


                        showDataList();

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

        if (myOrderDtoList.size() != 0) {
            orderAdapter = new OrderAdapter(MyOrderActivity.this, myOrderDtoList, adaptorInflator);
            lvOrder.setAdapter(orderAdapter);
        } else {

        }

    }

    public void genrateCheckSum(final int position) {
        Random otp1 = new Random();
        StringBuilder builder = new StringBuilder();
        for (int count = 0; count <= 10; count++) {
            builder.append(otp1.nextInt(10));
        }
        orderID = builder.toString();
        //   finalPrice = (Float.valueOf(totalPay) + Float.valueOf(shoppingpay));

        HashMap<String, String> checksumParams = new HashMap<>();

        checksumParams.put(Consts.MID_PAYTM, "VqOOGn90907310937924");
        checksumParams.put(Consts.ORDER_ID_PAYTM, orderID);
        checksumParams.put(Consts.CUST_ID_PAYTM, loginDTO.getId());
        checksumParams.put(Consts.MOBILE_NO_PAYTM, "");
        checksumParams.put(Consts.EMAIL_PAYTM, myOrderDtoList.get(position).getEmail());
        checksumParams.put(Consts.CHANNEL_ID_PAYTM, "WAP");
        checksumParams.put(Consts.WEBSITE_PAYTM, "WEBSTAGING");
        checksumParams.put(Consts.TXN_AMOUNT_PAYTM, myOrderDtoList.get(position).getFinal_price() + "");
        checksumParams.put(Consts.INDUSTRY_TYPE_ID_PAYTM, "Retail");
        checksumParams.put(Consts.CALLBACK_URL_PAYTM, "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=" + orderID);

        Log.e(TAG, " param --->" + checksumParams.toString());


        ProjectUtils.showProgressDialog(mContext, true, "Please Wait!!");
        AndroidNetworking.post(Consts.PAYTM_CHECKSUM)
                .addBodyParameter(checksumParams)
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ProjectUtils.pauseProgressDialog();
                        Log.e(TAG, " response body --->" + response.toString());

                        JSONParser jsonParser = new JSONParser(mContext, response);
                        if (jsonParser.RESULT) {
                            try {
                                checksumString = response.getJSONObject("data").getString("CHECKSUMHASH");
                                initializePaytmPayment(position);
                            } catch (Exception e) {

                            }
                        } else {
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        ProjectUtils.pauseProgressDialog();
                        Log.e(TAG, " error body --->" + anError.getErrorBody() + " error msg --->" + anError.getMessage());
                    }
                });
    }


    private void initializePaytmPayment(final int position) {
        PaytmPGService paytmPGService = PaytmPGService.getProductionService();

        HashMap<String, String> Params = new HashMap<>();


        Params.put(Consts.MID_PAYTM, "VqOOGn90907310937924");
        Params.put(Consts.ORDER_ID_PAYTM, orderID);
        Params.put(Consts.CUST_ID_PAYTM, loginDTO.getId());
        Params.put(Consts.MOBILE_NO_PAYTM, "");
        Params.put(Consts.EMAIL_PAYTM, myOrderDtoList.get(position).getEmail());
        Params.put(Consts.CHANNEL_ID_PAYTM, "WAP");
        Params.put(Consts.WEBSITE_PAYTM, "WEBSTAGING");
        Params.put(Consts.TXN_AMOUNT_PAYTM, myOrderDtoList.get(position).getFinal_price() + "");
        Params.put(Consts.INDUSTRY_TYPE_ID_PAYTM, "Retail");
        Params.put(Consts.CALLBACK_URL_PAYTM, "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=" + orderID);
        Params.put(Consts.CHECKSUMHASH_PAYTM, checksumString);

        Log.e(TAG, "Paytm param --->" + Params.toString());

        //creating a paytmOrder instance using the hashmap
        PaytmOrder order = new PaytmOrder(Params);

        //initializing a PaytmPGService
        paytmPGService.initialize(order, null);

        //finally starting the paytm payment transaction using PaytmPGService
        paytmPGService.startPaymentTransaction(this, true, true, new PaytmPaymentTransactionCallback() {
            @Override
            public void onTransactionResponse(Bundle inResponse) {
                Log.e("TXNID", "onTransactionResponse: " + inResponse.getString("TXNID"));
                Log.e(TAG, "onTransactionResponse: " + inResponse.getString("RESPMSG"));

                if (inResponse.getString("STATUS").equals("TXN_SUCCESS")) {
                    String TXNID = inResponse.getString("TXNID");
                    makeOrder(TXNID, "1", position);
                }

            }

            @Override
            public void networkNotAvailable() {
                Log.d(TAG, "networkNotAvailable: ");
            }

            @Override
            public void clientAuthenticationFailed(String inErrorMessage) {
            }

            @Override
            public void someUIErrorOccurred(String inErrorMessage) {

            }

            @Override
            public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                Log.d(TAG, "onErrorLoadingWebPage: " + String.valueOf(iniErrorCode));
                Log.d(TAG, "onErrorLoadingWebPage: " + inErrorMessage);
                Log.d(TAG, "onErrorLoadingWebPage: " + inFailingUrl);
            }

            @Override
            public void onBackPressedCancelTransaction() {

            }

            @Override
            public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                Log.d(TAG, "onTransactionCancel: " + inErrorMessage);
                Log.d(TAG, "onTransactionCancel: " + inResponse);
            }
        });
    }


    public void startPayment(int position) {

        float finalPrice = Float.valueOf(myOrderDtoList.get(position).getFinal_price()) * 100f;
        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", myOrderDtoList.get(position).getName());
            options.put("description", "");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", finalPrice + "");

            JSONObject preFill = new JSONObject();
            preFill.put("email", myOrderDtoList.get(position).getEmail());
            // preFill.put("contact", myOrderDtoList.get(position).get());

            options.put("prefill", preFill);

            co.open(this, options);
        } catch (Exception e) {
            Toast.makeText(this, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();

            // makeOrder(razorpayPaymentID);

        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment failed" /*+ code + " " + response*/, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }

    public void makeOrder(String paymentId, final String status, int position) {

        HashMap<String, String> parmsOrder = new HashMap<>();
        parmsOrder.put(Consts.USER_ID, loginDTO.getId());
        parmsOrder.put(Consts.ADDRESS, myOrderDtoList.get(position).getAddress());
        parmsOrder.put(Consts.PAYMENT_ID, paymentId);
        parmsOrder.put(Consts.NAME, myOrderDtoList.get(position).getName());
        parmsOrder.put(Consts.EMAIL, myOrderDtoList.get(position).getEmail());
        parmsOrder.put(Consts.SHIPPING_COST, "0");
        parmsOrder.put(Consts.PAYMENT_STATUS, status);
        parmsOrder.put(Consts.ORDER_ID, myOrderDtoList.get(position).getOrder_id());

        ProjectUtils.showProgressDialog(mContext, true, "Please Wait!!");
        new HttpsRequest(Consts.ORDER, parmsOrder, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        MakeOrderDTO makeOrderDTO = new Gson().fromJson(response.getJSONObject("data").toString(), MakeOrderDTO.class);

                        if (status.equals("1")) {
                            Intent intent = new Intent(MyOrderActivity.this, ViewOrderDetails.class);
                            intent.putExtra(Consts.MAKE_ORDER, makeOrderDTO);
                            startActivity(intent);
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    ProjectUtils.showLong(mContext, msg);

                }
            }
        });
    }

}
