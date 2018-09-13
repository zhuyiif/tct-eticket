package com.funenc.eticket.ui.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.funenc.eticket.R;
import com.funenc.eticket.model.User;
import com.funenc.eticket.okhttp.HttpUtils;
import com.funenc.eticket.storage.AppStore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qiniu.android.common.FixedZone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.KeyGenerator;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UserInfoActivity extends AppCompatActivity {

    @InjectView(R.id.head_icon)
    Button headButton;

    HttpUtils httpUtils = new HttpUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_user_info);
        ButterKnife.inject(this);

    }

    @OnClick(R.id.head_icon)
    void onClickUserInfo(Button btn) {
        Log.d("account", "head icon click");

        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 1);


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(selectedImage);
                        Drawable selected = Drawable.createFromStream(inputStream, selectedImage.toString());
                        headButton.setBackground(selected);
                    } catch (FileNotFoundException e) {
                        Drawable yourDrawable = getResources().getDrawable(R.drawable.left_ad);
                        headButton.setBackground(yourDrawable);
                    }

                }

                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();

                    //get qiniu token


                    String token = AppStore.getToken(getApplicationContext());
                    Log.d("gettoken", token);

                    try {

                        httpUtils.getQiniuToken(token, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {

                                String responseString = response.body().string();
                                Log.d("getQiniuToken", responseString);

                            }
                        });
                    } catch (Exception e) {

                    }


                    Configuration config = new Configuration.Builder()
                            .chunkSize(512 * 1024)        // 分片上传时，每片的大小。 默认256K
                            .putThreshhold(1024 * 1024)   // 启用分片上传阀值。默认512K
                            .connectTimeout(10)           // 链接超时。默认10秒
                            .useHttps(true)               // 是否使用https上传域名
                            .responseTimeout(60)          // 服务器响应超时。默认60秒
                            .zone(FixedZone.zone0)        // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
                            .build();
                    // 重用uploadManager。一般地，只需要创建一个uploadManager对象
                    UploadManager uploadManager = new UploadManager(config);

                    try {
                        InputStream inputStream = getContentResolver().openInputStream(selectedImage);
                        Drawable selected = Drawable.createFromStream(inputStream, selectedImage.toString());
                        headButton.setBackground(selected);


                        String data = selectedImage.toString();
                        String key = "zacktest.jpg"; //用uuid.jpg 命名

                        //从协议获取token
                        String qiniuToken = "";

                        uploadManager.put(data, key, qiniuToken,
                                new UpCompletionHandler() {
                                    @Override
                                    public void complete(String key, ResponseInfo info, JSONObject res) {
                                        //res包含hash、key等信息，具体字段取决于上传策略的设置
                                        if (info.isOK()) {
                                            Log.i("qiniu", "Upload Success");
                                        } else {
                                            Log.i("qiniu", "Upload Fail");
                                            //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                                        }
                                        Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + res);
                                    }
                                }, null);

                    } catch (FileNotFoundException e) {
                        Drawable yourDrawable = getResources().getDrawable(R.drawable.left_ad);
                        headButton.setBackground(yourDrawable);
                    }

                }
                break;
        }
    }

}
