package com.example.eticket.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.eticket.R;
import com.example.eticket.ui.adapter.JourneyHistoryListAdapter;
import com.example.eticket.ui.pojo.JourneyHistoryItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class FragmentJourneyHistory extends Fragment {
    @InjectView(R.id.journeyHistory)
    ListView journeyHistoryItemList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View page = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_journey_history, container, false);
        ButterKnife.inject(this, page);
        List<JourneyHistoryItem> data = new ArrayList<JourneyHistoryItem>(4);
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
        JourneyHistoryListAdapter adapter = new JourneyHistoryListAdapter(getContext(), data);
        journeyHistoryItemList.setAdapter(adapter);
        return page;
    }
}
