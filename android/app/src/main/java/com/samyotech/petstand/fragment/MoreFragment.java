package com.samyotech.petstand.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.samyotech.petstand.R;
import com.samyotech.petstand.activity.Memories.GalleryActivity;
import com.samyotech.petstand.activity.MyOrder.MyOrderActivity;
import com.samyotech.petstand.activity.UserProfile.UserProfileActivity;
import com.samyotech.petstand.activity.WebViewActivity;
import com.samyotech.petstand.activity.chat.ChatActivity;
import com.samyotech.petstand.activity.contactmessage.ContactMessageActivity;
import com.samyotech.petstand.activity.event.ShowMyEventActivity;
import com.samyotech.petstand.activity.filter.SearchActivity;
import com.samyotech.petstand.activity.notification.NotificationActivity;
import com.samyotech.petstand.activity.petlist.PetListr;
import com.samyotech.petstand.activity.petmarket.CatagoryPetMarket;
import com.samyotech.petstand.activity.register.ChangePasswordActivity;
import com.samyotech.petstand.activity.register.LoginSignupactivity;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;


public class MoreFragment extends Fragment implements View.OnClickListener {
    private View view;
    private LinearLayout llChangePassword, llChat, llPetMarket, llEvent, llmessage, llnotification, llProfile, llMemories, llOrder, llSearch, llInvite, llShare, llContact, llReport, llFAQ, llAbout, rlLogout, terms, privatePolicy, your_pets;
    private SharedPrefrence prefrence;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_more, container, false);
        prefrence = SharedPrefrence.getInstance(getActivity());
//        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init(view);
        return view;
    }

    public void init(View v) {
        llMemories = (LinearLayout) v.findViewById(R.id.llMemories);
        llOrder = (LinearLayout) v.findViewById(R.id.llOrder);
        llProfile = (LinearLayout) v.findViewById(R.id.llProfile);
        your_pets = (LinearLayout) v.findViewById(R.id.your_pets);
        llSearch = (LinearLayout) v.findViewById(R.id.llSearch);
        llInvite = (LinearLayout) v.findViewById(R.id.llInvite);
        llShare = (LinearLayout) v.findViewById(R.id.llShare);
        llContact = (LinearLayout) v.findViewById(R.id.llContact);
        llReport = (LinearLayout) v.findViewById(R.id.llReport);
        llFAQ = (LinearLayout) v.findViewById(R.id.llFAQ);
        llAbout = (LinearLayout) v.findViewById(R.id.llAbout);
        privatePolicy = (LinearLayout) v.findViewById(R.id.privatePolicy);
        rlLogout = (LinearLayout) v.findViewById(R.id.rlLogout);
        terms = (LinearLayout) v.findViewById(R.id.terms);
        llmessage = (LinearLayout) v.findViewById(R.id.llmessage);
        llnotification = (LinearLayout) v.findViewById(R.id.llnotification);
        llEvent = (LinearLayout) v.findViewById(R.id.llEvent);
        llPetMarket = (LinearLayout) v.findViewById(R.id.llPetMarket);
        llChat = (LinearLayout) v.findViewById(R.id.llChat);
        llChangePassword = (LinearLayout) v.findViewById(R.id.llChangePassword);

        llMemories.setOnClickListener(this);
        llProfile.setOnClickListener(this);
        your_pets.setOnClickListener(this);
        llOrder.setOnClickListener(this);
        llSearch.setOnClickListener(this);
        llInvite.setOnClickListener(this);
        llShare.setOnClickListener(this);
        llContact.setOnClickListener(this);
        llReport.setOnClickListener(this);
        llFAQ.setOnClickListener(this);
        llAbout.setOnClickListener(this);
        privatePolicy.setOnClickListener(this);
        rlLogout.setOnClickListener(this);
        terms.setOnClickListener(this);
        llnotification.setOnClickListener(this);
        llmessage.setOnClickListener(this);
        llEvent.setOnClickListener(this);
        llPetMarket.setOnClickListener(this);
        llChat.setOnClickListener(this);
        llChangePassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.your_pets:
                startActivity(new Intent(getActivity(), PetListr.class));
                break;
            case R.id.llPetMarket:
                startActivity(new Intent(getActivity(), CatagoryPetMarket.class));
                break;
            case R.id.llMemories:
                startActivity(new Intent(getActivity(), GalleryActivity.class));
                break;
            case R.id.llmessage:
                startActivity(new Intent(getActivity(), ContactMessageActivity.class));
                break;
            case R.id.llnotification:
                startActivity(new Intent(getActivity(), NotificationActivity.class));
                break;
            case R.id.llChangePassword:
                startActivity(new Intent(getActivity(), ChangePasswordActivity.class));
                break;
            case R.id.llProfile:
                startActivity(new Intent(getActivity(), UserProfileActivity.class));
                break;
            case R.id.llOrder:
                startActivity(new Intent(getActivity(), MyOrderActivity.class));
                break;
            case R.id.llSearch:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            case R.id.llChat:
                startActivity(new Intent(getActivity(), ChatActivity.class));
                break;
            case R.id.llEvent:
                startActivity(new Intent(getActivity(), ShowMyEventActivity.class));
                break;
            case R.id.llInvite:
                Intent inviteIntent = new Intent(android.content.Intent.ACTION_SEND);
                inviteIntent.setType("text/plain");
                inviteIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name) + " - Invite");
                inviteIntent.putExtra(android.content.Intent.EXTRA_TEXT, Consts.SHARE_TEXT);
                startActivity(Intent.createChooser(inviteIntent, "Share Using:"));
                break;
            case R.id.llShare:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name) + " - Invite");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Consts.SHARE_TEXT);
                startActivity(Intent.createChooser(sharingIntent, "Share Using:"));
                break;
            case R.id.llContact:
                Intent intentC = new Intent(getActivity(), WebViewActivity.class);
                intentC.putExtra("title", Consts.CONTACT_TITLE);
                intentC.putExtra("url", Consts.CONTACT_URL);
                startActivity(intentC);
                break;
            case R.id.llReport:
                Intent intentR = new Intent(getActivity(), WebViewActivity.class);
                intentR.putExtra("title", Consts.REPORT_TITLE);
                intentR.putExtra("url", Consts.REPORT_URL);
                startActivity(intentR);
                break;
            case R.id.llFAQ:
                Intent intentF = new Intent(getActivity(), WebViewActivity.class);
                intentF.putExtra("title", Consts.FAQ_TITLE);
                intentF.putExtra("url", Consts.FAQ_URL);
                startActivity(intentF);
                break;
            case R.id.llAbout:
                Intent intentA = new Intent(getActivity(), WebViewActivity.class);
                intentA.putExtra("title", Consts.ABOUT_TITLE);
                intentA.putExtra("url", Consts.ABOUT_URL);
                startActivity(intentA);
               /* Intent intentA = new Intent(getActivity(), AboutUsActivity.class);
                startActivity(intentA);*/
                break;
            case R.id.privatePolicy:
                open(Consts.PRIVATE_TITLE, Consts.PRIVATE_URL);
                break;
            case R.id.terms:
                open(Consts.TERMS_TITLE, Consts.TERMS_URL);
                break;
            case R.id.rlLogout:
                confirmLogout();
                break;
        }
    }

    public void open(String title, String url) {
        Intent intent = new Intent(getActivity(), WebViewActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    public void logout() {
        prefrence.clearPreferences(SharedPrefrence.IS_LOGIN);
        Intent intent = new Intent(getActivity(), LoginSignupactivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    public void confirmLogout() {
        try {
            new AlertDialog.Builder(getActivity())
                    .setIcon(R.drawable.walk_icon)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setMessage("Are you sure want to logout?")
                    .setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            logout();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
