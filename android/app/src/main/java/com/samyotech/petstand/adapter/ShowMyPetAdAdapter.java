package com.samyotech.petstand.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.petmarket.AddPetMarketActivity;
import com.samyotech.petstand.activity.petmarket.CommentPetMarket;
import com.samyotech.petstand.activity.petmarket.UploadMyPetimageActivity;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.models.PetMarketDTO;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomTextView;
import com.samyotech.petstand.utils.CustomTextViewBold;
import com.samyotech.petstand.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by mayank on 16/2/18.
 */

public class ShowMyPetAdAdapter extends RecyclerView.Adapter<ShowMyPetAdAdapter.MyViewHolder> {

    private ArrayList<PetMarketDTO> list;
    private ArrayList<PetMarketDTO> petMarketDTOlist = null;
    private SharedPrefrence prefrence;
    private LoginDTO loginDTO;
    private Context context;
    int lastPosition = -1;

    public ShowMyPetAdAdapter(Context context, ArrayList<PetMarketDTO> petMarketDTOlist) {
        this.petMarketDTOlist = petMarketDTOlist;
        this.context = context;
        prefrence = SharedPrefrence.getInstance(context);
        loginDTO = prefrence.getParentUser(Consts.LOGINDTO);
        this.list = new ArrayList<>();
        this.list.addAll(petMarketDTOlist);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.showmypetmarketadapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.ctvbPetTitle.setText(petMarketDTOlist.get(position).getTitle());
        holder.ctvPetType.setText("Pets|" + petMarketDTOlist.get(position).getPet_name());
        holder.CTVBcommnetcount.setText(petMarketDTOlist.get(position).getComment_count());
        holder.CTVBImagecount.setText(petMarketDTOlist.get(position).getPetImage().size() + "");
        holder.ctvPetDisc.setText(petMarketDTOlist.get(position).getDescription());
        holder.ctvPrice.setText(petMarketDTOlist.get(position).getPrice() + " USD");

        if (petMarketDTOlist.get(position).getPetImage().size() > 0) {
            Glide.with(context)
                    .load(petMarketDTOlist.get(position).getPetImage().get(0).getImage())
                    .placeholder(R.drawable.ears_icon)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.ivPetPic);
        }

        holder.LLcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.click_event));
                Intent in = new Intent(context, CommentPetMarket.class);
                in.putExtra(Consts.POSTID, petMarketDTOlist.get(position).getId());
                context.startActivity(in);
            }
        });
        holder.LLedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.click_event));
                Intent in = new Intent(context, AddPetMarketActivity.class);
                in.putExtra(Consts.PET_DTO, petMarketDTOlist.get(position));
                in.putExtra(Consts.FLAG, 1);
                context.startActivity(in);
            }
        });
        holder.llDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.click_event));
                lastPosition = position;
                deleteimageDialog(position, view);
            }
        });
        holder.RLpet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.click_event));

                Intent in = new Intent(context, UploadMyPetimageActivity.class);
                in.putExtra(Consts.IMAGE, petMarketDTOlist.get(position).getPetImage());
                in.putExtra(Consts.PET_ID, petMarketDTOlist.get(position).getId());
                context.startActivity(in);

            }
        });

    }

    @Override
    public int getItemCount() {
        return petMarketDTOlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CustomTextViewBold ctvbPetTitle, CTVBcommnetcount, CTVBImagecount;
        CustomTextView ctvPetType, ctvPetDisc;
        CustomTextViewBold ctvPrice;
        LinearLayout LLcomment, llDelete, LLedit;
        ImageView ivPetPic;
        RelativeLayout RLpet;


        public MyViewHolder(View item) {
            super(item);

            ctvbPetTitle = item.findViewById(R.id.ctvbPetTitle);
            ctvPetType = item.findViewById(R.id.ctvPetType);
            ctvPetDisc = item.findViewById(R.id.ctvPetDisc);
            ctvPrice = item.findViewById(R.id.ctvPrice);
            LLcomment = item.findViewById(R.id.LLcomment);

            ivPetPic = item.findViewById(R.id.ivPetPic);
            CTVBImagecount = item.findViewById(R.id.CTVBImagecount);
            CTVBcommnetcount = item.findViewById(R.id.CTVBcommnetcount);
            RLpet = item.findViewById(R.id.RLpet);
            LLedit = item.findViewById(R.id.LLedit);
            llDelete = item.findViewById(R.id.llDelete);
        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        petMarketDTOlist.clear();
        if (charText.length() == 0) {
            petMarketDTOlist.addAll(list);
        } else {
            for (PetMarketDTO petMarketDTO : list) {
                if (petMarketDTO.getTitle().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    petMarketDTOlist.add(petMarketDTO);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void deleteimageDialog(final int i, View view) {

        AlertDialog.Builder alertbox = new AlertDialog.Builder(view.getRootView().getContext());
        alertbox.setMessage("Do you want to delete?");
        alertbox.setTitle(" Delete Pet Ad");
        alertbox.setIcon(R.mipmap.ic_launcher);

        alertbox.setPositiveButton("Remove",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0,
                                        int arg1) {
                        deleteImageAPI(i);

                    }
                });
        alertbox.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0,
                                        int arg1) {

                    }
                });


        alertbox.show();
    }

    public void deleteImageAPI(int i) {
        ProjectUtils.showProgressDialog(context, true, "Please wait....");
        new HttpsRequest(Consts.DELETE_PET_FROM_MARKET, CommentParams(i), context).stringPost("Adapter Comment", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {
                    petMarketDTOlist.remove(lastPosition);
                    notifyDataSetChanged();

                    ProjectUtils.showLong(context, msg);

                } else {
                    ProjectUtils.showLong(context, msg);
                }


            }

        });
    }

    public Map<String, String> CommentParams(int i) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Consts.PET_ID, petMarketDTOlist.get(i).getId());
        params.put(Consts.USER_ID, loginDTO.getId());

        return params;
    }


}