package com.funenc.eticket.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.funenc.eticket.R;
import com.funenc.eticket.engine.AppEngine;
import com.funenc.eticket.storage.AppStore;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SystemSettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_system_setting);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.logout)
    void logout(View view){
        AppEngine.logout();
        finish();
    }

    @OnClick(R.id.back)
    void back(View view){
        finish();
    }

    @OnClick(R.id.feedback)
    void feedback(View view){
        if(AppStore.isLogin(this)) {
            //设置默认联系方式
            FeedbackAPI.setDefaultUserContactInfo(AppStore.getSelfUser().getPhone());
            FeedbackAPI.openFeedbackActivity();
        }
    }
}
