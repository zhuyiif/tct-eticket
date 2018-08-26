package com.example.eticket.fragment;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.eticket.R;
import com.example.eticket.Utility;
import com.example.eticket.api.OperationService;
import com.example.eticket.model.HeadlineListResponse;
import com.example.eticket.okhttp.ApiFactory;
import com.example.eticket.ui.component.HeadlineListViewAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FragmentHeadlineList extends Fragment {

    public static final String CATEGORY_ID_KEY = "CATEGORY_ID_KEY";
    private int categoryId;
    private int lastPage;
    public ListView listView;
    private Handler updateHandler;
    private HeadlineListViewAdapter headlineListViewAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        categoryId = getArguments().getInt(CATEGORY_ID_KEY);
        return LayoutInflater.from(getActivity()).inflate(R.layout.fragment6, container, false);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onStart() {
        super.onStart();

        final View multiTabLayout =  getActivity().findViewById(R.id.tabrootlayout);

        listView = getView().findViewById(R.id.listView);
        headlineListViewAdapter = new HeadlineListViewAdapter(getContext());
        listView.setAdapter(headlineListViewAdapter);
        updateHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                headlineListViewAdapter.notifyDataSetChanged();
                Utility.setListViewHeightBasedOnChildren(listView, multiTabLayout);
                super.handleMessage(msg);
            }
        };

        ApiFactory apiFactory = new ApiFactory();
        apiFactory.createService(OperationService.BASE_ADDR, OperationService.class, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.i("HeadlineList response", body);
                if(response.isSuccessful()) {
                    ObjectMapper mapper = new ObjectMapper();
                    HeadlineListResponse result = mapper.readValue(body, HeadlineListResponse.class);
                    headlineListViewAdapter.appendHeadlineList(result.getContent().getList());
                    updateHandler.sendEmptyMessage(0);
                }
            }
        }).getHeadlineList(null, null, null, categoryId, null);

        Utility.setListViewHeightBasedOnChildren(listView, multiTabLayout);


    }



}
