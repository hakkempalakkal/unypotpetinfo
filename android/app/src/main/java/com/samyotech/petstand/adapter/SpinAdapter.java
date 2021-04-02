package com.samyotech.petstand.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.samyotech.petstand.models.BreedDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SpinAdapter extends ArrayAdapter<BreedDTO> {

    // Your sent context
    private Context context;
    // Your custom values for the spinner (User)
    //private ArrayList<BreedDTO> breedDTOs;
    List<BreedDTO> objects = null;
    List<BreedDTO> originalList;
    private LayoutInflater inflater;

    public SpinAdapter(Context context, int textViewResourceId,
                       List<BreedDTO> breedDTOs) {
        super(context, textViewResourceId, breedDTOs);
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.objects = breedDTOs;
        this.originalList = new ArrayList<BreedDTO>();
        this.originalList.addAll(objects);
    }

    public int getCount() {
        return objects.size();
    }

    public BreedDTO getItem(int position) {
        return objects.get(position);
    }

    public long getItemId(int position) {
        return position;
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


    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item

        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        return label;
    }
}