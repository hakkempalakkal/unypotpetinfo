package com.samyotech.petstand.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.samyotech.petstand.R;
import com.samyotech.petstand.models.JoinedUserDTO;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.CustomTextView;
import com.samyotech.petstand.utils.ProjectUtils;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mayank on 16/2/18.
 */

public class JoinedUserListAdapter extends RecyclerView.Adapter<JoinedUserListAdapter.MyViewHolder> {


    private ArrayList<JoinedUserDTO> joinedUserDTOList;
    private SharedPrefrence prefrence;
    private Context context;

    public JoinedUserListAdapter(Context context, ArrayList<JoinedUserDTO> joinedUserDTOList) {
        this.joinedUserDTOList = joinedUserDTOList;
        this.context = context;
        prefrence = SharedPrefrence.getInstance(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_joined_user_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        Glide.with(context)
                .load(joinedUserDTOList.get(position).getProfile_pic())
                .placeholder(R.drawable.default_error)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivUser);

        holder.CTVname.setText(ProjectUtils.getFirstLetterCapital(joinedUserDTOList.get(position).getFirst_name()));

    }

    @Override
    public int getItemCount() {
        return joinedUserDTOList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView ivUser;
        CustomTextView CTVname;

        public MyViewHolder(View item) {
            super(item);
            ivUser = item.findViewById(R.id.ivUser);
            CTVname = item.findViewById(R.id.CTVname);

        }
    }

}