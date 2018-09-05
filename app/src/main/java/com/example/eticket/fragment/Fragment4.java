package com.example.eticket.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.eticket.R;


public class Fragment4 extends Fragment {

    private WebView webView;
    //private static final String URL = "http://operator-app.funenc.com/page/#/infos/show?infoId=2";
    private static final String URL = "https://operator-app.funenc.com/page/#/subwayLine";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment4, container, false);
        webView = rootView.findViewById(R.id.info);

        // 替换成baidu 就可以了
        String url = "https://operator-app.funenc.com/page/#/infos";
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setAppCacheEnabled(true);

        webView.loadUrl(url);
        webView.setFocusable(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                //默认返回false,强行改写成为true,那么不会走系统的浏览器,会直接使用webview加载
                return true;

            }
        });





        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();



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

}