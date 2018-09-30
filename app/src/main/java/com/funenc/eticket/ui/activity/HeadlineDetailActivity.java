package com.funenc.eticket.ui.activity;

import android.content.Intent;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.funenc.eticket.R;
import com.funenc.eticket.ui.component.SharePanel;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class HeadlineDetailActivity extends AppCompatActivity {
    public static final String URL = "url";
    private String url;
    @InjectView(R.id.webView)
    WebView webView;
    private BottomSheetDialog sharePanel;

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
            sharePanel = new SharePanel(this);
            sharePanel.setCancelable(true);
            sharePanel.setContentView(R.layout.layout_share_panel);
        }
        sharePanel.show();
    }
}
