package com.example.eticket.engine;

import android.content.Context;
import android.util.Log;

import com.example.eticket.okhttp.HttpUtils;
import com.example.eticket.storage.AppStore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AppEngine {
    private static Context appContext = null;
    public static void init(Context context){
        if(appContext==null) {
            appContext = context;
        }
    }

    public static void login(String id, String verifyKey, final LoginCallback callback){
        HttpUtils httpUtils = new HttpUtils();
        try {
            httpUtils.login(id, verifyKey, new Callback(){
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                            Log.d("login", response.toString());
                            String responseString = response.body().string();
                            Log.d("login", responseString);

                            try {

                                JSONObject respObj = new JSONObject(responseString);
                                Log.d("login", respObj.toString());

                                AppStore.saveLoginContent(appContext,respObj);

                                JSONObject contentObj = respObj.getJSONObject("content");
                                Log.d("contentobj",contentObj.toString());
                                int code = respObj.getInt("code");

                                Log.d("code", String.valueOf(code));

                                if(code == 0){
                                    callback.afterLogin(true);
                                }


                            } catch (Throwable t) {
                                Log.e("login", t.toString());
                            }

                        }
                    }
            );
        } catch (IOException e) {
            Log.e("login",e.toString());
        } catch (JSONException e) {
            Log.e("login",e.toString());
        }
        callback.afterLogin(false);
    }

    public static void logout(){
        AppStore.removeToken(appContext);
    }
}
