package com.samyotech.petstand.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.Memories.AddMultipleImage;
import com.samyotech.petstand.activity.editPet.EditPet;
import com.samyotech.petstand.activity.petprofile.PetViewActivity;
import com.samyotech.petstand.activity.petprofile.ProfilePets;
import com.samyotech.petstand.activity.petprofile.RatingPet;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.models.PetListDTO;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.CustomTextView;
import com.samyotech.petstand.utils.CustomTextViewBold;
import com.samyotech.petstand.utils.ProjectUtils;
import com.samyotech.petstand.utils.ScreenshotUtils;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mayank on 14/2/18.
 */

public class PetListAdapter extends RecyclerView.Adapter<PetListAdapter.MyViewHolder> {

    private ArrayList<PetListDTO> petList;
    private SharedPrefrence prefrence;
    Context context;
    private File screenShotFile;
    Bitmap b = null;
    HashMap<String, File> parmsFile = new HashMap<>();
    private Dialog dialog_share, dialog;
    private LinearLayout llShareMedia, llShareWall, llCancel;
    private LoginDTO loginDTO;
    private CustomTextView tvYes, tvNo;
    private String TAG = PetListAdapter.class.getSimpleName();
    private ImageView ivShare;

    public PetListAdapter(Context context, ArrayList<PetListDTO> petList) {
        this.petList = petList;
        this.context = context;
        prefrence = SharedPrefrence.getInstance(context);
        loginDTO = prefrence.getParentUser(Consts.LOGINDTO);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pet_list, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        try {

            holder.ctvbPetTitle.setText(petList.get(position).getPetName());
            holder.tvDec.setText(petList.get(position).getPetName() + " is very active achieving daily target's.");
            holder.ctvbPetType.setText(petList.get(position).getPet_type_name());
            holder.ctvAge.setText(ProjectUtils.calculateAge(petList.get(position).getBirth_date()));
            holder.ctvBreed.setText(petList.get(position).getBreed_name());
            holder.ctvCity.setText(petList.get(position).getCity() + ", " + petList.get(position).getCountry());
            holder.tvView.setText(petList.get(position).getView_profile());
            holder.tvRating.setText(petList.get(position).getTotal_rating_user());
            holder.ratingBar.setRating(Float.parseFloat(petList.get(position).getRating()));
            holder.ratingBar.setRating(Float.parseFloat(petList.get(position).getRating()));

            if (petList.get(position).getSex().equalsIgnoreCase("Male")) {
                holder.ivGender.setImageDrawable(context.getResources().getDrawable(R.drawable.male));

            } else {
                holder.ivGender.setImageDrawable(context.getResources().getDrawable(R.drawable.female_a));
            }

            if (petList.get(position).getVerified().equals("0")) {
                holder.ivVerify.setVisibility(View.GONE);
            } else {
                holder.ivVerify.setVisibility(View.VISIBLE);
            }


            Glide
                    .with(context)
                    .load(petList.get(position).getPet_img_path())
                    .placeholder(R.drawable.ears_icon)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.ivPetPic);

        } catch (Exception e) {
            e.printStackTrace();
            ProjectUtils.showLong(context, "Something went wrong...");
        }


        holder.tvViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.click_event));
                Intent in = new Intent(context, ProfilePets.class);
                in.putExtra(Consts.PET_PROFILE, petList.get(position));
                context.startActivity(in);
            }
        });

        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.click_event));
                Intent in = new Intent(context, EditPet.class);
                in.putExtra(Consts.PET_PROFILE, petList.get(position));
                context.startActivity(in);
            }
        });
        holder.LLView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.click_event));
                Intent in = new Intent(context, PetViewActivity.class);
                in.putExtra(Consts.PET_ID, petList.get(position).getId());
                context.startActivity(in);
            }
        });


        holder.ivGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.click_event));
                Intent in = new Intent(context, AddMultipleImage.class);
                in.putExtra(Consts.PET_PROFILE, petList.get(position));
                context.startActivity(in);
            }
        });
        holder.ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeScreenshot(holder);
            }
        });
        holder.llRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.click_event));
                Intent in = new Intent(context, RatingPet.class);
                in.putExtra("pet_id", petList.get(position).getId());
                context.startActivity(in);
            }
        });
    }

    @Override
    public int getItemCount() {
        return petList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CustomTextViewBold ctvbPetType, tvViewProfile;
        ImageView ivGender, ivEdit, ivGallery, ivShare, ivVerify;
        CardView cvBase;
        CircleImageView ivPetPic;
        CustomTextView ctvAge, ctvBreed, ctvCity, ctvbPetTitle, tvView, tvRating, tvDec;
        RatingBar ratingBar;
        LinearLayout llRating, LLView;

        public MyViewHolder(View item) {
            super(item);
            llRating = (LinearLayout) item.findViewById(R.id.llRating);
            cvBase = (CardView) item.findViewById(R.id.cvBase);
            ivEdit = (ImageView) item.findViewById(R.id.ivEdit);
            ivGallery = (ImageView) item.findViewById(R.id.ivGallery);
            ivShare = (ImageView) item.findViewById(R.id.ivShare);
            ivPetPic = (CircleImageView) item.findViewById(R.id.ivPetPic);
            ivGender = (ImageView) item.findViewById(R.id.ivGender);
            ctvbPetTitle = (CustomTextView) item.findViewById(R.id.ctvbPetTitle);
            ctvbPetType = (CustomTextViewBold) item.findViewById(R.id.ctvbPetType);
            tvViewProfile = (CustomTextViewBold) item.findViewById(R.id.tvViewProfile);
            ctvAge = (CustomTextView) item.findViewById(R.id.ctvAge);
            ctvCity = (CustomTextView) item.findViewById(R.id.ctvCity);
            ctvBreed = (CustomTextView) item.findViewById(R.id.ctvBreed);
            tvView = (CustomTextView) item.findViewById(R.id.tvView);
            tvRating = (CustomTextView) item.findViewById(R.id.tvRating);
            ratingBar = (RatingBar) item.findViewById(R.id.ratingBar);
            tvDec = item.findViewById(R.id.tvDec);
            LLView = item.findViewById(R.id.LLView);
            ivVerify = item.findViewById(R.id.ivVerify);


        }
    }

    private void takeScreenshot(MyViewHolder holder) {


        b = ScreenshotUtils.screenShot(holder.cvBase);

        if (b != null) {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

            File saveFile = ScreenshotUtils.getMainDirectoryName(context);//get the path to save screenshot
            screenShotFile = ScreenshotUtils.store(b, Consts.PET_CARE + timeStamp + ".jpg", saveFile);//save the screenshot to selected path
            parmsFile.put(Consts.MEDIA, screenShotFile);
            dialogshare_social();
        } else {

        }

    }

    public void dialogshare_social() {
        dialog = new Dialog(context/*, android.R.style.Theme_Dialog*/);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailog_share_option);

        ///dialog.getWindow().setBackgroundDrawableResource(R.color.black);
        llShareMedia = (LinearLayout) dialog.findViewById(R.id.llShareMedia);
        llShareWall = (LinearLayout) dialog.findViewById(R.id.llShareWall);
        llCancel = (LinearLayout) dialog.findViewById(R.id.llCancel);

        dialog.show();
        dialog.setCancelable(false);
        llCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (screenShotFile != null) {
                    screenShotFile.delete();
                }
                dialog.dismiss();
            }
        });
        llShareWall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogshare(b);
                dialog.dismiss();
            }
        });
        llShareMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareMedia(b);
                dialog.dismiss();
            }
        });

    }

    public void dialogshare(Bitmap b) {
        dialog_share = new Dialog(context, android.R.style.Theme_Dialog);
        dialog_share.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_share.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_share.setContentView(R.layout.dailog_share);

        tvYes = (CustomTextView) dialog_share.findViewById(R.id.tvYes);
        tvNo = (CustomTextView) dialog_share.findViewById(R.id.tvNo);
        ivShare = (ImageView) dialog_share.findViewById(R.id.ivShare);
        dialog_share.show();
        dialog_share.setCancelable(false);
        ivShare.setImageBitmap(b);
        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_share.dismiss();

                if (screenShotFile != null) {
                    screenShotFile.delete();
                }
            }
        });
        tvYes.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addPost();


                    }
                });

    }

    public void shareMedia(Bitmap mBitmap) {
        try {
            Bitmap b = mBitmap;
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/jpeg");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    b, "Title", null);
            Uri imageUri = Uri.parse(path);
            share.putExtra(Intent.EXTRA_STREAM, imageUri);
            context.startActivity(Intent.createChooser(share, "Select"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addPost() {
        ProjectUtils.showProgressDialog(context, false, "Please wait..");

        new HttpsRequest(Consts.ADD_POST_API, getParms(), parmsFile, context).imagePost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {
                    ProjectUtils.showLong(context, msg);
                    dialog_share.dismiss();
                    if (screenShotFile != null) {
                        screenShotFile.delete();
                    }
                } else {
                    ProjectUtils.showLong(context, msg);
                }
            }

        });
    }

    public Map<String, String> getParms() {
        HashMap<String, String> params = new HashMap<>();
        params.put(Consts.USER_ID, loginDTO.getId());
        params.put(Consts.CONTENT, "wall");
        params.put(Consts.COMUNITY_ID, loginDTO.getComunity_id() + "");
        params.put(Consts.TITLE, "wall");
        params.put(Consts.POSTTYPE, "image");
        return params;
    }


}