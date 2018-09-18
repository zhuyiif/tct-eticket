package com.funenc.eticket.engine;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.funenc.eticket.api.OperationService;
import com.funenc.eticket.model.UserInfoResponse;
import com.funenc.eticket.okhttp.ApiFactory;
import com.funenc.eticket.storage.AppStore;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SelfUserInfoFetcher implements Runnable {
    @Override
    public void run() {
        ApiFactory apiFactory = new ApiFactory();
        apiFactory.createService(OperationService.BASE_ADDR, OperationService.class, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("getUserInfo", "call WEB API failure", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.i("UserInfo response", body);
                if(response.isSuccessful()){
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        UserInfoResponse userInfoResponse = mapper.readValue(body, UserInfoResponse.class);
                        AppStore.setSelfUser(userInfoResponse.getContent().getUser());
                    } catch (IOException e) {
                        Log.e("UserInfo response", "failure" , e);
                    }
                }
            }
        }).getUserInfo(AppStore.getToken(AppEngine.getSystemContext()));
    }
}
