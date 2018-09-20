package com.funenc.eticket.engine;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.funenc.eticket.api.SubwayService;
import com.funenc.eticket.model.SeedInfo;
import com.funenc.eticket.okhttp.ApiFactory;
import com.funenc.eticket.storage.AppStore;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class QrSeedFetcher implements Runnable {
    @Override
    public void run() {
        ApiFactory apiFactory = new ApiFactory();
        apiFactory.createService(SubwayService.BASE_ADDR, SubwayService.class, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("QrSeed response", "failure", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.i("QrSeed response", body);
                if(response.isSuccessful()) {
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        SeedInfo result = mapper.readValue(body, SeedInfo.class);
                        AppStore.setTicketCodeCreateKey(AppEngine.getSystemContext(), result.getKey());
                        AppStore.setTicketCodeCreateSeed(AppEngine.getSystemContext(), result.getSeed());
                    } catch (Exception e) {
                        Log.e("QrSeed response", "failure" , e);
                    }
                }
            }
        }).getQrSeed(AppStore.getToken(AppEngine.getSystemContext()));
    }
}
