package com.funenc.eticket.okhttp;

import android.util.Log;

import com.funenc.eticket.storage.AppStore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtils {

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    public String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();


        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }


    public void postGetSMSCode(String phoneNumber) throws IOException, JSONException {

        JSONObject postPhone = new JSONObject();
        postPhone.put("phone", phoneNumber);


        RequestBody formBody = new FormBody.Builder()
                .add("phone", phoneNumber)
                .build();


        Request request = new Request.Builder()
                .url("https://scan-app.funenc.com/api/users/sms_code")
                .post(formBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("http post","failed in callback");

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Log.d("http", response.toString());

                String responseString = response.body().string();

                Log.d("http", responseString);

            }
        });


    }

    public void login(String phoneNumber, String code, final Callback cb) throws IOException, JSONException {

        JSONObject postPhone = new JSONObject();
        postPhone.put("phone", phoneNumber);


        RequestBody formBody = new FormBody.Builder()
                .add("phone", phoneNumber)
                .add("code",code)
                .build();

        Request request = new Request.Builder()
                .url("https://scan-app.funenc.com/api/users/login")
                .post(formBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("http get","failed in callback");
                cb.onFailure(call,e);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                cb.onResponse(call,response);

            }
        });


    }

    public  void getBanner(final Callback cb)  throws IOException, JSONException {

        Request request = new Request.Builder()
                .url("https://operator-app.funenc.com/api/slides")
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("http get",e.toString());
                cb.onFailure(call,e);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                cb.onResponse(call,response);
            }
        });




    }

    public  void getMe(String token,final Callback cb)  throws IOException, JSONException {

        Request request = new Request.Builder()
                .url("https://operator-app.funenc.com/api/users/me")
                .addHeader("app-token", token)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("get me",e.toString());
                cb.onFailure(call,e);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                cb.onResponse(call,response);
            }
        });

    }

    public  void getHeadlineCate(final Callback cb)  throws IOException, JSONException {

        Request request = new Request.Builder()
                .url("https://operator-app.funenc.com/api/headline/categories")
                .build();


        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("get headline categorey",e.toString());
                cb.onFailure(call,e);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                cb.onResponse(call,response);
            }
        });

    }

    public  void getQiniuToken(String token,final Callback cb)  throws IOException, JSONException {

        Request request = new Request.Builder()
                .url("https://operator-app.funenc.com/api/auth/app/qiniu/token")
                .addHeader("app-token", token)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("get getQiniuToken ",e.toString());
                cb.onFailure(call,e);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                cb.onResponse(call,response);
            }
        });

    }








}
