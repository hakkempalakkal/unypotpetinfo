package com.samyotech.petstand.activity.food;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.BaseActivity;
import com.samyotech.petstand.adapter.CartAdapter;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.CartDTO;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.models.MakeOrderDTO;
import com.samyotech.petstand.models.SendShopifyProductDTO;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomButton;
import com.samyotech.petstand.utils.CustomTextView;
import com.samyotech.petstand.utils.CustomTextViewBold;
import com.samyotech.petstand.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mayank on 17/2/18.
 */

public class Cart extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout back;
    ListView lvCart;
    private CustomTextView ctvNumber;
    private Context mContext;
    private String TAG = Cart.class.getSimpleName();
    private SharedPrefrence prefrence;
    private LoginDTO loginDTO;
    private ArrayList<CartDTO> cartDTOList;
    ArrayList<SendShopifyProductDTO> list = new ArrayList<>();
    private ArrayList<SendShopifyProductDTO> sendShopifyProductDTOList;
    private CartAdapter cartAdapter;
    private CustomButton btNext, btcontinueshop;
    private CustomTextViewBold tvgrandtotel, tvShippingtotel;
    private LinearLayout llNoFound;
    private MakeOrderDTO makeOrderDTO;
    HashMap<String, String> parmsOrder = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(Cart.this);
        setContentView(R.layout.cart_frag);
        mContext = Cart.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        loginDTO = prefrence.getParentUser(Consts.LOGINDTO);

        parmsOrder.put(Consts.USER_ID, loginDTO.getId());
        parmsOrder.put(Consts.ADDRESS, loginDTO.getAddress());
        init();
    }

    public void init() {
        llNoFound = (LinearLayout) findViewById(R.id.llNoFound);
        ctvNumber = (CustomTextView) findViewById(R.id.ctvNumber);
        lvCart = (ListView) findViewById(R.id.lvCart);
        back = (LinearLayout) findViewById(R.id.back);
        tvShippingtotel = findViewById(R.id.tvShippingtotel);
        back.setOnClickListener(this);

        tvgrandtotel = (CustomTextViewBold) findViewById(R.id.tvgrandtotel);
        btNext = (CustomButton) findViewById(R.id.btNext);
        btcontinueshop = (CustomButton) findViewById(R.id.btcontinueshop);
        btNext.setOnClickListener(this);
        btcontinueshop.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.click_event));
        switch (view.getId()) {
            case R.id.btNext:
                if (loginDTO.getAddress().equals("")) {
                    ProjectUtils.showLong(mContext, "Please fill your address");
                } else {
                    cartnext();
                }
                break;
            case R.id.back:
                finish();
                break;
            case R.id.btcontinueshop:
                Intent intent = new Intent(Cart.this, BaseActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void cartnext() {
        try {
            if (cartDTOList.size() > 0) {
                ProjectUtils.showAlertDialogWithCancel(mContext, "Confirm", "Look like you have confirmed your order. Customer team will contact you. Thank you", "Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        //makeOrder();
                        Intent inn = new Intent(mContext, AddressActivity.class);
                        inn.putExtra(Consts.PAYMENT_STATUS, tvgrandtotel.getText().toString().trim().substring(2));
                        if (!tvShippingtotel.getText().toString().trim().equals("Free")) {
                            inn.putExtra(Consts.SHIPPING_COST, tvShippingtotel.getText().toString().trim().substring(2));
                        }else {
                            inn.putExtra(Consts.SHIPPING_COST,"0");
                        }
                        startActivity(inn);
                    }
                }, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
            } else {
                ProjectUtils.showLong(mContext, "No items in cart.");
            }
        } catch (Exception e) {
            ProjectUtils.showLong(mContext, "No items in cart.");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMyCart();
    }

    public void getMyCart() {
        ProjectUtils.showProgressDialog(mContext, true, "Please Wait!!");
        new HttpsRequest(Consts.GET_MY_CART, getparms(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    Type listType = new TypeToken<List<CartDTO>>() {
                    }.getType();
                    try {
                        cartDTOList = new ArrayList<>();
                        cartDTOList = (ArrayList<CartDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), listType);
                        showcartlistdata();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    // ProjectUtils.showLong(mContext, msg);
                    llNoFound.setVisibility(View.VISIBLE);
                    lvCart.setVisibility(View.GONE);
                    tvShippingtotel.setText(" " + 0);
                    tvgrandtotel.setText(" " + 0);
                    ctvNumber.setText(" " + 0);
                }
            }
        });

    }

    public HashMap<String, String> getparms() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.USER_ID, loginDTO.getId());
        Log.e("parms", parms.toString());
        return parms;
    }


    public void showcartlistdata() {

        float valuesum = 0f;
        for (int i = 0; i < cartDTOList.size(); i++) {
            valuesum = valuesum + (cartDTOList.get(i).getPrice_dicount() * (Float.parseFloat(String.valueOf(cartDTOList.get(i).getQuantity()))));
        }


        if (valuesum > 500f) {
            Float shipping = 0f;

            for (int i = 0; i < cartDTOList.size(); i++) {
                if (cartDTOList.get(i).getMandatory().equals("1")) {
                    if (Float.parseFloat(cartDTOList.get(i).getShipping_cost()) > shipping) {
                        shipping = Float.parseFloat(cartDTOList.get(i).getShipping_cost());
                    }
                }
            }
            if (shipping == 0f) {
                tvShippingtotel.setText("Free");
            } else {
                tvShippingtotel.setText(cartDTOList.get(0).getCurrency_type() + " " + shipping + "");
            }

        } else {
            Float shipping = 0f;
            for (int i = 0; i < cartDTOList.size(); i++) {
                if (Float.parseFloat(cartDTOList.get(i).getShipping_cost()) > shipping) {
                    shipping = Float.parseFloat(cartDTOList.get(i).getShipping_cost());
                }
            }

            if (shipping == 0f) {
                tvShippingtotel.setText("Free");
            } else {
                tvShippingtotel.setText(cartDTOList.get(0).getCurrency_type() + " " + shipping + "");
            }
        }

        tvgrandtotel.setText(cartDTOList.get(0).getCurrency_type() + " " + ProjectUtils.round(valuesum, 2) + "");
        //   tvShippingtotel.setText(cartDTOList.get(0).getCurrency_type() + " " + ProjectUtils.round(valueshipping, 1) + "");

        if (cartDTOList.size() > 0) {
            llNoFound.setVisibility(View.GONE);
            lvCart.setVisibility(View.VISIBLE);
            cartAdapter = new CartAdapter(Cart.this, cartDTOList);
            lvCart.setAdapter(cartAdapter);
            ctvNumber.setText(String.valueOf(cartDTOList.size()));
        } else {
            llNoFound.setVisibility(View.VISIBLE);
            lvCart.setVisibility(View.GONE);
            tvgrandtotel.setText(" " + 0);
            ctvNumber.setText(" " + 0);
        }


    }


    public void cardpricemethodPlus(float pr) {

        String strvalue = tvgrandtotel.getText().toString().trim().substring(2);
        float val = Float.parseFloat(strvalue);
        tvgrandtotel.setText(cartDTOList.get(0).getCurrency_type() + " " + ProjectUtils.round((val + pr), 2) + "");
    }

    public void cardpricemethodMinus(float pr) {
        String strvalue = tvgrandtotel.getText().toString().trim().substring(2);
        float val = Float.parseFloat(strvalue);
        tvgrandtotel.setText(cartDTOList.get(0).getCurrency_type() + " " + ProjectUtils.round((val - pr), 2) + "");

    }


    public void makeOrder() {
        ProjectUtils.showProgressDialog(mContext, true, "Please Wait!!");
        new HttpsRequest(Consts.ORDER, parmsOrder, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        makeOrderDTO = new Gson().fromJson(response.getJSONObject("data").toString(), MakeOrderDTO.class);

                        Intent intent = new Intent(Cart.this, ViewOrderDetails.class);
                        intent.putExtra(Consts.MAKE_ORDER, makeOrderDTO);
                        startActivity(intent);
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