package com.samyotech.petstand.fragment.Home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.food.Cart;
import com.samyotech.petstand.activity.home.BreedInfoHome;
import com.samyotech.petstand.adapter.AdapterBrand;
import com.samyotech.petstand.adapter.AdapterNewProductsForAnim;
import com.samyotech.petstand.adapter.AdapterOffers;
import com.samyotech.petstand.adapter.AdapterProductsForAnim;
import com.samyotech.petstand.adapter.Adapter_animals;
import com.samyotech.petstand.adapter.SlidingImage_Adapter;
import com.samyotech.petstand.databinding.HomeFragmentBinding;
import com.samyotech.petstand.activity.food.SearchActivity;
import com.samyotech.petstand.fragment.NearBy.HostelActivity;
import com.samyotech.petstand.fragment.NearBy.SalonActivity;
import com.samyotech.petstand.fragment.NearBy.ShopActivity;
import com.samyotech.petstand.fragment.NearBy.TraninerActivity;
import com.samyotech.petstand.fragment.NearBy.VaterinaryActivity;
import com.samyotech.petstand.https.HttpsRequest;
import com.samyotech.petstand.interfaces.Helper;
import com.samyotech.petstand.models.BannerDTO;
import com.samyotech.petstand.models.CartDTO;
import com.samyotech.petstand.models.HomeDTO;
import com.samyotech.petstand.models.LoginDTO;
import com.samyotech.petstand.network.NetworkManager;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;
import com.samyotech.petstand.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private String TAG = HomeFragment.class.getCanonicalName();
    private HomeFragment mcontext;
    private HomeFragmentBinding binding;
    HomeDTO homeDTO;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    //  ArrayList<ImageModel> imageModelArrayList;
    private int[] myImageList = new int[]{R.drawable.pager, R.drawable.pager, R.drawable.pager};

    private SharedPrefrence sharedPrefrence;
    private LoginDTO loginDTO;

    private ArrayList<BannerDTO> bannerDTOArrayList;

    private LinearLayoutManager layoutManager;
    private LinearLayoutManager layoutManager_products;
    private LinearLayoutManager layoutManager_offers;
    private LinearLayoutManager layoutManager_brands;

    private Adapter_animals adapter_animals;
    private AdapterProductsForAnim adapter;
    private AdapterNewProductsForAnim newProductsForAnim;
    private AdapterOffers adapterOffers;
    private AdapterBrand adapterBrand;//458066

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mcontext = HomeFragment.this;
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false);
        bannerDTOArrayList = new ArrayList<>();
        bannerDTOArrayList = populateList();
        sharedPrefrence = SharedPrefrence.getInstance(getActivity());
        loginDTO = sharedPrefrence.getParentUser(Consts.LOGINDTO);
        listners();
        return binding.getRoot();
    }

    private void listners() {
        binding.llPetShop.setOnClickListener(this);
        binding.llVeterinarian.setOnClickListener(this);
        binding.llPetsGrooming.setOnClickListener(this);
        binding.llHostels.setOnClickListener(this);
        binding.llTrainers.setOnClickListener(this);
        binding.llBreadInfo.setOnClickListener(this);
        binding.ctvNumber.setOnClickListener(this);
        binding.llCart.setOnClickListener(this);

        binding.IVsearch.clearFocus();
        binding.IVsearch.setFocusable(false);
        binding.IVsearch.setClickable(true);

    }


    private ArrayList<BannerDTO> populateList() {

        ArrayList<BannerDTO> list = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            BannerDTO bannerDTO = new BannerDTO();
            //  bannerDTO.setImage_drawable(bannerDTOArrayList[i]);
            list.add(bannerDTO);
        }

        return list;
    }

    public void setPager() {
        binding.pager.setAdapter(new SlidingImage_Adapter(getActivity(), homeDTO.getBanner()));
        binding.pager.setCurrentItem(0);
        binding.pager.startAutoScroll();
        binding.pager.setInterval(6000);
        binding.pager.setCycle(true);

        binding.IVsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });


        binding.indicator.setViewPager(binding.pager);
        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        binding.indicator.setRadius(5 * density);

        NUM_PAGES = homeDTO.getBanner().size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                binding.pager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 5000, 5000);

        // Pager listener over indicator
        binding.indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int po, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (NetworkManager.isConnectToInternet(getActivity())) {
            homegetdetail();
            getMyCart();
        } else {
            ProjectUtils.showLong(getActivity(), "Please enable your internet connection.");
        }
    }

    public void homegetdetail() {

        ProjectUtils.showProgressDialog(getActivity(), false, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.HOMEDATA, getParamshome(), getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {
                        // ProjectUtils.showToast(getActivity(), msg);
                        binding.ll.setVisibility(View.VISIBLE);
                        homeDTO = new Gson().fromJson(response.getJSONObject("data").toString(), HomeDTO.class);

                        setPager();

                        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                        binding.rvAnimalCat.setLayoutManager(layoutManager);
                        adapter_animals = new Adapter_animals(getActivity(), homeDTO.getCategory());
                        binding.rvAnimalCat.setAdapter(adapter_animals);


                        layoutManager_products = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                        binding.rvProducts.setLayoutManager(layoutManager_products);
                        adapter = new AdapterProductsForAnim(getActivity(), homeDTO.getPopular_products());
                        binding.rvProducts.setAdapter(adapter);

                        layoutManager_offers = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                        binding.rvTopOffers.setLayoutManager(layoutManager_offers);
                        adapterOffers = new AdapterOffers(getActivity(), homeDTO.getOffer());
                        binding.rvTopOffers.setAdapter(adapterOffers);

                        layoutManager_brands = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                        binding.rvTopBrandsOfPetFood.setLayoutManager(layoutManager_brands);
                        adapterBrand = new AdapterBrand(getActivity(), homeDTO.getBrand());
                        binding.rvTopBrandsOfPetFood.setAdapter(adapterBrand);

                        layoutManager_products = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                        binding.rvNewProducts.setLayoutManager(layoutManager_products);
                        newProductsForAnim = new AdapterNewProductsForAnim(getActivity(), homeDTO.getNew_Fresh());
                        binding.rvNewProducts.setAdapter(newProductsForAnim);

                        try {
                            if (homeDTO.getContact().getName() != null) {
                                binding.ctvName.setText("Name : nil ");
                                binding.ctvNumber.setText("Contact Number : nil ");
                                binding.ctvEmail.setText("Email : nil ");
                            }
                        } catch (Exception e) {

                        }
                        try {
                            if (homeDTO.getBreed().getBreed_name() != null) {

                                Glide.with(getActivity())
                                        .load(homeDTO.getBreed().getImage())
                                        .placeholder(R.drawable.about_us_back)
                                        .into(binding.ivType);
                                binding.ctvbType.setText("Know About " + homeDTO.getBreed().getBreed_name());
                            } else {

                            }
                        } catch (Exception e) {

                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    ProjectUtils.showToast(getActivity(), msg);
                    binding.ll.setVisibility(View.GONE);
                }
            }
        });
    }

    protected HashMap<String, String> getParamshome() {
        HashMap<String, String> paramshome = new HashMap<>();
        paramshome.put(Consts.USER_ID, loginDTO.getId());

        return paramshome;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llPetShop:
                Intent in = new Intent(getActivity(), ShopActivity.class);
                startActivity(in);
                break;
            case R.id.llVeterinarian:
                Intent in1 = new Intent(getActivity(), VaterinaryActivity.class);
                startActivity(in1);
                break;
            case R.id.llPets_Grooming:
                Intent in2 = new Intent(getActivity(), SalonActivity.class);
                startActivity(in2);
                break;
            case R.id.llHostels:
                Intent in3 = new Intent(getActivity(), HostelActivity.class);
                startActivity(in3);
                break;
            case R.id.llTrainers:
                Intent in4 = new Intent(getActivity(), TraninerActivity.class);
                startActivity(in4);
                break;
            case R.id.llCart:
                Intent in5 = new Intent(getActivity(), Cart.class);
                startActivity(in5);
                break;
            case R.id.llBreadInfo:
                Intent inBreed = new Intent(getActivity(), BreedInfoHome.class);
                inBreed.putExtra(Consts.HOME_BREED, homeDTO.getBreed());
                startActivity(inBreed);
                break;
            case R.id.ctvNumber:
                callDialog(v);
                //makecall(homeDTO.getContact().getMobile_no());
                break;
        }
    }


    public void callDialog(View view) {

        AlertDialog.Builder alertbox = new AlertDialog.Builder(view.getRootView().getContext());
        alertbox.setMessage("do you want to call on "+homeDTO.getContact().getMobile_no()+"?");
        alertbox.setTitle("Call");
        alertbox.setIcon(R.mipmap.ic_launcher);

        alertbox.setPositiveButton("Call",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0,
                                        int arg1) {
                        makecall(homeDTO.getContact().getMobile_no());

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

    private void makecall(String phone) {

       /* if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 101);
        } else {*/
        try {
            Intent my_callIntent = new Intent(Intent.ACTION_DIAL);
            my_callIntent.setData(Uri.parse("tel:" + phone));
            getActivity().startActivity(my_callIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // }
    }

    public void getMyCart() {
        new HttpsRequest(Consts.GET_MY_CART, getparmsCart(), getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    Type listType = new TypeToken<List<CartDTO>>() {
                    }.getType();
                    try {
                        ArrayList cartDTOList = new ArrayList<>();
                        cartDTOList = (ArrayList<CartDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), listType);

                        if (cartDTOList.size() > 0) {
                            binding.tvCardcount.setVisibility(View.VISIBLE);
                            binding.tvCardcount.setText(String.valueOf(cartDTOList.size()));
                        } else {
                            binding.tvCardcount.setVisibility(View.GONE);
                            binding.tvCardcount.setText(" " + 0);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    // ProjectUtils.showLong(mContext, msg);
                    binding.tvCardcount.setVisibility(View.GONE);
                    binding.tvCardcount.setText(" " + 0);

                }
            }
        });
    }

    public HashMap<String, String> getparmsCart() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.USER_ID, loginDTO.getId());
        Log.e("parms", parms.toString());
        return parms;
    }


}





