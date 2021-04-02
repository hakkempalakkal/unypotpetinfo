package com.samyotech.petstand.adapter;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.samyotech.petstand.R;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.models.PetMarketDTO;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.ProjectUtils;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyPetImagesAdapter extends RecyclerView.Adapter<MyPetImagesAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<PetMarketDTO.PetImage> mImagesList;
    String pet_id = "";
    int lastPosition = -1;
    private static final int TYPE_FULL = 0;
    private static final int TYPE_HALF = 1;
    private static final int TYPE_QUARTER = 2;
    private SharedPrefrence prefrence;
    private LoginDTO loginDTO;


    public MyPetImagesAdapter(Context context, ArrayList<PetMarketDTO.PetImage> imagesList, String pet_id) {
        this.mContext = context;
        this.mImagesList = imagesList;
        this.pet_id = pet_id;
        prefrence = SharedPrefrence.getInstance(mContext);
        loginDTO = prefrence.getParentUser(Consts.LOGINDTO);
    }


    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

        final View itemView = LayoutInflater.from(mContext).inflate(R.layout.adapter_recycle_view_images, parent, false);

        ViewHolder holder = new ViewHolder(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {


        Glide.with(mContext).load(mImagesList.get(position).getImage()).placeholder(R.drawable.default_error).centerCrop().into(holder.getImage());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastPosition = position;
                deleteimageDialog(position, view);
               /* Intent inn = new Intent(mContext, ImageviewpagerActivity.class);
                inn.putExtra(Consts.IMAGE, mImagesList);
                mContext.startActivity(inn);*/
            }
        });

    }

    @Override
    public int getItemCount() {
        return mImagesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image, delete;

        public ViewHolder(View v) {
            super(v);
            image = v.findViewById(R.id.image_view);
            delete = v.findViewById(R.id.delete);
        }

        public ImageView getImage() {
            return image;
        }

    }


    public void deleteimageDialog(final int i, View view) {

        AlertDialog.Builder alertbox = new AlertDialog.Builder(view.getRootView().getContext());
        alertbox.setMessage("Do you want to remove this image?");
        alertbox.setTitle(" Delete Image");
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
        ProjectUtils.showProgressDialog(mContext, true, "Please wait....");
        new HttpsRequest(Consts.DELETE_MEDIA, CommentParams(i), mContext).stringPost("Adapter Comment", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {
                    mImagesList.remove(lastPosition);
                    notifyDataSetChanged();

                    ProjectUtils.showLong(mContext, msg);

                } else {
                    ProjectUtils.showLong(mContext, msg);
                }


            }

        });
    }

    public Map<String, String> CommentParams(int i) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Consts.PET_ID, pet_id);
        params.put(Consts.MEDIA_ID, mImagesList.get(i).getMedia_id());
        params.put(Consts.USER_ID, loginDTO.getId());

        return params;
    }


}
