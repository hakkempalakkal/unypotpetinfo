package com.samyotech.petstand.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
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
import com.samyotech.petstand.activity.chat.OneTwoOneChatDemo;
import com.samyotech.petstand.activity.petmarket.CommentPetMarket;
import com.samyotech.petstand.activity.petprofile.UserPetProfileForWallActivity;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.models.PetMarketDTO;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomTextView;
import com.samyotech.petstand.utils.CustomTextViewBold;
import com.samyotech.petstand.utils.ProjectUtils;
import com.samyotech.petstand.utils.imagepager.ImageviewpagerActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mayank on 16/2/18.
 */

public class ShowPetMarketAdapter extends RecyclerView.Adapter<ShowPetMarketAdapter.MyViewHolder> {

    private ArrayList<PetMarketDTO> list;
    private ArrayList<PetMarketDTO> petMarketDTOlist = null;
    private SharedPrefrence prefrence;
    private LoginDTO loginDTO;
    private Context context;

    public ShowPetMarketAdapter(Context context, ArrayList<PetMarketDTO> petMarketDTOlist) {
        this.petMarketDTOlist = petMarketDTOlist;
        this.context = context;
        prefrence = SharedPrefrence.getInstance(context);
        loginDTO = prefrence.getParentUser(Consts.LOGINDTO);
        this.list = new ArrayList<>();
        this.list.addAll(petMarketDTOlist);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.showpetmarketadapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.ctvbPetTitle.setText(petMarketDTOlist.get(position).getTitle());
        holder.ctvPetType.setText("Pets | " + petMarketDTOlist.get(position).getPet_name());
        holder.CTVBcommnetcount.setText(petMarketDTOlist.get(position).getComment_count());
        holder.CTVBImagecount.setText(petMarketDTOlist.get(position).getPetImage().size() + "");
        holder.ctvPetDisc.setText(petMarketDTOlist.get(position).getDescription());
        holder.ctvPrice.setText(petMarketDTOlist.get(position).getPrice() + " USD");
        holder.ctvPetOwner.setText(petMarketDTOlist.get(position).getFirst_name() + " | " + ProjectUtils.convertTimestampToTime(ProjectUtils.correctTimestamp(Long.parseLong(petMarketDTOlist.get(position).getCreated_at()))));

        if (petMarketDTOlist.get(position).getPetImage().size() > 0) {
            Glide.with(context)
                    .load(petMarketDTOlist.get(position).getPetImage().get(0).getImage())
                    .placeholder(R.drawable.ears_icon)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.ivPetPic);
        }

        Glide.with(context)
                .load(petMarketDTOlist.get(position).getProfile_pic())
                .placeholder(R.drawable.dummy_user)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.IVowner);
        if (petMarketDTOlist.get(position).getIs_fav().equals("1")) {
            holder.IVfav.setImageResource(R.drawable.is_fav);
        } else {
            holder.IVfav.setImageResource(R.drawable.is_notfav);
        }


        holder.IVfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (petMarketDTOlist.get(position).getIs_fav().equals("1")) {
                    addRemove(holder, position);
                } else {
                    addFav(holder, position);
                }
            }
        });


        // holder.tvOff.setText(context.getResources().getString(R.string.Rs) + " " +ProjectUtils.round( foodList.get(position).getP_rate() - foodList.get(position).getP_rate_sale(),1) + " \n SAVE");

        holder.LLcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.click_event));
                Intent in = new Intent(context, CommentPetMarket.class);
                in.putExtra(Consts.POSTID, petMarketDTOlist.get(position).getId());
                context.startActivity(in);
            }
        });
        holder.RLpet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.click_event));
                if (petMarketDTOlist.get(position).getPetImage().size() > 0) {
                    Intent in = new Intent(context, ImageviewpagerActivity.class);
                    in.putExtra(Consts.IMAGE, petMarketDTOlist.get(position).getPetImage());
                    context.startActivity(in);
                } else {
                    ProjectUtils.showToast(context, "No image added");
                }
            }
        });
        holder.LLchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.click_event));
                Intent in = new Intent(context, OneTwoOneChatDemo.class);
                in.putExtra(Consts.USER_ID, petMarketDTOlist.get(position).getUser_id());
                in.putExtra(Consts.NAME, petMarketDTOlist.get(position).getFirst_name());
                context.startActivity(in);
            }
        });
        holder.LLcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.click_event));
                if (!petMarketDTOlist.get(position).getMobile_no().equals("")) {
                    ProjectUtils.makecall(petMarketDTOlist.get(position).getMobile_no(), context);
                }
            }
        });
        holder.LLimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.click_event));
                Intent intent = new Intent(context, UserPetProfileForWallActivity.class);
                intent.putExtra(Consts.USER_ID, petMarketDTOlist.get(position).getUser_id());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return petMarketDTOlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CustomTextViewBold ctvbPetTitle, CTVBcommnetcount, CTVBImagecount, ctvPrice;
        CustomTextView ctvPetType, ctvPetDisc, ctvPetOwner;
        LinearLayout LLcall, LLcomment, LLchat, LLimage;
        ImageView ivPetPic, IVfav;
        RelativeLayout RLpet;
        CircleImageView IVowner;


        public MyViewHolder(View item) {
            super(item);

            ctvbPetTitle = item.findViewById(R.id.ctvbPetTitle);
            ctvPetType = item.findViewById(R.id.ctvPetType);
            ctvPetDisc = item.findViewById(R.id.ctvPetDisc);
            ctvPrice = item.findViewById(R.id.ctvPrice);
            LLcomment = item.findViewById(R.id.LLcomment);
            LLcall = item.findViewById(R.id.LLcall);
            LLchat = item.findViewById(R.id.LLchat);
            ivPetPic = item.findViewById(R.id.ivPetPic);
            IVfav = item.findViewById(R.id.IVfav);
            CTVBImagecount = item.findViewById(R.id.CTVBImagecount);
            CTVBcommnetcount = item.findViewById(R.id.CTVBcommnetcount);
            RLpet = item.findViewById(R.id.RLpet);
            ctvPetOwner = item.findViewById(R.id.ctvPetOwner);
            IVowner = item.findViewById(R.id.IVowner);
            LLimage = item.findViewById(R.id.LLimage);
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

    public void addFav(final MyViewHolder holder, final int i) {
        ProjectUtils.showProgressDialog(context, true, "Please Wait!!");
        new HttpsRequest(Consts.ADD_FAV_PET, getparms(i), context).stringPost("Showpet", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        petMarketDTOlist.get(i).setIs_fav("1");
                        holder.IVfav.setImageResource(R.drawable.is_fav);

                    } catch (Exception e) {
                        e.printStackTrace();
                        //  tvNo.setVisibility(View.VISIBLE);

                    }

                } else {
                    ProjectUtils.showLong(context, msg);
                    //  tvNo.setVisibility(View.VISIBLE);

                }
            }
        });

    }

    public void addRemove(final MyViewHolder holder, final int i) {
        ProjectUtils.showProgressDialog(context, true, "Please Wait!!");
        new HttpsRequest(Consts.REMOVE_FAV_PET, getparms(i), context).stringPost("Showpet", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        petMarketDTOlist.get(i).setIs_fav("0");
                        holder.IVfav.setImageResource(R.drawable.is_notfav);


                    } catch (Exception e) {
                        e.printStackTrace();
                        //  tvNo.setVisibility(View.VISIBLE);

                    }

                } else {
                    ProjectUtils.showLong(context, msg);
                    //tvNo.setVisibility(View.VISIBLE);

                }
            }
        });

    }

    public HashMap<String, String> getparms(int i) {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.USER_ID, loginDTO.getId());
        parms.put(Consts.PET_ID, petMarketDTOlist.get(i).getId());

        Log.e("parms", parms.toString());
        return parms;
    }

}