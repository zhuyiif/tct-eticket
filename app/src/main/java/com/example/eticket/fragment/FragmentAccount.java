package com.example.eticket.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.eticket.R;
import com.example.eticket.engine.AppEngine;
import com.example.eticket.storage.AppStore;
import com.example.eticket.ui.activity.LoginActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class FragmentAccount extends Fragment {
    @InjectView(R.id.logged_bg)
    View bgLogged;
    @InjectView(R.id.personal_data_logged_bg)
    View bgPersonalDataLogged;
    @InjectView(R.id.allMileages)
    LinearLayout allMileages;
    @InjectView(R.id.login)
    Button btLogin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_account, container, false);
        ButterKnife.inject(this, view);
//        refreshPage();
        return view;
    }

    @OnClick(R.id.login)
    void onLogin(View bt) {
        startActivity(new Intent().setClass(getContext(), LoginActivity.class));
    }

    @OnClick(R.id.about)
    void onLogout(View bt){
        AppEngine.logout();
        refreshPage();
    }

    void refreshPage(){
        if (AppStore.isLogin(getContext())) {
            bgLogged.setVisibility(View.VISIBLE);
            bgPersonalDataLogged.setVisibility(View.VISIBLE);
            allMileages.setVisibility(View.VISIBLE);
            btLogin.setVisibility(View.GONE);
        }else {
            bgLogged.setVisibility(View.GONE);
            bgPersonalDataLogged.setVisibility(View.GONE);
            allMileages.setVisibility(View.GONE);
            btLogin.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshPage();
    }
}
