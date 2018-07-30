package com.example.eticket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.example.eticket.fragment.Fragment1;
import com.example.eticket.fragment.Fragment2;
import com.example.eticket.fragment.Fragment3;
import com.example.eticket.fragment.Fragment4;
import com.example.eticket.fragment.Fragment5;
import com.example.eticket.storage.AppStore;
import com.example.eticket.ui.activity.LoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("MainActivity","start main");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
        getSupportActionBar().hide();

        setContentView(R.layout.activity_main);



        BottomBar bottomBar = findViewById(R.id.bottom_bar);
        bottomBar.setContainer(R.id.fl_container)
                .setTitleBeforeAndAfterColor("#999999", "#ff5d5e")
                .addItem(Fragment1.class,
                        "首页",
                        R.drawable.item1_before,
                        R.drawable.item1_after)
                .addItem(Fragment2.class,
                        "订单",
                        R.drawable.item2_before,
                        R.drawable.item2_after)
                .addItem(Fragment3.class,
                        "我的",
                        R.drawable.item3_before,
                        R.drawable.item3_after)
                .addItem(Fragment4.class,
                        "测试4",
                        R.drawable.item2_before,
                        R.drawable.item2_after)
                .addItem(Fragment5.class,
                        "测试5",
                        R.drawable.item2_before,
                        R.drawable.item2_after)
                .build();

        if(!AppStore.isLogin(this)){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
//        if(!AppStore.isLogin(this)){
////            startActivity(new Intent().setClass(this, LoginActivity.class));
//            Intent intent = new Intent(this, LoginActivity.class);
//            startActivity(intent);
//        }
    }
}
