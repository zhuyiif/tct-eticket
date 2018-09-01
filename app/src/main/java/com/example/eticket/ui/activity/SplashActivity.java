

package com.example.eticket.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.example.eticket.MainActivity;
import com.example.eticket.R;
import com.example.eticket.api.SubwayService;
import com.example.eticket.model.SeedInfo;
import com.example.eticket.okhttp.ApiFactory;
import com.example.eticket.storage.AppStore;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SplashActivity extends AppCompatActivity {

    private final String TAG = "SplashActivity";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            new Thread() {
                @Override
                public void run() {
                    ApiFactory apiFactory = new ApiFactory();
                    apiFactory.createService(SubwayService.BASE_ADDR, SubwayService.class, new Callback() {

                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.i("QrSeed response", "failure", e);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String body = response.body().string();
                            Log.i("QrSeed response", body);
                            if(response.isSuccessful()) {
                                ObjectMapper mapper = new ObjectMapper();
                                try {
                                    SeedInfo result = mapper.readValue(body, SeedInfo.class);
                                    AppStore.setTicketCodeCreateKey(SplashActivity.this, result.getKey());
                                    AppStore.setTicketCodeCreateSeed(SplashActivity.this, result.getSeed());
                                } catch (IOException e) {
                                    Log.e("QrSeed response", "failure" , e);
                                }
                            }
                        }
                    }).getQrSeed(AppStore.getToken(SplashActivity.this));
                }
            }.start();
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
