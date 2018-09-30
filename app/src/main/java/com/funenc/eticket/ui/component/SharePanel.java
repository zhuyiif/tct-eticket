package com.funenc.eticket.ui.component;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;

import com.funenc.eticket.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SharePanel extends BottomSheetDialog {
    public SharePanel(@NonNull Context context) {
        super(context);
    }

    public SharePanel(@NonNull Context context, int theme) {
        super(context, theme);
    }

    protected SharePanel(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.cancle)
    void cancleShare(View view){
        dismiss();
    }

    @OnClick(R.id.share_to_wechat_friend)
    void shareToWechatFriend(View view){
        // link wechat API
    }
}
