package com.funenc.eticket.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.funenc.eticket.R;
import com.funenc.eticket.engine.AppEngine;

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
}
