package com.samyotech.petstand.activity.food;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.location.places.Place;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.PaymentViewActivity;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.jsonparser.JSONParser;
import com.samyotech.petstand.models.AddressDTO;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.models.MakeOrderDTO;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomEditText;
import com.samyotech.petstand.utils.ProjectUtils;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.schibstedspain.leku.LocationPickerActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static com.schibstedspain.leku.LocationPickerActivityKt.LATITUDE;
import static com.schibstedspain.leku.LocationPickerActivityKt.LONGITUDE;
import static com.schibstedspain.leku.LocationPickerActivityKt.ZIPCODE;

public class OrdershipedActivity extends AppCompatActivity implements OnClickListener, PaymentResultListener {

    Context mContext;
    CustomEditText cetZipCode, cetName, cetMobileNo, cetAddressMap, cetAddress, cetEmail, cetCity, cetCountry;
    RadioGroup RGpayment;
    RadioButton RBcaseon, RBonline;

    CardView cvOrder;
    LinearLayout back;
    private String TAG = OrdershipedActivity.class.getSimpleName();
    private SharedPrefrence prefrence;
    private LoginDTO loginDTO;
    private Place place;
    private MakeOrderDTO makeOrderDTO;
    HashMap<String, String> parmsOrder = new HashMap<>();
    private CountryCodePicker countryCodePicker;
    String orderID = "";
    String totalPay = "", shoppingpay = "";
    Double latitude, longitude;
    AddressDTO addressDTO;
    int flag = 0;
    String orderId;
    String checksumString = "";
    float finalPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(OrdershipedActivity.this);
        setContentView(R.layout.activity_ordershiped);
        mContext = OrdershipedActivity.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        loginDTO = prefrence.getParentUser(Consts.LOGINDTO);

        totalPay = getIntent().getStringExtra(Consts.PAYMENT_STATUS);
        shoppingpay = getIntent().getStringExtra(Consts.SHIPPING_COST);


        Checkout.preload(getApplicationContext());
        findUI();
        if (getIntent().hasExtra(Consts.FLAG)) {
            flag = getIntent().getIntExtra(Consts.FLAG, 0);
            if (flag == 1) {
                addressDTO = (AddressDTO) getIntent().getSerializableExtra(Consts.ADDRESS);
                showData();
            }
        }

    }

    private void showData() {
        cetAddressMap.setText(addressDTO.getAddress());
        cetCountry.setText(addressDTO.getCountry());
        cetCity.setText(addressDTO.getCity());
        cetAddress.setText(addressDTO.getLandMark());
        cetZipCode.setText(addressDTO.getZip());

    }

    private void findUI() {
        countryCodePicker = (CountryCodePicker) findViewById(R.id.ccp);
        cetName = findViewById(R.id.cetName);
        cetMobileNo = findViewById(R.id.cetMobileNo);
        cetAddressMap = findViewById(R.id.cetAddressMap);
        cetAddress = findViewById(R.id.cetAddress);
        RGpayment = findViewById(R.id.RGpayment);
        RBonline = findViewById(R.id.RBonline);
        RBcaseon = findViewById(R.id.RBcaseon);
        cvOrder = findViewById(R.id.cvOrder);
        RBcaseon = findViewById(R.id.RBcaseon);
        back = findViewById(R.id.back);
        cetEmail = findViewById(R.id.cetEmail);
        cetCity = findViewById(R.id.cetCity);
        cetCountry = findViewById(R.id.cetCountry);
        cetZipCode = findViewById(R.id.cetZipCode);

        back.setOnClickListener(this);
        cvOrder.setOnClickListener(this);
        cetAddressMap.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.cvOrder:
                order();
                break;
            case R.id.cetAddressMap:
                ProjectUtils.showProgressDialog(mContext, true, "Please Wait!!");
                findPlace();
                break;
        }
    }

    private void order() {
        if (!validateName()) {
            return;
        } else if (!validateMobile()) {
            return;
        } else if (!validateEmail()) {
            return;
        } else if (!validateAddress(cetAddressMap)) {
            return;
        } else if (!validateAddress(cetAddress)) {
            return;
        } else if (!validateCity()) {
            return;
        } else if (!validateCountry()) {
            return;
        } else if (!validateZip()) {
            return;
        } else {
            //payment();
            genrateCheckSum();
        }
    }


    public void genrateCheckSum() {
        Random otp1 = new Random();
        StringBuilder builder = new StringBuilder();
        for (int count = 0; count <= 10; count++) {
            builder.append(otp1.nextInt(10));
        }
        orderID = builder.toString();
        finalPrice = (Float.valueOf(totalPay) + Float.valueOf(shoppingpay));

        HashMap<String, String> checksumParams = new HashMap<>();

        checksumParams.put(Consts.MID_PAYTM, "VqOOGn90907310937924");
        checksumParams.put(Consts.ORDER_ID_PAYTM, orderID);
        checksumParams.put(Consts.CUST_ID_PAYTM, loginDTO.getId());
        checksumParams.put(Consts.MOBILE_NO_PAYTM, cetMobileNo.getText().toString());
        checksumParams.put(Consts.EMAIL_PAYTM, cetEmail.getText().toString().trim());
        checksumParams.put(Consts.CHANNEL_ID_PAYTM, "WAP");
        checksumParams.put(Consts.WEBSITE_PAYTM, "WEBSTAGING");
        checksumParams.put(Consts.TXN_AMOUNT_PAYTM, finalPrice + "");
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
                                makeOrder("", "0");
                                initializePaytmPayment();
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


    private void initializePaytmPayment() {
        PaytmPGService paytmPGService = PaytmPGService.getProductionService();

        HashMap<String, String> Params = new HashMap<>();

        Params.put(Consts.MID_PAYTM, "VqOOGn90907310937924");
        Params.put(Consts.ORDER_ID_PAYTM, orderID);
        Params.put(Consts.CUST_ID_PAYTM, loginDTO.getId());
        Params.put(Consts.MOBILE_NO_PAYTM, cetMobileNo.getText().toString());
        Params.put(Consts.EMAIL_PAYTM, cetEmail.getText().toString().trim());
        Params.put(Consts.CHANNEL_ID_PAYTM, "WAP");
        Params.put(Consts.WEBSITE_PAYTM, "WEBSTAGING");
        Params.put(Consts.TXN_AMOUNT_PAYTM, finalPrice + "");
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
                    makeOrder(TXNID, "1");
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


    public void payment() {


        String url = "http://phpstack-225750-688566.cloudwaysapps.com/Paypal/paypal?user_id=" + loginDTO.getId() +
                "&order_id=" + orderID + "&user_name=" + cetName.getText().toString() + "&amount=1"/* +totalPay*/;
        Intent intent = new Intent(OrdershipedActivity.this, PaymentViewActivity.class);
        intent.putExtra(Consts.PAYAMENT_URL, url);
        intent.putExtra(Consts.ADDRESS, cetAddress.getText().toString());
        intent.putExtra(Consts.LANDMARK, cetAddressMap.getText().toString());
        intent.putExtra(Consts.NAME, cetName.getText().toString());
        intent.putExtra(Consts.COUNTRY_CODE, countryCodePicker.getSelectedCountryCode() + "");
        intent.putExtra(Consts.MOBILE_NO, cetMobileNo.getText().toString());
        intent.putExtra(Consts.EMAIL, cetEmail.getText().toString());
        intent.putExtra(Consts.CITY, cetCity.getText().toString());
        intent.putExtra(Consts.COUNTRY, cetCountry.getText().toString());
        intent.putExtra(Consts.ZIP, cetZipCode.getText().toString().trim());
        intent.putExtra(Consts.ORDER_ID, orderID);
        startActivity(intent);
        finish();
    }


    public boolean validateName() {
        if (!ProjectUtils.isEditTextFilled(cetName)) {
            cetName.setError("Please enter your name");
            cetName.requestFocus();
            return false;
        } else {
            cetName.setError(null);
            cetName.clearFocus();
            return true;
        }
    }

    public boolean validateZip() {
        if (!ProjectUtils.isEditTextFilled(cetZipCode)) {
            cetZipCode.setError("Please enter zip code");
            cetZipCode.requestFocus();
            return false;
        } else {
            cetZipCode.setError(null);
            cetZipCode.clearFocus();
            return true;
        }
    }

    public boolean validateCountry() {
        if (!ProjectUtils.isEditTextFilled(cetCountry)) {
            cetCountry.setError("Please enter your country");
            cetCountry.requestFocus();
            return false;
        } else {
            cetCountry.setError(null);
            cetCountry.clearFocus();
            return true;
        }
    }

    public boolean validateCity() {
        if (!ProjectUtils.isEditTextFilled(cetCity)) {
            cetCity.setError("Please enter your city");
            cetCity.requestFocus();
            return false;
        } else {
            cetCity.setError(null);
            cetCity.clearFocus();
            return true;
        }
    }

    public boolean validateMobile() {
        if (!ProjectUtils.isEditTextFilled(cetMobileNo)) {
            cetMobileNo.setError("Please enter your mobile.");
            cetMobileNo.requestFocus();
            return false;
        } else {
            cetMobileNo.setError(null);
            cetMobileNo.clearFocus();
            return true;
        }
    }

    public boolean validateEmail() {
        if (!ProjectUtils.isEmailValid(cetEmail.getText().toString().trim())) {
            cetEmail.setError("Please enter correct email.");
            cetEmail.requestFocus();
            return false;
        } else {
            cetEmail.setError(null);
            cetEmail.clearFocus();
            return true;
        }

    }

    public boolean validateAddress(CustomEditText editText) {
        if (!ProjectUtils.isEditTextFilled(editText)) {
            editText.setError("Please enter your Address.");
            editText.requestFocus();
            return false;
        } else {
            editText.setError(null);
            editText.clearFocus();
            return true;
        }
    }


    public void findPlace() {
        try {
            Intent locationPickerIntent = new LocationPickerActivity.Builder()
                    .withGooglePlacesEnabled()
                    //.withLocation(41.4036299, 2.1743558)
                    .build(mContext);

            startActivityForResult(locationPickerIntent, 101);
        } catch (Exception e) {
            // TODO: Handle the error.
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                try {
                    ProjectUtils.pauseProgressDialog();

                    latitude = data.getDoubleExtra(LATITUDE, 0.0);
                    longitude = data.getDoubleExtra(LONGITUDE, 0.0);
                    Log.d("LONGITUDE****", longitude.toString());

                    String postalcode = data.getStringExtra(ZIPCODE);

                    getAddress(latitude, longitude);


                } catch (Exception e) {

                }
            }
        }

    }

    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getSubThoroughfare();
            Log.e("IGA", "Address" + add);
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);

            cetAddressMap.setText(obj.getAddressLine(0));

            String areacode = obj.getPostalCode();
            String city = obj.getSubAdminArea();
            String state = obj.getAdminArea();
            String country = obj.getCountryName();

         /*   if (city != null) {
                cetCity.setText(city);
            }
            if (country != null) {
                cetCountry.setText("india");
            }*/
            if (areacode != null) {
                cetZipCode.setText(areacode);
            }


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void makeOrder(String paymentId, final String status) {

        parmsOrder.put(Consts.USER_ID, loginDTO.getId());
        parmsOrder.put(Consts.ADDRESS, cetAddressMap.getText().toString());
        parmsOrder.put(Consts.PAYMENT_ID, paymentId);
        parmsOrder.put(Consts.LANDMARK, cetAddress.getText().toString());
        parmsOrder.put(Consts.NAME, cetName.getText().toString());
        parmsOrder.put(Consts.COUNTRY_CODE, countryCodePicker.getSelectedCountryCode() + "");
        parmsOrder.put(Consts.MOBILE_NO, cetMobileNo.getText().toString());
        parmsOrder.put(Consts.EMAIL, cetEmail.getText().toString());
        parmsOrder.put(Consts.CITY, cetCity.getText().toString());
        parmsOrder.put(Consts.COUNTRY, cetCountry.getText().toString());
        parmsOrder.put(Consts.ZIP, cetZipCode.getText().toString().trim());
        parmsOrder.put(Consts.SHIPPING_COST, shoppingpay);
        parmsOrder.put(Consts.PAYMENT_STATUS, status);
        parmsOrder.put(Consts.ORDER_ID, orderID);

        ProjectUtils.showProgressDialog(mContext, true, "Please Wait!!");
        new HttpsRequest(Consts.ORDER, parmsOrder, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        makeOrderDTO = new Gson().fromJson(response.getJSONObject("data").toString(), MakeOrderDTO.class);

                        if (status.equals("1")) {
                            Intent intent = new Intent(OrdershipedActivity.this, ViewOrderDetails.class);
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


    public void startPayment() {

        Random otp1 = new Random();
        StringBuilder builder = new StringBuilder();
        for (int count = 0; count <= 10; count++) {
            builder.append(otp1.nextInt(10));
        }
        orderID = builder.toString();

        makeOrder("", "0");

        float finalPrice = (Float.valueOf(totalPay) + Float.valueOf(shoppingpay)) * 100f;

        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", cetName.getText().toString());
            options.put("description", "");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", finalPrice + "");

            JSONObject preFill = new JSONObject();
            preFill.put("email", cetEmail.getText().toString());
            preFill.put("contact", cetMobileNo.getText().toString());

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }


    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();

            makeOrder(razorpayPaymentID, "1");

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


}
