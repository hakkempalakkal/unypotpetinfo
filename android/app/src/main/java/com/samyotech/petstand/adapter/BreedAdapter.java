package com.samyotech.petstand.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.samyotech.petstand.R;
import com.samyotech.petstand.models.BreedDTO;
import com.samyotech.petstand.utils.CustomTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BreedAdapter extends BaseAdapter {

    List<BreedDTO> objects = null;
    List<BreedDTO> originalList;
    Context context;
    DisplayImageOptions options;
    private Animation animShow, animHide;

    View tempView1, tempView2, tempView3;
    int lastPosition = -1;

    public BreedAdapter(Context context, int textViewResourceId, List<BreedDTO> objects) {
        super();
        this.objects = objects;
        this.context = context;
        this.originalList = new ArrayList<BreedDTO>();
        this.originalList.addAll(objects);
        initAnimation();
    }

    private void initAnimation() {
        animShow = AnimationUtils.loadAnimation(context, R.anim.fadein);
        animHide = AnimationUtils.loadAnimation(context, R.anim.fadeout);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        final ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(context);
            convertView = layoutInflator.inflate(R.layout.adapter_breed_info, null);
            viewHolder = new ViewHolder();
            viewHolder.llBreedExpandedView = (LinearLayout) convertView.findViewById(R.id.llBreedExpandedView);
            viewHolder.llBreedHeaderView = (LinearLayout) convertView.findViewById(R.id.llBreedHeaderView);
            viewHolder.rlBreedHeaderViewCollapsed = (RelativeLayout) convertView.findViewById(R.id.rlBreedHeaderViewCollapsed);
            viewHolder.rlBreedHeaderViewExpanded = (RelativeLayout) convertView.findViewById(R.id.rlBreedHeaderViewExpanded);
            viewHolder.llBreedInfo = (LinearLayout) convertView.findViewById(R.id.llBreedInfo);
            viewHolder.llRoot = (LinearLayout) convertView.findViewById(R.id.llRoot);
            viewHolder.ivPetBreedCollapsed = (ImageView) convertView.findViewById(R.id.ivPetBreedCollapsed);
            viewHolder.ivPetBreedExpanded = (ImageView) convertView.findViewById(R.id.ivPetBreedExpanded);
            viewHolder.tvBreedNameCollapsed = (CustomTextView) convertView.findViewById(R.id.tvBreedNameCollapsed);
            viewHolder.tvBreedNameExpanded = (CustomTextView) convertView.findViewById(R.id.tvBreedNameExpanded);
            viewHolder.tv_origin = (CustomTextView) convertView.findViewById(R.id.tv_origin);
            viewHolder.tv_origin_header = (CustomTextView) convertView.findViewById(R.id.tv_origin_header);
            viewHolder.tv_lifeSpan = (CustomTextView) convertView.findViewById(R.id.tv_lifeSpan);
            viewHolder.tvWeightFemaleBreed = (CustomTextView) convertView.findViewById(R.id.tvWeightFemaleBreed);
            viewHolder.tvWeightMaleBreed = (CustomTextView) convertView.findViewById(R.id.tvWeightMaleBreed);
            viewHolder.tvHeightFemaleBreed = (CustomTextView) convertView.findViewById(R.id.tvHeightFemaleBreed);
            viewHolder.tvHeightMaleBreed = (CustomTextView) convertView.findViewById(R.id.tvHeightMaleBreed);
            viewHolder.tv_temperaments = (CustomTextView) convertView.findViewById(R.id.tv_temperaments);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        if (position % 4 == 0) {
            viewHolder.llRoot.setBackground(context.getResources().getDrawable(R.drawable.breed_bg_first));
        } else if (position % 4 == 1) {
            viewHolder.llRoot.setBackground(context.getResources().getDrawable(R.drawable.breed_bg_second));
        } else if (position % 4 == 2) {
            viewHolder.llRoot.setBackground(context.getResources().getDrawable(R.drawable.breed_bg_third));
        } else if (position % 4 == 3) {
            viewHolder.llRoot.setBackground(context.getResources().getDrawable(R.drawable.breed_bg_fourth));
        }

        try {
            viewHolder.tvBreedNameCollapsed.setText(objects.get(position).getBreed_name().trim());
            viewHolder.tvBreedNameExpanded.setText(objects.get(position).getBreed_name().trim());
            viewHolder.tv_origin.setText(objects.get(position).getOrigin().trim());
            viewHolder.tv_origin_header.setText(objects.get(position).getOrigin().trim());
            viewHolder.tv_lifeSpan.setText(objects.get(position).getLife_span().trim());
            viewHolder.tvWeightFemaleBreed.setText(objects.get(position).getWeight_female().trim());
            viewHolder.tvWeightMaleBreed.setText(objects.get(position).getWeight_male().trim());
            viewHolder.tvHeightFemaleBreed.setText(objects.get(position).getHeight_female().trim());
            viewHolder.tvHeightMaleBreed.setText(objects.get(position).getHeight_male().trim());
            viewHolder.tv_temperaments.setText(objects.get(position).getTemperament().trim());

            updateHomeImage(viewHolder.ivPetBreedCollapsed, objects.get(position).getImage_path());
            updateHomeImage(viewHolder.ivPetBreedExpanded, objects.get(position).getImage_path());
        } catch (Exception e) {
            e.printStackTrace();
        }


        viewHolder.llBreedHeaderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.llBreedExpandedView.getVisibility() == View.VISIBLE) {

                    viewHolder.rlBreedHeaderViewCollapsed.setVisibility(View.VISIBLE);
                    viewHolder.rlBreedHeaderViewCollapsed.startAnimation(animShow);

                    viewHolder.rlBreedHeaderViewExpanded.setVisibility(View.GONE);
                    viewHolder.rlBreedHeaderViewExpanded.startAnimation(animHide);

                    viewHolder.llBreedExpandedView.setVisibility(View.GONE);
                    viewHolder.llBreedExpandedView.startAnimation(animHide);

                } else {
                    viewHolder.llBreedExpandedView.setVisibility(View.VISIBLE);
                    viewHolder.llBreedExpandedView.startAnimation(animShow);

                    viewHolder.rlBreedHeaderViewExpanded.setVisibility(View.VISIBLE);
                    viewHolder.rlBreedHeaderViewExpanded.startAnimation(animShow);

                    viewHolder.rlBreedHeaderViewCollapsed.setVisibility(View.GONE);
                    viewHolder.rlBreedHeaderViewCollapsed.startAnimation(animHide);

//                    if (lastPosition > -1) {
//                        //     tempView1.startAnimation(animHide);
//                        tempView1.setVisibility(View.GONE);
//                        //     tempView2.startAnimation(animHide);
//                        tempView2.setVisibility(View.GONE);
//                        tempView3.setVisibility(View.VISIBLE);
//                        //     tempView3.startAnimation(animShow);
//
//                    }
//
//                    tempView1 = viewHolder.llBreedExpandedView;
//                    tempView2 = viewHolder.rlBreedHeaderViewExpanded;
//                    tempView3 = viewHolder.rlBreedHeaderViewCollapsed;
//                    lastPosition = position;
                }
            }
        });


        return convertView;
    }

    public void updateHomeImage(final ImageView imageView, String uri) {
        try {
            if (!uri.equals(""))
                ImageLoader.getInstance().displayImage(uri, imageView, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        objects.clear();
        if (charText.length() == 0) {
            objects.addAll(originalList);
        } else {
            for (BreedDTO breedDTO : originalList) {
                if (breedDTO.getBreed_name().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    objects.add(breedDTO);
                }
            }
        }
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        CustomTextView tvBreedNameCollapsed, tvBreedNameExpanded, tv_origin_header, tv_origin, tv_lifeSpan, tvWeightFemaleBreed, tvWeightMaleBreed, tvHeightFemaleBreed, tvHeightMaleBreed, tv_temperaments;
        ImageView ivPetBreedCollapsed, ivPetBreedExpanded;
        LinearLayout llBreedExpandedView, llBreedInfo;
        LinearLayout llBreedHeaderView, llRoot;
        RelativeLayout rlBreedHeaderViewCollapsed, rlBreedHeaderViewExpanded;
    }

}