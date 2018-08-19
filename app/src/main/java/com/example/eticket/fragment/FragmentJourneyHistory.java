package com.example.eticket.fragment;

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
import android.widget.Toast;

import com.example.eticket.R;
import com.example.eticket.api.SubwayService;
import com.example.eticket.model.Journey;
import com.example.eticket.model.JourneyListResponse;
import com.example.eticket.okhttp.ApiFactory;
import com.example.eticket.ui.adapter.JourneyHistoryListAdapter;
import com.example.eticket.ui.pojo.JourneyHistoryItem;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.widget.Toast.LENGTH_SHORT;


public class FragmentJourneyHistory extends Fragment {
    @InjectView(R.id.journeyHistory)
    ListView journeyHistoryItemList;
    private List<JourneyHistoryItem> data;
    private Handler updateHandler;
    public static final SimpleDateFormat INPUT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    public static final SimpleDateFormat OUTPUT_TIME_FORMAT = new SimpleDateFormat("HH:mm");
    public static final SimpleDateFormat OUTPUT_DAY_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View page = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_journey_history, container, false);
        ButterKnife.inject(this, page);
        data = new ArrayList<JourneyHistoryItem>(4);
        JourneyHistoryItem item1 = new JourneyHistoryItem.Builder().journeyDate(
                new Date(2018, 8, 30)).inStationName("大明宫西").inDealTime("07:57")
                .status(JourneyHistoryItem.JourneyStatus.GOING).build();
        data.add(item1);
        JourneyHistoryItem item2 = new JourneyHistoryItem.Builder().inStationName("小寨").inDealTime("22:27")
                .status(JourneyHistoryItem.JourneyStatus.EXCEPTION).build();
        data.add(item2);
        JourneyHistoryItem item3 = new JourneyHistoryItem.Builder().journeyDate(
                new Date(2018, 8, 29)).inStationName("小寨").outStationName("大明宫西").inDealTime("06:57").outDealTime("08:12")
                .status(JourneyHistoryItem.JourneyStatus.PAYED).build();
        data.add(item3);
        JourneyHistoryItem item4 = new JourneyHistoryItem.Builder().journeyDate(
                new Date(2018, 8, 28)).inStationName("大明宫西").outStationName("大明宫西").inDealTime("11:57")
                .status(JourneyHistoryItem.JourneyStatus.TO_PAY).build();
        data.add(item4);
        final JourneyHistoryListAdapter adapter = new JourneyHistoryListAdapter(getContext(), data);
        journeyHistoryItemList.setAdapter(adapter);

        updateHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what == 0) {
                    adapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(getContext(), msg.getData().getString("message"), LENGTH_SHORT).show();
                }
            }
        };
        return page;
    }

    private static List<JourneyHistoryItem> convertResult(JourneyListResponse response) {
        List<JourneyHistoryItem> data = new ArrayList<JourneyHistoryItem>(response.getContent().getPageSize());
        Date currentShowDate = null;
        for (Journey journey : response.getContent().getList()) {
            JourneyHistoryItem.Builder builder = new JourneyHistoryItem.Builder();
            JourneyHistoryItem item = null;
            try {
                builder.outDealTime(OUTPUT_TIME_FORMAT.format(INPUT_DATE_FORMAT.parse(journey.getOutDealTime())))
                        .outStationName(journey.getOutStationName()).status(journey.getStatus())
                        .inDealTime(OUTPUT_TIME_FORMAT.format(INPUT_DATE_FORMAT.parse(journey.getInDealTime())))
                        .inStationName(journey.getInStationName())
                        .journeyDate(INPUT_DATE_FORMAT.parse(journey.getCreatedAt()));
                item = builder.build();
                if (currentShowDate != null) {
                    if (OUTPUT_DAY_FORMAT.format(currentShowDate).equals(OUTPUT_DAY_FORMAT.format(item.getJourneyDate()))){
                        item.setJourneyDate(null);
                    }
                }else {
                    currentShowDate = item.getJourneyDate();
                }
            } catch (ParseException e) {
                Log.e("FragmentJourneyHistory", "parse date in the response", e);
            }
            data.add(item);
        }
        return data;
    }

    @Override
    public void onResume() {
        super.onResume();
        ApiFactory apiFactory = new ApiFactory();
        // a test token as default
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MywiaWF0IjoxNTMwNDk4MDE5fQ.5q75yMd0uCB5p3GhCUV4-YzvLvT19IyFAqi0yuDvM3o";
//        if(AppStore.isLogin(getContext())){
//            token = AppStore.getToken(getContext());
//        }
        apiFactory.createService(SubwayService.BASE_ADDR, SubwayService.class, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("FragmentJourneyHistory", "Load error", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.i("JourneyList response", body);
                ObjectMapper mapper = new ObjectMapper();
                JourneyListResponse result = mapper.readValue(body, JourneyListResponse.class);
                if(result.getCode() == 0) {
                    data.clear();
                    data.addAll(convertResult(result));
                }
                Message msg = updateHandler.obtainMessage(result.getCode());
                Bundle data = new Bundle();
                data.putString("message",result.getMessage());
                msg.setData(data);
                updateHandler.sendMessage(msg);
            }
        }).getJourneyList(token, "1");
    }
}
