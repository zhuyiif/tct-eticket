package com.funenc.eticket;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.alibaba.sdk.android.feedback.impl.IActivityCallback;
import com.alibaba.sdk.android.feedback.util.ErrorCode;
import com.alibaba.sdk.android.feedback.util.FeedbackErrorCallback;
import com.funenc.eticket.storage.AppStore;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Callable;

public class TicketApplication extends Application {
    public final static String DEFAULT_APPKEY = "25071828";
    public final static String DEFAULT_APPSECRET = "498c8a86ad5b685c9ff0eaadf7866533";

    @Override
    public void onCreate() {
        super.onCreate();

        /**
         * 添加自定义的error handler
         */
        FeedbackAPI.addErrorCallback(new FeedbackErrorCallback() {
            @Override
            public void onError(Context context, String errorMessage, ErrorCode code) {
                Toast.makeText(context, "ErrMsg is: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
        FeedbackAPI.addLeaveCallback(new Callable() {
            @Override
            public Object call() throws Exception {
                Log.d("DemoApplication", "custom leave callback");
                return null;
            }
        });

        /**
         * 建议放在此处做初始化
         */
        FeedbackAPI.init(this, DEFAULT_APPKEY, DEFAULT_APPSECRET);

        /**
         * 在Activity的onCreate中执行的代码
         * 可以设置状态栏背景颜色和图标颜色，这里使用com.githang:status-bar-compat来实现
         */
        FeedbackAPI.setActivityCallback(new IActivityCallback() {
            @Override
            public void onCreate(Activity activity) {
//                StatusBarCompat.setStatusBarColor(activity,getResources().getColor(R.color.aliwx_setting_bg_nor),true);
            }
        });

        /**
         * 自定义参数演示
         */
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("loginTime", "登录时间");
            jsonObject.put("visitPath", "登陆，关于，反馈");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        FeedbackAPI.setAppExtInfo(jsonObject);

        /**
         * 以下是设置UI
         */
        //沉浸式任务栏，控制台设置为true之后此方法才能生效
        FeedbackAPI.setTranslucent(true);
        //设置返回按钮图标
        FeedbackAPI.setBackIcon(R.drawable.back);
        //设置标题栏"历史反馈"的字号，需要将控制台中此字号设置为0
        FeedbackAPI.setHistoryTextSize(20);
        //设置标题栏高度，单位为像素
        FeedbackAPI.setTitleBarHeight(100);

        // register WX API
        IWXAPI wxapi = WXAPIFactory.createWXAPI(getApplicationContext(), AppStore.WX_APP_ID, true);
        wxapi.registerApp(AppStore.WX_APP_ID);
    }
}
