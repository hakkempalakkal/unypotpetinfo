package com.samyotech.petstand.adapter;

import android.content.DialogInterface;
import android.graphics.Paint;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.food.Cart;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.CartDTO;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomTextView;
import com.samyotech.petstand.utils.CustomTextViewBold;
import com.samyotech.petstand.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mayank on 19/1/19.
 */

public class CartAdapter extends BaseAdapter {
    ArrayList<CartDTO> cartDTOList;
    LayoutInflater inflater;
    Cart context;
    private String TAG = CartAdapter.class.getSimpleName();
    private HashMap<String, String> parmsUpdateCart = new HashMap<>();
    private HashMap<String, String> parmsRemoveCart = new HashMap<>();
    int lastPosition = -1;
    private DialogInterface dialog_delete;

    public CartAdapter(Cart context, ArrayList<CartDTO> cartDTOList) {
        this.cartDTOList = cartDTOList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return cartDTOList.size();
    }

    @Override
    public Object getItem(int position) {
        return cartDTOList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final CartAdapter.MyViewHolder mViewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.cart_adapter, parent, false);
            mViewHolder = new CartAdapter.MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (CartAdapter.MyViewHolder) convertView.getTag();
        }


        Glide.with(context).
                load(cartDTOList.get(position).getImg_path())
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mViewHolder.ivFoodPic);


        mViewHolder.ctvbFoodTitle.setText(cartDTOList.get(position).getP_name());
        if (!cartDTOList.get(position).getColor().equals("")) {
            mViewHolder.ctvFoodcolor.setText("Color : " + cartDTOList.get(position).getColor());
        } else {
            mViewHolder.ctvFoodcolor.setVisibility(View.GONE);
        }
        if (!cartDTOList.get(position).getSize().equals("")) {
            mViewHolder.ctvFoodsize.setText("Size : " + cartDTOList.get(position).getSize());
        } else {
            mViewHolder.ctvFoodsize.setVisibility(View.GONE);
        }
        if (!cartDTOList.get(position).getWeight().equals("")) {
            mViewHolder.ctvFoodWeight.setText("Weight : " + cartDTOList.get(position).getWeight());
        } else {
            mViewHolder.ctvFoodWeight.setVisibility(View.GONE);
        }
        mViewHolder.ctvFoodDesc.setText(cartDTOList.get(position).getP_description());
        mViewHolder.cetQuantity.setText(cartDTOList.get(position).getQuantity());
        mViewHolder.ctvbPrice.setText(cartDTOList.get(position).getCurrency_type() + " " + ProjectUtils.round(cartDTOList.get(position).getPrice_dicount(), 2) + "");
        mViewHolder.ctvbPrice1.setText(cartDTOList.get(position).getCurrency_type() + " " + ProjectUtils.round(cartDTOList.get(position).getP_rate(), 2) + "");
        mViewHolder.ctvbPrice1.setPaintFlags(mViewHolder.ctvbPrice1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        mViewHolder.cvMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(mViewHolder.cetQuantity.getText().toString());
                if (quantity > 0) {
                    quantity = quantity - 1;
                    context.cardpricemethodMinus(cartDTOList.get(position).getPrice_dicount());

                    if (quantity == 0) {
                        parmsRemoveCart.put(Consts.USER_ID, cartDTOList.get(position).getUser_id());
                        parmsRemoveCart.put(Consts.PRODUCT_ID, cartDTOList.get(position).getProduct_id());
                        lastPosition = position;
                        removeCart(false);
                    } else {
                        mViewHolder.cetQuantity.setText("" + quantity);
                        parmsUpdateCart.put(Consts.USER_ID, cartDTOList.get(position).getUser_id());
                        parmsUpdateCart.put(Consts.PRODUCT_ID, cartDTOList.get(position).getProduct_id());
                        parmsUpdateCart.put(Consts.QUANTITY, String.valueOf(quantity));
                        updateCart();
                    }
                }
            }
        });
        mViewHolder.cvPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity1 = Integer.parseInt(mViewHolder.cetQuantity.getText().toString());
                quantity1 = quantity1 + 1;
               if (Integer.valueOf(cartDTOList.get(position).getFood_product_quantity()) > quantity1) {
                    mViewHolder.cetQuantity.setText("" + quantity1);

                    parmsUpdateCart.put(Consts.USER_ID, cartDTOList.get(position).getUser_id());
                    parmsUpdateCart.put(Consts.PRODUCT_ID, cartDTOList.get(position).getProduct_id());
                    parmsUpdateCart.put(Consts.QUANTITY, String.valueOf(quantity1));
                    updateCart();

                    context.cardpricemethodPlus(cartDTOList.get(position).getPrice_dicount());
                } else {
                    ProjectUtils.showToast(context, "Stock is limited, please try later");
                }

            }
        });
        mViewHolder.ivRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete(position);
            }
        });
        return convertView;
    }

    public void delete(final int pos) {
        try {
            new AlertDialog.Builder(context)
                    .setIcon(R.drawable.walk_icon)
                    .setTitle("Delete")
                    .setMessage("Are you sure want to delete from cart?")
                    .setPositiveButton("Confirm!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog_delete = dialog;
                            lastPosition = pos;

                            parmsRemoveCart.put(Consts.USER_ID, cartDTOList.get(pos).getUser_id());
                            parmsRemoveCart.put(Consts.PRODUCT_ID, cartDTOList.get(pos).getProduct_id());

                            removeCart(true);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class MyViewHolder {
        CustomTextViewBold ctvbFoodTitle, ctvbPrice1, ctvbPrice;

        ImageView ivFoodPic, ivRemove;
        RelativeLayout rlCart;
        CardView cvRemove, cvMinus, cvPlus;
        CustomTextView cetQuantity, ctvFoodDesc, ctvFoodcolor, ctvFoodsize, ctvFoodWeight;


        public MyViewHolder(View item) {
            ctvbFoodTitle = (CustomTextViewBold) item.findViewById(R.id.ctvbFoodTitle);
            ctvbPrice1 = (CustomTextViewBold) item.findViewById(R.id.ctvbPrice1);
            ctvbPrice = (CustomTextViewBold) item.findViewById(R.id.ctvbPrice);
            cetQuantity = (CustomTextView) item.findViewById(R.id.cetQuantity);
            ctvFoodcolor = (CustomTextView) item.findViewById(R.id.ctvFoodcolor);
            ctvFoodsize = (CustomTextView) item.findViewById(R.id.ctvFoodsize);
            ctvFoodWeight = (CustomTextView) item.findViewById(R.id.ctvFoodWeight);
            ctvFoodDesc = (CustomTextView) item.findViewById(R.id.ctvFoodDesc);
            ivRemove = (ImageView) item.findViewById(R.id.ivRemove);
            ivFoodPic = (ImageView) item.findViewById(R.id.ivFoodPic);
            rlCart = (RelativeLayout) item.findViewById(R.id.rlCart);
            cvRemove = (CardView) item.findViewById(R.id.cvRemove);
            cvMinus = (CardView) item.findViewById(R.id.cvMinus);
            cvPlus = (CardView) item.findViewById(R.id.cvPlus);
        }
    }

    public void updateCart() {
        // ProjectUtils.showProgressDialog(context, true, "Please Wait!!");
        new HttpsRequest(Consts.UPDATE_CART_QUANTITY, parmsUpdateCart, context).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    ProjectUtils.showLong(context, msg);
                } else {
                    ProjectUtils.showLong(context, msg);
                }
            }
        });
    }

    public void removeCart(final boolean tagDailog) {
        ProjectUtils.showProgressDialog(context, true, "Please Wait!!");
        new HttpsRequest(Consts.REMOVE_PRODUCT_CART, parmsRemoveCart, context).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    ProjectUtils.showLong(context, msg);
                    if (tagDailog) {
                        dialog_delete.dismiss();
                    }

                    cartDTOList.remove(lastPosition);
                    notifyDataSetChanged();
                    context.getMyCart();
                } else {
                    ProjectUtils.showLong(context, msg);
                }
            }
        });
    }
}