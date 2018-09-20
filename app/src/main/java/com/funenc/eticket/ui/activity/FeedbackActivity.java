package com.funenc.eticket.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.funenc.eticket.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class FeedbackActivity extends AppCompatActivity {
    @InjectView(R.id.editText)
    TextView tvEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_feedback);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.back)
    void back(View view){
        finish();
    }

    @OnClick(R.id.submit)
    void submit(View view){
        String content = tvEdit.getText().toString();
    }
}
