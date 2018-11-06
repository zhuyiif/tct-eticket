package com.funenc.eticket.engine;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.funenc.eticket.api.OperationService;
import com.funenc.eticket.model.StationListResponse;
import com.funenc.eticket.model.UserInfoResponse;
import com.funenc.eticket.okhttp.ApiFactory;
import com.funenc.eticket.storage.AppStore;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by tingken.com on 2018-11-1.
 */
public class StationListFetcher implements Runnable {
    @Override
    public void run() {
        ApiFactory apiFactory = new ApiFactory();
        apiFactory.createService(OperationService.BASE_ADDR, OperationService.class, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("getStationList", "call WEB API failure", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.i("getStationList response", body);
                if(response.isSuccessful()){
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        StationListResponse stationListResponse = mapper.readValue(body, StationListResponse.class);
                        AppStore.setStationList(stationListResponse.getContent().getList());
                    } catch (IOException e) {
                        Log.e("getStationList response", "failure" , e);
                    }
                }
            }
        }).getStationList();
    }
}
