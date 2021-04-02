package com.samyotech.petstand.adapter;

import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.samyotech.petstand.R;
import com.samyotech.petstand.models.ProductReview;
import com.samyotech.petstand.utils.CustomTextView;
import com.samyotech.petstand.utils.CustomTextViewBold;
import com.samyotech.petstand.utils.ProjectUtils;

import java.util.ArrayList;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * Created by varunverma on 27/7/17.
 */

public class AdapterAllReview extends RecyclerView.Adapter<AdapterAllReview.ReviewHolder> {
    private ArrayList<ProductReview> productReviewList;
    private Context context;

    public AdapterAllReview(Context context, ArrayList<ProductReview> productReviewList) {
        this.productReviewList = productReviewList;
        this.context = context;
    }

    /*
      * function onCreateViewHolder() set layout
       */
    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_all_review, parent, false);
        ReviewHolder holder = new ReviewHolder(v);
        return holder;
    }


    /*
        * function onBindViewHolder() set data to holders like imageview, textview
        *
        *
         */
    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {

        holder.tvTitle.setText(productReviewList.get(position).getTitle());
        holder.tvReview.setText(productReviewList.get(position).getDescription());
        holder.tvName.setText("by "+productReviewList.get(position).getUser_name()+" in "+ ProjectUtils.convertTimestampToDate(ProjectUtils.correctTimestamp(Long.parseLong(productReviewList.get(position).getCreated_date()))));
        holder.ratingBar.setRating(Float.parseFloat(productReviewList.get(position).getRating()));



    }

    @Override
    public int getItemCount() {
        return productReviewList.size();
    }


    public class ReviewHolder extends RecyclerView.ViewHolder {
        /**
         * dealer variable
         */

        public CustomTextViewBold tvTitle;
        public CustomTextView tvReview,tvName;
        public CardView reviewCardView;
        public MaterialRatingBar ratingBar;

        public ReviewHolder(View itemView) {
            super(itemView);

            /**
             * identify variable by there id
             *
             */
            tvTitle = (CustomTextViewBold) itemView.findViewById(R.id.tvTitle);
            tvReview = (CustomTextView) itemView.findViewById(R.id.tvReview);
            tvName = (CustomTextView) itemView.findViewById(R.id.tvName);
            reviewCardView = (CardView) itemView.findViewById(R.id.reviewCardView);
            ratingBar = (MaterialRatingBar) itemView.findViewById(R.id.ratingBar);
        }
    }
}
