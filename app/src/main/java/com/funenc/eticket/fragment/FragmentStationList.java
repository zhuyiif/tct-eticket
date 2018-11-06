package com.funenc.eticket.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.funenc.eticket.R;
import com.funenc.eticket.api.OperationService;
import com.funenc.eticket.model.OrderListResponse;
import com.funenc.eticket.model.StationListResponse;
import com.funenc.eticket.okhttp.ApiFactory;
import com.funenc.eticket.storage.AppStore;
import com.funenc.eticket.ui.activity.WebViewActivity;
import com.funenc.eticket.ui.adapter.StationListViewAdapter;

import java.io.IOException;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by tingken.com on 2018-11-1.
 */
public class FragmentStationList extends Fragment {

    public static final String LINE_NO = "LINE_NO";
    private static final int PAY_SUCCESS = 2;
    private List<StationListResponse.Station> stationList;
    @InjectView(R.id.listView)
    public ListView listView;
    private Handler responseHandler;
    private OrderListResponse.Order order;
    private String lineNo;

    public FragmentStationList() {
        this.responseHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case PAY_SUCCESS:
                        final Context context = getContext();
                        View successView=LayoutInflater.from(context).inflate(R.layout.layout_add_out_station_success, null);
                        ((TextView)successView.findViewById(R.id.inStationName)).setText(order.getInStationName());
                        ((TextView)successView.findViewById(R.id.inDealTime)).setText(AppStore.OUTPUT_TIME_FORMAT.format(order.getInTradeTime()));
                        ((TextView)successView.findViewById(R.id.outStationName)).setText(AppStore.getStationLineInfo().get(lineNo).get(msg.arg1).getcStationName());
                        AlertDialog.Builder builder = new AlertDialog.Builder(context,3);
                        builder.setView(successView);
                        final AlertDialog successDialog = builder.create();
                        successView.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String url = String.format("%s/page/#/recordDetail?token=%s&orderid=%s", OperationService.BASE_ADDR, AppStore.getToken(getContext()), order.getId());
                                Intent intent = new Intent().setClass(getContext(), WebViewActivity.class);
                                intent.putExtra(WebViewActivity.URL, url);
                                startActivity(intent);
                            }
                        });
                        successView.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                successDialog.dismiss();
                            }
                        });
                        successDialog.show();
                        break;
                }
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        lineNo = getArguments().getString(LINE_NO);
        order = (OrderListResponse.Order) getArguments().getSerializable(AppStore.ORDER);
        Log.d("FragmentStationList", "order=" + (order == null ? "" : order.toString()));
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_simple_scroll_list, container, false);
        ButterKnife.inject(this, view);

        final Context context = getContext();
        listView.setAdapter(new StationListViewAdapter(context, AppStore.getStationLineInfo().get(lineNo)));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final StationListResponse.Station out = AppStore.getStationLineInfo().get(lineNo).get(position);
                View confirmView = LayoutInflater.from(context).inflate(R.layout.layout_confirm_out_station, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(context,3);
                builder.setView(confirmView);
                ((TextView)confirmView.findViewById(R.id.alertTitle)).setText(out.getcStationName());
                final AlertDialog confirmDialog = builder.create();
                confirmDialog.show();
                confirmView.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        confirmDialog.dismiss();
                    }
                });
                confirmView.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        confirmDialog.dismiss();
                        ApiFactory apiFactory = new ApiFactory();
                        apiFactory.createService(OperationService.BASE_ADDR, OperationService.class, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.e("changeOrder", "fail", e);
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                Log.d("changeOrder", "response message: " + response.message());
                                Log.d("changeOrder", "response isSuccessful: " + response.isSuccessful());
                                Log.d("changeOrder", "response body: " + response.body().string());
                                Message.obtain(responseHandler, PAY_SUCCESS, position, 0).sendToTarget();
                            }
                        }).changeOrder(out.getLineNo(), out.getStationNo(), "0000000000000000", "0000000000000000", "0000000000000000", AppStore.getToken(getContext()), order.getId());
                    }
                });
            }
        });
        return view;
    }


}
