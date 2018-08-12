package com.example.eticket.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.eticket.R;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class TopUpActivity extends AppCompatActivity {

    @InjectView(R.id.amount_5)
    Button btnAmount5;
    @InjectView(R.id.amount_10)
    Button btnAmount10;
    @InjectView(R.id.amount_20)
    Button btnAmount20;
    @InjectView(R.id.amount_30)
    Button btnAmount30;
    @InjectView(R.id.amount_50)
    Button btnAmount50;
    @InjectView(R.id.amount_100)
    Button btnAmount100;

    @InjectView(R.id.btn_alipay)
    Button btnAlipay;
    @InjectView(R.id.editAmount)
    TextView editAmount;

    Map<Button, Integer> amountBtnGroup = new HashMap<Button, Integer>(6);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_top_up);
        ButterKnife.inject(this);
        amountBtnGroup.put(btnAmount5, 5);
        amountBtnGroup.put(btnAmount10, 10);
        amountBtnGroup.put(btnAmount20, 20);
        amountBtnGroup.put(btnAmount30, 30);
        amountBtnGroup.put(btnAmount50, 50);
        amountBtnGroup.put(btnAmount100, 100);
    }

    @OnClick({R.id.amount_5, R.id.amount_10, R.id.amount_20, R.id.amount_30, R.id.amount_50, R.id.amount_100})
    void onChangeAmount(View view) {
        for (Button btn : amountBtnGroup.keySet()) {
            if(btn.getId()!=view.getId()) {
                btn.setSelected(false);
            }
        }
        ((Button) view).setSelected(true);
        editAmount.setText(String.valueOf(amountBtnGroup.get(view)));
    }

    @OnClick(R.id.btn_alipay)
    void onChooseAlipay(View view) {
        btnAlipay.setSelected(!btnAlipay.isSelected());
    }

    @OnClick(R.id.editAmount)
    void onInputAmount(View view) {
        for (Button btn : amountBtnGroup.keySet()) {
            btn.setSelected(false);
        }
    }

    @OnClick(R.id.text_remove)
    void onRemoveText(View view){
        editAmount.setText("");
    }

    @OnClick(R.id.back)
    void onBack(View view){
        finish();
    }
}
