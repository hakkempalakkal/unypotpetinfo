package com.samyotech.petstand.utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

public class CustomAutoCompleteTextChangedListener implements TextWatcher{

    public static final String TAG = "CustomAutoCompleteTextChangedListener.java";
    Context context;

    public CustomAutoCompleteTextChangedListener(Context context){
        this.context = context;
    }

    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTextChanged(CharSequence userInput, int start, int before, int count) {

        try{
         /*   MainActivity mainActivity = ((MainActivity) context);
            mainActivity.myAdapter.notifyDataSetChanged();
            MyObject[] myObjs = mainActivity.databaseH.read(userInput.toString());
            mainActivity.myAdapter = new AutocompleteCustomArrayAdapter(mainActivity, R.layout.list_view_row_item, myObjs);
            mainActivity.myAutoComplete.setAdapter(mainActivity.myAdapter);*/
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}