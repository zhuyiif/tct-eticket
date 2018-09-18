package com.funenc.eticket.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.funenc.eticket.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class InputActivity extends AppCompatActivity {
    public static final String TITLE = "TITLE";
    public static final String VALUE = "VALUE";
    @InjectView(R.id.title)
    TextView tvTitle;
    @InjectView(R.id.editText)
    TextView tvEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_input);
        ButterKnife.inject(this);

        tvTitle.setText(getIntent().getStringExtra(TITLE));
        tvEdit.setText(getIntent().getStringExtra(VALUE));
    }

    @OnClick(R.id.back)
    void back(View view){
        finish();
    }

    @OnClick(R.id.confirm)
    void confirm(View view){
        Intent result = new Intent();
        result.putExtra(VALUE, tvEdit.getText().toString());
        setResult(RESULT_OK, result);
        finish();
    }

    @OnClick(R.id.text_remove)
    void onRemoveText(View view){
        tvEdit.setText("");
    }
}
