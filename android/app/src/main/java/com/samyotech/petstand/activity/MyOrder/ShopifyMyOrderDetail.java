package com.samyotech.petstand.activity.MyOrder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.food.Cart;
import com.samyotech.petstand.activity.food.FoodDetail;
import com.samyotech.petstand.activity.food.OrdershipedActivity;
import com.samyotech.petstand.activity.food.WriteReview;
import com.samyotech.petstand.adapter.AdapterAllReview;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.jsonparser.JSONParser;
import com.samyotech.petstand.models.CartDTO;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.models.ProductReview;
import com.samyotech.petstand.models.SendShopifyProductDTO;
import com.samyotech.petstand.models.ShopifyMyOrderDTO;
import com.samyotech.petstand.models.SingleShopifyOrderDTO;
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

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * Created by mayank on 16/2/18.
 */

public class ShopifyMyOrderDetail extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout back, llWriteReview;
    private CardView cvMinus, cvPlus, cvAddCart;
    private CustomTextView ctvDesc, cetQuantity, tvCardcount;
    private MaterialRatingBar ratingBar;
    private Context mContext;
    private RelativeLayout layoutIncDecProductItem;
    //  private ProductSopifyDTO foodListDTO;
    private ImageView ivFoodPic;
    private CustomTextViewBold ctvbAmount, ctvbAmountSale, tvProdName;
    private String TAG = FoodDetail.class.getSimpleName();
    private HashMap<String, String> parmsAddCart = new HashMap<>();
    private HashMap<String, String> parmsUpdateCart = new HashMap<>();
    private HashMap<String, String> parmsRemoveCart = new HashMap<>();
    private HashMap<String, String> parmsCheckProduct = new HashMap<>();
    private SharedPrefrence prefrence;
    private LoginDTO loginDTO;
    private RelativeLayout llCart;
    private ArrayList<ProductReview> productReviewList;
    private LinearLayoutManager linearLayoutManager;
    private AdapterAllReview adapterAllReview;
    private RecyclerView rvReview;
    private ArrayList<CartDTO> cartDTOList;
    private ArrayList<SendShopifyProductDTO> sendShopifyProductDTOList;
    String strjsonarray = "";
    String orderID = "";
    SingleShopifyOrderDTO singleShopifyOrderDTO;
    ShopifyMyOrderDTO shopifyMyOrderDetail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(ShopifyMyOrderDetail.this);
        setContentView(R.layout.my_order_detail_frag);
        mContext = ShopifyMyOrderDetail.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        loginDTO = prefrence.getParentUser(Consts.LOGINDTO);
        orderID = getIntent().getStringExtra(Consts.ORDER_ID);
        shopifyMyOrderDetail = (ShopifyMyOrderDTO) getIntent().getSerializableExtra(Consts.ORDER);
        init();

    }


    public void init() {
      /*  if (getIntent().hasExtra(Consts.PRODUCT_DETAILS)) {
            foodListDTO = (ProductSopifyDTO) getIntent().getSerializableExtra(Consts.PRODUCT_DETAILS);
            productReviewList = new ArrayList<>();
            // productReviewList = foodListDTO.getReviewlist();
        }*/
        rvReview = (RecyclerView) findViewById(R.id.rvReview);
        llCart = (RelativeLayout) findViewById(R.id.llCart);
        llCart.setOnClickListener(this);
        llWriteReview = (LinearLayout) findViewById(R.id.llWriteReview);

        llWriteReview.setOnClickListener(this);
        ivFoodPic = (ImageView) findViewById(R.id.ivFoodPic);
        tvProdName = (CustomTextViewBold) findViewById(R.id.tvProdName);
        ctvbAmountSale = (CustomTextViewBold) findViewById(R.id.ctvbAmountSale);
        ctvbAmount = (CustomTextViewBold) findViewById(R.id.ctvbAmount);
        layoutIncDecProductItem = (RelativeLayout) findViewById(R.id.layoutIncDecProductItem);
        cetQuantity = (CustomTextView) findViewById(R.id.cetQuantity);
        tvCardcount = (CustomTextView) findViewById(R.id.tvCardcount);
        ctvDesc = (CustomTextView) findViewById(R.id.ctvDesc);
        cvPlus = (CardView) findViewById(R.id.cvPlus);
        cvPlus.setOnClickListener(this);

        cvMinus = (CardView) findViewById(R.id.cvMinus);
        cvMinus.setOnClickListener(this);

        cvAddCart = (CardView) findViewById(R.id.cvAddCart);
        cvAddCart.setOnClickListener(this);

        back = (LinearLayout) findViewById(R.id.back);
        back.setOnClickListener(this);

        ratingBar = (MaterialRatingBar) findViewById(R.id.ratingBar);


        tvProdName.setText("Order Id :"+shopifyMyOrderDetail.getOrder_id());
        ctvDesc.setText(ProjectUtils.removeHtmlFromText(shopifyMyOrderDetail.getTitle()));
        ctvbAmountSale.setText("$ " + shopifyMyOrderDetail.getPrice());
        ctvbAmount.setText("Quantity : "+shopifyMyOrderDetail.getQuantity());
      //  ctvbAmount.setPaintFlags(ctvbAmount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        Glide.with(mContext)
                .load(shopifyMyOrderDetail.getImage())
                .placeholder(R.drawable.app_icon).into(ivFoodPic);
        //  ratingBar.setRating(Float.parseFloat(foodListDTO.getProduct_rating()));
        linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);

        rvReview.setLayoutManager(linearLayoutManager);
        rvReview.setItemAnimator(new DefaultItemAnimator());
        adapterAllReview = new AdapterAllReview(mContext, productReviewList);
        rvReview.setNestedScrollingEnabled(true);
        rvReview.setScrollBarSize(0);
        rvReview.setAdapter(adapterAllReview);

    }

    @Override
    protected void onResume() {
        super.onResume();
        parmsCheckProduct.put(Consts.USER_ID, loginDTO.getId());
        //parmsCheckProduct.put(Consts.PRODUCT_ID, foodListDTO.getId());

        checkProdcut();
        getMyCart();
        //getOrder();
    }

    @Override
    public void onClick(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.click_event));
        switch (view.getId()) {
            case R.id.cvMinus:
                int quantity = Integer.parseInt(cetQuantity.getText().toString());
                if (quantity > 0) {
                    quantity = quantity - 1;

                    if (quantity == 1) {
                        // cvAddCart.setVisibility(View.VISIBLE);
                        // layoutIncDecProductItem.setVisibility(View.GONE);
                        cetQuantity.setText("" + quantity);
                        parmsRemoveCart.put(Consts.USER_ID, loginDTO.getId());
                        //   parmsRemoveCart.put(Consts.PRODUCT_ID, foodListDTO.getId());

                        removeCart();
                    } else {
                        cetQuantity.setText("" + quantity);
                        parmsUpdateCart.put(Consts.USER_ID, loginDTO.getId());
                        //parmsUpdateCart.put(Consts.PRODUCT_ID, foodListDTO.getId());
                        parmsUpdateCart.put(Consts.QUANTITY, String.valueOf(quantity));
                        updateCart();
                    }
                }
                break;
            case R.id.cvPlus:
                int quantity1 = Integer.parseInt(cetQuantity.getText().toString());
                quantity1 = quantity1 + 1;
                cetQuantity.setText("" + quantity1);

                parmsUpdateCart.put(Consts.USER_ID, loginDTO.getId());
                //parmsUpdateCart.put(Consts.PRODUCT_ID, foodListDTO.getId());
                parmsUpdateCart.put(Consts.QUANTITY, String.valueOf(quantity1));
                updateCart();
                break;
            case R.id.cvAddCart:
                cartnext();
                //addCart();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.llCart:
                startActivity(new Intent(mContext, Cart.class));
                break;
            case R.id.llWriteReview:
                Intent in = new Intent(mContext, WriteReview.class);
                // in.putExtra(Consts.PRODUCT_ID, foodListDTO.getId());
                startActivity(in);
                break;
        }
    }

    private void cartnext() {
        try {
            ProjectUtils.showAlertDialogWithCancel(mContext, "Confirm", "Look like you have confirmed your order. Customer team will contact you. Thank you", "Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    //makeOrder();
                    Intent inn = new Intent(mContext, OrdershipedActivity.class);
                   /* inn.putExtra(Consts.VARIANT_ID, foodListDTO.getId());
                    inn.putExtra(Consts.TITLE, foodListDTO.getTitle());
                    inn.putExtra(Consts.PRICE, foodListDTO.getVariants().get(0).getPrice());
                    inn.putExtra(Consts.IMAGE, foodListDTO.getImage().getSrc());*/
                    inn.putExtra(Consts.QUANTITY, cetQuantity.getText().toString().trim());
                    startActivity(inn);
                }
            }, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        } catch (Exception e) {

        }
    }


    public void getOrder() {
        ProjectUtils.showProgressDialog(mContext, true, "Please Wait!!");
        AndroidNetworking.post(Consts.GET_ORDER)
                .addBodyParameter(Consts.ORDER_ID, orderID)
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ProjectUtils.pauseProgressDialog();

                        JSONParser jsonParser = new JSONParser(mContext, response);
                        if (jsonParser.RESULT) {
                            try {

                                singleShopifyOrderDTO = new Gson().fromJson(response.getJSONObject("data").toString(), SingleShopifyOrderDTO.class);

                               /* tvProdName.setText(singleShopifyOrderDTO.get());
                                ctvDesc.setText(ProjectUtils.removeHtmlFromText(foodListDTO.getBody_html()));
                                ctvbAmountSale.setText("$ " + foodListDTO.getVariants().get(0).getPrice());
                                ctvbAmount.setText("$ " + foodListDTO.getVariants().get(0).getPrice());
                                ctvbAmount.setPaintFlags(ctvbAmount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                Glide.with(mContext)
                                        .load(foodListDTO.getImage().getSrc())
                                        .placeholder(R.drawable.app_icon).into(ivFoodPic);
*/
                            } catch (JSONException e) {
                                e.printStackTrace();
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

    void updateCart() {
        ProjectUtils.showProgressDialog(mContext, true, "Please Wait!!");
        new HttpsRequest(Consts.UPDATE_CART_QUANTITY, parmsUpdateCart, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    ProjectUtils.showLong(mContext, msg);

                } else {
                    ProjectUtils.showLong(mContext, msg);
                }
            }
        });
    }

    public void removeCart() {
        ProjectUtils.showProgressDialog(mContext, true, "Please Wait!!");
        new HttpsRequest(Consts.REMOVE_PRODUCT_CART, parmsRemoveCart, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    ProjectUtils.showLong(mContext, msg);

                } else {
                    ProjectUtils.showLong(mContext, msg);
                }
            }
        });
    }

    public void checkProdcut() {
        ProjectUtils.showProgressDialog(mContext, true, "Please Wait!!");
        new HttpsRequest(Consts.CHECK_PRODUCT, parmsCheckProduct, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {

                    try {
                        String qu = response.getString("data");
                        cetQuantity.setText(qu);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // cvAddCart.setVisibility(View.GONE);
                    //layoutIncDecProductItem.setVisibility(View.VISIBLE);
                } else {
                    //  cvAddCart.setVisibility(View.VISIBLE);
                    // layoutIncDecProductItem.setVisibility(View.GONE);
                }
            }
        });
    }


    public void getMyCart() {
        new HttpsRequest(Consts.GET_MY_CART, getparmsCart(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    Type listType = new TypeToken<List<CartDTO>>() {
                    }.getType();
                    try {
                        cartDTOList = new ArrayList<>();
                        cartDTOList = (ArrayList<CartDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), listType);

                        if (cartDTOList.size() > 0) {
                            tvCardcount.setVisibility(View.VISIBLE);
                            tvCardcount.setText(String.valueOf(cartDTOList.size()));
                        } else {
                            tvCardcount.setVisibility(View.GONE);
                            tvCardcount.setText(" " + 0);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    ProjectUtils.showLong(mContext, msg);
                    tvCardcount.setVisibility(View.GONE);
                    tvCardcount.setText(" " + 0);
                }
            }
        });

    }

    public HashMap<String, String> getparmsCart() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.USER_ID, loginDTO.getId());
        Log.e("parms", parms.toString());
        return parms;
    }

}
