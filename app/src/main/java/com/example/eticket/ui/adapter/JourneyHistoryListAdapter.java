package com.example.eticket.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.eticket.R;
import com.example.eticket.ui.pojo.JourneyHistoryItem;
import com.example.eticket.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.List;

public class JourneyHistoryListAdapter extends BaseAdapter {
    private Context context;
    private List<JourneyHistoryItem> journeyHistoryItemList;

    public JourneyHistoryListAdapter(Context context, List<JourneyHistoryItem> journeyHistoryItemList) {
        this.context = context;
        this.journeyHistoryItemList = journeyHistoryItemList;
    }

    @Override
    public int getCount() {
        return journeyHistoryItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return journeyHistoryItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.layout_journey_history_item, null);
        JourneyHistoryItem item = journeyHistoryItemList.get(position);
        if (item.getJourneyDate() == null) {
            convertView.findViewById(R.id.dateContainer).setVisibility(View.GONE);
        } else {
            TextView tvDate = convertView.findViewById(R.id.date);
            SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
            tvDate.setText(sdf.format(item.getJourneyDate()));
        }
        TextView tvInStationName = convertView.findViewById(R.id.inStationName);
        tvInStationName.setText(item.getInStationName());
        if (StringUtils.isEmpty(item.getOutStationName())) {
            convertView.findViewById(R.id.outStation).setVisibility(View.GONE);
        } else {
            TextView tvOutStationName = convertView.findViewById(R.id.outStationName);
            tvOutStationName.setText(item.getOutStationName());
        }
        TextView tvInDealTime = convertView.findViewById(R.id.inDealTime);
        tvInDealTime.setText(item.getInDealTime());
        if (StringUtils.isEmpty(item.getOutDealTime())) {
            convertView.findViewById(R.id.outDeal).setVisibility(View.GONE);
        } else {
            TextView tvOutDealTime = convertView.findViewById(R.id.outDealTime);
            tvOutDealTime.setText(item.getOutDealTime());
        }
        switch (item.getStatus()) {
            case COMPLETE:
                convertView.findViewById(R.id.status_complete).setVisibility(View.VISIBLE);
                break;
            case PAYED:
                convertView.findViewById(R.id.status_payed).setVisibility(View.VISIBLE);
                break;
            case GOING:
                convertView.findViewById(R.id.status_going).setVisibility(View.VISIBLE);
                break;
            case TO_PAY:
                convertView.findViewById(R.id.status_to_pay).setVisibility(View.VISIBLE);
                break;
            case EXCEPTION:
                convertView.findViewById(R.id.status_exception).setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
        return convertView;
    }
}
