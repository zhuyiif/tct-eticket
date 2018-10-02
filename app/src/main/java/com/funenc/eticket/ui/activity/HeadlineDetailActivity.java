package com.funenc.eticket.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.funenc.eticket.R;
import com.funenc.eticket.storage.AppStore;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class HeadlineDetailActivity extends AppCompatActivity {
    public static final int THUMB_SIZE = 150;
    public static final String URL = "url";
    public static final String TITLE = "街探";
    public static final String DESC = "DESC";
    public static final String IMAGE = "image";
    private String url;
    @InjectView(R.id.webView)
    WebView webView;
    private BottomSheetDialog sharePanel;
    private IWXAPI wxapi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_headline_detail);
        ButterKnife.inject(this);

        url = getIntent().getStringExtra(URL);

        // load h5 page
        WebSettings settings = webView.getSettings();
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setAppCacheEnabled(true);

        webView.setFocusable(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                //默认返回false,强行改写成为true,那么不会走系统的浏览器,会直接使用webview加载
                return true;

            }
        });
        webView.loadUrl(url);

        // register WX API
        wxapi = WXAPIFactory.createWXAPI(this, AppStore.WX_APP_ID, true);
//        wxapi.registerApp(AppStore.WX_APP_ID);
    }

    @Override
    public void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        String newUrl = data.getStringExtra(URL);
        if(newUrl!=null && !newUrl.equals(url)){
            url = newUrl;
            webView.loadUrl(url);
        }
        super.onActivityReenter(resultCode, data);
    }

    @OnClick(R.id.back)
    void back(View view){
        finish();
    }

    @OnClick(R.id.share)
    void showSharePanel(View view){
        if(sharePanel == null) {
            sharePanel = new BottomSheetDialog(this);
            sharePanel.setCancelable(true);
            sharePanel.setContentView(R.layout.layout_share_panel);
            sharePanel.findViewById(R.id.share_to_wechat_friend).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // link wechat API
                    shareToWechat(SendMessageToWX.Req.WXSceneSession);
                    sharePanel.dismiss();
                    sharePanel = null;
                }
            });
            sharePanel.findViewById(R.id.cancle).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sharePanel.dismiss();
                    sharePanel = null;
                }
            });
            sharePanel.findViewById(R.id.share_to_wechat_circle).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // link wechat API
                    shareToWechat(SendMessageToWX.Req.WXSceneTimeline);
                    sharePanel.dismiss();
                    sharePanel = null;
                }
            });
            sharePanel.findViewById(R.id.share_to_qq_friend).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // link qq API
                }
            });
        }
        sharePanel.show();
    }

    private void shareToWechat(int wxSceneSession) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = TITLE;
        msg.description = getIntent().getStringExtra(DESC);
        byte buff[] = getIntent().getByteArrayExtra(IMAGE);
        msg.thumbData = buff;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("jietan");
        req.message = msg;
        req.scene = wxSceneSession;
        if(!wxapi.sendReq(req)){
            Bitmap bmp = BitmapFactory.decodeByteArray(buff, 0, buff.length);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.WEBP, 100, baos);
            bmp.recycle();
            msg.thumbData = baos.toByteArray();
            try {
                baos.close();
            } catch (Exception e) {
                Log.e("HeadlineDetailActivity", "baos close error", e);
            }
            wxapi.sendReq(req);
        }
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
