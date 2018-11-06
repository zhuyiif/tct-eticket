

package com.funenc.eticket.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.funenc.eticket.MainActivity;
import com.funenc.eticket.R;
import com.funenc.eticket.api.SubwayService;
import com.funenc.eticket.engine.AppEngine;
import com.funenc.eticket.engine.QrSeedFetcher;
import com.funenc.eticket.engine.SelfUserInfoFetcher;
import com.funenc.eticket.model.SeedInfo;
import com.funenc.eticket.okhttp.ApiFactory;
import com.funenc.eticket.storage.AppStore;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SplashActivity extends AppCompatActivity {

    private final String TAG = "SplashActivity";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppEngine.init(this);
        // 设置没有标题栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        RelativeLayout layoutSplash = (RelativeLayout) findViewById(R.id.activity_splash);
        if (AppStore.wasShownGuidePages(SplashActivity.this)) {
            startActivity(new Intent().setClass(SplashActivity.this, MainActivity.class));
            finish();
        } else {
            Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
            startActivity(intent);
            finish();
        }

        if(AppStore.isLogin(this)) {
            new Thread(new QrSeedFetcher()).start();
            new Thread(new SelfUserInfoFetcher()).start();
        }

        AlphaAnimation alphaAnimation = new AlphaAnimation(.0f, 1.0f);
        alphaAnimation.setDuration(1000);//设置动画播放时长1000毫秒（1秒）
        layoutSplash.startAnimation(alphaAnimation);
        //设置动画监听
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            //动画结束
            @Override
            public void onAnimationEnd(Animation animation) {
                //页面的跳转
//                animation.cancel();
//                this.onAnimationEnd(animation);
                if (AppStore.wasShownGuidePages(SplashActivity.this)) {
                    startActivity(new Intent().setClass(SplashActivity.this, MainActivity.class));
                } else {
                    Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

                animation.cancel();
                this.onAnimationEnd(animation);
            }
        });
    }
}
