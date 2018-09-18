package com.funenc.eticket.ui.activity;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.funenc.eticket.R;
import com.funenc.eticket.api.OperationService;
import com.funenc.eticket.model.QiniuTokenResponse;
import com.funenc.eticket.model.User;
import com.funenc.eticket.model.UserInfoResponse;
import com.funenc.eticket.okhttp.ApiFactory;
import com.funenc.eticket.okhttp.HttpUtils;
import com.funenc.eticket.storage.AppStore;
import com.funenc.eticket.util.StringUtils;
import com.qiniu.android.common.FixedZone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

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

    private static final int AVATAR = 0, GENDER = 1, NAME = 2, EMAIL = 3;

    @InjectView(R.id.head_icon)
    ImageView head;
    @InjectView(R.id.gender)
    TextView tvGender;
    @InjectView(R.id.name)
    TextView tvName;
    @InjectView(R.id.emailAddr)
    TextView tvEmailAddr;

    HttpUtils httpUtils = new HttpUtils();

    Handler viewUpdateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case AVATAR:
                    Picasso.get().load(AppStore.getSelfUser().getAvatar()).into(head);
                    break;
                case GENDER:
                    tvGender.setText(AppStore.getSelfUser().getGender());
                    break;
                case NAME:
                    tvName.setText(AppStore.getSelfUser().getName());
                    break;
                case EMAIL:
                    tvEmailAddr.setText(AppStore.getSelfUser().getEmail());
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_user_info);
        ButterKnife.inject(this);

        User self = AppStore.getSelfUser();
        if (!StringUtils.isBlank(self.getAvatar())) {
            Picasso.get().load(AppStore.getSelfUser().getAvatar()).into(head);
        }
        if (!StringUtils.isBlank(self.getGender())) {
            tvGender.setText(AppStore.getSelfUser().getGender());
        }
        if (!StringUtils.isBlank(self.getName())) {
            tvName.setText(AppStore.getSelfUser().getName());
        }
        if (!StringUtils.isBlank(self.getEmail())) {
            tvEmailAddr.setText(AppStore.getSelfUser().getEmail());
        }
    }

    @OnClick(R.id.head_icon)
    void onClickUserInfo(View head) {
        Log.d("account", "head icon click");

        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 1);


    }

    public void onActivityResult(int requestCode, int resultCode, Intent returnedIntent) {
        super.onActivityResult(requestCode, resultCode, returnedIntent);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = returnedIntent.getData();
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(selectedImage);
                        Drawable selected = Drawable.createFromStream(inputStream, selectedImage.toString());
                        head.setBackground(selected);
                    } catch (FileNotFoundException e) {
                        Drawable yourDrawable = getResources().getDrawable(R.drawable.left_ad);
                        head.setBackground(yourDrawable);
                    }

                }

                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    final Uri selectedImageUri = returnedIntent.getData();

                    //get qiniu token
                    String token = AppStore.getToken(getApplicationContext());
                    Log.d("gettoken", "app-token:" + token);

                    try {
                        httpUtils.getQiniuToken(token, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.e("getQiniuToken", "failure", e);
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {

                                String responseString = response.body().string();
                                Log.d("getQiniuToken", responseString);
                                if (response.isSuccessful()) {
                                    ObjectMapper mapper = new ObjectMapper();
                                    QiniuTokenResponse qiniuTokenResponse = mapper.readValue(responseString, QiniuTokenResponse.class);

                                    Configuration config = new Configuration.Builder()
                                            .chunkSize(512 * 1024)        // 分片上传时，每片的大小。 默认256K
                                            .putThreshhold(1024 * 1024)   // 启用分片上传阀值。默认512K
                                            .connectTimeout(10)           // 链接超时。默认10秒
                                            .useHttps(true)               // 是否使用https上传域名
                                            .responseTimeout(60)          // 服务器响应超时。默认60秒
//                                            .zone(FixedZone.zone0)        // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
                                            .build();
                                    // 重用uploadManager。一般地，只需要创建一个uploadManager对象
                                    UploadManager uploadManager = new UploadManager(config);


                                    final String path = getFilePathFromContentUri(selectedImageUri, getContentResolver());
                                    String key = AppStore.getSelfUser().getPhone() + "-portrait"; //用uuid.jpg 命名

                                    //从协议获取token
                                    String qiniuToken = qiniuTokenResponse.getContent().getToken();

                                    uploadManager.put(path, key, qiniuToken,
                                            new UpCompletionHandler() {
                                                @Override
                                                public void complete(String key, ResponseInfo info, JSONObject res) {
                                                    //res包含hash、key等信息，具体字段取决于上传策略的设置
                                                    if (info.isOK()) {
                                                        Log.i("qiniu", "Upload Success for " + path);
                                                        updateSelfUser(AVATAR, "https://qiniu.funenc.com/" + key);
                                                    } else {
                                                        Log.i("qiniu", "Upload Fail for " + path);
                                                        //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                                                    }
                                                    Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + res);
                                                }
                                            }, null);
                                }
                            }
                        });
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                            Drawable selected = Drawable.createFromStream(inputStream, selectedImageUri.toString());
                            head.setBackground(selected);
                        } catch (FileNotFoundException e) {
                            Drawable yourDrawable = getResources().getDrawable(R.drawable.left_ad);
                            head.setBackground(yourDrawable);
                        }
                    } catch (Exception e) {
                        Log.e("upload", "failure", e);
                    }
                }
                break;
            case NAME:
            case EMAIL:
                if (resultCode == RESULT_OK) {
                    updateSelfUser(requestCode, returnedIntent.getStringExtra(InputActivity.VALUE));
                }
                break;
        }
    }

    @OnClick(R.id.genderControl)
    void changeGender(View view) {
        final String items[] = {"男", "女"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this, 3);
//        builder.setTitle("单选");
//        builder.setIcon(R.mipmap.ic_launcher);
        builder.setItems(items,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateSelfUser(GENDER, items[which]);
                    }
                });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void updateSelfUser(final int item, String value) {
        User self = new User(AppStore.getSelfUser());
        switch (item) {
            case NAME:
                self.setName(value);
                break;
            case EMAIL:
                self.setEmail(value);
                break;
            case AVATAR:
                self.setAvatar(value);
                break;
            case GENDER:
                self.setGender(value);
                break;
        }
        ApiFactory apiFactory = new ApiFactory(httpUtils);
        OperationService service = apiFactory.createService(OperationService.BASE_ADDR, OperationService.class, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("changeUserInfo", "failure", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.d("changeUserInfo", "success with: " + body);
                if (response.isSuccessful()) {
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        UserInfoResponse userInfoResponse = mapper.readValue(body, UserInfoResponse.class);
                        AppStore.setSelfUser(userInfoResponse.getContent().getUser());
                    } catch (IOException e) {
                        Log.e("UserInfo response", "failure", e);
                    }
                }
                viewUpdateHandler.sendEmptyMessage(item);
            }
        });
        service.changeUserInfo(self.getName(), self.getGender(), self.getEmail(), self.getAvatar(), AppStore.getToken(UserInfoActivity.this));
    }

    public static String getFilePathFromContentUri(Uri selectedVideoUri,
                                                   ContentResolver contentResolver) {
        String filePath;
        String[] filePathColumn = {MediaStore.MediaColumns.DATA};

        Cursor cursor = contentResolver.query(selectedVideoUri, filePathColumn, null, null, null);
//      也可用下面的方法拿到cursor
//      Cursor cursor = this.context.managedQuery(selectedVideoUri, filePathColumn, null, null, null);

        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }

    @OnClick(R.id.name)
    void changeName(View view) {
        Intent intent = new Intent(this, InputActivity.class);
        intent.putExtra(InputActivity.TITLE, "昵称");
        intent.putExtra(InputActivity.VALUE, AppStore.getSelfUser().getName());
        startActivityForResult(intent, NAME);
    }

    @OnClick(R.id.emailAddr)
    void changeEmailAddr(View view) {
        Intent intent = new Intent(this, InputActivity.class);
        intent.putExtra(InputActivity.TITLE, "邮箱");
        intent.putExtra(InputActivity.VALUE, AppStore.getSelfUser().getEmail());
        startActivityForResult(intent, EMAIL);
    }

    @OnClick(R.id.back)
    void back(View view) {
        finish();
    }
}
