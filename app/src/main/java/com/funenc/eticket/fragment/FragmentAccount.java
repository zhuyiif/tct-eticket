package com.funenc.eticket.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.funenc.eticket.R;
import com.funenc.eticket.model.User;
import com.funenc.eticket.okhttp.HttpUtils;
import com.funenc.eticket.storage.AppStore;
import com.funenc.eticket.ui.activity.LoginActivity;
import com.funenc.eticket.ui.activity.SystemSettingActivity;
import com.funenc.eticket.ui.activity.TopUpActivity;
import com.funenc.eticket.ui.activity.UserInfoActivity;
import com.funenc.eticket.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FragmentAccount extends Fragment {
    @InjectView(R.id.unlogged_bg)
    View bgUnlogged;
    @InjectView(R.id.logged_bg)
    View bgLogged;
    @InjectView(R.id.personal_data_logged_bg)
    View bgPersonalDataLogged;
    @InjectView(R.id.allMileages)
    LinearLayout allMileages;
    @InjectView(R.id.login)
    Button btLogin;
    @InjectView(R.id.mileage)
    TextView milesText;
    @InjectView(R.id.balance)
    TextView balanceText;
    @InjectView(R.id.award)
    TextView awardText;
    @InjectView(R.id.controlContainer)
    ViewGroup controlContainer;
    @InjectView(R.id.dataContainer)
    ViewGroup dataContainer;
    @InjectView(R.id.name)
    TextView tvName;
    @InjectView(R.id.avatar)
    ImageView imageAvatar;

    HttpUtils httpUtils = new HttpUtils();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_account, container, false);
        ButterKnife.inject(this, view);
//        refreshPage();
        bgUnlogged.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            boolean isNotSet = true;
            @Override
            public void onGlobalLayout() {
                if(isNotSet) {
                    ViewGroup.LayoutParams layoutParams = controlContainer.getLayoutParams();
                    layoutParams.height = bgUnlogged.getHeight();
                    controlContainer.requestLayout();
                    isNotSet = false;
                }
            }
        });
        bgPersonalDataLogged.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            boolean isNotSet = true;
            @Override
            public void onGlobalLayout() {
                if(isNotSet) {
                    ViewGroup.LayoutParams layoutParams = dataContainer.getLayoutParams();
                    layoutParams.height = bgPersonalDataLogged.getHeight();
                    dataContainer.requestLayout();
                    isNotSet = false;
                }
            }
        });
        return view;
    }

    @OnClick(R.id.login)
    void onLogin(View bt) {
        startActivity(new Intent().setClass(getContext(), LoginActivity.class));
    }

    @OnClick(R.id.systemSetting)
    void onLogout(View bt){
        startActivity(new Intent().setClass(getContext(), SystemSettingActivity.class));
    }

    @OnClick(R.id.user_info)
    void onClickUserInfo(View view) {
        Log.d("account", "info click");
        if(AppStore.isLogin(getContext())) {
            startActivity(new Intent().setClass(getContext(), UserInfoActivity.class));
        }
    }

    void refreshPage(){
        if (AppStore.isLogin(getContext())) {
            bgLogged.setVisibility(View.VISIBLE);
            bgPersonalDataLogged.setVisibility(View.VISIBLE);
            allMileages.setVisibility(View.VISIBLE);
            btLogin.setVisibility(View.GONE);

            //get me info

            try {
                String token = AppStore.getToken(getActivity().getApplicationContext());
                Log.d("gettoken", token);
                httpUtils.getMe(token,new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        String responseString = response.body().string();
                        Log.d("getme", responseString);

                        JSONObject respObj = null;
                        try {
                            respObj = new JSONObject(responseString);
                            JSONObject contentObj = respObj.getJSONObject("content");
                            Log.d("contentobj",contentObj.toString());

                           //respObj.getJSONObject("user").toString();
                            Gson gson = new GsonBuilder().create();
                           final User me =  gson.fromJson(contentObj.getJSONObject("user").toString(), User.class);
                            Log.d("me",me.getPhone());
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    milesText.setText(Float.toString(me.getMileage()));
                                    balanceText.setText(Float.toString(me.getScore()));
                                    tvName.setText(me.getName());
                                    if(!StringUtils.isBlank(me.getAvatar())) {
                                        Picasso.get().load(me.getAvatar()).into(imageAvatar);
                                    }
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("getme", respObj.toString());





                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

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

    @OnClick(R.id.addBalance)
    public void onAddBalance(View btn){
        startActivity(new Intent().setClass(getContext(), TopUpActivity.class));
    }
}
