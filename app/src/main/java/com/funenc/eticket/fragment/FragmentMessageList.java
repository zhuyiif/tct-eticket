package com.funenc.eticket.fragment;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.funenc.eticket.R;
import com.funenc.eticket.api.OperationService;
import com.funenc.eticket.model.MessageListResponse;
import com.funenc.eticket.okhttp.ApiFactory;
import com.funenc.eticket.storage.AppStore;
import com.funenc.eticket.ui.adapter.MessageListViewAdapter;

import java.io.IOException;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FragmentMessageList extends Fragment {

    public static final String IS_MESSAGE = "IS_MESSAGE";
    private List<MessageListResponse.Message> messageList;
    @InjectView(R.id.listView)
    public ListView listView;
    private Handler updateHandler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final int isMessage = getArguments().getInt(IS_MESSAGE);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_simple_scroll_list, container, false);
        ButterKnife.inject(this, view);

        updateHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                listView.setAdapter(new MessageListViewAdapter(getContext(), messageList, isMessage==1));
            }
        };
        ApiFactory apiFactory = new ApiFactory();
        apiFactory.createService(OperationService.BASE_ADDR, OperationService.class, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("FragmentMessageList", "getInformationList error", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.i("MessageList response", body);
                if (response.isSuccessful()) {
                    ObjectMapper mapper = new ObjectMapper();
                    MessageListResponse result = mapper.readValue(body, MessageListResponse.class);
                    if(result.getCode() == 0) {
                        messageList = result.getContent().getList();
                        updateHandler.sendEmptyMessage(0);
                    }
                }
            }
        }).getInformationList(1, 20, isMessage, AppStore.getToken(getContext()));
        return view;
    }


}
