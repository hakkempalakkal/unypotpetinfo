package com.samyotech.petstand.fragment.NearBy;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.BaseActivity;
import com.samyotech.petstand.databinding.NearByFragBinding;
import com.samyotech.petstand.utils.CustomTextViewBold;

/**
 * Created by mayank on 20/2/18.
 */

public class NearByFrag extends Fragment implements View.OnClickListener {
    private View view;
    private NearByFragBinding binding;

    private LinearLayout back;
    public BaseActivity baseActivity;
    private FrameLayout containerss;
    private LinearLayout llUser, llPet, llVat, llSalon, llHostal;
    private FragmentManager fragmentManager;
    private UserListFrag userListFrag = new UserListFrag();
    private PetListFrag petListFrag = new PetListFrag();
    private VatListFrag vatListFrag = new VatListFrag();
    public ShopFragment shopFragment = new ShopFragment();
    public SalonFragment salonFragment = new SalonFragment();
    public HostelFragment hostelFragment = new HostelFragment();
    public VaterinaryFragment vaterinaryFragment = new VaterinaryFragment();

    private CustomTextViewBold tvUser, tvPet, tvVat, tvSalon, tvHostal;
    public ImageView ivUser, ivPet, ivVat, ivSalon, ivHostal;
    public static ImageView IVfilter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.near_by_frag, container, false);
        view = binding.getRoot();


       // binding.llUser.setOnClickListener(this);
        binding.cardClick1.setOnClickListener(this);
        binding.cardClick2.setOnClickListener(this);
        binding.cardClick3.setOnClickListener(this);
        binding.cardClick4.setOnClickListener(this);
        binding.cardClick5.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.cardClick1:
                Intent in = new Intent(getActivity(),ShopActivity.class);
                startActivity(in);
                break;
               case R.id.cardClick2:
                Intent in1 = new Intent(getActivity(),VaterinaryActivity.class);
                startActivity(in1);
                break;
               case R.id.cardClick3:
                Intent in2 = new Intent(getActivity(),SalonActivity.class);
                startActivity(in2);
                break;
               case R.id.cardClick4:
                Intent in3 = new Intent(getActivity(),HostelActivity.class);
                startActivity(in3);
                break;
                case R.id.cardClick5:
                Intent in4 = new Intent(getActivity(),TraninerActivity.class);
                startActivity(in4);
                break;



        }

    }



}
