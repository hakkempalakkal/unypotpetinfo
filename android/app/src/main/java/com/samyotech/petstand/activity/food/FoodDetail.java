package com.samyotech.petstand.activity.food;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samyotech.petstand.R;
import com.samyotech.petstand.adapter.AdapterAllReview;
import com.samyotech.petstand.adapter.ProductColorAdapter;
import com.samyotech.petstand.adapter.ProductImagePagerAdapter;
import com.samyotech.petstand.adapter.ProductSizeAdapter;
import com.samyotech.petstand.adapter.ProductWeightAdapter;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.CartDTO;
import com.samyotech.petstand.models.FoodListDTO;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.models.OffersDTO;
import com.samyotech.petstand.models.ProductReview;
import com.samyotech.petstand.models.SingleFoodListDTO;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.AutoScrollViewPager;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomTextView;
import com.samyotech.petstand.utils.CustomTextViewBold;
import com.samyotech.petstand.utils.ImageZoomActivity;
import com.samyotech.petstand.utils.ProjectUtils;
import com.samyotech.petstand.utils.ResizableCustomView;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

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

public class FoodDetail extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout back, llWriteReview;
    private CardView cvMinus, cvPlus, cvAddCart;
    private CustomTextView ctvDesc, cetQuantity, tvCardcount, ctvShippingCost;
    private MaterialRatingBar ratingBar;
    private Context mContext;
    private RelativeLayout layoutIncDecProductItem;
    private FoodListDTO foodListDTO;
    private ImageView ivFoodPic, ivSoldOut;
    private CustomTextViewBold ctvbAmount, ctvbAmountSale, tvProdName, ctvbDiscount, ctvAddtoCart;
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
    private RecyclerView rvReview, rvColor, rvSize, rvWeight;
    MaterialBetterSpinner spinner;
    private ArrayList<CartDTO> cartDTOList;
    ProductColorAdapter productColorAdapter;
    ProductSizeAdapter productSizeAdapter;
    ProductWeightAdapter productWeightAdapter;
    AutoScrollViewPager VPimages;
    ProductImagePagerAdapter productImagePagerAdapter;
    CustomTextViewBold ctvSize, ctvColor, ctvHeaderWeight;
    CustomTextView ctvWeight;
    SingleFoodListDTO singleFoodListDTO;
    ArrayList<SingleFoodListDTO.ProjectDetails> product_deatils = new ArrayList<>();
    LinearLayout llDiscount;


    String P_pet_type = "";
    String p_type = "";
    String product_id = "";
    String product_quantity = "";
    String mandatory = "";
    String weight = "";
    String p_id = "";
    String brand_id = "";
    String p_name = "";
    String image = "";
    int flag = 0;
    OffersDTO offersDTO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.setStatusBarGradiant(FoodDetail.this);
        setContentView(R.layout.food_detail_frag);
        mContext = FoodDetail.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        loginDTO = prefrence.getParentUser(Consts.LOGINDTO);
        init();
    }


    public void init() {
        if (getIntent().hasExtra(Consts.FLAG)) {
            flag = getIntent().getIntExtra(Consts.FLAG, 0);
            if (flag == 1) {
                if (getIntent().hasExtra(Consts.PRODUCT_DETAILS)) {
                    offersDTO = (OffersDTO) getIntent().getSerializableExtra(Consts.PRODUCT_DETAILS);
                    P_pet_type = offersDTO.getP_pet_type();
                    p_type = offersDTO.getP_type();
                    p_id = offersDTO.getP_id();
                    p_name = offersDTO.getP_name();
                    brand_id = offersDTO.getC_id();
                }
            }
        } else {
            if (getIntent().hasExtra(Consts.PRODUCT_DETAILS)) {
                foodListDTO = (FoodListDTO) getIntent().getSerializableExtra(Consts.PRODUCT_DETAILS);
                P_pet_type = getIntent().getStringExtra(Consts.P_PET_TYPE);
                p_type = getIntent().getStringExtra(Consts.P_TYPE);
                p_id = foodListDTO.getP_id();
                p_name = foodListDTO.getP_name();
                brand_id = foodListDTO.getC_id();
         /*       productReviewList = new ArrayList<>();
                productReviewList = foodListDTO.getReviewlist();*/

            }
        }
        rvReview = (RecyclerView) findViewById(R.id.rvReview);
        rvColor = (RecyclerView) findViewById(R.id.rvColor);
        rvSize = (RecyclerView) findViewById(R.id.rvSize);

        llCart = (RelativeLayout) findViewById(R.id.llCart);
        llCart.setOnClickListener(this);
        llWriteReview = (LinearLayout) findViewById(R.id.llWriteReview);
        VPimages = findViewById(R.id.VPimages);

        llWriteReview.setOnClickListener(this);
        ivFoodPic = (ImageView) findViewById(R.id.ivFoodPic);
        ivSoldOut = (ImageView) findViewById(R.id.ivSoldOut);
        tvProdName = (CustomTextViewBold) findViewById(R.id.tvProdName);
        ctvbAmountSale = (CustomTextViewBold) findViewById(R.id.ctvbAmountSale);
        ctvbAmount = (CustomTextViewBold) findViewById(R.id.ctvbAmount);
        ctvAddtoCart = findViewById(R.id.ctvAddtoCart);
        layoutIncDecProductItem = (RelativeLayout) findViewById(R.id.layoutIncDecProductItem);
        cetQuantity = (CustomTextView) findViewById(R.id.cetQuantity);
        tvCardcount = (CustomTextView) findViewById(R.id.tvCardcount);
        ctvDesc = (CustomTextView) findViewById(R.id.ctvDesc);
        cvPlus = (CardView) findViewById(R.id.cvPlus);
        ctvShippingCost = findViewById(R.id.ctvShippingCost);
        ctvSize = findViewById(R.id.ctvSize);
        ctvColor = findViewById(R.id.ctvColor);
        ctvWeight = findViewById(R.id.ctvWeight);
        spinner = findViewById(R.id.spinner);
        ctvHeaderWeight = findViewById(R.id.ctvHeaderWeight);
        ctvbDiscount = findViewById(R.id.ctvbDiscount);
        llDiscount = findViewById(R.id.llDiscount);

        cvPlus.setOnClickListener(this);

        cvMinus = (CardView) findViewById(R.id.cvMinus);
        cvMinus.setOnClickListener(this);

        cvAddCart = (CardView) findViewById(R.id.cvAddCart);
        cvAddCart.setOnClickListener(this);

        back = (LinearLayout) findViewById(R.id.back);
        back.setOnClickListener(this);

        ratingBar = (MaterialRatingBar) findViewById(R.id.ratingBar);


        ivFoodPic.setOnClickListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        parmsCheckProduct.put(Consts.USER_ID, loginDTO.getId());
        parmsCheckProduct.put(Consts.PRODUCT_ID, p_id);

        checkProdcut();
        getMyCart();
        singleProduct();
    }

    @Override
    public void onClick(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.click_event));
        switch (view.getId()) {
            case R.id.cvMinus:
                int quantity = Integer.parseInt(cetQuantity.getText().toString());
                if (quantity > 0) {
                    quantity = quantity - 1;

                    if (quantity == 0) {
                        cvAddCart.setVisibility(View.VISIBLE);
                        layoutIncDecProductItem.setVisibility(View.GONE);

                        parmsRemoveCart.put(Consts.USER_ID, loginDTO.getId());
                        parmsRemoveCart.put(Consts.PRODUCT_ID, p_id);

                        removeCart();
                    } else {
                        cetQuantity.setText("" + quantity);
                        parmsUpdateCart.put(Consts.USER_ID, loginDTO.getId());
                        parmsUpdateCart.put(Consts.PRODUCT_ID, p_id);
                        parmsUpdateCart.put(Consts.QUANTITY, String.valueOf(quantity));
                        parmsUpdateCart.put(Consts.MANDATORY, mandatory);
                        updateCart();
                    }
                }
                break;
            case R.id.cvPlus:
                int quantity1 = Integer.parseInt(cetQuantity.getText().toString());
                if (Integer.valueOf(product_quantity) > quantity1) {
                    quantity1 = quantity1 + 1;
                    cetQuantity.setText("" + quantity1);

                    parmsUpdateCart.put(Consts.USER_ID, loginDTO.getId());
                    parmsUpdateCart.put(Consts.PRODUCT_ID, p_id);
                    parmsUpdateCart.put(Consts.QUANTITY, String.valueOf(quantity1));
                    parmsUpdateCart.put(Consts.MANDATORY, mandatory);
                    updateCart();
                } else {
                    ProjectUtils.showToast(mContext, "Stock is limited, please try later");
                    click();
                }
                break;
            case R.id.cvAddCart:
                if (!product_quantity.equals("0") && !product_quantity.equals("")) {
                    if (singleFoodListDTO.getColor().size() > 0 && singleFoodListDTO.getSize().size() > 0) {
                        cartAdd();
                    } else if (singleFoodListDTO.getColor().size() > 0) {
                        cartColorAdd();
                    } else if (singleFoodListDTO.getSize().size() > 0) {
                        cartSizeAdd();
                    } else {
                        addCart();
                    }
                } else {
                    ProjectUtils.showToast(mContext, "Out Of Stock");
                }
                break;
            case R.id.back:
                finish();
                break;
            case R.id.llCart:
                startActivity(new Intent(mContext, Cart.class));
                break;
            case R.id.llWriteReview:
                Intent in = new Intent(mContext, WriteReview.class);
                in.putExtra(Consts.PRODUCT_ID, p_id);
                startActivity(in);
                break;
            case R.id.ivFoodPic:
                Intent inn = new Intent(mContext, ImageZoomActivity.class);
                inn.putExtra(Consts.IMAGE, image);
                startActivity(inn);
                break;
        }
    }

    private void cartAdd() {
        try {
            if (ProductColorAdapter.manulist.size() > 0) {
                if (ProductSizeAdapter.manulist.size() > 0) {
                    addCart();
                } else {
                    ProjectUtils.showToast(mContext, "Plaese select product size");
                }
            } else {
                ProjectUtils.showToast(mContext, "Plaese select product color");
            }
        } catch (Exception e) {
        }
    }

    private void cartColorAdd() {
        try {
            if (ProductColorAdapter.manulist.size() > 0) {

                addCart();
            } else {
                ProjectUtils.showToast(mContext, "Plaese select product color");
            }
        } catch (Exception e) {
        }
    }

    private void cartSizeAdd() {
        try {

            if (ProductSizeAdapter.manulist.size() > 0) {
                addCart();
            } else {
                ProjectUtils.showToast(mContext, "Plaese select product size");
            }
        } catch (Exception e) {
        }
    }

    public void addCart() {
        String colorstr = ProductColorAdapter.manulist.toString();
        String sizestr = ProductSizeAdapter.manulist.toString();
        cvAddCart.setVisibility(View.GONE);
        layoutIncDecProductItem.setVisibility(View.VISIBLE);
        cetQuantity.setText("" + 1);
        parmsAddCart.put(Consts.USER_ID, loginDTO.getId());
        parmsAddCart.put(Consts.PRODUCT_ID, product_id);
        parmsAddCart.put(Consts.COLOR, colorstr.substring(1, colorstr.length() - 1));
        parmsAddCart.put(Consts.SIZE, sizestr.substring(1, sizestr.length() - 1));
        parmsAddCart.put(Consts.QUANTITY, "1");
        parmsAddCart.put(Consts.SHIPPING_COST, singleFoodListDTO.getShipping_cost());
        parmsAddCart.put(Consts.MANDATORY, mandatory);
        parmsAddCart.put(Consts.WEIGHT, weight);
        addToCart();
    }


    public void singleProduct() {
        ProjectUtils.showProgressDialog(mContext, true, "Please Wait!!");
        new HttpsRequest(Consts.GET_SINGLE_PRODUCTS, getparms(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    // ProjectUtils.showLong(mContext, msg);
                    try {
                        singleFoodListDTO = new Gson().fromJson(response.getJSONObject("data").toString(), SingleFoodListDTO.class);

                        showData();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    ProjectUtils.showLong(mContext, msg);
                }
            }
        });
    }//http://peteats.in/Admin/WebService/getSingleProducts

    private void showData() {


        if (singleFoodListDTO.getColor().size() == 0) {
            ctvColor.setVisibility(View.GONE);
            rvColor.setVisibility(View.GONE);
        }
        if (singleFoodListDTO.getSize().size() == 0) {
            ctvSize.setVisibility(View.GONE);
            rvSize.setVisibility(View.GONE);
        }


        tvProdName.setText(singleFoodListDTO.getP_name());
        ctvDesc.setText(singleFoodListDTO.getP_description());
        if (!singleFoodListDTO.getWeight().equals("") && !singleFoodListDTO.getWeight().equals("0")) {
            ctvWeight.setText("Weight : " + singleFoodListDTO.getWeight());
        } else {
            ctvWeight.setVisibility(View.GONE);
        }

        ResizableCustomView.doResizeTextView(ctvDesc, 2, "View More", true);


        try {
            ratingBar.setRating(Float.parseFloat(singleFoodListDTO.getProduct_rating()));
        } catch (Exception e) {

        }
        linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);


        productImagePagerAdapter = new ProductImagePagerAdapter(mContext, singleFoodListDTO.getProImage());
        VPimages.setAdapter(productImagePagerAdapter);
        VPimages.setCurrentItem(0);
        VPimages.startAutoScroll();
        VPimages.setInterval(1500);
        VPimages.setCycle(true);
        VPimages.setStopScrollWhenTouch(true);

        LinearLayoutManager sizeLayoutManagaer = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        rvSize.setLayoutManager(sizeLayoutManagaer);
        productSizeAdapter = new ProductSizeAdapter(FoodDetail.this, singleFoodListDTO.getProduct_deatils());//FIRST CHANGE
        rvSize.setAdapter(productSizeAdapter);


        if (!singleFoodListDTO.getWeight().equals("0") && !singleFoodListDTO.getWeight().equals("")) {
            spinner.setVisibility(View.VISIBLE);
            ctvHeaderWeight.setVisibility(View.VISIBLE);
            /*LinearLayoutManager weightLayoutManagaer = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            rvWeight.setLayoutManager(weightLayoutManagaer);
            productWeightAdapter = new ProductWeightAdapter(FoodDetail.this, singleFoodListDTO.getProduct_deatils());//FIRST CHANGE
            rvWeight.setAdapter(productWeightAdapter);*/

            try {
                if (singleFoodListDTO.getProduct_deatils().size() > 0) {
                    spinner.setText(singleFoodListDTO.getProduct_deatils().get(0).getWeight());
                }
            } catch (Exception e) {

            }
            ArrayAdapter<SingleFoodListDTO.ProjectDetails> adapterthick = new ArrayAdapter<SingleFoodListDTO.ProjectDetails>(mContext, android.R.layout.simple_list_item_1, singleFoodListDTO.getProduct_deatils());
            adapterthick.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapterthick);

            spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    setPrice(position);


                    // singleFoodListDTO.notifyItemChanged(position);
                }

            });

        }

        rvReview.setLayoutManager(linearLayoutManager);
        rvReview.setItemAnimator(new DefaultItemAnimator());
        adapterAllReview = new AdapterAllReview(mContext, singleFoodListDTO.getReviewlist());
        rvReview.setNestedScrollingEnabled(true);
        rvReview.setScrollBarSize(0);
        rvReview.setAdapter(adapterAllReview);

        try {
            if (singleFoodListDTO.getProduct_deatils().size() > 0) {
                setPrice(0);
                ProductSizeAdapter.manulist.add(singleFoodListDTO.getProduct_deatils().get(0).getSize());
            }
        } catch (Exception e) {

        }
    }

    public void setPrice(int position) {
        Log.e("IMAGE", singleFoodListDTO.getProduct_deatils().get(position).getImg_path());

        if (singleFoodListDTO.getProduct_deatils().get(position).getDiscount().equals("0")) {
            llDiscount.setVisibility(View.GONE);
        } else {
            llDiscount.setVisibility(View.VISIBLE);
            ctvbDiscount.setText(singleFoodListDTO.getProduct_deatils().get(position).getDiscount() + "% off");
        }

        if (singleFoodListDTO.getProduct_deatils().get(position).getShipping_cost().equals("0")) {
            ctvShippingCost.setText("Free");
            ctvShippingCost.setTextColor(getResources().getColor(R.color.red));
        } else {
            ctvShippingCost.setText(singleFoodListDTO.getCurrency_type() + " " + ProjectUtils.round(Float.parseFloat(singleFoodListDTO.getProduct_deatils().get(position).getShipping_cost()), 2) + "");
            ctvShippingCost.setTextColor(getResources().getColor(R.color.inset));
        }
        ctvbAmountSale.setText(singleFoodListDTO.getCurrency_type() + " " + ProjectUtils.round(Float.parseFloat(singleFoodListDTO.getProduct_deatils().get(position).getFinal_amount()), 2) + "");
        ctvbAmount.setText(singleFoodListDTO.getCurrency_type() + " " + ProjectUtils.round(Float.parseFloat(singleFoodListDTO.getProduct_deatils().get(position).getP_rate_sale()), 2) + "");
        ctvbAmount.setPaintFlags(ctvbAmount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        LinearLayoutManager categoryLayoutManagaer = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        rvColor.setLayoutManager(categoryLayoutManagaer);
        productColorAdapter = new ProductColorAdapter(mContext, singleFoodListDTO.getProduct_deatils().get(position).getColor());//FIRST CHANGE
        rvColor.setAdapter(productColorAdapter);


        product_id = singleFoodListDTO.getProduct_deatils().get(position).getP_id();
        product_quantity = singleFoodListDTO.getProduct_deatils().get(position).getQuantity();
        mandatory = singleFoodListDTO.getProduct_deatils().get(position).getMandatory();
        parmsCheckProduct.put(Consts.PRODUCT_ID, singleFoodListDTO.getProduct_deatils().get(position).getP_id());
        weight = singleFoodListDTO.getProduct_deatils().get(position).getWeight();

        image = singleFoodListDTO.getProduct_deatils().get(position).getImg_path();

        if(product_quantity.equalsIgnoreCase("0")){
            ivSoldOut.setVisibility(View.VISIBLE);
            ctvAddtoCart.setText("Sold Out");
            cvAddCart.setEnabled(false);
        }else{
            ivSoldOut.setVisibility(View.GONE);
            ctvAddtoCart.setText("Add To Cart");
            cvAddCart.setEnabled(true);
        }

        Glide.with(mContext)
                .load(image)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivFoodPic);

        checkProdcut();
    }

    public HashMap<String, String> getparms() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.P_PET_TYPE, P_pet_type);
        parms.put(Consts.USER_ID, loginDTO.getId());
        parms.put(Consts.P_TYPE, p_type);
        parms.put(Consts.C_ID, brand_id);
        parms.put(Consts.P_ID, p_id);
        parms.put(Consts.P_NAME, p_name);
        parms.put(Consts.COUNTRY, "IN");
        Log.e("parms", parms.toString());
        return parms;
    }

    public void addToCart() {
        ProjectUtils.showProgressDialog(mContext, true, "Please Wait!!");
        new HttpsRequest(Consts.ADD_TO_CART, parmsAddCart, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    ProjectUtils.showLong(mContext, msg);
                    getMyCart();

                } else {
                    ProjectUtils.showLong(mContext, msg);
                }
            }
        });
    }

    public void updateCart() {
        //ProjectUtils.showProgressDialog(mContext, true, "Please Wait!!");
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
                    getMyCart();

                } else {
                    ProjectUtils.showLong(mContext, msg);
                }
            }
        });
    }

    public void checkProdcut() {
        //  ProjectUtils.showProgressDialog(mContext, true, "Please Wait!!");
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
                    cvAddCart.setVisibility(View.GONE);
                    layoutIncDecProductItem.setVisibility(View.VISIBLE);
                } else {
                    cvAddCart.setVisibility(View.VISIBLE);
                    layoutIncDecProductItem.setVisibility(View.GONE);
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
                    //   ProjectUtils.showLong(mContext, msg);
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

    public void click() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.walk_icon)
                .setTitle(R.string.app_name)
                .setMessage("Stock is limited, please try later")
                /*.setPositiveButton("Yes, Ok!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent i = new Intent();
                        i.setAction(Intent.ACTION_MAIN);
                        i.addCategory(Intent.CATEGORY_HOME);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    }
                })*/
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

}
